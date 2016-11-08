package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.compound.DASet;

/**
 * Created by dertroglodyt on 07.11.16.
 */

public class DAPatentSet extends DASet<DAPatent> {

    public static DAPatentSet create(){
        return new DAPatentSet();
    }

    @Override
    public DAPatentSet fromStream(DataInputStream stream) throws IOException {
        final DAPatentSet set = new DAPatentSet();
        final byte v = stream.readByte(); // version
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final int x = stream.readInt();
        for (int i = 0; i < x; i++) {
            final DAPatent q = new DAPatent().fromStream(stream);
            set.add(q);
        }
        return set;
    }

    @Deprecated
    public DAPatentSet() {
        super();
    }
}
