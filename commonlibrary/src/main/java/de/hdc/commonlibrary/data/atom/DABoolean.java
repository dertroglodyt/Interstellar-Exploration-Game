/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DABoolean.java
 *
 * Created on 6. Mai 2005, 03:13
 */

package de.hdc.commonlibrary.data.atom;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Simply holds a String.
 * @author Martin
 */

public class DABoolean extends DataAtom {

    public static final DABoolean TRUE = new DABoolean(true);
    public static final DABoolean FALSE = new DABoolean(false);

    public static DABoolean create(final boolean value) {
        return new DABoolean(value);
    }

    @Deprecated
    public DABoolean() {
        super();
        value = false;
    }

    @Override
    public final String toString() {
        if (value) {
            return "TRUE";
        }
        return "FALSE";
    }

    @Override
    public DABoolean fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return new DABoolean(stream.readBoolean());
    }

    @Override
    public final void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeBoolean(value);
    }

    private static final byte VERSION = 1;
    private final boolean value;

    private DABoolean(final boolean value) {
        super();
        this.value = value;
    }
}
