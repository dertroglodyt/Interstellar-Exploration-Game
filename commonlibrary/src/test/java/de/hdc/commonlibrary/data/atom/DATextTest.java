/*
 *  Created by DerTroglodyt on 2016-11-11 17:48
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static junit.framework.Assert.assertTrue;

public class DATextTest {
    @Test
    public void toString1() throws Exception {

    }

    @Test
    public void toStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(64);
        DAText.create("").toStream(new DataOutputStream(bos));
        bos.close();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAText b = new DAText().fromStream(dis);
        assertTrue(b.toString(), "".equals(b.toString()));

        bos = new ByteArrayOutputStream(64);
        DAText.create("′ôäöü@€").toStream(new DataOutputStream(bos));
        bos.close();
        dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        b = new DAText().fromStream(dis);
        assertTrue(b.toString(), "′ôäöü@€".equals(b.toString()));
    }

    @Test
    public void fromStream() throws Exception {

    }
}
