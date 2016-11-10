/*
 *  Created by DerTroglodyt on 2016-11-10 09:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.moduleclass;

import javax.measure.quantity.Volume;

import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.market.DAWareClass;

import static android.R.attr.type;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DATankClass extends DABasicModuleClass {

    /**
     * Maximum storage Volume.
     */
    public final DAValue<Volume> fCapacity;
    /**
     * ID of Ware that is stored.
     */
    public final DAUniqueID fStoreTypeID;

    /**
     * Type of ware that can be stored.
     * Lazy init because of WareClass lazy init.
     */
    public final transient DAWareClass fStoreType;

    @Deprecated
    public DATankClass() {
        super();
        fCapacity = null;
        fStoreTypeID = null;
        fStoreType = null;
    }

//    public DATankClass(AssetPool.AssetNameWareClass assetName) {
//        super(assetName);
//        // Initialize from asset user data.
//        fCapacity = getAssetData(Constants.UserDataKey.Capacity, new DAValue<Volume>(0, SI.CUBIC_METRE));
//        String wareClassName = getAssetData(Constants.UserDataKey.WareClass, "");
//        fStoreType = AssetPool.waresTree.getWareClass(new DALine(wareClassName));
//        fStoreTypeID = fStoreType.getItemID();
//        recalc();
//    }

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
        return name + " [" + type + "]";
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

//    public DAWareClass getType() {
//        if (fStoreTypeID.isValid() && (! fStoreType.getItemID().isValid())) {
//            DAWareClass wc = AssetPool.waresTree.getWareClass(fStoreTypeID);
//            if (wc == null) {
//                return fStoreType;
//            }
//            fStoreType = wc;
//            if (fStoreType.getVolume().is(DAValue.Sign.ZERO)) {
//                kgPerM3 = new DAValue<VolumetricDensity>(0.0, VolumetricDensity.UNIT);
//            } else {
//                kgPerM3 = fStoreType.getMass().div(fStoreType.getVolume()).to(VolumetricDensity.UNIT);
//            }
//        }
//        return fStoreType;
//    }

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

//    @Override
//    public DAbmTank create() {
//        return new DAbmTank(this, null);
//    }
//
//    @Override
//    public DAbmTank create(DATransform trans) {
//        DAbmTank p = new DAbmTank(this, trans);
////        p.resolveOther(aParentContainer);
//        return p;
//    }

}
