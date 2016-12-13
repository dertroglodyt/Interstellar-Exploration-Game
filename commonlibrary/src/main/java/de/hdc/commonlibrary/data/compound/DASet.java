/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DAPropertyTable.java
 *
 * Created on 6. Mai 2005, 16:34
 */

package de.hdc.commonlibrary.data.compound;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 * A dynamic length alphabetical ordered ware of properties (actionName, value pairs).
 * Useful for simple INI-files.
 *
 * @author Martin
 */
//@SuppressWarnings("serial")
public abstract class DASet<Q extends IDataAtom> extends DataAtom {

    protected DASet() {
        super();
        this.set = new ConcurrentSkipListSet<Q>();
    }

    public void removeAll() {
        set.clear();
    }

    public Iterator<Q> getItems() {
        return set.iterator();
    }

    public void add(Q value) {
        set.add(value);
    }

    public void remove(Q key) {
        set.remove(key);
    }

    public int size() {
        return set.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DAPropertyTable{(" + set.size() + ")");
        for (Q aSet : set) {
            sb.append(aSet + "\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int doCompare(IDataAtom o) {
//        if (!(o instanceof DASet)) {
//            return -1;
//        }
        if (set.size() != ((DASet) o).size()) {
            return -1;
        }
        if (! ((DASet) o).set.equals(set)) {
            return -1;
        }
        return 0;
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeInt(set.size());
        for (Q aSet : set) {
            aSet.toStream(stream);
        }
    }

//    public static <Q extends IDataAtom> DASet<Q> fromStream(DataInputStream stream) throws IOException {
//        final ConcurrentSkipListSet<Q> set = new ConcurrentSkipListSet<Q>();
//        final byte v = stream.readByte(); // version
//        if (v < 1) {
//            throw new IllegalArgumentException("Invalid version number " + v);
//        }
//        final int x = stream.readInt();
//        for (int i = 0; i < x; i++) {
//            final Q q = Q.fromStream(stream);
//            set.add(q);
//        }
//        return new DASet<Q>(set);
//        ;
//    }

    private final ConcurrentSkipListSet<Q> set;

    private static final byte VERSION = 1;

}
