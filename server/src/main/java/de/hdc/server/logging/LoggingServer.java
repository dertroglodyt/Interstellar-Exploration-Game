package de.hdc.server.logging;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.protocol.DALogEntry;

/**
 * Created by DerTroglodyt on 2016-12-17 10:36.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class LoggingServer {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println();
            throw new RuntimeException("Invalid start parameters!\nUsage: java Loggingserver <registerIP> <localPort>\n");
        }
        System.out.println("Logging server started: " + DADateTime.now());
        try {
            new LoggingServer(args[0], args[1]).run();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        System.out.println("Logging server stopped: " + DADateTime.now());
    }

    public LoggingServer(String registerIP, String localPort) throws IOException {
        this.registerIP = registerIP;
        int port = Integer.valueOf(localPort);
        this.inputPort = new ServerSocket(port);
        log = new PrintWriter(new BufferedWriter(new FileWriter(new File(filePath), true)));
    }

    public void run() throws IOException {
        // todo: register at registerServer. Or: config for docker process.
        log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("Server started.")));
        // Get notified when the program is terminating.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("Server stopped.")));
                log.flush();
                System.out.println("Logging server stopped: " + DADateTime.now());
            }
        });

        try {
            while (true) {
                Runnable task = new Connection(inputPort.accept());
//                log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("New Thread.")));
                exec.execute(task);
            }
        } catch (Throwable t) {
            log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.ERROR, DAText.create(t.toString())));
            throw new RuntimeException(t.toString());
        }
    }

    private final static String filePath = "./default.log";
    private static final Executor exec = Executors.newCachedThreadPool();

    /**
     * Register start and shutdown at this IP + Port.
     */
    private final String registerIP;
    /**
     * Read LogEntry's from this Port.
     */
    private final ServerSocket inputPort;
    private final PrintWriter log;

    private void log(DALogEntry entry) {
        log.println(entry.time.toString() + "|" + entry.level + "|" + entry.message);
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
//                log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("Socket EOF.")));
            } catch (IOException e) {
                log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.WARNING
                        , DAText.create("Socket IOException." + e.getMessage())));
            }
//            log(DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("Socket exit.")));
        }

        private Socket	socket;
    }

}
