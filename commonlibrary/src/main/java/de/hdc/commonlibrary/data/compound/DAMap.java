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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 * A dynamic length alphabetical ordered ware of properties (name, value pairs).
 * Useful for simple INI-files.
 *
 * @author Martin
 */
//@SuppressWarnings("serial")
public abstract class DAMap<Q extends IDataAtom, T extends IDataAtom> extends DataAtom {

    protected DAMap() {
        super();
        table = new ConcurrentSkipListMap<>();
    }

    public void removeAll() {
        table.clear();
    }

    public Set<Q> getKeySet() {
        return table.keySet();
    }

    public T get(Q key) {
        return table.get(key);
    }

    public T get(Q key, T defaultValue) {
        final T model = table.get(key);
        if (model != null) {
            return model;
        }
        return defaultValue;
    }

    public void set(Q key, T value) {
        table.put(key, value);
    }

    public void remove(Q key) {
        table.remove(key);
    }

    public Collection<T> getValueList() {
        return table.values();
    }

    public int size() {
        return table.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DAPropertyTable{(" + table.size() + ")");
        for (Map.Entry<Q, T> qtEntry : table.entrySet()) {
            sb.append(qtEntry.getKey() + ": " + qtEntry.getValue() + "\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int doCompare(IDataAtom o) {
//        if (!(o instanceof DAMap)) {
//            return -1;
//        }
        for (Map.Entry<Q, T> qtEntry : table.entrySet()) {
            if (((DAMap) o).get(qtEntry.getKey()) == null) {
                return -1;
            }
            if (((DAMap) o).get(qtEntry.getKey()) != qtEntry.getValue()) {
                return -1;
            }
        }
        return 0;
}

//    @Override
//    public void toStream(DataOutputStream stream) throws IOException {
//        stream.writeByte(VERSION);
//        stream.writeInt(table.size());
//        for (Map.Entry<Q, T> entry : table.entrySet()) {
//            entry.getKey().toStream(stream);
//            entry.getValue().toStream(stream);
//        }
//    }

//    public static DATextList<Q, T> fromStream(DataInputStream stream) throws IOException {
//        final byte v = stream.readByte(); // version
//        if (v < 1) {
//            throw new IllegalArgumentException("Invalid version number " + v);
//        }
//        final DAMap<?, ?> t = new DAMap();
//        final int x = stream.readInt();
//        for (int i = 0; i < x; i++) {
//            final Q key = Q.fromStream(stream);
//            final T a = T.fromStream(stream);
//            t.setValue(key, a);
//        }
//        return t;
//    }

    protected final ConcurrentSkipListMap<Q, T> table;

    private static final byte VERSION = 1;

}
