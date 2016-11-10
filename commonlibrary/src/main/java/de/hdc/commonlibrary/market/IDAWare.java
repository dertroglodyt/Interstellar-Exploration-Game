/*
 *  Created by DerTroglodyt on 2016-11-10 09:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.quantity.Pieces;

/**
 *
 * @author martin
 */
public interface IDAWare extends IDataAtom {

//    public void resolveOther(DVCsmModuleContainer aParentContainer);
    public DAUniqueID getClassID();
    public DAUniqueID getItemID();
    public DAText getName();
    public IDAWare setName(DAText newName);
    public DAWareClass getWareClass();
    public DAValue<Mass> getMass();
    public Unit<?> getUnit();
    public DAValue<Volume> getVolume();
    public DAValue<Pieces> getAmount();
    public boolean isUnique();
    public IDAWare makeUnique(DAText name);
    public DAWare.SubType getSubType();

}
