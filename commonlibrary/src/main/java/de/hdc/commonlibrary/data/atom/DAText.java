/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.atom;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * What's this?
 *
 * @author dertroglodyt
 */
public class DAText extends DataAtom {

    public static DAText create(@NonNull String value) {
        return new DAText(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeUTF(value);
    }

    @Override
    public DAText fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return new DAText(stream.readUTF());
    }

    private static final byte VERSION = 1;
    private final String value;

    private DAText(String s) {
        super();
        value = s;
    }

    @Deprecated
    public DAText() {
        super();
        value = null;
    }

}
