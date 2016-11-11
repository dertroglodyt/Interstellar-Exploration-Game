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

import java.util.ArrayList;
import java.util.List;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.module.DAWaresContainer;

/**
 *
 * @author martin
 */
public interface IDAWare extends IDataAtom {

    public void init(DAWareClassMap map);
    public DAUniqueID getClassID();
    public DAUniqueID getItemID();
    public DAText getName();
    public DAWareClass getWareClass();
    public DAValue<Mass> getMass();
    public Unit<?> getUnit();
    public DAValue<Volume> getVolume();
    public DAValue<Pieces> getAmount();
    public boolean add(DAValue<Pieces> value);
    public boolean sub(DAValue<Pieces> value);
    public boolean isUnique();
    public IDAWare makeUnique(DAText name);
    public DAWare.SubType getSubType();
}
