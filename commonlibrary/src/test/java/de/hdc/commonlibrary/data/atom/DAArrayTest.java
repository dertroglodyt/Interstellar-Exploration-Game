/*
 *  Created by DerTroglodyt on 2016-11-23 08:11
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

import static junit.framework.Assert.assertTrue;

public class DAArrayTest {
    @Test
    public void toStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        DAArray<DAValue<Length>> expected = DAArray.create();
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAArray b = new DAArray().fromStream(dis);
        assertTrue(b.toString(), expected.equals(b));

        bos = new ByteArrayOutputStream(1024);
        expected = DAArray.create();
        expected.add(DAValue.create("1 m"));
        expected.add(DAValue.create("10 m"));
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        b = new DAArray().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));
    }

    @Test
    public void fromStream() throws Exception {

    }
}
