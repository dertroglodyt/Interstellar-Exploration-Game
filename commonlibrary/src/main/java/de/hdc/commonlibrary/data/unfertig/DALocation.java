/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.unfertig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAVector;

/**
 * What's this?
 * todo unfertig
 *
 * @author dertroglodyt
 */
public class DALocation implements IDataAtom {

    public static DALocation create(String s) {
        return new DALocation(DAVector.create(SI.METER, 1, 1, 1));
    }

    @Override
    public int doCompare(IDataAtom o) {
        //todo
        return 0;
    }

    @Override
    public String toString() {
        //todo
        return "";
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
//        stream.writeUTF(value);
    }

    @Override
    public DALocation fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return DALocation.create(stream.readUTF());
    }

    private static final byte VERSION = 1;

    private DALocation(DAVector<Length> v) {
        //todo
    }

}
