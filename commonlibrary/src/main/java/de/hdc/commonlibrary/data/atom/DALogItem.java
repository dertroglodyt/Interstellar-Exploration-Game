/*
 *  Created by DerTroglodyt on 2016-11-27 01:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DATypedResult.java
 *
 * Created on 6. Mai 2005, 19:34
 */

package de.hdc.commonlibrary.data.atom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.IDataAtom;

/**
 * Stores an object with its creation time.
 * @param <T>
 * @author Martin
 */
public class DALogItem<T extends IDataAtom> extends DataAtom {

    private DADateTime time;
    public final T data;

    @Deprecated
    public DALogItem() {
        super();
        time = null;
        data = null;
    }

    public DALogItem(T aData) {
        super();
        time = DADateTime.now();
        data = aData;
    }

    public DALogItem(DADateTime aTime, T aData) {
        super();
        time = aTime;
        data = aData;
    }

    @Override
    public String toString() {
        return time + " " + data;
    }

    public DADateTime getTime() {
        return time;
    }

    public void resetTime() {
        time = DADateTime.now();
    }

    public T getData() {
        return data;
    }

    @Override
    public void toStream(final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        time.toStream(stream);
        stream.writeUTF(data.getClass().getName());
        data.toStream(stream);
    }

    @Override
    public DALogItem<T> fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }

        DADateTime atime = new DADateTime().fromStream(stream);
        String cn = stream.readUTF();
        try {
            final T adata = (T) ((IDataAtom) Class.forName(cn).newInstance()).fromStream(stream);
            return new DALogItem<T>(atime, adata);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    private static final byte VERSION = 1;

}
