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
import java.util.concurrent.CopyOnWriteArrayList;

import javax.measure.quantity.Dimensionless;
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
        BigDecimal sum = BigDecimal.ZERO;
        for (DAValue<Q> v : vector) {
            DAValue<Q> x = v.to(u);
            sum = sum.add(x.sqr().getBigDecimal());
        }
        DAValue si = DAValue.create(sum, Dimensionless.UNIT).sqrt();
        final List<DAValue<Q>> vr = new ArrayList<>(x);
        for (DAValue<Q> v : vector) {
            vr.add((DAValue<Q>) v.div(si));
        }
        return new DAVector<Q>(vr);
    }

    @Override
    public int doCompare(IDataAtom o) {
//        if (!(o instanceof DAVector)) {
//            return -1;
//        }
        if (vector.size() != ((DAVector<?>) o).getSize()) {
            return -1;
        }
        if (!((DAVector<?>) o).getUnit().isCompatible(getUnit())) {
            return -1;
        }
        for (int i = 0; i < vector.size(); i++) {
            if (0 != vector.get(i).doCompare((((DAVector<?>) o).vector.get(i)))) {
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

        stream.writeByte(vector.size());
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
        final int x = stream.readByte();
        List<DAValue<Q>> avector = new ArrayList<DAValue<Q>>(x);
        for (int i = 0; i < x; i++) {
            avector.add(new DAValue().fromStream(stream));
        }
        return new DAVector<Q>(avector);
    }

    private static final byte VERSION = 1;

    private final CopyOnWriteArrayList<DAValue<Q>> vector;

    @Deprecated
    public DAVector() {
        super();
        vector = null;
    }

    private DAVector(Unit<Q> unit, BigDecimal... values) {
        super();
        vector = new CopyOnWriteArrayList<DAValue<Q>>();
        for (BigDecimal v : values) {
            vector.add(DAValue.create(v, unit));
        }
    }

    private DAVector(Unit<Q> unit, double... values) {
        super();
        vector = new CopyOnWriteArrayList<DAValue<Q>>();
        for (double v : values) {
            vector.add(DAValue.create(BigDecimal.valueOf(v), unit));
        }
    }

    private DAVector(List<DAValue<Q>> values) {
        super();
        vector = new CopyOnWriteArrayList<>();
        vector.addAll(values);
    }

    private DAVector(DAValue<Q>... values) {
        super();
        vector = new CopyOnWriteArrayList<>();
        Collections.addAll(vector, values);
    }

}
