/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DADateTime.java
 * TODO: Unschön!
 * Created on 6. Mai 2005, 04:10
 */
package de.hdc.commonlibrary.data.atom;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.IDataAtom;
//todo Neue java date und time klassen verwenden?
/**
 * Holds a point in time in form of it's julian date.
 *
 * @author Martin
 */
@SuppressWarnings("serial")
public class DADateTime extends DataAtom {

    public static final long DAY_SECONDS = 86400L;

    public static final DADateTime now() {
        // Milli seconds since 1.1.1970 00:00:00
        final DAValue<Duration> jd = DAValue.create(System.currentTimeMillis(), SI.MILLI(SI.SECOND));
        return new DADateTime(jd.add(JD_1970_1_1));
    }

    public static final DADateTime create(DAValue<Duration> julianDate) {
        return new DADateTime(julianDate);
    }

    public static final DADateTime create(DADateTime date) {
        return new DADateTime(date.asJulianDate());
    }

    @Deprecated
    public DADateTime() {
        super();
        date = null;
    }

    private DADateTime(DAValue<Duration> julianDate) {
        super();
        this.date = julianDate;
    }

    @Override
    public String toString() {
        if (date != NEVER.asJulianDate()) {
            return asDINDate();
        } else {
            return NEVER_STR;
        }
    }

    @Override
    public int doCompare(IDataAtom o) {
//        if (!(o instanceof DADateTime)) {
//            return -1;
//        }
        return this.date.compareTo(((DADateTime)o).date);
    }

    public String asDINDate() {
        final double jd = date.doubleValue(NonSI.DAY) + 0.5;
        final long z = (long) Math.floor(jd);
        double f = jd - z;
        long a = 0;
        if (z < 2299161L) {
            a = z;
        } else {
            long g = (long) Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + g - (long) Math.floor(g/4);
        }
        long b = a + 1524;
        long c = (long) Math.floor((b-122.1) / 365.25);
        long d = (long) Math.floor(365.25 * c);
        long e = (long) Math.floor((b-d) / 30.6001);
        long day = b - d - (long) (Math.floor(30.6001 * e));
        long month = ((e < 14L) ? e - 1L : e - 13L);
        long year = ((month > 2L) ? c - 4716L : c - 4715L);

        double x;
        int[] r = new int[4];
        for (int i = 0; i < 4; i++) {
            switch(i) {
                case 0:
                    f = f * 24.0;
                    break;
                case 1:
                case 2:
                    f = f * 60.0;
                    break;
                case 3:
                    f = f * 1000.0;
                    break;
            }
            x = Math.floor(f);
            r[i] = (int) x;
            f = f - x;
        }

        return year
                + "-" + ((month < 10L) ? "0" + month : month)
                + '-' + ((day < 10L) ? "0" + day : day)
                + "T" + ((r[0] < 10L) ? "0" + r[0] : r[0])
                + ":" + ((r[1] < 10L) ? "0" + r[1] : r[1])
                + ":" + ((r[2] < 10L) ? "0" + r[2] : r[2])
                + "." + ((r[3] < 10L) ? "0" + r[3] : r[3]);
    }

    public DAValue<Duration> asJulianDate() {
        return date;
    }

    /**
     * Adds the given period to the current date/time.
     * Returns the new DADateTime.
     *
     * @param duration
     *
     * @return
     */
    public DADateTime addDuration(DAValue<Duration> duration) {
        if (date == null) {
            return NEVER;
        }
        return new DADateTime(date.add(duration));
    }

    /**
     * The difference between to dates in time units.
     *
     */
    public DAValue<Duration> sub(DADateTime other) {
        if ((date == null) || (other.isNever())) {
            throw new IllegalArgumentException("Can not subtract from <NEVER>.");
        }
        return date.sub(other.date);
    }

    public boolean isNever() {
        return (date.compareTo(NEVER) == 0);
    }

    public static DADateTime getNEVER() {
        return NEVER;
    }

    /**
     * Checks if this date is in the interval
     * [Min(dateA, dateB))..]Max(dateA, dateB).
     *
     * If dateA == dateB always returns false.
     *
     */
    public boolean fallsInto(DADateTime dateA, DADateTime dateB) {
        if ((dateA == null) || (dateB == null)) {
            throw new IllegalArgumentException();
        }
        if (dateA.compareTo(dateB) <= 0) {
            return ((dateA.compareTo(this) <= 0) && (this.compareTo(dateB) < 0));
        } else {
            return ((dateB.compareTo(this) <= 0) && (this.compareTo(dateA) < 0));
        }
    }

    /**
     * Checks if date is in the interval
     * [Min(startdate, startdate + duration)..]Max(startdate, startdate + duration).
     *
     * If dateA == dateB always returns false.
     *
     */
    public boolean fallsInto(DADateTime startdate, DAValue<Duration> duration) {
        final DADateTime enddate = startdate.addDuration(duration);
        return fallsInto(startdate, enddate);
    }

    @Override
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        date.toStream(stream);
    }

    @Override
    public DADateTime fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DAValue<Duration> adate = new DAValue<Duration>().fromStream(stream);
        return create(adate);
    }

    private static final byte VERSION = 1;
    private static final String NEVER_STR = "<NEVER>";
    private static final DADateTime NEVER = DADateTime.create(DAValue.<Duration>create("-1"));
    private static final DAValue<Duration> JD_1970_1_1 = DAValue.create("2440587.5 [day]");

    private final DAValue<Duration> date;

    private DADateTime(String julianDayNumber) {
        super();
        if (julianDayNumber.equals(NEVER_STR)) {
            this.date = NEVER.asJulianDate();
            return;
        }
        this.date = DAValue.create(julianDayNumber);
    }

    private long getSecondsOfDay() {
        final double tail = (date.doubleValue(NonSI.DAY) + 0.5) % 1;
        return (long) Math.floor(tail * DAY_SECONDS);
    }

}
