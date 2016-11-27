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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.measure.quantity.Duration;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.atom.DAArray;
import de.hdc.commonlibrary.data.atom.DALogItem;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.market.DAOrganizationBasic;
import de.hdc.commonlibrary.market.DAWare;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.market.DAWareTypeTree;
import de.hdc.commonlibrary.market.IDAWare;
import de.hdc.commonlibrary.moduleclass.DABasicModuleClass;

import static javax.measure.unit.SI.CUBIC_METRE;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAStorage extends DABasicModule {

    /**
     * List of cargo containers.
     */
    private DAArray<DALogItem<DAWaresContainer>> container;
    private DAUniqueID leaserID;

    private transient DAOrganizationBasic leasert;

    @Deprecated
    public DAStorage() {
        super();
        leaserID = null;
        container = null;
    }

    @Override
    public void init(DAWareTypeTree tree) {
//        super.init(tree);
        for (DALogItem<DAWaresContainer> c : container) {
            c.getData().init(tree);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DAStorage: {\n");
        for (DALogItem<DAWaresContainer> c : container) {
            sb.append(c.getData().toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public DAValue<Mass> getMass() {
        DAValue<Mass> m = getMass();
        for (DALogItem<DAWaresContainer> c : container) {
            m =  m.add(c.getData().getMass());
        }
        return m;
    }

    @Override
    @Deprecated
    public boolean add(DAValue<Pieces> value) {
        throw new IllegalAccessError();
    }

    @Override
    @Deprecated
    public boolean sub(DAValue<Pieces> value) {
        throw new IllegalAccessError();
    }

    /**
     * 80% of Storage is usable for cargo containers.
     * @return
     */
    public DAValue<Volume> getMaxCargoSpace() {
        return getWareClass().volume.scale(0.8);
    }

    public DAValue<Volume> getUsedSpace() {
        DAValue<Volume> v = DAValue.create(0, SI.CUBIC_METRE);
        for (DALogItem<DAWaresContainer> c : container) {
            v = v.add(c.getData().getVolume());
        }
        return v;
    }

    public DAValue<Volume> getFreeSpace() {
        DAValue<Volume> v = getMaxCargoSpace();
        for (DALogItem<DAWaresContainer> c : container) {
            v = v.sub(c.getData().getVolume());
        }
        return v;
    }

    public DAResult canAdd(DAArray<DAWaresContainer> container) {
        if (! isOnline()) {
            return DAResult.createFailed("Lagerraum ist nicht online.", "DAStorage.canAdd");
        }
        DAValue<Volume> v = DAValue.create(0, CUBIC_METRE);
        DAWareClass.Size size = wareClass.getSize();
        for (DAWaresContainer wc : container) {
            if (wc.wareClass.getSize().isBiggerOrEqual(size)) {
                return DAResult.createFailed("Container type does not fit into storage.", "DAStorage.canAdd");
            }
            v = v.add(wc.getVolume());
        }
        if (getFreeSpace().isSmallerThan(v)) {
            return DAResult.createFailed("Not enough free space.", "DAStorage.canAdd");
        }
        return DAResult.createOK("ok", "DAStorage.canAdd");
    }

    public DAResult<?> canAdd(DAWaresContainer container) {
        if (! isOnline()) {
            return DAResult.createFailed("Lagerraum ist nicht online.", "DAStorage.canAdd");
        }
        if (container.getWareClass.getSize().isBiggerOrEqual(wareClass.getSize())) {
            return DAResult.createFailed("Container type does not fit into storage.", "DAStorage.canAdd");
        }
        if (getFreeSpace().isLessThan(container.getVolume())) {
            return DAResult.createFailed("Not enough free space.", "DAStorage.canAdd");
        }
        return DAResult.createOK("ok", "DAStorage.canAdd");
    }

    public DAResult canAddAmount(IDAWare amount) {
        if (! isOnline()) {
            return DAResult.createFailed("Lagerraum ist nicht online.", "DAStorage.canAddAmount");
        }
        if (amount.getWareClass().size.isBiggerOrEqual(getWareClass().size)) {
            return DAResult.createFailed("Ware type does not fit into storage.", "DAStorage.canAddAmount");
        }
        if (getFreeSpace().isSmallerThan(amount.getVolume())) {
            return DAResult.createFailed("Not enough free space.", "DAStorage.canAddAmount");
        }
        return DAResult.createOK("ok", "DAStorage.canAddAmount");
    }

    public DAResult add(DAWaresContainer container) {
        DAResult r = canAdd(container);
        if (! r.isOK()) {
            return r;
        }
        boolean br = this.container.add(new DALogItem<DAWaresContainer>(container));
        if (br) {
            return DAResult.createOK("ok", "DAStorage.add");
        } else {
            return DAResult.createWarning("Could not add ware to storage.", "DAStorage.add");
        }
    }

    /**
     * Add all container on list to this storage.
     * If not all container can be added the returned vector holds the remaining containers on return.
     * @param container
     * @return
     */
    public DAResult<DAArray<DAWaresContainer>> add(DAArray<DAWaresContainer> container) {
        final DAArray<DAWaresContainer> rc = DAArray.create();
        for (DAWaresContainer wc : container) {
            rc.add(wc);
        }
        try {
            Iterator<DAWaresContainer> i = container.iterator();
            while (i.hasNext()) {
                DAWaresContainer wc = i.next();
                DAResult r = add(wc);
                if (! r.isOK()) {
                    return new DAResult<DAArray<DAWaresContainer>>(r.getMessage(), r.getResultType()
                            , rc, "DAStorage.add");
                } else {
                    rc.remove(wc);
                }
            }
            return new DAResult<DAArray<DAWaresContainer>>("ok", DAResult.ResultType.OK
                , rc, "DAStorage.add");
        } catch (Throwable t) {
            DAResult r = DAResult.createWarning(t.toString(), "DAStorage.add");
            return new DAResult<DAArray<DAWaresContainer>>(r.getMessage(), r.getResultType()
                            , rc, "DAStorage.add");
        }
    }

    /**
     * Tries to add the given amount of ware to the storage.
     * If an error occurs returns the amount of ware that could NOT be added.
     * @param wa
     * @return
     */
    public DAResult<DAWare> addAmount(DAWare wa) {
        DAArray<DAWaresContainer> vwc = DAWaresContainer.packageWare(wa.getWare(), wa.getAmount(), this);
        DAResult<DAArray<DAWaresContainer>> r = add(vwc);
        wa.sub(wa.getAmount());
        for (DAWaresContainer c : r.getResult()) {
            wa.add(c.getActualAmount());
        }
        return new DAResult<DAWare>(r.getMessage(), r.getResultType(), wa, "DAStorage.addAmount");
    }

    public DALogItem<DAWaresContainer> get(DAWaresContainer wc) {
        for (DALogItem<DAWaresContainer> li : container) {
            if (li.getData().equals(wc)) {
                return li;
            }
        }
        return null;
    }

    public void removeAllStored() {
        container.clear();
    }

    /**
     * Removes all container on list from this storage.
     * If not all container can be removed the returned vector holds the containers already removed on return.
     * @param container
     * @return
     */
    public DAResult<DAArray<DAWaresContainer>> remove(DAArray<DAWaresContainer> container) {
        final DAArray<DAWaresContainer> rc = DAArray.create();
        for (DAWaresContainer wc : container) {
            rc.add(wc);
        }

        try {
            Iterator<DAWaresContainer> i = container.iterator();
            while (i.hasNext()) {
                DAWaresContainer wc = i.next();
                if (this.container.remove(get(wc))) {
                    rc.remove(wc);
                } else {
                    return new DAResult<DAArray<DAWaresContainer>>("Can not remove ware from storage"
                            , DAResult.ResultType.WARNING, rc, "DAStorage.remove");
                }
            }
            return new DAResult<DAArray<DAWaresContainer>>("ok", DAResult.ResultType.OK
                , rc, "DAStorage.remove");
        } catch (Throwable t) {
            DAResult<DAArray<DAWaresContainer>> r = DAResult.createWarning(t.toString(), "DAStorage.remove");
            return new DAResult<DAArray<DAWaresContainer>>(r.getMessage(), r.getResultType()
                            , rc, "DAStorage.remove");
        }
    }

    public boolean remove(DAWaresContainer container) {
        return this.container.remove(get(container));
    }

    public DAArray<DALogItem<DAWaresContainer>> getList() {
        return container;
    }

    public DAWaresContainer getContainer(DAUniqueID id) {
        if (id == null) {
            return null;
        }
        for (DALogItem<DAWaresContainer> li : container) {
            if (id.equals(li.getData().getItemID())) {
                return li.getData();
            }
        }
        return null;
    }

    public DAArray<DAWaresContainer> getContainers() {
        final DAArray<DAWaresContainer> v = DAArray.create();
        for (DALogItem<DAWaresContainer> li : container) {
            v.add(li.getData());
        }
        return v;
    }

    public DAUniqueID getLeaserID() {
        return leaserID;
    }

    public void setLeaserID(DAUniqueID id) {
        leaserID = id;
    }

    /**
     * Repackages wares so that they use as few containers as possible.
     * Should only be available in stations as those have an unlimited supply of all sizes of containers.
     */
    public void compact() {
        // TODO: implement
    }

    /**
     * Splits given container contents on to 2 containers so that one of them contains only given amount.
     * The remainder container is left in this storage.
     * @param conti The container holding to much ware.
     * @param a Amount of ware the container should hold.
     * @return A container with given amount.
     */
    public DAResult<DAWaresContainer> split(DAWaresContainer conti, DAValue<Pieces> a) {
        DAWaresContainer wc = getContainer(conti.getItemID());
        if (wc == null) {
            return new DAResult<DAWaresContainer>("Container not in storage.", DAResult.ResultType.WARNING, "DAStorage");
        }
        if (a.doubleValue() <= 0) {
            return new DAResult<DAWaresContainer>("Amount must be greater than 0.", DAResult.ResultType.WARNING, "DAStorage");
        }
        if (a.isGreaterThan(wc.getActualAmount())) {
            return new DAResult<DAWaresContainer>("Container must hold more than given split amount.", DAResult.ResultType.WARNING
                    , "DAStorage");
        }
        // remove x from container so that a remains
        DAValue<Pieces> x = wc.getActualAmount().sub(a);
        DAArray<DAWaresContainer> wc2 = DAWaresContainer.packageWare(wc.getContent(), x, this);
        DAResult r = add(wc2);
        if (! r.isOK()) {
            return new DAResult<DAWaresContainer>(r.getMessage(), DAResult.ResultType.FAILED, "DAStorage");
        }
        if (! wc.sub(x)) {
            return new DAResult<DAWaresContainer>(r.getMessage(), DAResult.ResultType.FAILED, "DAStorage");
        }
        return new DAResult<DAWaresContainer>("ok", DAResult.ResultType.OK, wc, "DAStorage");
    }

//    @Deprecated
//    @Override
//    public DAStorageClass getWareClass() {
//        return (DAStorageClass) super.getWareClass();
//    }

//    @Override
//    public void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
//        super.longTick(actWorldTime, t);
//        for (DALogItem<DAWaresContainer> li : container) {
//            li.getData().longTick(actWorldTime, t);
//        }
//    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        container.toStream(stream);
        leaserID.toStream(stream);
        super.toStream(stream);
    }

    @Override
    public DAStorage fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }

        DAArray<DALogItem<DAWaresContainer>> athings = new DAArray().fromStream(stream);
        DAUniqueID aleaserID = new DAUniqueID().fromStream(stream);
        )
        return new DAStorage(super.fromStream(stream), container, aleaserID);
    }

    private static final byte VERSION = 1;

    private DAStorage(DAUniqueID leaserID, State state, DAValue<Duration> delayToGo
            , DAValue<Energy> actHitPoints, DAUniqueID wareClassID, DAText itemName) {
        super(state, ModuleType.STORAGE, delayToGo, actHitPoints, wareClassID, itemName);
        this.leaserID = leaserID;
        this.container = new DAArray<DALogItem<DAWaresContainer>>();
    }

    private DAStorage(DAUniqueID leaserID, DAArray<DALogItem<DAWaresContainer>> container
            , State state, DAValue<Duration> delayToGo, DAValue<Energy> actHitPoints
            , DAUniqueID wareClassID, DAText itemName) {
        super(state, ModuleType.STORAGE, delayToGo, actHitPoints, wareClassID, itemName);
        this.leaserID = leaserID;
        this.container = container;
    }

    private DAStorage(DABasicModule bm, DAArray<DALogItem<DAWaresContainer>> container, DAUniqueID leaserID) {
        this(leaserID, container, bm.getState(), bm.getDelayToGo(), bm.getAktHitpoints()
                , bm.getClassID(), bm.getName());
    }
}
