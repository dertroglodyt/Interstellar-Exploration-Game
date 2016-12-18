package de.hdc.commonlibrary.protocol;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 * Created by DerTroglodyt on 2016-12-17 11:09.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class DALogEntry extends DataAtom {

    public enum Level {
        UNKNOWN, DEBUG, INFO, WARNING, ERROR
    }

    public static DALogEntry create(DADateTime time, Level level, DAText message) {
        return new DALogEntry(time, level, message);
    }

    public final DADateTime time;
    public final Level level;
    public final DAText message;

    @Deprecated
    public DALogEntry() {
        time = null;
        level = null;
        message = null;
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        time.toStream(stream);
        stream.writeUTF(level.toString());
        message.toStream(stream);
    }

    @Override
    public DALogEntry fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DADateTime atime = new DADateTime().fromStream(stream);
        Level alevel = Level.valueOf(stream.readUTF());
        DAText amsg = new DAText().fromStream(stream);
        return new DALogEntry(atime, alevel, amsg);
    }

    private static final byte VERSION = 1;

    private DALogEntry(DADateTime time, Level level, DAText message) {
        super();
        this.time = time;
        this.level = level;
        this.message = message;
    }

}
