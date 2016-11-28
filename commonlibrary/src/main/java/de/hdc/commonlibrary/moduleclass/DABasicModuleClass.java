/*
 *  Created by DerTroglodyt on 2016-11-10 10:32
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.moduleclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Duration;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.atom.DABoolean;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.market.DAPatentSet;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.module.DADamage;
import de.hdc.commonlibrary.module.DAGoodFlow;
import de.hdc.commonlibrary.module.DAGoodFlowList;

import static de.hdc.commonlibrary.market.DAWareTypeTreeBootstrap.ELECTRICAL_POWER;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DABasicModuleClass extends DAWareClass {

    /**
     * Volume/Amount flow of needed goods in [<UNIT>/s].
     * For Example: Air condition needed/produced.
     * For Example: Fuel needed/produced.
     * Positive values mean good is produced.
     */
    public final DAGoodFlowList goodFlow;
    /**
     * Amount of Energy [Joule] which can be absorbed before being destroyed.
     */
    public final DAValue<Energy> maxHitPoints;
    /**
     * Amount of Energy [Joule] which is absorbed without harm.
     */
    public final DADamage resistance;
    /**
     * Delay after setting a module to online before it is functional.
     */
    public final DAValue<Duration> onlineDelay;
    /**
     * Does this module shut down on Overflow of input?
     */
    public final DABoolean offLineOnOverflow;
    /**
     * Patents necessary for operating this module.
     */
    public final DAPatentSet prerequisites;

    public static DABasicModuleClass create(DAUniqueID id, DAUniqueID typeID, DAText name, DAText description
            , Size size, State state, Unit<?> unit, DAValue<Mass> mass, DAValue<Volume> volume, DAText assetName
            , DAGoodFlowList goodFlow, DAValue<Energy> maxHitPoints, DADamage resistance
            , DAValue<Duration> onlineDelay, DABoolean offLineOnOverflow, DAPatentSet prerequisites) {
        return new DABasicModuleClass(id, typeID, name, description, size, state, unit, mass, volume, assetName
                , goodFlow, maxHitPoints, resistance, onlineDelay, offLineOnOverflow, prerequisites);
    }

    @Deprecated
    public DABasicModuleClass() {
        super();
        goodFlow = null;
        maxHitPoints = null;
        resistance = null;
        onlineDelay = null;
        offLineOnOverflow = null;
        prerequisites = null;
    }

    public DAGoodFlow getGoodFlow(DAWareClass good) {
        return goodFlow.get(good);
    }

    public DAGoodFlow getGoodFlow(DAUniqueID wareClassID) {
        return goodFlow.get(wareClassID);
    }

    public DAValue<Energy> getOnlinePower() {
        DAValue<Energy> f = (DAValue<Energy>) getGoodFlow(ELECTRICAL_POWER).flow;
        if (f == null) {
            f = DAValue.<Energy>create(0, SI.JOULE);
        }
        return f;
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        super.toStream(stream);
        stream.writeByte(VERSION);

        goodFlow.toStream(stream);
        maxHitPoints.toStream(stream);
        resistance.toStream(stream);
        onlineDelay.toStream(stream);
        offLineOnOverflow.toStream(stream);
        prerequisites.toStream(stream);
    }

    @Override
    public DABasicModuleClass fromStream(DataInputStream stream) throws IOException {
        DAWareClass wc = super.fromStream(stream);
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }

        final DAGoodFlowList goodFlow = new DAGoodFlowList().fromStream(stream);
        final DAValue<Energy> maxHitPoints = new DAValue().fromStream(stream);
        final DADamage resistance = new DADamage().fromStream(stream);
        final DAValue<Duration> onlineDelay = new DAValue().fromStream(stream);
        final DABoolean offLineOnOverflow = new DABoolean().fromStream(stream);
        final DAPatentSet prerequisites = new DAPatentSet().fromStream(stream);

        return new DABasicModuleClass(wc, goodFlow, maxHitPoints, resistance, onlineDelay
                , offLineOnOverflow, prerequisites);
    }

    private static final byte VERSION = 1;

    private DABasicModuleClass(DAWareClass wc, DAGoodFlowList goodFlow, DAValue<Energy> maxHitPoints
            , DADamage resistance, DAValue<Duration> onlineDelay, DABoolean offLineOnOverflow
            , DAPatentSet prerequisites) {
        super(wc);
        this.goodFlow = goodFlow;
        this.maxHitPoints = maxHitPoints;
        this.resistance = resistance;
        this.onlineDelay = onlineDelay;
        this.offLineOnOverflow = offLineOnOverflow;
        this.prerequisites = prerequisites;
    }

    private DABasicModuleClass(DAUniqueID id, DAUniqueID typeID, DAText name, DAText description
            , Size size, State state, Unit<?> unit, DAValue<Mass> mass, DAValue<Volume> volume, DAText assetName
            , DAGoodFlowList goodFlow, DAValue<Energy> maxHitPoints, DADamage resistance
            , DAValue<Duration> onlineDelay, DABoolean offLineOnOverflow, DAPatentSet prerequisites) {
        super(id, typeID, name, description, size, state, Pieces.UNIT, mass, volume, assetName);
        this.goodFlow = goodFlow;
        this.maxHitPoints = maxHitPoints;
        this.resistance = resistance;
        this.onlineDelay = onlineDelay;
        this.offLineOnOverflow = offLineOnOverflow;
        this.prerequisites = prerequisites;
    }

}
