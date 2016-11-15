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
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.measure.DecimalMeasure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;

/**
 * Holds an integer value and a corresponding unit.
 * This should be an Immutable.
 *
 * @param <Q> Dimension of the unit.
 *
 * @author Martin
 */
//@SuppressWarnings("serial")
public class DAInteger<Q extends Quantity> extends DataAtom {

    public static <T extends Quantity> DAInteger<T> create(BigInteger value, Unit<T> unit) {
        return new DAInteger<>(value, unit);
    }

    public static <T extends Quantity> DAInteger<T> create(BigDecimal value, Unit<T> unit) {
        return new DAInteger<>(value.toBigInteger(), unit);
    }

    public static <T extends Quantity> DAInteger<T> create(long value, Unit<T> unit) {
        return new DAInteger<>(BigInteger.valueOf(value), unit);
    }

    public static <T extends Quantity> DAInteger<T> create(String value) {
        //todo nötig?
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
        final DecimalMeasure<T> dm = DecimalMeasure.valueOf(v);
        return new DAInteger<>(dm);
    }

    @Deprecated
    public DAInteger() {
        super();
        value = null;
    }

    @Override
    //@SuppressWarnings("unchecked")
    public int doCompare(@NonNull IDataAtom o) {
//        if (!(o instanceof DAInteger)) {
//            return -1;
//        }
        if (!((DAInteger<?>) o).getUnit()
                .isCompatible(value.getUnit())) {
            return -1;
        }
        return value.getValue().compareTo(((DAInteger<?>) o).value.getValue());
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
        final Unit<?> u = value.getUnit().getStandardUnit();
        return value.doubleValue((Unit<Q>) u);
    }

    public double doubleValue(Unit<Q> unit) {
        return value.doubleValue(unit);
    }

    /*
     * Returns truncated result of conversion to another unit.
     */
    public DAInteger<Q> to(Unit<Q> unit) {
        return new DAInteger<>(value.to(unit, MC));
    }

    /**
     *
     * @return 1 / value
     */
    public DAValue<? extends Quantity> inverse() {
        return DAValue.create(BigDecimal.ONE.divide(value.getValue(), MC), getUnit().inverse());
    }

    /**
     *
     * @return -value
     */
    public DAInteger<? extends Quantity> negate() {
        return create(value.getValue().negate(MC), getUnit());
    }

    public DAInteger<Q> add(DAInteger<Q> other) {
        final DAInteger<Q> v = other.to(getUnit());
        return create(value.getValue().add(v.value.getValue(), MC), getUnit());
    }

    public DAInteger<Q> sub(DAInteger<Q> other) {
        final DAInteger<Q> v = other.to(getUnit());
        return create(value.getValue().subtract(v.value.getValue(), MC), getUnit());
    }

    public DAInteger<? extends Quantity> mul(DAInteger<?> other) {
        final BigDecimal bd = value.getValue().multiply(other.value.getValue(), MC);
        return create(bd, getUnit().times(other.getUnit()));
    }

    public DAValue<? extends Quantity> div(DAInteger<?> other) {
        final BigDecimal bd = value.getValue().divide(other.value.getValue(), MC);
        return DAValue.create(bd, getUnit().divide(other.getUnit()));
    }

    public DAInteger<? extends Quantity> divInt(DAInteger<?> other) {
        final BigDecimal bd = value.getValue().divide(other.value.getValue(), MC);
        return create(bd.toBigInteger(), getUnit().divide(other.getUnit()));
    }

    public boolean isZero() {
        return (value.getValue().compareTo(BigDecimal.ZERO) == 0);
    }

    public DAInteger<Q> tail(Unit<Q> u) {
//        return create(value.to(u).getValue().remainder(BigDecimal.ONE), u);
        return new DAInteger<Q>(BigInteger.ZERO, u);
    }

    public DAInteger<Q> head(Unit<Q> u) {
        final BigDecimal bd = value.to(u).getValue();
        return create(bd.subtract(bd.remainder(BigDecimal.ONE)), u);
    }

    @Override
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeUTF(value.toString());
    }

    @Override
    public DAInteger<Q> fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return create(stream.readUTF());
    }

    private static final byte VERSION = 1;
    private static final MathContext MC = new MathContext(30, RoundingMode.HALF_EVEN);

    private final DecimalMeasure<Q> value;

    private DAInteger(DecimalMeasure<Q> value) {
        super();
        this.value = DecimalMeasure.valueOf(new BigDecimal(value.getValue().toBigInteger()), value.getUnit());
    }

    private DAInteger(BigInteger value, Unit<Q> unit) {
        super();
        this.value = DecimalMeasure.valueOf(new BigDecimal(value), unit);
    }

}
