package de.hdc.server.logging;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.protocol.DALogEntry;

/**
 * Created by DerTroglodyt on 2016-12-17 12:38.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class LoggingTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 8080);
        DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DALogEntry entry = DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("Ein Test."));
        for (int i=0; i < 100000; i++) {
            entry.toStream(os);
        }
        os.flush();
        os.close();
        socket.close();

        socket = new Socket("127.0.0.1", 8080);
        os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        entry = DALogEntry.create(DADateTime.now(), DALogEntry.Level.INFO, DAText.create("Ein zweiter Test."));
        for (int i=0; i < 5; i++) {
            entry.toStream(os);
        }
        Thread.sleep(10000);
        for (int i=0; i < 5; i++) {
            entry.toStream(os);
        }
        os.flush();
        os.close();
    }
}
