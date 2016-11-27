/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.atom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hdc.commonlibrary.data.IDataAtom;

/**
 * What's this?
 *
 * @author dertroglodyt
 * @param <Q>
 */
public class DAArray<Q extends IDataAtom> extends DataAtom implements Iterable<Q> {

    public static <T extends IDataAtom> DAArray<T> create(T... x) {
        return new DAArray<T>(x);
    }

    @Override
    public String toString() {
        return "DAVector{" + vector + '}';
    }

    @Override
    public int doCompare(IDataAtom o) {
//        if (!(o instanceof DAVector)) {
//            return -1;
//        }
        if (vector.size() != ((DAArray<?>) o).getSize()) {
            return -1;
        }
        for (int i = 0; i < vector.size(); i++) {
            if (0 != vector.get(i).doCompare((((DAArray<?>) o).vector.get(i)))) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public Iterator<Q> iterator() {
        return vector.iterator();
    }

    public int getSize() {
        return vector.size();
    }

    public void add(Q item) {
        vector.add(item);
    }

    public void addAll(Collection<Q> item) {
        vector.addAll(item);
    }

    public boolean remove(Q item) {
        return vector.remove(item);
    }

    public void clear() {
        vector.clear();
    }

    public Q get(int index) {
        return vector.get(index);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        stream.write(vector.size());
        if (vector.size() > 0) {
            stream.writeUTF(vector.get(0).getClass().getName());
            for (Q v : vector) {
                v.toStream(stream);
            }
        }
    }

    @Override
    public DAArray<Q> fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        try {
            final int x = stream.readByte();
            List<Q> avector = new ArrayList<Q>(x);
            if (x > 0) {
                final Class<?> c = Class.forName(stream.readUTF());
                for (int i = 0; i < x; i++) {
                    avector.add((Q) ((IDataAtom) c.newInstance()).fromStream(stream));
                }
            }
            return new DAArray<Q>(avector);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
    }

    private static final byte VERSION = 1;

    private final CopyOnWriteArrayList<Q> vector;

    @Deprecated
    public DAArray() {
        super();
        vector = null;
    }

    private DAArray(List<Q> values) {
        super();
        vector = new CopyOnWriteArrayList<Q>();
        vector.addAll(values);
    }

    private DAArray(Q... values) {
        super();
        vector = new CopyOnWriteArrayList<Q>();
        Collections.addAll(vector, values);
    }

}
