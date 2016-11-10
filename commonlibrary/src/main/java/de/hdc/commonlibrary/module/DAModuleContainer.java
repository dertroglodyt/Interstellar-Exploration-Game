/*
 *  Created by DerTroglodyt on 2016-11-08 13:46
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.module;

import java.util.ArrayList;

import javax.measure.quantity.Duration;
import javax.measure.quantity.Energy;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.market.DAWare;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.market.DAWareClassMap;

/**
 * Entity (like a ship or a station) that contains modules.
 * Is "tickable" for calculating acceleration and damage propagation.
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAModuleContainer extends DABasicModule {

//    static {
//        DVCBasicDataModel.register(DAModuleContainer.class, createImageIcon("/datavault/common/space/icon/DAModuleContainer.gif"));
//    }

    /**
     * Modules are kept as child nodes in DATree.
     * Duplicated here for quick access.
     */
    protected transient final DAValue<Energy> actEnergy;
    protected transient final DAGoodFlowList combinedGoodFlows;
    protected transient final ArrayList<DABasicModule> allModules;
//    protected transient final DAVector<DAbmPropulsion> engines;
//    protected transient final DAVector<DAbmStorage> storages;
//    protected transient final DAVector<DAbmRentableStorage> rentStorages;
//    protected transient final DAVector<DAbmHangar> hangars;
//    protected transient final DAVector<DAbmWeapon> weapons;
    protected transient final ArrayList<DAAbstractTank> tanks;
//    protected transient final DAVector<DAbmFactory> factories;

    private transient final TankPool tankPool;

    @Deprecated
    public DAModuleContainer() {
        super();
        actEnergy = null;
        combinedGoodFlows = null;
        allModules = null;
        tanks = null;
        tankPool = null;
    }
//    public DAModuleContainer(DABasicModuleClass aWareClass) {
//        super(aWareClass);
//        actEnergy = new DAValue<Energy>(0, SI.JOULE);
//        combinedGoodFlows = new DAGoodFlowList();
////            combinedGoodFlows.set(DAResourcePool.ELECTRICAL_POWER, new DAPVEnergy(0, DAUnit.KnownUnit.J));
//        allModules = new ArrayList<DABasicModule>();
////        engines = new DAVector<DAbmPropulsion>(DAbmPropulsion.class);
////        storages = new DAVector<DAbmStorage>(DAbmStorage.class);
////        rentStorages = new DAVector<DAbmRentableStorage>(DAbmRentableStorage.class);
////        hangars = new DAVector<DAbmHangar>(DAbmHangar.class);
////        weapons = new DAVector<DAbmWeapon>(DAbmWeapon.class);
//        tanks = new ArrayList<DAAbstractTank>();
////        factories = new DAVector<DAbmFactory>(DAbmFactory.class);
//        tankPool = new TankPool();
//
////        remoteListener = new HashSet<IDVCRemoteDataModel>(0);
//    }

    @Override
    public void init(DAWareClassMap map, DABasicModule parent) {
        super.init(map, parent);
        for (DABasicModule bm : allModules) {
            bm.init(map, this);
        }
    }

//    private static void calcInertia(DAModuleContainer mc, Vector3d v, Vector3d v2) {
//        //Vector3d p1 = new Vector3d();
//        // mass center is origin for calc
////        Vector3d p1 = mc.getMassCenter();
////        p1.scale(-1.0);
//        // calc all children mass points
//        for (DABasicModule mo : mc.getModules()) {
////            if (mo instanceof DAModuleContainer) {
////                DAModuleContainer c = (DAModuleContainer) mo;
////                DAModuleContainer.calcInertia(c, v, v2);
////            }
//
//            double m = mo.getMassKG();
//            Vector3d p = mo.getPos();
//            //p.add(p1);
//            v.x += (p.y * p.y + p.z * p.z) * m;
//            v.y += (p.x * p.x + p.z * p.z) * m;
//            v.z += (p.y * p.y + p.x * p.x) * m;
//            if (Double.isNaN(v.x) || Double.isNaN(v.y) || Double.isNaN(v.z)) {
//                DVCErrorHandler.raiseError(DAResult.createWarning("NaN! " + v.toString()
//                        , "DAModuleContainer.calcInertia"));
//                return;
//            }
//
//            v2.x -= (p.x * p.y) * m;
//            v2.y -= (p.y * p.z) * m;
//            v2.z -= (p.x * p.z) * m;
//            if (Double.isNaN(v2.x) || Double.isNaN(v2.y) || Double.isNaN(v2.z)) {
//                DVCErrorHandler.raiseError(DAResult.createWarning("NaN! " + v2.toString()
//                        , "DAModuleContainer.calcInertia"));
//                return;
//            }
//        }
//        // don't forget my own mass
//        // simplification: own mass is spread evenly about volume of this group
////        p1 = mc.getMassCenter();
//        double r = mc.getMaxDist().getBaseValue() / 6.0;
//        Vector3d p1 = new Vector3d(r, r, r);
//        double m = mc.physical.getGroupMass().getBaseValue();
//        v.x += (p1.y * p1.y + p1.z * p1.z) * m;
//        v.y += (p1.x * p1.x + p1.z * p1.z) * m;
//        v.z += (p1.y * p1.y + p1.x * p1.x) * m;
//
//        v2.x -= (p1.x * p1.y) * m;
//        v2.y -= (p1.y * p1.z) * m;
//        v2.z -= (p1.x * p1.z) * m;
//    }

    //TODO
    private void internalRecalc() {
//        try {
////            DVCErrorHandler.createDebug("recalc", "DAModuleContainer");
//            // When read from stream as part of a warestree we need to first read ALL of the tree.
//            // else some of the wares may not have already been read in and yield a null pointer exception
//            // if searching for their ware class by their warecalssID.
//            if (wareClass == null) {
//                return;
//            }
//            //mass = new DAPVMass(getWareClass().getClassMass().getBaseValue(), DAUnit.KnownUnit.kg);
//            physical.setMassCenter(new Vector3d());
//            physical.clearMass();
////            physical.clearVolume();
//            actEnergy.setValueForced(0);
//            combinedGoodFlows.removeAll();
////            combinedGoodFlows.set(DAResourcePool.ELECTRICAL_POWER, new DAPVEnergy(0, DAUnit.KnownUnit.J));
//            for (DABasicModule m : allModules) {
//                try {
//                    if (m instanceof DAModuleContainer) {
//                        ((DAModuleContainer) m).internalRecalc();
//                    }
//                    //mass.addTo(m.getMassInternal());
//                    physical.addMass(m.getMassKG());
////                    physical.addVolume(m.getVolumeM3());
//                    Vector3d pm = m.getPos();
//                    pm.scale(m.getMassKG());
//                    physical.getMassCenter().add(pm);
//                    if (m.isOnline()) {
//                        actEnergy.addTo(m.getOnlinePower());
//                    }
//                    combinedGoodFlows.add(m.getGoodFlows());
//                    pm = null;
//                } catch (DVCUnitMismatchException | DVCPrecisionMismatchException ex) {
//                    DVCErrorHandler.raiseError(ex, "DAModuleContainer.recalc");
//                }
//            }
//            physical.addMass(wareClass.getMass().getBaseValue());
////            physical.addVolume(getWareClass().getVolume().getBaseValue());
//            Vector3d pm = getPos();
//            pm.scale(wareClass.getMass().getBaseValue());
//            physical.getMassCenter().add(pm);
//            pm = null;
////            if (physical.getGroupMass().getValue() != 0.0) {
//            if (physical.getMassKG() != 0.0) {
//                physical.getMassCenter().scale(1.0 / physical.getMassKG());
//            }
//            // recalc inertia moments for local x-, y-, z-axis
//            Vector3d v = new Vector3d(0, 0, 0);
//            Vector3d v2 = new Vector3d(0, 0, 0);
//            calcInertia(this, v, v2);
//            physical.setInertiaMoments(v);
//            physical.setDevitationMoments(v2);
//            v = null;
//            v2 = null;
//        } catch (Throwable t) {
//            DVCErrorHandler.raiseError(DAResult.createSerious(t.toString(), "DAModuleContainer.internalRecalc"));
//        }
    }

    public void addModule(DABasicModule m) {
//            if (! m.isUnique()) {
//                m.makeUnique(DAWare.NOT_NAMED);
//            }
        allModules.add(m);
//            if (isGraficsInit()) {
//                m.addToTrans(physical.getTransform());
////                physical.addGraficNode(m.getGraficNode());
//            }
//            mp.getModule().getObjectGroup().setParent(this.getObjectGroup(), this);
        //mass.addTo(m.getMassInternal());
//        physical.addMass(m.getMassKG());
//            physical.addVolume(m.getVolumeM3());
        if (m.isOnline()) {
            actEnergy.add(m.getOnlinePower());
        }
////            combinedGoodFlows.add(m.getGoodFlows());
//        if (m instanceof DAbmPropulsion) {
//            engines.add((DAbmPropulsion) m);
//        }
//        if (m instanceof DAbmStorage) {
//            storages.add((DAbmStorage) m);
//        }
//        if (m instanceof DAbmRentableStorage) {
//            rentStorages.add((DAbmRentableStorage) m);
//        }
//        if (m instanceof DAbmHangar) {
//            hangars.add((DAbmHangar) m);
//        }
//        if (m instanceof DAbmWeapon) {
//            weapons.add((DAbmWeapon) m);
//        }
//        if (m instanceof DAAbstractTank) {
//            tanks.add((DAAbstractTank) m);
//            tankPool.add((DAAbstractTank) m);
//        }
//        if (m instanceof DAbmFactory) {
//            factories.add((DAbmFactory) m);
//        }
        internalRecalc();
    }

    public void removeModule(DABasicModule m) {
        allModules.remove(m);
//            m.addToTrans(physical.getTransform());
//            physical.removeGraficNode(m.getGraficNode());
        //mass.subtract(m.getMassInternal());
//        physical.removeMass(m.getMassKG());
//            physical.removeVolume(m.getVolumeM3());
        if (m.isOnline()) {
            actEnergy.sub(m.getOnlinePower());
        }
////            combinedGoodFlows.subtract(m.getGoodFlows());
//        if (m instanceof DAbmPropulsion) {
//            engines.remove((DAbmPropulsion) m);
//        }
//        if (m instanceof DAbmStorage) {
//            storages.remove((DAbmStorage) m);
//        }
//        if (m instanceof DAbmRentableStorage) {
//            rentStorages.remove((DAbmRentableStorage) m);
//        }
//        if (m instanceof DAbmHangar) {
//            hangars.remove((DAbmHangar) m);
//        }
//        if (m instanceof DAbmWeapon) {
//            weapons.remove((DAbmWeapon) m);
//        }
//        if (m instanceof DAAbstractTank) {
//            tanks.remove((DAAbstractTank) m);
//            tankPool.remove((DAAbstractTank) m, typeID);
//        }
//        if (m instanceof DAbmFactory) {
//            factories.remove((DAbmFactory) m);
//        }
        internalRecalc();
    }

    public ArrayList<DABasicModule> getModules() {
        return allModules;
    }

//    public DABasicModule getModule(DAUniqueID id) {
//        for (DABasicModule bm : allModules) {
//            if (bm.getItemID().equals(id)) {
//                return bm;
//            }
//            if (bm instanceof DAbmRentableStorage) {
//                DAbmStorage s = ((DAbmRentableStorage) bm).getStorageByModuleID(id);
//                if (s != null) {
//                    return s;
//                }
//            }
//        }
//        return null;
//    }
//
//    public DAbmWaresContainer getContainer(DAUniqueID id) {
//        for (DAbmStorage sto : getStorages()) {
//            for (DAbmWaresContainer wc : sto.getContainers()) {
//                if (wc.getItemID().equals(id)) {
//                    return wc;
//                }
//            }
//        }
//        return null;
//    }

//    public ArrayList<DAbmPropulsion> getEngines() {
//        return engines;
//    }
//
//    public ArrayList<DAbmStorage> getStorages() {
//        return storages;
//    }
//
//    public ArrayList<DAbmRentableStorage> getRentableStorages() {
//        return rentStorages;
//    }
//
//    public ArrayList<DAbmHangar> getHangars() {
//        return hangars;
//    }
//
//    public ArrayList<DAbmWeapon> getWeapons() {
//        return weapons;
//    }

    public ArrayList<DAAbstractTank> getTanks() {
        return tanks;
    }

//    public ArrayList<DAbmFactory> getFactories() {
//        return factories;
//    }

    public TankPool getTankPool() {
        return tankPool;
    }

    @Override
    public DAValue<Energy> getPowerConsumption() {
        if (isOnline()) {
            return actEnergy;
        } else {
            return DAValue.<Energy>create(0, SI.JOULE);
        }
    }

    @Override
    public DAValue<Energy> getOnlinePower() {
        DAGoodFlow gf = combinedGoodFlows.get(DAWareClass.ELECTRICAL_POWER);
        if (gf == null) {
            gf = DAGoodFlow.create(DAWareClass.ELECTRICAL_POWER.id, DAValue.<Energy>create(0, SI.JOULE));
            combinedGoodFlows.set(DAWareClass.ELECTRICAL_POWER, gf);
        }
        return (DAValue<Energy>) gf.flow;
    }

    @Override
    public DAValue<Energy> getMaxHitpoints() {
        return getWareClass().maxHitPoints;
    }

    @Override
    public DAGoodFlow getGoodFlow(DAWareClass good) {
        return combinedGoodFlows.get(good);
    }

    @Override
    public DAGoodFlowList getGoodFlows() {
        return combinedGoodFlows;
    }

//    @Override
//    public DAValue<Mass> getTotalMass() {
//        DAValue<Mass> m = getTotalMass();
//        for (DABasicModule bm : allModules) {
//            m = m.add(bm.getTotalMass());
//        }
//        return m;
//    }

//    @Override
//    public DVCDataEditor getEditor(DVCDataEditor.EditMode editmode, DVCAbstractUser user, boolean visible) {
//        DVCDataEditor de = new DVCseModuleContainer(this, editmode, user, visible);
//        addListener(de);
//        return de;
//    }

//    @Override
//    public void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
//        super.longTick(actWorldTime, t);
//        for (DABasicModule bm : allModules) {
//            bm.longTick(actWorldTime, t);
//        }
//        for (DABasicModule bm : allModules) {
//            bm.longTickNotify();
//        }
//        internalRecalc();
//    }

    public void preTick(DAValue<Duration> t) {
        // TODO
//        try {
////            for (DABasicModule bm : modules) {
////                bm.tick(nanos);
////            }
//            DAPVForce f = new DAPVForce(0, DAUnit.KnownUnit.N.getUnit()
//                    , DAPVDirection.StandardDirections.NORTH.getDirection(DVCCoordinateSystem.Type.CARTESIAN_RIGHT_HANDED_XZ));
//            for (DABasicModule m : allModules) {
//                if (m.isOnline()) {
//                    if (m instanceof DAbmPropulsion) {
//                        if (((DAbmPropulsion) m).getThrustValue() > 0) {
//                            DAPVForce th = ((DAbmPropulsion) m).getThrust();
////                            DVCErrorHandler.createDebug("r: " + m.getPos().length() + " f: " + th
////                                    , "DAModuleContainer.tick");
//                            f.addForce(th);
//                            physical.addRotForce(th, m.getPos(), t);
//                        }
//                    }
//                }
//            }
////            DVCErrorHandler.createDebug("rv: " + physical.getAngularVelocity().toParseString(), "DAModuleContainer.tick");
//            // Transform forces relative to ship into absolute forces
//            f.transform(getAbsoluteTrans());
//            physical.addForce(f.getVector().toVector3d());
////            double sec = nanos / 1e9;
////            if (sec == 0) {
////                sec = 1.0 / 1e9;
////            }
////            //m = 30.0/1000.0;
////            // translational forces
////            Vector3d v = f.getVector().toVector3d();
////            v.scale(1.0 / mass.getDoubleValue());
////            v.scale(sec);
////            getVelocity().applyAcceleration(v);
////
////            Vector3d vv = getVelocity().getVector();
////            vv.scale(sec);
////            Vector3d vgp = getPosition();
////            vgp.add(vv);
////            setGroupPosition(vgp);
////
////            // rotational forces
////            v = f.getVector().toVector3d();
////            double r = v.length();
////            double alpha = Math.acos(v.x / r);
////            double beta = Math.acos(v.y / r);
////            double gamma = Math.acos(v.z / r);
//
//            // comment in later for moving modules
////            super.preTick(t);
////            notifyListener(this);
//        } catch (Exception ex) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("Exception for object <" + this.toString() + ">" + ex.toString(), "DAModuleContainer.tick"));
//        }
    }

//    @Override
//    public void postTick(DAPVTime t) {
//        throw new UnsupportedOperationException();
//    }

//    @Override
//    public DVCDataEditor getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseModuleContainer de = new DVCseModuleContainer(this, editmode, user);
//        addListener(de);
//        return de;
//    }
//
//    public static DVCDataEditor getParentEditor(DAModuleContainer model, EditMode editmode, DVCAbstractUser user) {
//        DVCseBasicModule de = new DVCseBasicModule(model, editmode, user);
//        model.addListener(de);
//        return de;
//    }

//    @Override
//    public void addRemoteListener(IDVCRemoteDataModel rl) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("remoteListener not initialized!"
//                    , "DAModuleContainer.addRemoteListener"));
//            return;
//        }
//        remoteListener.add(rl);
//    }
//
//    @Override
//    public void removeRemoteListener(IDVCRemoteDataModel rl) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("remoteListener not initialized!"
//                    , "DAModuleContainer.removeRemoteListener"));
//            return;
//        }
//        remoteListener.remove(rl);
//    }
//
//    @Override
//    public void notifyRemoteListener(DAOwningThread newOwner) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("notifyListener not initialized!"
//                    , "DAModuleContainer.addRemoteListener"));
//            return;
//        }
//        for (IDVCRemoteDataModel rd : remoteListener) {
//            rd.ownerChanged(newOwner);
//        }
//    }
//
//    @Override
//    public void notifyRemoteListener(DVCBasicDataModel newData) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("notifyListener not initialized!"
//                    , "DAModuleContainer.addRemoteListener"));
//            return;
//        }
//        for (IDVCRemoteDataModel rd : remoteListener) {
//            rd.dataChanged(newData);
//        }
//    }
//
    // TODO
//    public void processShipYardActiona(DAVector<DAShipYardAction> actions) {
//        for (DAShipYardAction sya : actions) {
//            DABasicModule m = sya.getModule();
//            switch (sya.getType()) {
//                case ADD: {
//                    addModule(m);
//                    break;
//                }
//                case REMOVE: {
//                    removeModule(m);
//                    break;
//                }
//                case MOVETO: {
//                    getModule(m.getItemID()).setTransFrom(m.getTransformCopy());
//                    break;
//                }
//                case REPAIR: {
//                    DABasicModule mm = getModule(m.getItemID());
//                    mm.getAktHitpoints().setValueForced(mm.getMaxHitpoints());
//                    break;
//                }
//            }
//        }
//    }

//    @Override
//    public DAModuleContainer clone() {
//        DAModuleContainer s = new DAModuleContainer(wareClass);
////        s.setID(getItemID());
//        for (DABasicModule bm : allModules) {
//            s.addModule(bm.clone());
//        }
////        s.resolve(worldNode);
//        return s;
//    }

    @Override
    public DAWare.SubType getSubType() {
        return null;
    }

}
