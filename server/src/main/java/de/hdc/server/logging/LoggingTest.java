package de.hdc.server.logging;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import de.hdc.commonlibrary.data.atom.DAInetAddress;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.server.MasterServer;

/**
 * Created by DerTroglodyt on 2016-12-17 12:38.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class LoggingTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        MasterServer ms = new MasterServer(8001);
        Thread master = new Thread(ms);
        master.start();

        LoggingServer ls = new LoggingServer(DAInetAddress.create("127.0.0.1:8001"), 8080);
        Thread log = new Thread(ls);
        log.start();

        DAText sourceIP = DAText.create(InetAddress.getLocalHost().toString());

        System.out.println("Huge data count Test. ");
        Socket socket = new Socket("127.0.0.1", 8080);
        DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DALogEntry entry;
        for (int i=0; i < 100000; i++) {
            entry = DALogEntry.create(sourceIP, DAResult.ResultType.INFO, DAText.create("Huge data count Test. " + i));
            entry.toStream(os);
        }
        os.flush();
        os.close();
        Thread.sleep(1000);
        socket.close();

        System.out.println("Pause Test.");
        socket = new Socket("127.0.0.1", 8080);
        os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        entry = DALogEntry.create(sourceIP, DAResult.ResultType.INFO, DAText.create("Pause Test."));
        for (int i=0; i < 5; i++) {
            entry.toStream(os);
        }
        Thread.sleep(10000);
        entry = DALogEntry.create(sourceIP, DAResult.ResultType.INFO, DAText.create("Pause Test, after."));
        for (int i=0; i < 5; i++) {
            entry.toStream(os);
        }
        os.flush();
        Thread.sleep(1000);
        os.close();

        System.out.println("Invalid data Test.");
        socket = new Socket("127.0.0.1", 8080);
        os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        entry = DALogEntry.create(sourceIP, DAResult.ResultType.INFO, DAText.create("Ivalid data Test."));
        for (int i=0; i < 5; i++) {
            os.writeUTF("Invalid data!");
        }
        entry.toStream(os);
        os.flush();
        Thread.sleep(1000);
        os.close();

        ls.stop();
        ms.stop();
        log.interrupt();
        master.interrupt();
    }
}
