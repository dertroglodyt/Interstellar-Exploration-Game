/*
 *  Created by DerTroglodyt on 2016-11-11 15:54
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by dertroglodyt on 05.11.16.
 */
public class DABooleanTest {
    @Test
    public void fromStream() throws Exception {
    }

    @Test
    public void toStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(64);
        DABoolean.create(true).toStream(new DataOutputStream(bos));
        bos.close();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DABoolean b = new DABoolean().fromStream(dis);
        assertTrue(b.toString(), "TRUE".equals(b.toString()));

        bos = new ByteArrayOutputStream(64);
        DABoolean.create(false).toStream(new DataOutputStream(bos));
        bos.close();
        dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        b = new DABoolean().fromStream(dis);
        assertFalse(b.toString(), "TRUE".equals(b.toString()));
    }

    @Test
    public void toString1() throws Exception {
        DABoolean v1 = DABoolean.create(true);
        assertTrue("TRUE".equals(v1.toString()));

        v1 = DABoolean.create(false);
        assertTrue("FALSE".equals(v1.toString()));
    }

    @Test
    public void compareTo() throws Exception {
        DABoolean v1 = DABoolean.create(true);
        assertTrue(v1.compareTo(v1) == 0);

        DABoolean v2 = DABoolean.create(true);
        assertTrue(v1.compareTo(v2) == 0);

        DABoolean v3 = DABoolean.create(false);
        assertTrue(v1.compareTo(v3) + "", v1.compareTo(v3) != 0);

        assertTrue(DABoolean.FALSE.compareTo(v1) != 0);
    }

    @Test
    public void equals() throws Exception {
        DABoolean v1 = DABoolean.create(true);
        assertTrue(DABoolean.TRUE.equals(v1));

        v1 = DABoolean.create(false);
        assertTrue(DABoolean.FALSE.equals(v1));

        v1 = DABoolean.create(true);
        assertFalse(DABoolean.FALSE.equals(v1));
    }

}
