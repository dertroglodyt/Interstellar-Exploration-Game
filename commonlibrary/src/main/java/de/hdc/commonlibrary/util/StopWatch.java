/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.util;

import java.util.HashMap;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;

/**
 * Helper class to keep track of time spend in subroutines.
 * A hash table keeps log of starting times of processes / calls.
 * On presenting the same id as on start() one can then get the elapsed() time.
 *
 * @author dertroglodyt
 */
public class StopWatch {

    /**
     * Keep a log of this point in time tied to the given id.
     *
     * @param id Key to fetch the elapsed time later.
     */
    public static void start(DAUniqueID id) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null!");
        }
        synchronized (starts) {
            starts.put(id, System.nanoTime());
        }
    }

    /**
     * Add the given elapsed time to this point in time tied to the given id.
     *
     * @param id Key to fetch the total elapsed time later.
     * @param elapsed Time previously elapsed.
     */
    public static void resume(DAUniqueID id, DAValue<Duration> elapsed) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null!");
        }
        if (elapsed == null) {
            throw new IllegalArgumentException("elapsed can not be null!");
        }
        synchronized (starts) {
            starts.put(id, System.nanoTime() - elapsed.longValue(SI.NANO(SI.SECOND)));
        }
    }

    /**
     * Returns the elapsed time tied to this key.
     *
     * @param id Key presented earlier while starting / resuming.
     * @return Time in nano seconds since earlier start / resume.
     */
    public static DAValue<Duration> elapsed(DAUniqueID id) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null!");
        }
        return DAValue.create(System.nanoTime() - starts.get(id), SI.NANO(SI.SECOND));
    }

    private static final HashMap<DAUniqueID, Long> starts = new HashMap<>(100);

    private StopWatch() {
    }


}
