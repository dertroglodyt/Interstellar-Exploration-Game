/*
 *  Created by DerTroglodyt on 2016-11-27 01:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.compound.DAMap;

/**
 * Created by dertroglodyt on 07.11.16.
 */

public class DAFlowMap extends DAMap<DAUniqueID, DAGoodFlow> {

    public static DAFlowMap create() {
        return new DAFlowMap();
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeInt(VERSION);
        stream.writeInt(table.size());
        for (Map.Entry<DAUniqueID, DAGoodFlow> entry : table.entrySet()) {
            entry.getKey().toStream(stream);
            entry.getValue().toStream(stream);
        }
    }

    @Override
    public DAFlowMap fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte(); // version
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DAFlowMap t = new DAFlowMap();
        final int x = stream.readInt();
        for (int i = 0; i < x; i++) {
            final DAUniqueID key = new DAUniqueID().fromStream(stream);
            final DAGoodFlow a = new DAGoodFlow().fromStream(stream);
            t.set(key, a);
        }
        return t;
    }

    private static final byte VERSION = 1;

    @Deprecated
    public DAFlowMap() {
        super();
    }
}
