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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWaresContainerClass extends DABasicModuleClass {

    @Deprecated
    public DAWaresContainerClass() {
        super();
    }

//    public DAbmWaresContainerClass(DVCsmWaresType aWaresType
//            , DVCdmLine actionName, DVCdmText description, Size aSize, DVCdmPVMass aMass) {
//        super(aWaresType, aSize, actionName, description, aMass, new DVCdmPVEnergy(1000)
//                , new DVCdmDoubleFloat(100), new DVCsmGoodFlowList(), new DVCsmDamage());
////        double x = aSize.getSize();
////        DVCsm3DObject box = DVCsm3DObject.createBox(null, x, x, x, StandardMaterial.Stahl.getMaterial());
////        box.setMass(new DVCdmPVMass(0));
////        box.setName(new DVCdmLine("backbone"));
////        box.setTexture(DVCsmTextureFactory.getDefaultTexture(DefaultTexture.IO));
////        group.add(box);
//        group.setName(actionName);
//    }

//    public DAbmWaresContainerClass(Size aSize, DVCdmLine actionName, DVCdmText description) {
//        super(aSize, actionName, description
//                , new DVCdmPVMass(aSize.getMaxVolume().getBaseValue() * 100, DVCdmUnit.KnownUnit.kg)   // 100kg / m³
//                , new DVCdmPVEnergy(aSize.getMaxVolume().getBaseValue() * 100, DVCdmUnit.KnownUnit.J) // 100J / m³
//                , new DVCdmDoubleFloat(1)
//                , new DVCsmGoodFlowList()
//                , new DVCsmDamage());
//    }
//
//    public DAbmWaresContainerClass(String idStr, Size aSize, DVCdmLine actionName, DVCdmText description) {
//        super(idStr, aSize, actionName, description
//                , new DVCdmPVMass(aSize.getMaxVolume().getBaseValue() * 100, DVCdmUnit.KnownUnit.kg)   // 100kg / m³
//                , new DVCdmPVEnergy(aSize.getMaxVolume().getBaseValue() * 100, DVCdmUnit.KnownUnit.J) // 100J / m³
//                , new DVCdmDoubleFloat(1)
//                , new DVCsmGoodFlowList()
//                , new DVCsmDamage());
//    }
//
//    @Override
//    public DAbmWaresContainerClass clone() {
//        DAbmWaresContainerClass pc = new DAbmWaresContainerClass(getItemID().toString()
//                , size, getClassName().clone(), description.clone());
//        pc.setID(getItemID());
//        pc.group = group.clone();
//        return pc;
//    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

    }

    @Override
    public DAWaresContainerClass fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return new DAWaresContainerClass();
    }

    private static final byte VERSION = 1;

}
