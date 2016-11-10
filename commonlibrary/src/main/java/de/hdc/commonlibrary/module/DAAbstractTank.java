/*
 *  Created by DerTroglodyt on 2016-11-10 09:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.module;

import javax.measure.quantity.Dimensionless;

import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.market.DAWareClass;

/**
 *
 * @author martin
 */
public abstract class DAAbstractTank extends DABasicModule {

    public abstract DAValue<?> getFree();
    public abstract DAValue<?> getStored();
    public abstract DAValue<Dimensionless> getUsed();
    public abstract DAValue<?> getCapacity();
    public abstract DAWareClass getStoreClass();
    public abstract DAResult<DAGoodFlow> change(DAGoodFlow flow);
    public abstract void fillUp();
    public abstract void drainEmpty();

}
