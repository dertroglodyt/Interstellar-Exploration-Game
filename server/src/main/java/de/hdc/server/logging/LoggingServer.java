package de.hdc.server.logging;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAInetAddress;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.protocol.DAParameterList;
import de.hdc.server.Communicate;
import de.hdc.server.DAServerMessage;

/**
 * Created by DerTroglodyt on 2016-12-17 10:36.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class LoggingServer implements Runnable {

    public static int CONNECTION_TIMEOUT = 0;
    public static int CONNECTION_TIMEOUT_REGISTER = 0;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new RuntimeException("Invalid start parameters!\n"
                    + "Usage: java LoggingServer <registerIP>:<#registerPort> <#localPort>\n"
                    + "Example: java LoggingServer 192.168.178.1:8081 8082");
        }
        System.out.println("Logging server started: " + DADateTime.now());
        try {
            DAInetAddress ia = DAInetAddress.create(args[0]);
            new LoggingServer(ia, Integer.valueOf(args[1])).run();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        System.out.println("Logging server stopped: " + DADateTime.now());
    }

    public LoggingServer(DAInetAddress registerIP, int localPort) throws IOException {
        this.registerIP = registerIP;
        this.inputPort = localPort;
        this.inputSocket = new ServerSocket(inputPort);
        this.shutdown = false;
        log = new PrintWriter(new BufferedWriter(new FileWriter(new File(filePath), true)));
    }

    public void run() {
        // register at registerServer.
        register();

        // Get notified when the program is terminating.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // unregister at register server
                unregister();
                log(DALogEntry.create(localhost, DAResult.ResultType.FATAL, DAText.create("Server stopped.")));
                log.flush();
            }
        });

        try {
            while (! shutdown) {
                Runnable task = new Connection(inputSocket.accept());
//                log(DALogEntry.create(localhost, DADateTime.now(), DAResult.ResultType.INFO, DAText.create("New Thread.")));
                exec.execute(task);
            }
        } catch (Throwable t) {
            log(DALogEntry.create(localhost, DAResult.ResultType.FATAL, DAText.create(t.toString())));
            log.flush();
            throw new RuntimeException(t.toString());
        }
    }

    public void stop() throws InterruptedException {
        shutdown = true;
        exec.shutdown();
        Thread.sleep(1000);
        exec.shutdownNow();
    }

    private final static String filePath = "./logging_server.log";
    private static final ExecutorService exec = Executors.newCachedThreadPool();
    private static final DAText localhost = DAText.create("LOGGING_SERVER");

    /**
     * Register start and shutdown at this IP + Port.
     */
    private final DAInetAddress registerIP;
    /**
     * Read LogEntry's from this Port.
     */
    private final int inputPort;
    private final ServerSocket inputSocket;
    private final PrintWriter log;
    private boolean shutdown;

    private void log(DALogEntry entry) {
        synchronized (log) {
            log.println(entry.source + "|" + entry.time + "|" + entry.level + "|" + entry.message);
        }
    }

    /**
     * Register at master server.
     */
    private void register() {
        log(DALogEntry.create(localhost, DAResult.ResultType.INFO, DAText.create("Server started. Registering at " + registerIP + "...")));
        log.flush();
        DAParameterList pl = DAParameterList.create();
        try {
            pl.add(DAServerMessage.Parms.SENDER_IP, DAInetAddress.create(InetAddress.getLocalHost().getHostName(), inputPort));
        } catch (IOException e) {
            log(DALogEntry.create(localhost, DAResult.ResultType.FATAL, DAText.create(e.getMessage())));
            log.flush();
            throw new RuntimeException(e);
        }
        DAServerMessage msg = new DAServerMessage(DAServerMessage.Action.REGISTER_LOG_SERVER, pl);

        DAResult<DAServerMessage> r = Communicate.sendReceive(registerIP, msg);
        if (! r.isOK()) {
            log(DALogEntry.create(localhost, DAResult.ResultType.FATAL, DAText.create(r.getMessage())));
            log.flush();
        }
        DAServerMessage resp = r.getResult();
        if (resp.parameterList.get(DAServerMessage.Parms.OK) == null) {
            log(DALogEntry.create(localhost, DAResult.ResultType.FATAL, DAText.create("Register server: no OK returned.")));
            log.flush();
            throw new RuntimeException("Register server: no OK returned.");
        }

        log(DALogEntry.create(localhost, DAResult.ResultType.INFO, DAText.create("Server registered.")));
        log.flush();
    }

    private void unregister() {
        log(DALogEntry.create(localhost, DAResult.ResultType.INFO, DAText.create("Unregistering server...")));
        log.flush();
            DAParameterList pl = DAParameterList.create();
            DAServerMessage msg = new DAServerMessage(DAServerMessage.Action.UNREGISTER_LOG_SERVER, pl);

            DAResult<DAServerMessage> r = Communicate.sendReceive(registerIP, msg);
            if (! r.isOK()) {
                log(DALogEntry.create(localhost, DAResult.ResultType.WARNING, DAText.create(r.getMessage())));
                log.flush();
            }
            DAServerMessage resp = r.getResult();
            if (resp.parameterList.get(DAServerMessage.Parms.OK) == null) {
                log(DALogEntry.create(localhost, DAResult.ResultType.FATAL, DAText.create("Register server: no OK returned.")));
                log.flush();
                throw new RuntimeException("Register server: no OK returned.");
            }
        log(DALogEntry.create(localhost, DAResult.ResultType.INFO, DAText.create("Server unregistered.")));
        log.flush();
    }

    private class Connection implements Runnable {

        public Connection(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                while (true) {
                    log(new DALogEntry().fromStream(in));
                }
            } catch (EOFException e) {
                // nothing to do. stream is closed.
//                log(DALogEntry.create(localhost, DADateTime.now(), DAResult.ResultType.INFO, DAText.create("Socket EOF.")));
            } catch (Exception e) {
                log(DALogEntry.create(localhost, DAResult.ResultType.WARNING
                        , DAText.create("Socket Exception." + e.getMessage())));
                log.flush();
            }
//            log(DALogEntry.create(localhost, DADateTime.now(), DAResult.ResultType.INFO, DAText.create("Socket exit.")));
        }

        private final Socket socket;
    }

}
