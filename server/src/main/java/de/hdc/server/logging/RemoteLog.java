/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.server.logging;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import de.hdc.commonlibrary.data.atom.DAInetAddress;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.protocol.DAParameterList;
import de.hdc.commonlibrary.util.Log;
import de.hdc.server.Communicate;
import de.hdc.server.DAServerMessage;

/**
 *
 * @author martin
 */
public class RemoteLog {

    public final static int port = 8080;

    public RemoteLog(String registerIP, int registerPort, String name) throws UnknownHostException {
        // get remote log server address
        // send our ip
        DAInetAddress ia = DAInetAddress.create(InetAddress.getLocalHost().getHostName(), port);
        DAParameterList pl = DAParameterList.create();
        pl.add(DAServerMessage.Parms.SENDER_IP, DAInetAddress.create(InetAddress.getLocalHost().getHostName(), port));
        DAServerMessage msg = new DAServerMessage(DAServerMessage.Action.GET_LOG_SERVER, pl);
        DAResult<DAServerMessage> r = Communicate.sendReceive(ia, msg);
        if (! r.isOK()) {
            throw new RuntimeException("Register server returned error. " + r.getMessage());
        }
        DAParameterList re = r.getResult().parameterList;
        if (re.get(DAServerMessage.Parms.DEST_IP) == null) {
            throw new RuntimeException("Register server returned no logging server IP.");
        }
        logServerIP = (DAInetAddress) re.get(DAServerMessage.Parms.DEST_IP);
        source = DAText.create(InetAddress.getLocalHost().getHostName() + port + "-" + name);
    }

    public void result(DAResult<?> result) {
        DALogEntry e = DALogEntry.create(source, result.getResultType()
                , DAText.create(result.getSource() + ": " + result.getMessage()));
        log(e);
    }

    public void throwable(Class<?> clazz, Throwable t, String methodName) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(bos));
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.FATAL
                , DAText.create(clazz.getName() + "." + methodName + "\n" + bos.toString()));
        log(e);
    }

    public void debug(Class<?> clazz, String message, Object... arguments) {
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.DEBUG
                , DAText.create(clazz.getName() + ": " + unpack(message, arguments)));
        log(e);
    }

    public void info(Class<?> clazz, String message, Object... arguments) {
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.INFO
                , DAText.create(clazz.getName() + ": " + unpack(message, arguments)));
        log(e);
    }

    public void ok(Class<?> clazz, String message, Object... arguments) {
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.OK
                , DAText.create(clazz.getName() + ": " + unpack(message, arguments)));
        log(e);
    }

    public void failed(Class<?> clazz, String message, Object... arguments) {
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.FAILED
                , DAText.create(clazz.getName() + ": " + unpack(message, arguments)));
        log(e);
    }

    public void warn(Class<?> clazz, String message, Object... arguments) {
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.WARNING
                , DAText.create(clazz.getName() + ": " + unpack(message, arguments)));
        log(e);
    }

    public void fatal(Class<?> clazz, String message, Object... arguments) {
        DALogEntry e = DALogEntry.create(source, DAResult.ResultType.FATAL
                , DAText.create(clazz.getName() + ": " + unpack(message, arguments)));
        log(e);
    }

    private final DAText source;
    private final DAInetAddress logServerIP;
    private DataOutputStream os;

    private static String unpack(String message, Object... arguments) {
        StringBuilder sb = new StringBuilder(message);
        int i = 0;
        for (Object parm : arguments) {
            int start = sb.indexOf("{" + i + "}");
            if (start >= 0) {
                sb.replace(start, start + ("{" + i + "}").length(), (parm != null)?parm.toString():"null");
            }
            i += 1;
        }
        return sb.toString();
    }

    private void log(DALogEntry entry) {
        try {
            if (os == null ) {
                final Socket socket = new Socket(logServerIP.ip, logServerIP.port);
                os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            }
            entry.toStream(os);
        } catch (IOException e) {
            os = null;
            Log.warn(RemoteLog.class, "Remote log server disconnected." + e);
            DAResult r = new DAResult(entry.message.toString(), entry.level, entry.source.toString() + " " + entry.time.toString());
            Log.result(r);
        }
    }

    private RemoteLog() {
        source = null;
        logServerIP = null;
        os = null;
    }

}
