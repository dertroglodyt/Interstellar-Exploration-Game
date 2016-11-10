/*
 *  Created by DerTroglodyt on 2016-11-10 09:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.iegcommon.moduleclass;

import de.dertroglodyt.common.data.SerialUID;

import de.dertroglodyt.common.data.types.atom.DALine;
import de.dertroglodyt.common.data.types.atom.DAUniqueID;
import de.dertroglodyt.common.data.types.atom.DAValue;
import de.dertroglodyt.iegcommon.core.AssetPool;
import de.dertroglodyt.iegcommon.market.DAWareClass;
import de.dertroglodyt.iegcommon.messaging.Constants;
import de.dertroglodyt.iegcommon.module.DAbmTank;
import de.dertroglodyt.iegcommon.physic.DATransform;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.SI;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DATankClass extends DABasicModuleClass {

    private static final long serialVersionUID = SerialUID.DAbmTankClass.value();

    /**
     * Maximum storage Volume.
     */
    private DAValue<Volume> fCapacity;
    /**
     * ID of Ware that is stored.
     */
    private DAUniqueID fStoreTypeID;

    /**
     * Type of ware that can be stored.
     * Lazy init because of WareClass lazy init.
     */
    private transient DAWareClass fStoreType;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        fCapacity.readExternal(in);
        fStoreTypeID.readExternal(in);
        fStoreType = new DAWareClass();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish isChanged read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are isChanged.
         */
        byte version = 1;
        out.writeByte(version);

        fCapacity.writeExternal(out);
        fStoreTypeID.writeExternal(out);
    }

    @Deprecated
    public DATankClass() {
        super("Tank");
    }

    public DATankClass(AssetPool.AssetNameWareClass assetName) {
        super(assetName);
        // Initialize from asset user data.
        fCapacity = getAssetData(Constants.UserDataKey.Capacity, new DAValue<Volume>(0, SI.CUBIC_METRE));
        String wareClassName = getAssetData(Constants.UserDataKey.WareClass, "");
        fStoreType = AssetPool.waresTree.getWareClass(new DALine(wareClassName));
        fStoreTypeID = fStoreType.getItemID();
        recalc();
    }

//    public DAbmTankClass(Size aSize
//            , DALine name, DAText description, DAPVMass aMass, DAWareClass aStoreType, double aCapacity) {
//        super(aSize, name, description, aMass, new DAPVEnergy(0, DAUnit.KnownUnit.J)
//                , new DADoubleFloat(100), new DAGoodFlowList(), new DADamage());
//        try {
//            fCapacity = new DADoubleFloat(aCapacity, aStoreType.getUnit());
//            setStoreType(aStoreType);
//            setName(name);
//        } catch (Throwable t) {
//            DVCErrorHandler.raiseError(DAResult.createSerious(t.getMessage(), "DABMTankClass()"));
//        }
//    }
//
//    private DAbmTankClass(String classIDStr, Size aSize, DALine name
//            , DAText description, DAPVMass aMass, DAWareClass aStoreType, double aCapacity) {
//        super(classIDStr, aSize, name, description, aMass, new DAPVEnergy(0, DAUnit.KnownUnit.J)
//                , new DADoubleFloat(100), new DAGoodFlowList(), new DADamage());
//        try {
//            fCapacity = new DADoubleFloat(aCapacity, aStoreType.getUnit());
//            setStoreType(aStoreType);
//            setName(name);
//        } catch (Throwable t) {
//            DVCErrorHandler.raiseError(DAResult.createSerious(t.getMessage(), "DABMTankClass(2)"));
//        }
//    }

    @Override
    public String toString() {
        return getName().toString() + " [" + getType() + "]";
    }

    @Override
    public String toParseString(String levelTab) {
        return toString() + " " + getMass() + " " + getVolume();
    }

    @Override
    public de.dertroglodyt.iegcommon.moduleclass.DATankClass parse(String value) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public void resolveOther() {
//        super.resolveOther();
//        storeType = DAResourcePool.getWareClass(storeTypeID);
//        if (storeType.getVolume().getBaseValue() != 0.0) {
//            kgPerM3 = storeType.getMass().getBaseValue() / storeType.getVolume().getBaseValue();
//        } else {
//            kgPerM3 = 0.0;
//        }
//    }

//    @Override
//    public DAbmTankClass clone() {
//        DAbmTankClass pc = new DAbmTankClass(getItemID().toString()
//                , size, getClassName().clone(), description.clone(), group.getMass().clone()
//                , getType(), fCapacity.getBaseValue());
//        pc.setID(getItemID());
//        pc.group = group.clone();
//        return pc;
//    }

    public DAValue<Volume> getCapacity() {
        return fCapacity;
    }

    public DAWareClass getType() {
        if (fStoreTypeID.isValid() && (! fStoreType.getItemID().isValid())) {
            DAWareClass wc = AssetPool.waresTree.getWareClass(fStoreTypeID);
            if (wc == null) {
                return fStoreType;
            }
            fStoreType = wc;
            if (fStoreType.getVolume().is(DAValue.Sign.ZERO)) {
                kgPerM3 = new DAValue<VolumetricDensity>(0.0, VolumetricDensity.UNIT);
            } else {
                kgPerM3 = fStoreType.getMass().div(fStoreType.getVolume()).to(VolumetricDensity.UNIT);
            }
        }
        return fStoreType;
    }

//    public void setStoreType(DAWareClass wc) {
//        if (wc == null) {
//            throw new IllegalArgumentException("WareClass is null!", "DABMTankClass.setStoreType");
//        }
//        fStoreTypeID = wc.getItemID();
//        fStoreType = wc;
//        fCapacity = fCapacity.to(fStoreType.getUnit());
//        if (fStoreType.getVolume().getBaseValue() != 0.0) {
//            kgPerM3 = fStoreType.getMass().getBaseValue() / fStoreType.getVolume().getBaseValue();
//        } else {
//            kgPerM3 = 0.0;
//        }
//        notifyListener(this);
//    }

    @Override
    public DAbmTank getInstance() {
        return new DAbmTank(this, null);
    }

    @Override
    public DAbmTank getInstance(DATransform trans) {
        DAbmTank p = new DAbmTank(this, trans);
//        p.resolveOther(aParentContainer);
        return p;
    }

//    @Override
//    public DVCseBMTankClass getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseBMTankClass de = new DVCseBMTankClass(this, editmode, user);
//        addListener(de);
//        return de;
//    }
//
//    public static DVCseBasicModuleClass getParentEditor(DABasicModuleClass model, EditMode editmode, DVCAbstractUser user) {
//        DVCseBasicModuleClass de = new DVCseBasicModuleClass(model, editmode, user);
//        model.addListener(de);
//        return de;
//    }

}
