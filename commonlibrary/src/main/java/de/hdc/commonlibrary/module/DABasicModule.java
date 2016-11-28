/*
 *  Created by DerTroglodyt on 2016-11-10 09:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

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
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.market.DAWareTypeTree;
import de.hdc.commonlibrary.market.IDAWare;
import de.hdc.commonlibrary.moduleclass.DABasicModuleClass;
import de.hdc.commonlibrary.util.Log;

/**
 *
 * @author martin
 */
public abstract class DABasicModule extends DATickable implements IDAWare {

    public enum State {
        /**
         * Module is offline because off error.
         */
        ERROR,
        /**
         * Module is offline. Does not consume / produce.
         */
        OFFLINE,
        /**
         * Module is offline but does already consume / produce.
         */
        GOING_ONLINE,
        /**
         * Module is online. Does consume / produce.
         */
        ONLINE,
        /**
         * Module is offline but does still consume / produce.
         */
        GOING_OFFLINE;
    }

    public enum ModuleType {
        NONE, PROPULSION, STORAGE, RENTABLE_STORAGE, HANGAR, WEAPON, TANK, CABIN, FACTORY, BACKBONE
        , CONVERTER, WARES_CONTAINER, SHIP
    }

    protected ModuleType moduleType;
    /**
     * Needed to restore ware class after serialisation
     */
    public final DAUniqueID classID;
    /**
     * Unique items have an ID. Otherwise multiple items of this ware are stackable.
     */
    public final DAUniqueID itemID;
    /**
     * Unique items have a name.
     */
    public final DAText itemName;
    /**
     * Gets populated by init().
     * Init() needs to be called after deserialization and Constructor.
     */
    private transient DABasicModuleClass wareClass;
    /**
     /**
     * Gets populated by init().
     * Init() needs to be called after deserialization and Constructor.
     * May be NULL!
     */
    private transient DABasicModule parentModule;
    /**
     * State of this module.
     */
    private State state;
    /**
     * Seconds until initiated state change is finished.
     */
    private DAValue<Duration> delayToGo;
    /**
     * Procent of MaxHitpoints (health).
     * [-100..0..+100]
     * Negative numbers mean "being build" (-1 .. -99 --> +100).
     * 0 means destroyed. No repair possible. Physically gone. (black)
     * 1..+25 means nonfunctional. Can be repaired. (red)
     * +25..+75 means partly functional. Can be repaired. (yellow)
     * +75..+99 means slightly damaged but fully functional. (dark green)
     * +100 means fully functonal. (light green)
     */
    private DAValue<Energy> actHitPoints;

    private DAResult<?> lastError;

    @Deprecated
    public DABasicModule() {
        super();
        moduleType = ModuleType.NONE;
        classID = null;
        itemID = null;
        itemName = null;
        state = null;
        delayToGo = null;
        actHitPoints = null;
        lastError = null;
    }

    @Deprecated
    @Override
    public void init(DAWareTypeTree tree) {
        throw new IllegalAccessError("DAModlueContainer");
    }

    public void init(DAWareTypeTree tree, DABasicModule parent) {
        wareClass = (DABasicModuleClass) tree.getWareClass(classID);
        if (wareClass == null) {
            throw new IllegalArgumentException("DABasicModule: Unknown DAWareClass!");
        }
        parentModule = parent;
    }

    public DAModuleContainer getParentContainer() {
        DATickable m = parentModule;
        while (! (m instanceof DAModuleContainer) && (m != null)) {
            m = parentModule.parentModule;
        }
        if (m == null) {
            throw new IllegalStateException("DABasicModule " + this.toString() + " without parent container!");
        }
        return (DAModuleContainer) m;
    }

//    public DABasicModule create(DABasicModuleClass aWareClass) {
//        return new DABasicModule(State.OFFLINE, DAValue.<Duration>create(0, SI.SECOND)
//                , aWareClass.maxHitPoints, DAResult.createOK("", "DABasicModule")
//                , aWareClass.id, DAUniqueID.createRandom(), DAText.create("<Not named>"));
//    }

    @Override
    public String toString() {
        return itemName + " (" + state + ")";
    }

    @Deprecated
    @Override
    public boolean add(DAValue<Pieces> value) {
        return false;
    }

    @Deprecated
    @Override
    public boolean sub(DAValue<Pieces> value) {
        return false;
    }

    public void setOnline(boolean on) {
        if (on && (state == State.OFFLINE)) {
//            if (! isUnique()) {
//                makeUnique(DAWare.NOT_NAMED);
//            }
            state = State.GOING_ONLINE;
            delayToGo = wareClass.onlineDelay;
            return;
        }
        if (! on && ((state == State.ONLINE)
                || (state == State.GOING_ONLINE)
                || (state == State.ERROR))) {
            if (state == State.ONLINE) {
                delayToGo = wareClass.onlineDelay;
            }
            state = State.GOING_OFFLINE;
        }
    }

    public boolean hasError() {
        return (state == State.ERROR);
    }

    public boolean isOnline() {
        return (state == State.ONLINE);
    }

    public boolean isConsuming() {
        return (state == State.ONLINE) || (state == State.GOING_ONLINE) || (state == State.GOING_OFFLINE);
    }

    public boolean isDestroyed() {
        return (actHitPoints.isZero());

    }

    public boolean isBeingConstructed() {
        return (actHitPoints.doubleValueBase() < 0.0);
    }

    public DAValue<Duration> getDelayToGo() {
        return delayToGo;
    }

    public State getState() {
        return state;
    }

    public void setErrorState(DAResult<?> e) {
        state = State.ERROR;
        lastError = e;
    }

    public DAResult<?> getLastError() {
        return lastError;
    }

    public DAValue<Energy> getPowerConsumption() {
        if (isOnline()) {
            return getOnlinePower();
        }
        return DAValue.<Energy>create(0, SI.JOULE);
    }

    public DAValue<Energy> getOnlinePower() {
        return wareClass.getOnlinePower();
    }

    public DAValue<Energy> getAktHitpoints() {
        return actHitPoints;
    }

    public DAValue<Energy> getMaxHitpoints() {
        return wareClass.maxHitPoints;
    }

    public DAGoodFlow getGoodFlow(DAWareClass good) {
        return wareClass.getGoodFlow(good);
    }

    public DAGoodFlowList getGoodFlows() {
        return wareClass.goodFlow;
    }

    @Override
    public IDAWare makeUnique(DAText name) {
        throw new IllegalAccessError("DABasicModule: Modules are always unique!");
    }

    @Override
    public DAValue<Pieces> getAmount() {
        return DAValue.<Pieces>create(1, Pieces.UNIT);
    }

    public void applyDamage(DADamage damage) {
        damage.applyDamage(actHitPoints, wareClass.resistance);
    }

    @Override
    public void tickImpl(DAValue<Duration> timeSinceLastTick) {
        if ((state == State.OFFLINE) && (! (this instanceof DAShip))) {
            return;
        }
        // switch off if parent module is off
        if (parentModule != null) {
//            if (getParentContainer() instanceof DAShip) {
//                if (((DAShip) getParentContainer()).getShipState() == DAShip.State.OFFLINE) {
//                    if (isOnline()) {
//                        setOnline(false);
//                    }
//                }
//            } else {
                if ((parentModule.getState() != State.ONLINE)
                        && (parentModule.getState() != State.GOING_ONLINE)) {
                    if (isOnline() || hasError()) {
                        Log.debug(DABasicModule.class, "longTick: Parent ship " + parentModule + " of module "
                                + this + " is offline. Going offline too.");
                        setOnline(false);
                    }
    //                return;
//                }
            }
        } else {
            // ships dont have parents and treat their state by themself
            // else without a parent: go offline
            if (! (this instanceof DAShip)) {
                if (isOnline()) {
                    Log.debug(DABasicModule.class, "longTick: Module " + this + " going offline.");
                    setOnline(false);
                }
            }
        }
        if (isConsuming() && (! (this instanceof DAShip))) {
            DABasicModuleClass wc = wareClass;
//            DAGoodFlowList gfl = wc.getGoodFlows();
            DAGoodFlowList gfl = getGoodFlows();
            for (DAGoodFlow gf : gfl.getValueList()) {
                // if not fully online do only the consuming
                if ((gf.flow.doubleValueBase() < 0) || ((gf.flow.doubleValueBase() >= 0) && (isOnline()))) {
                    DAResult<DAGoodFlow> r = getParentContainer().getTankPool().manage(gf);
                    if (isConsuming() && (!r.isOK())) {
                        if ((gf.flow.doubleValueBase() >= 0) && (wc.offLineOnOverflow == DABoolean.FALSE)) {
                            // This module does not go offline if produced amount can not be stored
                        } else {
                            // ships treat their state by themself
                            // all else go offline here
                            if (! (this instanceof DAShip)) {
                                //setOnline(false);
                                Log.debug(DABasicModule.class, "longTick: Module " + this + " of ship " + parentModule
                                        + " in error. " + r +".");
                                state = State.ERROR;
                            }
                        }
                    }
                }
            }
            if (delayToGo.doubleValueBase() > 0) {
                delayToGo = delayToGo.sub(timeSinceLastTick);
                if (delayToGo.doubleValueBase() <= 0) {
                    delayToGo = DAValue.<Duration>create(0, SI.SECOND);
                    if (state == State.GOING_ONLINE) {
                        state = State.ONLINE;
                    } else {
                        if (state == State.GOING_OFFLINE) {
                            state = State.OFFLINE;
                        } else {
                            if ((state != State.ONLINE) && (state != State.OFFLINE)) {
                                Log.debug(DABasicModule.class, "longTick: Module " + this + " of ship " + parentModule
                                    + " in error. State " + state +" and delayToGo <= 0.");
                                state = State.ERROR;
                            }
                            Log.warn(DABasicModule.class, "longTick: Unkown end state <" + state + ">.");
                        }
                    }
                }
                // Notification is done in module container
//                notifyListener();
            }
        }
        if ((delayToGo.doubleValueBase() == 0.0) || (this instanceof DAShip)) {
            // prevent getting stuck in GOING_ON/OFFLINE with remaining time = 0
            if (state == State.GOING_ONLINE) {
                state = State.ONLINE;
            } else {
                if (state == State.GOING_OFFLINE) {
                    state = State.OFFLINE;
                }
            }
            // special case for ships
            if (this instanceof DAShip) {
                delayToGo = DAValue.<Duration>create(0, SI.SECOND);
            }
        }
//        classGeometry.longTick(actWorldTime);
    }

    @Override
    public DAUniqueID getClassID() {
        return classID;
    }

    @Override
    public DAUniqueID getItemID() {
        return itemID;
    }

    @Override
    public DAText getName() {
        return wareClass.name;
    }

    @Override
    public DABasicModuleClass getWareClass() {
        return wareClass;
    }

    @Override
    public DAValue<Mass> getMass() {
        return wareClass.mass;
    }

    @Override
    public Unit<?> getUnit() {
        return wareClass.unit;
    }

    @Override
    public DAValue<Volume> getVolume() {
        return wareClass.volume;
    }

    @Override
    public boolean isUnique() {
        return (itemID != null);
    }

//    @Override
//    public DAWare makeUnique(DAText name) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        super.toStream(stream);
        stream.writeByte(VERSION);

        classID.toStream(stream);
        itemID.toStream(stream);
        itemName.toStream(stream);
        stream.writeUTF(state.toString());
        delayToGo.toStream(stream);
        actHitPoints.toStream(stream);
        lastError.toStream(stream);
    }

    /**
     * Init() needs to be called after deserialization.
     */
    @Override
    public DABasicModule fromStream(DataInputStream stream) throws IOException {
        super.fromStream(stream);
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAUniqueID aclassID = new DAUniqueID().fromStream(stream);
        DAUniqueID aitemID = new DAUniqueID().fromStream(stream);
        DAText aitemName = new DAText().fromStream(stream);
        State astate = State.valueOf(stream.readUTF());
        DAValue<Duration> adelayToGo = new DAValue().fromStream(stream);
        DAValue<Energy> aactHitPoints = new DAValue().fromStream(stream);
        DAResult<?> alastError = new DAResult().fromStream(stream);
        DAUniqueID aparentClassID = new DAUniqueID().fromStream(stream);

        return null;
    }

    private static final byte VERSION = 1;

    protected DABasicModule(DABasicModuleClass c, ModuleType type, DAText name) {
        this(State.OFFLINE, type, c.onlineDelay, c.maxHitPoints, c.id, name);
    }

    protected DABasicModule(State state, ModuleType type, DAValue<Duration> delayToGo
            , DAValue<Energy> actHitPoints, DAUniqueID wareClassID, DAText itemName) {
        this(state, type, delayToGo, actHitPoints, DAResult.createOK("", DAWareClass.class.getName())
                , wareClassID, DAUniqueID.createRandom(), itemName);
    }

    protected DABasicModule(State state, ModuleType type, DAValue<Duration> delayToGo
            , DAValue<Energy> actHitPoints, DAResult<?> lastError, DAUniqueID wareClassID
            , DAUniqueID itemID, DAText itemName) {
        super();
        this.moduleType = type;
        this.classID = wareClassID;
        this.itemID = itemID;
        this.itemName = itemName;
        this.state = state;
        this.delayToGo = delayToGo;
        this.actHitPoints = actHitPoints;
        this.lastError = lastError;
    }
}
