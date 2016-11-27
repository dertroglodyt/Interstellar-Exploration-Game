/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.util.StopWatch;
/**
 * Prototype for modules that get triggered on events / or every tick.
 * Modules may have a state which gets restored after world load.
 *
 * @author dertroglodyt
 */
public abstract class DATickable implements IDataAtom {

    protected DATickable() {
        super();
        timerID = DAUniqueID.createRandom();
        elapsed = DAValue.create(0, SI.SECOND);
    }

//    public DATickable(DAUniqueID id, DAValue<Duration> elapsed) {
//        super();
//        this.timerID = id;
//        this.elapsed = elapsed;
//    }

    @Override
    public String toString() {
        return "Module: " + timerID + " (" + elapsed.toString() + ")";
    }

    public final void tick(DAValue<Duration> timeSinceLastTick) {
        StopWatch.start(timerID);
        tickImpl(timeSinceLastTick);
        lock.lock();
        try {
            elapsed = elapsed.add(StopWatch.elapsed(timerID));
        } finally {
            lock.unlock();
        }
    }

    public DAValue<Duration> getElapsed() {
        // Return a copy of elapsed.
        lock.lock();
        try {
            return elapsed.to(SI.SECOND);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        timerID.toStream(stream);
        elapsed.toStream(stream);
    }

    @Override
    public DATickable fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        timerID = new DAUniqueID().fromStream(stream);
        elapsed = new DAValue<Duration>().<Duration>fromStream(stream);
        return this;
    }

    /**
     * Must be implemented by extending class.
     */
    protected abstract void tickImpl(DAValue<Duration> timeSinceLastTick);

    private static final byte VERSION = 1;

    private DAUniqueID timerID;
    private DAValue<Duration> elapsed = DAValue.create(0, SI.SECOND);
    private final transient Lock lock = new ReentrantLock();

}
