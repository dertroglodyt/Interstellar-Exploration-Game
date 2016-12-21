/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.atom;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.StringTokenizer;

/**
 * What's this?
 *
 * @author dertroglodyt
 */
public class DAInetAddress extends DataAtom {

    public static DAInetAddress create(String ip, int port) {
        return new DAInetAddress(ip, port);
    }

    public static DAInetAddress create(String addr) {
        StringTokenizer st = new StringTokenizer(addr, ":");
        if (st.countTokens() != 2) {
            throw new InvalidParameterException("Malformed InetAddress: <" + addr + ">");
        }
        return new DAInetAddress(st.nextToken(), Integer.valueOf(st.nextToken()));
    }

    public final String ip;
    public final int port;

    @Override
    public String toString() {
        return ip + ":" + port;
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        stream.writeUTF(ip);
        stream.writeInt(port);
    }

    @Override
    public DAInetAddress fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return new DAInetAddress(stream.readUTF(), stream.readInt());
    }

    private static final byte VERSION = 1;

    private DAInetAddress(String ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
    }

    @Deprecated
    public DAInetAddress() {
        super();
        ip = null;
        port = -1;
    }

}
