/*
 *  Created by DerTroglodyt on 2016-11-27 01:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DADateTimeTest {
    @Test
    public void toStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        DADateTime expected = DADateTime.create(DAValue.<Duration>create(2457672.78681, NonSI.DAY));
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DADateTime b = new DADateTime().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));


        bos = new ByteArrayOutputStream(1024);
        expected = DADateTime.create(DADateTime.getNEVER());
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        b = new DADateTime().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));
        assertTrue(expected.toString() + "###" + b.toString(), b.equals(DADateTime.getNEVER()));
    }

    @Test
    public void asDate() {
        DADateTime r = DADateTime.create(DAValue.<Duration>create(2457672.78681, NonSI.DAY));
        assertEquals("2016-10-11", r.asDate());
        assertEquals("06:53:00", r.asTime());
    }
}
