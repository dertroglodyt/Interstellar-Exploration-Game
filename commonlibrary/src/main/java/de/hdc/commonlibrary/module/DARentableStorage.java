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

import android.support.annotation.NonNull;

import org.jscience.economics.money.Money;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import de.hdc.commonlibrary.data.atom.DAArray;
import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DALogItem;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.compound.DAMap;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.market.DAWareTypeTree;
import de.hdc.commonlibrary.moduleclass.DABasicModuleClass;

/**
 * Mammut module containing a large number of storages that can be leased.
 * @author martin
 */
@SuppressWarnings("serial")
public class DARentableStorage extends DABasicModule {

    private static final DAValue<Duration> CASHIN_PERIOD = DAValue.create(1, NonSI.DAY);

    /**
     * List of cargo storages.
     * Key is UserID (ClanID).
     * DAIDMap<DALogItem<DAStorage>>
     */
    private DAMap<DAUniqueID, DALogItem<DAStorage>> leasedStorages;
    /**
     * Price per m² and minute.
     */
    private DAValue<Money> pricePerMin;

    private transient DADateTime lastCashed;

    @Deprecated
    public DARentableStorage() {
        super();
        leasedStorages = DAMap.create();
        pricePerMin = DAValue.create(0, NewUnits.CREDITS);
    }

    public DARentableStorage(DABasicModuleClass aWareClass, DAText name) {
        super(aWareClass, ModuleType.RENTABLE_STORAGE, name);
        leasedStorages = DAMap.create();
        pricePerMin = DAValue.create(0, NewUnits.CREDITS);
    }

    @Override
    public void init(DAWareTypeTree tree) {

    }

//    @Override
//    public void resolveOther(DAModuleContainer aParentContainer) {
//        super.resolveOther(aParentContainer);
//        for (DALogItem<DAStorage> li : leasedStorages.values()) {
//            DALogItem<DAStorage> c = li;
//            c.getData().resolveOther(aParentContainer);
//        }
//    }

//    @Override
//    public DAValue<Mass> getMass() {
//        DAValue<Mass> m = super.getTotalMass();
//        for (DALogItem<DAStorage> li : leasedStorages.values()) {
//            DALogItem<DAStorage> c = li;
//            m = m.add(c.getData().getTotalMass());
//        }
//        return m;
//    }

//    @Override
//    public DAValue<Mass> getTotalMass() {
//        return getMass();
//    }

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

//    public DAValue<Pieces> getMaxStorageCount() {
//        return getWareClass().getMaxStorages();
//    }
//
//    public DAUniqueID getStorageClassID() {
//        return getWareClass().getStorageClassID();
//    }

//    public DAValue<Pieces> getFreeStorageCount() {
//        return new DAValue<Pieces>(getMaxStorageCount().doubleValueBase() - leasedStorages.size(), NewUnits.PIECES);
//    }
//
//    public DAValue<Pieces> getUsedStorageCount() {
//        return new DAValue<Pieces>(leasedStorages.size(), NewUnits.PIECES);
//    }

    public DAStorage getStorageByLeaser(DAUniqueID leaserID) {
        DALogItem<DAStorage> li = leasedStorages.get(leaserID);
        if (li != null) {
            return li.getData();
        } else {
            return null;
        }
    }

    public DAStorage getStorageByModuleID(DAUniqueID moduleID) {
        for (DALogItem<DAStorage> li : leasedStorages.getValueList()) {
            DAStorage s = li.getData();
            if (s.getItemID().equals(moduleID)) {
                return s;
            }
        }
        return null;
    }

//    public DAStorage rentStorage(DAClan leaser) {
//        DAStorage sto = getStorageByLeaser(leaser.getUserID());
//        if (sto != null) {
//            return sto;
//        }
//        DAStorageClass wc = null;
//        try {
//            wc = (DAStorageClass) AssetPool.waresTree.getWareClass(getStorageClassID());
//            if (wc == null) {
//                Log.warn(de.dertroglodyt.iegcommon.module.DARentableStorage.class, "rentStorage: Ware class with id <" + getStorageClassID()
//                        + "> not found in WaresTree for rentable storage <" + this.toParseString("") + ">.");
//                return null;
//            }
//        } catch(ClassCastException cce) {
//            Log.warn(de.dertroglodyt.iegcommon.module.DARentableStorage.class, "rentStorage: Invalid storage class <" + getStorageClassID()
//                    + "> for RentableStorage. " + cce.toString());
//            return null;
//        }
//        sto = wc.getInstance();
//        sto.setLeaserID(leaser.getUserID());
//        sto.setOnline(true);
//        sto.setName(leaser.getFamilyName());
//        leasedStorages.addProperty(leaser.getUserID(), new DALogItem<DAStorage>(sto));
//        return sto;
//    }

//    public void removeStorage(DAStorage sto) {
//        for (DALogItem<DAStorage> li : leasedStorages.getValueList()) {
//            if (li.getData().equals(sto)) {
//                leasedStorages.removeByValue(li);
//                return;
//            }
//        }
//    }

//    public DAIDMap<DALogItem<DAStorage>> getList() {
//        return leasedStorages;
//    }

    public DAArray<DAStorage> getStorageList() {
        DAArray<DAStorage> v = new DAArray();
        for (DALogItem<DAStorage> li : leasedStorages.getValueList()) {
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

//    @Deprecated
//    @Override
//    public DARentableStorageClass getWareClass() {
//        return (DARentableStorageClass) wareClass;
//    }

    public DADateTime getLastCashed(DAUniqueID leaserID) {
        return leasedStorages.get(leaserID).getTime();
    }

    protected void resetLastCashed(DAUniqueID leaserID) {
        DALogItem<DAStorage> item = leasedStorages.get(leaserID);
        item.resetTime();
    }

//    protected void cashIn(DAUniqueID leaserID) {
//        if (getParentContainer() == null) {
//            return;
//        }
//        DALogItem<DAStorage> li = leasedStorages.getValue(leaserID);
//        DAClan c = li.getData().getLeaser();
//        if (c == null) {
//            Log.warn(de.dertroglodyt.iegcommon.module.DARentableStorage.class, "cashIn: Leasing clan not found!");
//            return;
//        }
//        DADateTime dt = getLastCashed(leaserID);
//        DAValue<Duration> diff = dt.getDiff(new DADateTime());
//        DAValue<Money> m = new DAValue<Money>(diff.to(NonSI.MINUTE).scale(pricePerMin.doubleValueBase()).doubleValueBase()
//                , NewUnits.CREDITS);
//        DAMarketTransaction mt = DAMarketTransaction.createTaxBill(getRootContainer().getOwningClan(), c
//                , "Lagerraumgebühr für " + c.toString(), m);
//        c.transaction(mt);
//        mt = DAMarketTransaction.createTaxReceipt(getRootContainer().getOwningClan(), c
//                , "Lagerraumgebühr für " + c.toString(), m);
//        getRootContainer().getOwningClan().transaction(mt);
//        resetLastCashed(c.getUserID());
//    }
//
//    protected void cashAllIn() {
//        if (getParentContainer() == null) {
//            return;
//        }
//        for (DAUniqueID id : leasedStorages.getKeySet()) {
//            cashIn(id);
//        }
//    }

//    @Override
//    public void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
//        super.longTick(actWorldTime, t);
//        for (DALogItem<DAStorage> li : leasedStorages.values()) {
//            li.getData().longTick(actWorldTime, t);
//        }
//        if (! isOnline()) {
//            return;
//        }
//        if (lastCashed == null) {
//            lastCashed = actWorldTime;
//        }
//        if (actWorldTime.compareTo(lastCashed) >= 0) {
//            lastCashed = actWorldTime;
//            lastCashed.addDuration(CASHIN_PERIOD, null);
//            cashAllIn();
//        }
//    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        leasedStorages.toStream(stream);
        pricePerMin.toStream(stream);
    }

    @Override
    public DARentableStorage fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAMap stor = new DAMap().fromStream(stream);
        DAValue price = new DAValue().fromStream(stream);
        return new DARentableStorage(stor, price);
    }

    private static final byte VERSION = 1;

    private DARentableStorage(DAMap<DAUniqueID, DALogItem<DAStorage>> storages, DAValue<Money> price) {
        super();
        this.leasedStorages = storages;
        this.pricePerMin = price;
    }

}
