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

    public static DADateTime now() {
        // Milli seconds since 1.1.1970 00:00:00
        final DAValue<Duration> jd = DAValue.create(System.currentTimeMillis(), SI.MILLI(SI.SECOND));
        return new DADateTime(jd.add(JD_1970_1_1));
    }

    @Deprecated
    public DADateTime() {
        super();
        date = null;
    }

    public DADateTime(DAValue<Duration> julianDate) {
        super();
        this.date = julianDate;
    }

    @Override
    public String toString() {
        if (date != null) {
            return date.to(NonSI.DAY).toString() + " [d]";
        } else {
            return NEVER_STR;
        }
    }

    @Override
    public int doCompare(IDataAtom o) {
        if (!(o instanceof DADateTime)) {
            return -1;
        }
        if (date != null) {
            return this.date.compareTo(((DADateTime)o).date);
        } else {
            if (((DADateTime)o).date == null) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    //todo result is plain wrong
    public String asDate() {
        final double jd = date.doubleValue(NonSI.DAY);
        final long z = (long) Math.floor(jd + 0.5);
        final double f = (jd + 0.5) - (double) z;
        long a = 0;
        if (z < 2299161L) {
            a = z;
        } else {
            long g = (long) Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + g - (long) Math.floor(g/4.0);
        }
        long b = a + 1524;
        long c = (long) Math.floor((b-122.1) / 365.25);
        long d = (long) Math.floor(365.25 * c);
        long e = (long) Math.floor((b-d) / 30.6001);
        long day = b - d - (long) (Math.floor(30.6001 * e) + f);
        long month = ((e < 14L) ? e - 1L : e - 13L);
        long year = ((month > 2L) ? c - 4716L : c - 4715L);

        return year + "-" + ((month < 10L) ? "0" + month : month)
                + '-' + ((day < 10L) ? "0" + day : day);
    }

    public String asTime() {
        long sec = getSecondsOfDay();
        long s = (long) Math.floor(sec % 60);
        long m = (long) Math.floor(sec / 60 % 60);
        long h = (long) Math.floor(sec / 3600 % 24);

        String r = ((h < 10) ? "0" + h : h) + ":"
                + ((m < 10) ? "0" + m : m) + ":"
                + ((s < 10) ? "0" + s : m);
        return r;
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
        return (date == null);
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
        stream.writeUTF(toString());
    }

    @Override
    public DADateTime fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return new DADateTime(stream.readUTF());
    }

    private static final byte VERSION = 1;
    private static final String NEVER_STR = "<NEVER>";
    private static final DADateTime NEVER = new DADateTime(NEVER_STR);
    private static final DAValue<Duration> JD_1970_1_1 = DAValue.create("2440587.5 [day]");

    private final DAValue<Duration> date;

    private DADateTime(String julianDayNumber) {
        super();
        if (julianDayNumber.equals(NEVER_STR)) {
            this.date = null;
            return;
        }
        this.date = DAValue.create(julianDayNumber);
    }

    private long getSecondsOfDay() {
        final double tail = (date.doubleValue(NonSI.DAY) + 0.5) % 1;
        return (long) Math.floor(tail * DAY_SECONDS);
    }

}
