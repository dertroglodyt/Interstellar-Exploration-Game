package de.hdc.commonlibrary.data.atom;

import org.junit.Test;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DADateTimeTest {
    @Test
    public void asDate() {
        DADateTime r = new DADateTime(DAValue.<Duration>create(2457672.78681, NonSI.DAY));
        assertEquals("2016-10-11", r.asDate());
        assertEquals("06:53:00", r.asTime());
    }
}
