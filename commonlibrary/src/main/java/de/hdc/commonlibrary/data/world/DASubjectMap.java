/*
 *  Created by DerTroglodyt on 2016-11-28 11:04
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.world;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.market.IDASubject;

public class DASubjectMap implements IDataAtom {

    @Deprecated
    public DASubjectMap() {
        map = null;
    }

    public IDASubject get(DAUniqueID id) {
        return map.get(id);
    }

    public void add(IDASubject subject) {
        map.put(subject.getId(), subject);
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeInt(map.values().size());
        for (IDASubject sub : map.values()) {
            stream.writeUTF(sub.getClass().getName());
            sub.toStream(stream);
        }
    }

    @Override
    public DASubjectMap fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        int x = stream.readInt();
        ConcurrentHashMap<DAUniqueID, IDASubject> amap = new ConcurrentHashMap<>(x);
        try {
            for (int i=0; i < x; i++) {
                Class<?> c = Class.forName(stream.readUTF());
                IDASubject sub = (IDASubject) ((IDASubject) c.newInstance()).fromStream(stream);
                amap.put(sub.getId(), sub);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return new DASubjectMap(amap);
    }

    private static final byte VERSION = 1;

    private final ConcurrentHashMap<DAUniqueID, IDASubject> map;

    private DASubjectMap(ConcurrentHashMap<DAUniqueID, IDASubject> map) {
        this.map = map;
    }
}
