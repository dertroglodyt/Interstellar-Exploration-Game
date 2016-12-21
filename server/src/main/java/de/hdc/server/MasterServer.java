package de.hdc.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAInetAddress;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.protocol.DAParameterList;
import de.hdc.server.logging.DALogEntry;

/**
 * Created by DerTroglodyt on 2016-12-19 11:33.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class MasterServer implements Runnable {

    public static int MAX_CONNECTION = 100;
    public static int CONNECTION_TIMEOUT = 1000;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Wrong number of parameters. Usage: java MasterServer <#port>");
            throw new IllegalArgumentException();
        }
        System.out.println("Master server started: " + DADateTime.now());
        new MasterServer(Integer.valueOf(args[0])).run();
        System.out.println("Master server stopped: " + DADateTime.now());
    }

    public MasterServer(int port) throws IOException {
        this.shutdown = false;
        this.socket = new ServerSocket(port, MAX_CONNECTION);
        this.log = new PrintWriter(new BufferedWriter(new FileWriter(new File(filePath), true)));
    }

    public void run() {
        // Get notified when the program is terminating.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown = true;
                log(DALogEntry.create(source, DAResult.ResultType.FATAL, DAText.create("Server stopped.")));
                log.flush();
            }
        });

        log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Server started.")));
        log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Listening on " + socket.getLocalSocketAddress())));
        log.flush();
        while (! shutdown) {
            try {
                while (! shutdown) {
//                    log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Awaiting connect...")));
//                    log.flush();
                    Runnable task = new Connection(socket.accept());
//                    log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("New connection.")));
//                    log.flush();
                    exec.execute(task);
                }
            } catch (Throwable t) {
                log(DALogEntry.create(source, DAResult.ResultType.FATAL, DAText.create(t.toString())));
                log.flush();
                throw new RuntimeException(t.toString());
            }
        }
        log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Server stopped.")));
        log.flush();
    }

    public void stop() throws InterruptedException {
        shutdown = true;
        exec.shutdown();
        Thread.sleep(1000);
        exec.shutdownNow();
    }

    private final static String filePath = "./master_server.log";
    private final static DAText source = DAText.create("MasterServer");
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    private final ServerSocket socket;
    private final PrintWriter log;

    private boolean shutdown;
    private DAInetAddress logServer;

    private void log(DALogEntry entry) {
        synchronized (log) {
            log.println(entry.source + "|" + entry.time + "|" + entry.level + "|" + entry.message);
        }
    }

    private class Connection implements Runnable {

        private Connection(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                socket.setSoTimeout(CONNECTION_TIMEOUT);
                final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            } catch (IOException e) {
                log(DALogEntry.create(source, DAResult.ResultType.WARNING
                        , DAText.create("Socket IOException. " + e.getMessage())));
                log.flush();
            }
            while (true) {
//                log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Horche...")));
//                log.flush();
                DataInputStream is = null;
                try {
                    is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                } catch (IOException e) {
                    log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Could not open input stream. " + e.getMessage())));
                    break;
                }
                DAServerMessage res = null;
                try {
                    res = new DAServerMessage().fromStream(is);
                } catch (EOFException e) {
                    // nothing to do. stream is closed.
//                    log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Socket EOF.")));
//                    log.flush();
                    break;
                } catch (IOException e) {
                    log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Could not read message from input stream. " + e.getMessage())));
                    break;
                }
//                log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Verstanden: " + res.action)));
//                log.flush();
                handle(res.action, res.parameterList, socket);
            }
//            log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Socket exit.")));
//            log.flush();
        }

        private final Socket socket;
    }

    private void handle(DAServerMessage.Action action, DAParameterList pl, Socket out) {
        switch (action) {
            case REGISTER_LOG_SERVER: {
                logServer = (DAInetAddress) pl.get(DAServerMessage.Parms.SENDER_IP);
                log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Registered: " + logServer)));
                log.flush();
                DAParameterList plr = DAParameterList.create();
                plr.add(DAServerMessage.Parms.OK, DAText.create("Log server registered."));
                DAServerMessage msg = new DAServerMessage(DAServerMessage.Action.REGISTER_LOG_SERVER, plr);
                Communicate.send(out, msg);
                break;
            }
            case UNREGISTER_LOG_SERVER: {
                logServer = null;
                log(DALogEntry.create(source, DAResult.ResultType.INFO, DAText.create("Unregistered: " + logServer)));
                log.flush();
                DAParameterList plr = DAParameterList.create();
                plr.add(DAServerMessage.Parms.OK, DAText.create("Log server unregistered."));
                DAServerMessage msg = new DAServerMessage(DAServerMessage.Action.UNREGISTER_LOG_SERVER, plr);
                Communicate.send(out, msg);
                break;
            }
            case GET_LOG_SERVER: {
                DAParameterList plr = DAParameterList.create();
                plr.add(DAServerMessage.Parms.SENDER_IP, pl.get(DAServerMessage.Parms.SENDER_IP));
                plr.add(DAServerMessage.Parms.DEST_IP, logServer);
                DAServerMessage msg = new DAServerMessage(DAServerMessage.Action.GET_LOG_SERVER, plr);
                Communicate.send(out, msg);
                break;
            }
        }
    }
}
