/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.compound.DAResult;

/**
 *
 * @author martin
 */
public interface IDAWare extends IDataAtom {

//    public void resolveOther(DVCsmModuleContainer aParentContainer);
    public DAUniqueID getTypeID();
    public DAUniqueID getClassID();
    public DAUniqueID getItemID();
    public DALine getName();
    public DAResult<DALine> setName(DALine newName);
    public DAWareClass getWareClass();
    public DAValue<Mass> getMass();
    public Unit<?> getUnit();
    public DAValue<Volume> getVolume();
    public boolean isUnique();
    public void makeUnique(DALine name);
//    @Override
//    public IDAWare clone();

}
