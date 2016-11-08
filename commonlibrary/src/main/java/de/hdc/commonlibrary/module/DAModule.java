/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.module;

import android.support.annotation.NonNull;

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
public class DAModule implements IDataAtom {

    public final DAUniqueID id;

    // Only used for deserialisation.
    @Deprecated
    public DAModule() {
        super();
        id = DAUniqueID.createRandom();
        elapsed = DAValue.create(0, SI.SECOND);
    }

    public DAModule(DAUniqueID id, DAValue<Duration> elapsed) {
        super();
        this.id = id;
        this.elapsed = elapsed;
    }

    @Override
    public String toString() {
        return "Module: " + id + " (" + elapsed.toString() + ")";
    }

    public final void tick(DAValue<Duration> timeSinceLastTick) {
        StopWatch.start(id);
        tickImpl(timeSinceLastTick);
        lock.lock();
        try {
            elapsed = elapsed.add(StopWatch.elapsed(id));
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
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        id.toStream(stream);
        elapsed.toStream(stream);
    }

    @Override
    public DAModule fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        final DAUniqueID id = new DAUniqueID().fromStream(stream);
        final DAValue<Duration> val = new DAValue().<Duration>fromStream(stream);
        return new DAModule(id, val);
    }

    protected void tickImpl(DAValue<Duration> timeSinceLastTick) {
        throw new IllegalAccessError();
    }

    private static final byte VERSION = 1;

    private final transient Lock lock = new ReentrantLock();
    private DAValue<Duration> elapsed = DAValue.create(0, SI.SECOND);

}
