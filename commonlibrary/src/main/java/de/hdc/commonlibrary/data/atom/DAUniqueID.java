/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DVCdmUniqueID.java
 *
 * Created on 12. Oktober 2005, 13:13
 *
 */

package de.hdc.commonlibrary.data.atom;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Random;

import de.hdc.commonlibrary.util.Hex;
import de.hdc.commonlibrary.util.Log;

/**
 *
 * @author Martin
 */
//@SuppressWarnings("serial")
public final class DAUniqueID extends DataAtom {

//    public DAUniqueID(int id) {
//        super(Hex.fromHexString(F.format(id)));
//    }

    public static DAUniqueID createRandom() {
        byte[] b = new byte[BYTE_COUNT];
        RANDOM.nextBytes(b);
        return new DAUniqueID(b);
    }

    @Override
    public String toString() {
        if (idStr != null) {
            return idStr;
        }
        StringBuilder sb = new StringBuilder(BYTE_COUNT * 2);
        for (int i=0; i < BYTE_COUNT; i++) {
            String hs = Integer.toHexString(byteList[i]);
            while (hs.length() < 2) {
                hs = '0' + hs;
            }
            sb.append(hs.substring(hs.length()-2));
        }
        if (sb.length() != (BYTE_COUNT * 2)) {
            Log.fatal(DAUniqueID.class, "ID <{0}> is invalid! Length: {1}", sb.toString(), sb.length());
            throw new IllegalArgumentException("ID <" + sb + "> is invalid. Length=" + sb.length() + "!");
        }
        idStr = sb.toString();
        return idStr;
    }

    /**
     * A UniqueID of all zeros is invalid!
     * @return
     */
    public boolean isValid() {
        for (int i=0; i < byteList.length; i++) {
            if (byteList[i] != 0) {
                return true;
            }
        }
        return false;
    }

    public URI toUri(byte jxtaIDType) {
        try {
            return new URI("urn:jxta:uuid-" + toString() + "00" + ((jxtaIDType < 10)?"0":"") + jxtaIDType);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeUTF(toString());
    }

    @Override
    public DAUniqueID fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return new DAUniqueID(Hex.fromHexString(stream.readUTF()));
    }

    private static final byte VERSION = 1;
    private static final transient Random RANDOM = new SecureRandom();
    private static final transient int BYTE_COUNT = 16;

    private final byte[] byteList;
    private transient String idStr;

    @Deprecated
    public DAUniqueID() {
        super();
        byteList = null;
    }

    private DAUniqueID(byte[] newID) {
        super();
        if (newID.length != BYTE_COUNT) {
            Log.fatal(DAUniqueID.class, "ID <{0}> has not length={1}!", newID, BYTE_COUNT);
            throw new IllegalArgumentException("ID byte array has not length=" + BYTE_COUNT + "!");
        }
        this.byteList = newID;
        this.idStr = null;
    }

    /**
     * Needed for Deserialisation of WareClasses (fixed IDs).
     * @param value
     * @return
     */
    public static DAUniqueID parse(String value) {
        try {
            String hexStr = value.replace(" ", "");
            if (hexStr.length() != (BYTE_COUNT * 2)) {
                Log.fatal(DAUniqueID.class, "hexID <{0}> has not length={1}!", hexStr, BYTE_COUNT * 2);
                throw new IllegalArgumentException("ID <" + hexStr + "> has not length=" + BYTE_COUNT * 2 + "!");
//                return null;
            }
            byte[] b = new byte[BYTE_COUNT];
            String s;
            for (int i=0; i < b.length; i++) {
                s = hexStr.substring(i*2, (i*2)+2);
                int x = Integer.parseInt(s, 16);
//                // hex string pairs range from 0..255
//                // bring to byte range -128..+127
//                b[i] = (byte) (x - 128);
                b[i] = (byte) x;
            }
            return new DAUniqueID(b);
        } catch (NumberFormatException e) {
            Log.throwable(DAUniqueID.class, e, "parse");
            return null;
        }
    }

}
