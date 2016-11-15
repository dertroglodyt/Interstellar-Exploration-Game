/*
 *  Created by DerTroglodyt on 2016-11-15 18:46
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SI;

import static junit.framework.Assert.assertTrue;

/**
 * Created by dertroglodyt on 05.11.16.
 */
public class DAValueTest {
    @Test
    public void toBaseUnit() throws Exception {
        DAValue v1 = DAValue.create(5, SI.METER);
        assertTrue(v1.toBaseUnit().getUnit().toString(), v1.toBaseUnit().getUnit().equals(SI.METER));

        DAValue v2 = DAValue.create(500, SI.CENTI(SI.METER));
        assertTrue(v2.toBaseUnit().getUnit().toString(), v2.toBaseUnit().getUnit().equals(SI.METER));
    }

    @Test
    public void doCompare() throws Exception {
        DAValue v1 = DAValue.create(5, SI.METER);
        assertTrue(v1.doCompare(v1) == 0);

        DAValue v2 = DAValue.create(500, SI.CENTI(SI.METER));
        assertTrue(v1.doCompare(v2) == 0);

        DAValue v3 = DAValue.create(501, SI.CENTI(SI.METER));
        assertTrue(v1.doCompare(v3) == -1);
        assertTrue(v2.doCompare(v3) == -1);

        assertTrue(v3.doCompare(v2) == 1);
        assertTrue(v3.doCompare(v1) == 1);

        v1 = DAValue.create(5.0, SI.METER).div(DAValue.create(200.0, Dimensionless.UNIT));
        assertTrue(v1.doCompare(v1) == 0);

        v2 = DAValue.create(2.5, SI.CENTI(SI.METER));
        assertTrue(v1 + "###" + v2, v1.doCompare(v2) == 0);

        v1 = DAValue.create(1.0, SI.METER).div(DAValue.create(300.0, Dimensionless.UNIT));
        assertTrue(v1.doCompare(v1) == 0);

        v2 = DAValue.create("0,333333333333333333333333333333 cm");
        assertTrue("\n" + v1 + "\n" + v2, v1.doCompare(v2) == 0);

        // todo result 0,9999 should be 1,0???
//        v1 = DAValue.create("1,0 cm");
//        v2 = DAValue.create("1 cm").div(DAValue.create("3")).mul(DAValue.create("3"));
//        assertTrue("\n" + v1 + "\n" + v2, v1.doCompare(v2) == 0);
    }
}
