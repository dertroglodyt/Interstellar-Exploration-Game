package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.IOException;

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
    public DAFlowMap fromStream(DataInputStream stream) throws IOException {
        final DAFlowMap t = new DAFlowMap();
        final byte v = stream.readByte(); // version
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
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
