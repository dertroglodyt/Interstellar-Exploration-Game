package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

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

    }
}
