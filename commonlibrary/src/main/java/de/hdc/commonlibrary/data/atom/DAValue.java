/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DALine.java
 *
 * Created on 6. Mai 2005, 03:13
 */
package de.hdc.commonlibrary.data.atom;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.measure.DecimalMeasure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;

/**
 * Holds a value and a corresponding unit.
 * This should be an Immutable.
 *
 * @param <Q> Dimension of the unit.
 * @author Martin
 */
//@SuppressWarnings("serial")
public class DAValue<Q extends Quantity> extends DataAtom {

    public static <T extends Quantity> DAValue<T> create(BigDecimal value, Unit<T> unit) {
        return new DAValue<>(new BigDecimal(value.toString(), MC), unit);
    }

    public static <T extends Quantity> DAValue<T> create(long value, Unit<T> unit) {
        return new DAValue<>(new BigDecimal(value, MC), unit);
    }

    public static <T extends Quantity> DAValue<T> create(double value, Unit<T> unit) {
        return new DAValue<>(new BigDecimal(value, MC), unit);
    }

    public static <T extends Quantity> DAValue<T> create(String value) {
        // todo nötig?
        final String v = value.replace("[", "")
                .replace("]", "")
                .replace(".", "")
                .replace(",", ".")
                .replace("1/", " /");
//        final int x = v.indexOf(' ');
//        if (x > 0) {
//            final BigDecimal bd = new BigDecimal(v.substring(0, x), MC);
//            Unit u = Unit.valueOf(v.substring(x + 1));
//            this.value = DecimalMeasure.valueOf(bd, (Unit<Q>) u);
//        } else {
//            final BigDecimal bd = new BigDecimal(v.substring(0, x), MC);
//            this.value = DecimalMeasure.valueOf(bd, (Unit<Q>) Dimensionless.UNIT);
//        }
        return new DAValue<T>(DecimalMeasure.valueOf(v));
    }

    @Override
    //@SuppressWarnings("unchecked")
    public int doCompare(@NonNull IDataAtom o) {
        if (!(o instanceof DAValue)) {
            return -1;
        }
        if (!((DAValue<?>) o).getUnit()
                .isCompatible(value.getUnit())) {
            return -1;
        }
        return toBaseUnit().compareTo(((DAValue<?>) o).toBaseUnit());
    }

    @Override
    public String toString() {
        return value.toString().replace('.', ',').replace(" /", "1/").trim();
    }

    public String getValueString() {
        final String s = toString();
        final int x = s.indexOf(' ');
        if (x > 0) {
            return s.substring(0, x);
        }
        return s;
    }

    public String getUnitString() {
        return value.getUnit().toString().replace(" /", "1/");
    }

    public Unit<Q> getUnit() {
        return value.getUnit();
    }

    public long longValue(Unit<Q> unit) {
        return value.longValue(unit);
    }

    @SuppressWarnings("unchecked")
    public double doubleValueBase() {
        return value.doubleValue((Unit<Q>) value.getUnit().getStandardUnit());
    }

    public double doubleValue(Unit<Q> unit) {
        return value.doubleValue(unit);
    }

    public DAValue<Q> to(Unit<Q> unit) {
        return new DAValue<>(value.to(unit, MC));
    }

    public DAValue<? extends Q> toBaseUnit() {
        Unit u = value.getUnit().getStandardUnit();
        return new DAValue<>(value.to(u, MC));
    }

    /**
     * @return 1 / value
     */
    public DAValue<? extends Quantity> inverse() {
        return new DAValue<>(BigDecimal.ONE.divide(value.getValue(), MC), getUnit().inverse());
    }

    /**
     * @return -value
     */
    public DAValue<? extends Quantity> negate() {
        return new DAValue<>(value.getValue().negate(MC), getUnit());
    }

    public DAValue<Q> add(DAValue<Q> other) {
        final DAValue<Q> v = other.to(getUnit());
        return new DAValue<>(value.getValue().add(v.value.getValue(), MC), getUnit());
    }

    public DAValue<Q> sub(DAValue<Q> other) {
        final DAValue<Q> v = other.to(getUnit());
        return new DAValue<>(value.getValue().subtract(v.value.getValue(), MC), getUnit());
    }

    public DAValue<? extends Quantity> mul(DAValue<?> other) {
        BigDecimal bd = value.getValue().multiply(other.value.getValue(), MC);
        return new DAValue<>(bd, getUnit().times(other.getUnit()));
    }

    public DAValue<? extends Quantity> div(DAValue<?> other) {
        BigDecimal bd = value.getValue().divide(other.value.getValue(), MC);
        return new DAValue<>(bd, getUnit().divide(other.getUnit()));
    }

    public boolean isZero() {
        return (value.getValue().signum() == 0);
    }

    public boolean isNegativ() {
        return (value.getValue().signum() == -1);
    }

    public boolean isPositiv() {
        return (value.getValue().signum() == 1);
    }

    public boolean isGreaterThan(DAValue<Q> other) {
        return (value.getValue().compareTo(other.value.getValue()) > 0);
    }

    public boolean isSmallerThan(DAValue<Q> other) {
        return (value.getValue().compareTo(other.value.getValue()) < 0);
    }

    public int sign() {
        return value.getValue().signum();
    }

    public DAValue<Q> tail(Unit<Q> u) {
        return new DAValue<>(value.to(u).getValue().remainder(BigDecimal.ONE), u);
    }

    public DAValue<Q> head(Unit<Q> u) {
        final BigDecimal bd = value.to(u).getValue();
        return new DAValue<>(bd.subtract(bd.remainder(BigDecimal.ONE)), u);
    }

    @Override
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeUTF(value.toString());
    }

    @Override
    public DAValue<Q> fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DecimalMeasure<Q> dm = DecimalMeasure.valueOf(stream.readUTF());
        return new DAValue<>(dm);
    }

    @Deprecated
    public DAValue() {
        super();
        value = null;
    }

    private static final byte VERSION = 1;
    private static final MathContext MC = new MathContext(30, RoundingMode.HALF_EVEN);

    private final DecimalMeasure<Q> value;

    private DAValue(DecimalMeasure<Q> value) {
        super();
        this.value = value;
    }

    private DAValue(BigDecimal value, Unit<Q> unit) {
        super();
        this.value = new DecimalMeasure<Q>(value, unit);
    }
}
