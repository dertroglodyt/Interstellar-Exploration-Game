/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.compound;

import java.io.DataInputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAText;

/**
 *
 * @author dertroglodyt
 */
public class DATextMap extends DAMap<DAText, DAText> {

    public static DATextMap create() {
        return new DATextMap();
    }

    @Override
    public DATextMap fromStream(DataInputStream stream) throws IOException {
        final DATextMap t = new DATextMap();
        final byte v = stream.readByte(); // version
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final int x = stream.readInt();
        for (int i = 0; i < x; i++) {
            final DAText key = new DAText().fromStream(stream);
            final DAText a = new DAText().fromStream(stream);
            t.set(key, a);
        }
        return t;
    }

    private static final byte VERSION = 1;

    @Deprecated
    public DATextMap() {
        super();
    }

}
