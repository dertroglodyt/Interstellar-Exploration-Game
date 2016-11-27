/*
 *  Created by DerTroglodyt on 2016-11-27 01:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import org.jscience.economics.money.Money;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.quantity.Duration;
import javax.measure.quantity.Mass;
import javax.measure.unit.NonSI;

import de.dertroglodyt.common.data.SerialUID;
import de.dertroglodyt.common.data.quantity.NewUnits;
import de.dertroglodyt.common.data.quantity.Pieces;
import de.dertroglodyt.common.data.types.atom.DADateTime;
import de.dertroglodyt.common.data.types.atom.DALogItem;
import de.dertroglodyt.common.data.types.atom.DAUniqueID;
import de.dertroglodyt.common.data.types.atom.DAValue;
import de.dertroglodyt.common.data.types.collection.DAIDMap;
import de.dertroglodyt.common.data.types.collection.DAVector;
import de.dertroglodyt.common.util.Log;
import de.dertroglodyt.iegcommon.core.AssetPool;
import de.dertroglodyt.iegcommon.core.DAClan;
import de.dertroglodyt.iegcommon.market.DAMarketTransaction;
import de.dertroglodyt.iegcommon.moduleclass.DABasicModuleClass;
import de.dertroglodyt.iegcommon.moduleclass.DARentableStorageClass;
import de.dertroglodyt.iegcommon.moduleclass.DAStorageClass;
import de.dertroglodyt.iegcommon.physic.DATransform;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.module.DABasicModule;

/**
 * Mammut module containing a large number of storages that can be leased.
 * @author martin
 */
@SuppressWarnings("serial")
public class DARentableStorage extends DABasicModule {

    private static final DAValue<Duration> CASHIN_PERIOD = new DAValue<Duration>(1, NonSI.DAY);

    /**
     * List of cargo storages.
     * Key is UserID (ClanID).
     * DAIDMap<DALogItem<DAStorage>>
     */
    private DAIDMap<DALogItem<DAStorage>> leasedStorages;
    /**
     * Price per m² and minute.
     */
    private DAValue<Money> pricePerMin;

    private transient DADateTime lastCashed;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        leasedStorages.readExternal(in);
        pricePerMin.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish changed read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are changed.
         */
        byte version = 1;
        out.writeByte(version);

        leasedStorages.writeExternal(out);
        pricePerMin.writeExternal(out);
    }

    @Deprecated
    public DARentableStorage() {
        super();
        leasedStorages = new DAIDMap<DALogItem<DAStorage>>(DALogItem.class);
        pricePerMin = new DAValue<Money>(0, NewUnits.CREDITS);
    }

    public DARentableStorage(DABasicModuleClass aWareClass) {
        super(aWareClass, null);
        leasedStorages = new DAIDMap<DALogItem<DAStorage>>(DALogItem.class);
        pricePerMin = new DAValue<Money>(0, NewUnits.CREDITS);
    }

    public DARentableStorage(DABasicModuleClass aWareClass, DATransform trans) {
        super(aWareClass, trans);
        leasedStorages = new DAIDMap<DALogItem<DAStorage>>(DALogItem.class);
        pricePerMin = new DAValue<Money>(0, NewUnits.CREDITS);
    }

//    @Override
//    public void resolveOther(DAModuleContainer aParentContainer) {
//        super.resolveOther(aParentContainer);
//        for (DALogItem<DAStorage> li : leasedStorages.values()) {
//            DALogItem<DAStorage> c = li;
//            c.getData().resolveOther(aParentContainer);
//        }
//    }

    @Override
    public DAValue<Mass> getMass() {
        DAValue<Mass> m = super.getTotalMass();
        for (DALogItem<DAStorage> li : leasedStorages.values()) {
            DALogItem<DAStorage> c = li;
            m = m.add(c.getData().getTotalMass());
        }
        return m;
    }

    @Override
    public DAValue<Mass> getTotalMass() {
        return getMass();
    }

//    @Override
//    public double getMassKG() {
//        double m = physical.getGroupMass().getBaseValue();
//        for (DALogItem<DAStorage> li : leasedStorages.values()) {
//            DALogItem<DAStorage> c = li;
//            m += c.getData().getMassKG();
//        }
//        return m;
//    }

    public DAValue<Money> getPricePerMin() {
        return pricePerMin;
    }

    public DAValue<Pieces> getMaxStorageCount() {
        return getWareClass().getMaxStorages();
    }

    public DAUniqueID getStorageClassID() {
        return getWareClass().getStorageClassID();
    }

    public DAValue<Pieces> getFreeStorageCount() {
        return new DAValue<Pieces>(getMaxStorageCount().doubleValueBase() - leasedStorages.count(), NewUnits.PIECES);
    }

    public DAValue<Pieces> getUsedStorageCount() {
        return new DAValue<Pieces>(leasedStorages.count(), NewUnits.PIECES);
    }

    public DAStorage getStorageByLeaser(DAUniqueID leaserID) {
        DALogItem<DAStorage> li = leasedStorages.getValue(leaserID);
        if (li != null) {
            return li.getData();
        } else {
            return null;
        }
    }

    public DAStorage getStorageByModuleID(DAUniqueID moduleID) {
        for (DALogItem<DAStorage> li : leasedStorages.values()) {
            DAStorage s = li.getData();
            if (s.getItemID().equals(moduleID)) {
                return s;
            }
        }
        return null;
    }

    public DAStorage rentStorage(DAClan leaser) {
        DAStorage sto = getStorageByLeaser(leaser.getUserID());
        if (sto != null) {
            return sto;
        }
        DAStorageClass wc = null;
        try {
            wc = (DAStorageClass) AssetPool.waresTree.getWareClass(getStorageClassID());
            if (wc == null) {
                Log.warn(de.dertroglodyt.iegcommon.module.DARentableStorage.class, "rentStorage: Ware class with id <" + getStorageClassID()
                        + "> not found in WaresTree for rentable storage <" + this.toParseString("") + ">.");
                return null;
            }
        } catch(ClassCastException cce) {
            Log.warn(de.dertroglodyt.iegcommon.module.DARentableStorage.class, "rentStorage: Invalid storage class <" + getStorageClassID()
                    + "> for RentableStorage. " + cce.toString());
            return null;
        }
        sto = wc.getInstance();
        sto.setLeaserID(leaser.getUserID());
        sto.setOnline(true);
        sto.setName(leaser.getFamilyName());
        leasedStorages.addProperty(leaser.getUserID(), new DALogItem<DAStorage>(sto));
        return sto;
    }

    public void removeStorage(DAStorage sto) {
        for (DALogItem<DAStorage> li : leasedStorages.values()) {
            if (li.getData().equals(sto)) {
                leasedStorages.removeByValue(li);
                return;
            }
        }
    }

    public DAIDMap<DALogItem<DAStorage>> getList() {
        return leasedStorages;
    }

    public DAVector<DAStorage> getStorageList() {
        DAVector<DAStorage> v = new DAVector<DAStorage>(DAStorage.class);
        for (DALogItem<DAStorage> li : leasedStorages.values()) {
            v.add(li.getData());
        }
        return v;
    }

//    @Override
//    public DARentableStorage clone() {
//        DARentableStorage pc = new DARentableStorage(wareClass);
////        pc.setID(getItemID());
//        pc.pricePerMin = pricePerMin.clone();
//        return pc;
////        DVCErrorHandler.raiseError(DAResult.createWarning("Not supported yet.", "DARentableStorage.clone"));
////        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Deprecated
    @Override
    public DARentableStorageClass getWareClass() {
        return (DARentableStorageClass) wareClass;
    }

    public DADateTime getLastCashed(DAUniqueID leaserID) {
        return leasedStorages.getValue(leaserID).getTime();
    }

    protected void resetLastCashed(DAUniqueID leaserID) {
        DALogItem<DAStorage> item = leasedStorages.getValue(leaserID);
        item.resetTime();
    }

    protected void cashIn(DAUniqueID leaserID) {
        if (getParentContainer() == null) {
            return;
        }
        DALogItem<DAStorage> li = leasedStorages.getValue(leaserID);
        DAClan c = li.getData().getLeaser();
        if (c == null) {
            Log.warn(de.dertroglodyt.iegcommon.module.DARentableStorage.class, "cashIn: Leasing clan not found!");
            return;
        }
        DADateTime dt = getLastCashed(leaserID);
        DAValue<Duration> diff = dt.getDiff(new DADateTime());
        DAValue<Money> m = new DAValue<Money>(diff.to(NonSI.MINUTE).scale(pricePerMin.doubleValueBase()).doubleValueBase()
                , NewUnits.CREDITS);
        DAMarketTransaction mt = DAMarketTransaction.createTaxBill(getRootContainer().getOwningClan(), c
                , "Lagerraumgebühr für " + c.toString(), m);
        c.transaction(mt);
        mt = DAMarketTransaction.createTaxReceipt(getRootContainer().getOwningClan(), c
                , "Lagerraumgebühr für " + c.toString(), m);
        getRootContainer().getOwningClan().transaction(mt);
        resetLastCashed(c.getUserID());
    }

    protected void cashAllIn() {
        if (getParentContainer() == null) {
            return;
        }
        for (DAUniqueID id : leasedStorages.getKeySet()) {
            cashIn(id);
        }
    }

    @Override
    public void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
        super.longTick(actWorldTime, t);
        for (DALogItem<DAStorage> li : leasedStorages.values()) {
            li.getData().longTick(actWorldTime, t);
        }
        if (! isOnline()) {
            return;
        }
        if (lastCashed == null) {
            lastCashed = actWorldTime;
        }
        if (actWorldTime.compareTo(lastCashed) >= 0) {
            lastCashed = actWorldTime;
            lastCashed.addDuration(CASHIN_PERIOD, null);
            cashAllIn();
        }
    }

//    @Override
//    public DVCseBMRentableStorage getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseBMRentableStorage de = new DVCseBMRentableStorage(this, editmode, user);
//        addListener(de);
//        return de;
//    }
//
//    public static DVCseBasicModule getParentEditor(DARentableStorage model, EditMode editmode, DVCAbstractUser user) {
//        DVCseBasicModule de = new DVCseBasicModule(model, editmode, user);
//        model.addListener(de);
//        return de;
//    }

}
