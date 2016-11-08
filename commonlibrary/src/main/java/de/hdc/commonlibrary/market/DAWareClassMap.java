package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.compound.DAMap;

/**
 * Global Map of all know DAWareClass.
 *
 * Created by dertroglodyt on 07.11.16.
 */

public class DAWareClassMap extends DAMap<DAUniqueID, DAWareClass> {

    public static DAWareClassMap create() {
        return new DAWareClassMap();
    }

    @Override
    public DAWareClassMap fromStream(DataInputStream stream) throws IOException {
        final DAWareClassMap t = new DAWareClassMap();
        final byte v = stream.readByte(); // version
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final int x = stream.readInt();
        for (int i = 0; i < x; i++) {
            final DAUniqueID key = new DAUniqueID().fromStream(stream);
            final DAWareClass a = new DAWareClass().fromStream(stream);
            t.set(key, a);
        }
        return t;
    }

    private static final byte VERSION = 1;

    @Deprecated
    public DAWareClassMap() {
        super();
    }
}
