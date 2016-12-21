package de.hdc.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAInetAddress;
import de.hdc.commonlibrary.data.compound.DAResult;

/**
 * Created by DerTroglodyt on 2016-12-21 09:28.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 *
 * Helper class to handle all repetitive tasks of sending / receiving objects via a socket.
 */

public class Communicate {

    public static <T extends IDataAtom> DAResult<T> send(DAInetAddress adr, IDataAtom... data) {
        Socket socket = null;
        try {
            socket = new Socket(adr.ip, adr.port);
            socket.setSoTimeout(1000);
        } catch (IOException e) {
            return DAResult.createWarning("Could not open socket. " + e.getMessage(), "Communicate.send");
        }
        DAResult<T> res = (DAResult<T>) send(socket, data);
        try {
            socket.close();
        } catch (IOException e) {
            return DAResult.createWarning("Could not close socket. " + e.getMessage(), "Communicate.send");
        }
        return res;
    }

    public static DAResult<?> send(Socket socket, IDataAtom... data) {
        // Send message
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            return DAResult.createWarning("Could not open output stream. " + e.getMessage(), "Communicate.send");
        }
        try {
            for (IDataAtom ia : data) {
                ia.toStream(os);
            }
            os.flush();
        } catch (IOException e) {
            return DAResult.createFailed("Could not write message to output stream. " + e.getMessage(), "Communicate.send");
        }
        return DAResult.createOK("Data send.", "Communicate.send");
    }

    public static <T extends IDataAtom> DAResult<T> receiveOnce(DAInetAddress adr, IDataAtom data) {
        Socket socket = null;
        try {
            socket = new Socket(adr.ip, adr.port);
            socket.setSoTimeout(1000);
        } catch (IOException e) {
            return DAResult.createWarning("Could not open socket. " + e.getMessage(), "Communicate.receive");
        }
        DAResult<T> res = (DAResult<T>) receiveOnce(socket, data);
        try {
            socket.close();
        } catch (IOException e) {
            return DAResult.createWarning("Could not close socket. " + e.getMessage(), "Communicate.receive");
        }
        return res;
    }

    public static DAResult<IDataAtom> receiveOnce(Socket socket, IDataAtom data) {
        // Send message
        DataInputStream is = null;
        try {
            is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            return DAResult.createWarning("Could not open input stream. " + e.getMessage(), "Communicate.receive");
        }
        DAServerMessage res = null;
        try {
            res = (DAServerMessage) data.fromStream(is);
        } catch (IOException e) {
            return DAResult.createFailed("Could not read message from input stream. " + e.getMessage(), "Communicate.receive");
        }
        return DAResult.createOK("Data read.", "Communicate.receive", res);
    }

    public static DAResult<DAServerMessage> sendReceive(DAInetAddress adr, IDataAtom... data) {
        Socket socket = null;
        try {
            socket = new Socket(adr.ip, adr.port);
            socket.setSoTimeout(1000);
        } catch (IOException e) {
            return DAResult.createWarning("Could not open socket. " + e.getMessage(), "Communicate.receive");
        }
        DAResult<DAServerMessage> res = sendReceive(socket, data);
        try {
            socket.close();
        } catch (IOException e) {
            return DAResult.createWarning("Could not close socket. " + e.getMessage(), "Communicate.receive");
        }
        return res;
    }

    public static DAResult<DAServerMessage> sendReceive(Socket socket, IDataAtom... data) {
        // Send message
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            return DAResult.createWarning("Could not open output stream. " + e.getMessage(), "Communicate.sendReceive");
        }
        try {
            for (IDataAtom ia : data) {
                ia.toStream(os);
            }
            os.flush();
        } catch (IOException e) {
            return DAResult.createFailed("Could not write message to output stream. " + e.getMessage(), "Communicate.sendReceive");
        }
        // Await response
        DataInputStream is = null;
        try {
            is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            return DAResult.createWarning("Could not open input stream. " + e.getMessage(), "Communicate.sendReceive");
        }
        DAServerMessage msg = null;
        try {
            msg = new DAServerMessage().fromStream(is);
        } catch (IOException e) {
            return DAResult.createFailed("Could not read message from input stream. " + e.getMessage(), "Communicate.sendReceive");
        }
        return DAResult.createOK("Data read.", "Communicate.sendReceive", msg);
    }

}
