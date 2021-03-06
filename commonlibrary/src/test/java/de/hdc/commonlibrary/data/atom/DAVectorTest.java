/*
 *  Created by DerTroglodyt on 2016-11-15 18:46
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DAVectorTest {
    @Test
    public void toStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        DAVector<Length> expected = DAVector.create(DAValue.create("1 m"), DAValue.create("1 m")
                , DAValue.create("1 m"));
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAVector b = new DAVector().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void normalize() {
        System.out.println("DAValue.normalize:");
        DAVector v = DAVector.create(SI.METER, 3.0, 3.0, 3.0).normalize();
        DAVector r = DAVector.create(SI.METER, 3.0/27.0, 3.0/27.0, 3.0/27.0);
        assertTrue(r.toString() + "->" + v.toString(), r.equals(v));

        v = DAVector.create(SI.METER, 2, 2, 2).normalize();
        r = DAVector.create(SI.METER, 1/3, 1/3, 1/3);
        assertTrue(r.toString() + "->" + v.toString(), r.equals(v));

        v = DAVector.create(SI.METER, 2, 2, 2).normalize();
        r = DAVector.create(SI.METER, 1/3, 1/3, 1/3);
        assertTrue(r.toString() + "->" + v.toString(), r.equals(v));
    }
}
