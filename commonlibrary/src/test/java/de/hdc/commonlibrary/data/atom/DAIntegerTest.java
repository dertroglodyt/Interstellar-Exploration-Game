/*
 *  Created by DerTroglodyt on 2016-11-11 16:34
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import static javax.measure.unit.SI.KILO;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DAIntegerTest {
    @Test
    public void toString1() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.toString(), "12 m".equals(i.toString()));

        i = DAInteger.create(BigDecimal.valueOf(-312.9), SI.METER);
        assertTrue(i.toString(), "-312 m".equals(i.toString()));

        i = DAInteger.create(BigInteger.valueOf(12), SI.METER);
        assertTrue(i.toString(), "12 m".equals(i.toString()));

        i = DAInteger.create(BigInteger.valueOf(-312), SI.METER);
        assertTrue(i.toString(), "-312 m".equals(i.toString()));

        i = DAInteger.create(12, SI.METER);
        assertTrue(i.toString(), "12 m".equals(i.toString()));

        i = DAInteger.create(-312, SI.METER);
        assertTrue(i.toString(), "-312 m".equals(i.toString()));

        i = DAInteger.create("12,3 m");
        assertTrue(i.toString(), "12 m".equals(i.toString()));

        i = DAInteger.create("-312,9 m");
        assertTrue(i.toString(), "-312 m".equals(i.toString()));
    }

    @Test
    public void getValueString() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.getValueString(), "12".equals(i.getValueString()));
    }

    @Test
    public void getUnitString() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.getUnitString(), "m".equals(i.getUnitString()));

        DAInteger<Dimensionless> i2 = DAInteger.create(BigDecimal.valueOf(12.3), Unit.ONE);
        assertTrue(i2.getUnitString(), "".equals(i2.getUnitString()));
    }

    @Test
    public void getUnit() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.getUnit().toString(), SI.METER.equals(i.getUnit()));

        DAInteger<Dimensionless> i2 = DAInteger.create(BigDecimal.valueOf(12.3), Unit.ONE);
        assertTrue(i2.getUnit().toString(), Unit.ONE.equals(i2.getUnit()));
    }

    @Test
    public void longValue() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.longValue(SI.CENTIMETER)+"", 1200L == i.longValue(SI.CENTIMETER));
    }

    @Test
    public void doubleValueBase() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.CENTIMETER);
        assertTrue(i.doubleValueBase()+"", 0.12 == i.doubleValueBase());
    }

    @Test
    public void doubleValue() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.doubleValue(SI.CENTIMETER)+"", 1200.0 == i.doubleValue(SI.CENTIMETER));
    }

    @Test
    public void to() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12000.3), SI.METER);
        assertTrue(i.doubleValueBase()+"", "1200000 cm".equals(i.to(SI.CENTIMETER).toString()));

        i = DAInteger.create(BigDecimal.valueOf(12000), SI.METER);
        assertTrue(i.to(SI.KILO(SI.METER)).toString(), "12 km".equals(i.to(SI.KILO(SI.METER)).toString()));
    }

    @Test
    public void inverse() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(4.3), SI.METER);
        assertTrue(i.inverse().doubleValueBase()+"", 0.25 == i.inverse().doubleValueBase());
        assertTrue(SI.METER.inverse().equals(i.inverse().getUnit()));
        assertTrue(i.inverse().toString()+"", "0,25 1/m".equals(i.inverse().toString()));
    }

    @Test
    public void negate() throws Exception {
        DAInteger<Length> i = DAInteger.create(BigDecimal.valueOf(12.3), SI.METER);
        assertTrue(i.negate().toString()+"", "-12 m".equals(i.negate().toString()));
    }

    @Test
    public void add() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(4.3), SI.METER);
        DAInteger<Length> i2 = DAInteger.create(BigDecimal.valueOf(8.9), SI.METER);
        assertTrue("12 m".equals(i1.add(i2).toString()));

        i2 = DAInteger.create(BigDecimal.valueOf(-8.9), SI.METER);
        assertTrue("-4 m".equals(i1.add(i2).toString()));
    }

    @Test
    public void sub() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(4.3), SI.METER);
        DAInteger<Length> i2 = DAInteger.create(BigDecimal.valueOf(8.9), SI.METER);
        assertTrue("-4 m".equals(i1.sub(i2).toString()));

        assertTrue("4 m".equals(i2.sub(i1).toString()));
    }

    @Test
    public void mul() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(4.3), SI.METER);
        DAInteger<Length> i2 = DAInteger.create(BigDecimal.valueOf(8.9), SI.METER);
        assertTrue(i1.mul(i2).getUnit().toString(), SI.SQUARE_METRE.equals(i1.mul(i2).getUnit()));
        assertTrue(i1.mul(i2).toString(), "32 m²".equals(i1.mul(i2).toString()));

        assertTrue(i2.sub(i1).toString(), "32 m²".equals(i2.mul(i1).toString()));
    }

    @Test
    public void div() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(4.3), SI.METER);
        DAInteger<Length> i2 = DAInteger.create(BigDecimal.valueOf(8.9), SI.METER);
        assertTrue(i1.div(i2).toString(), "0,5".equals(i1.div(i2).toString()));

        assertTrue("2".equals(i2.div(i1).toString()));
    }

    @Test
    public void divInt() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(4.3), SI.METER);
        DAInteger<Length> i2 = DAInteger.create(BigDecimal.valueOf(8.9), SI.METER);
        assertTrue(i1.divInt(i2).toString(), "0".equals(i1.divInt(i2).toString()));
    }

    @Test
    public void isZero() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(0.0), SI.METER);
        assertTrue(i1.isZero());
        i1 = DAInteger.create(BigDecimal.valueOf(2.0), SI.METER);
        assertFalse(i1.isZero());
        i1 = DAInteger.create(BigDecimal.valueOf(-4.0), SI.METER);
        assertFalse(i1.isZero());
    }

    @Test
    public void tail() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(12.6), SI.METER);
        assertTrue(i1.tail(KILO(SI.METER)).toString(), "0 km".equals(i1.tail(KILO(SI.METER)).toString()));

        i1 = DAInteger.create(BigDecimal.valueOf(12.6), KILO(SI.METER));
        assertTrue(i1.tail(SI.METER).toString(), "0 m".equals(i1.tail(SI.METER).toString()));
    }

    @Test
    public void head() throws Exception {
        DAInteger<Length> i1 = DAInteger.create(BigDecimal.valueOf(12.6), SI.METER);
        assertTrue(i1.head(SI.METER).toString(), "12 m".equals(i1.head(SI.METER).toString()));
    }

    @Test
    public void toStream() throws Exception {

    }

    @Test
    public void fromStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(64);
        DAInteger.<Length>create("12 m").toStream(new DataOutputStream(bos));
        bos.close();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAInteger<Length> b = new DAInteger().fromStream(dis);
        assertTrue(SI.METER.equals(b.getUnit()));
        assertTrue(b.toString(), "12 m".equals(b.toString()));

        bos = new ByteArrayOutputStream(64);
        DAInteger.<Mass>create(64, SI.KILOGRAM).toStream(new DataOutputStream(bos));
        bos.close();
        dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAInteger<Mass> c = new DAInteger().fromStream(dis);
        assertTrue(SI.KILOGRAM.equals(c.getUnit()));
        assertTrue(c.toString(), "64 kg".equals(c.toString()));
    }
}
