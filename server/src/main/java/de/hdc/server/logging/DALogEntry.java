package de.hdc.server.logging;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.compound.DAResult;

/**
 * Created by DerTroglodyt on 2016-12-17 11:09.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class DALogEntry extends DataAtom {

    public static DALogEntry create(DAText source, DAResult.ResultType level, DAText message) {
        return new DALogEntry(source, DADateTime.now(), level, message);
    }

    public final DAText source;
    public final DADateTime time;
    public final DAResult.ResultType level;
    public final DAText message;

    @Deprecated
    public DALogEntry() {
        source = null;
        time = null;
        level = null;
        message = null;
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        source.toStream(stream);
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
        DAText aip = new DAText().fromStream(stream);
        DADateTime atime = new DADateTime().fromStream(stream);
        DAResult.ResultType alevel = DAResult.ResultType.valueOf(stream.readUTF());
        DAText amsg = new DAText().fromStream(stream);
        return new DALogEntry(aip, atime, alevel, amsg);
    }

    private static final byte VERSION = 1;

    private DALogEntry(DAText source, DADateTime time, DAResult.ResultType level, DAText message) {
        super();
        this.source = source;
        this.time = time;
        this.level = level;
        this.message = message;
    }

}
