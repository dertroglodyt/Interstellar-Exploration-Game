/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.atom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;

import static android.R.attr.x;

/**
 * What's this?
 *
 * @author dertroglodyt
 * @param <Q>
 */
public class DAVector<Q extends Quantity> extends DataAtom {

    public static <T extends Quantity> DAVector<T> create(DAValue<T>... x) {
        return new DAVector<T>(x);
    }

    public static <T extends Quantity> DAVector<T> create(Unit<T> unit, BigDecimal... x) {
        return new DAVector<>(unit, x);
    }

    public static <T extends Quantity> DAVector<T> create(Unit<T> unit, double... x) {
        return new DAVector<T>(unit, x);
    }

    @Override
    public String toString() {
        return "DAVector{" + vector + '}';
    }

    @SuppressWarnings("unchecked")
    public DAVector<Q> normalize() {
        Unit u = vector.get(0).getUnit().getStandardUnit();
        DAValue<Q> r = DAValue.<Q>create(0L, u);
        for (DAValue<Q> v : vector) {
            r = r.add(v.to(u));
        }
        r = (DAValue<Q>)r.div(DAValue.<Q>create(vector.size(), u));
        final List<DAValue<Q>> vr = new ArrayList<>(x);
        for (DAValue<Q> v : vector) {
            vr.add((DAValue<Q>) v.div(r));
        }
        return new DAVector<Q>(vr);
    }

    @Override
    public int doCompare(IDataAtom o) {
        if (!(o instanceof DAVector)) {
            return -1;
        }
        if (!((DAVector<?>) o).getUnit().isCompatible(getUnit())) {
            return -1;
        }
        if (vector.size() != ((DAVector<?>) o).getSize()) {
            return -1;
        }
        for (int i = 0; i < vector.size(); i++) {
            if (! vector.get(i).equals(((DAVector<?>) o).vector.get(i))) {
                return -1;
            }
        }
        return 0;
    }

    public Unit<Q> getUnit() {
        return vector.get(0).getUnit();
    }

    public int getSize() {
        return vector.size();
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.write(vector.size());
        for (DAValue<Q> v : vector) {
            v.toStream(stream);
        }
    }

    @Override
    public DAVector<Q> fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final int x = stream.readInt();
        List<DAValue<Q>> avector = new ArrayList<DAValue<Q>>(x);
        for (int i = x; i < x; i++) {
            avector.add(new DAValue().fromStream(stream));
        }
        return new DAVector<Q>(vector);
    }

    private static final byte VERSION = 1;

    private final ArrayList<DAValue<Q>> vector;

    private DAVector() {
        super();
        throw new IllegalAccessError();
    }

    private DAVector(Unit<Q> unit, BigDecimal... values) {
        super();
        vector = new ArrayList<DAValue<Q>>(values.length);
        for (BigDecimal v : values) {
            vector.add(DAValue.create(v, unit));
        }
    }

    private DAVector(Unit<Q> unit, double... values) {
        super();
        vector = new ArrayList<DAValue<Q>>(values.length);
        for (double v : values) {
            vector.add(DAValue.create(BigDecimal.valueOf(v), unit));
        }
    }

    private DAVector(List<DAValue<Q>> values) {
        super();
        vector = new ArrayList<>(values.size());
        vector.addAll(values);
    }

    private DAVector(DAValue<Q>... values) {
        super();
        vector = new ArrayList<>(values.length);
        Collections.addAll(vector, values);
    }

}
