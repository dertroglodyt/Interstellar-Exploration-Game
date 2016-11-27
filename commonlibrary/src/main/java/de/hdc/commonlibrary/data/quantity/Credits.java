/*
 *  Created by DerTroglodyt on 2016-11-23 09:37
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.data.quantity;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.Unit;

/**
 *
 * @author martin
 */
public interface Credits extends Dimensionless {

    /**
     * Holds the SI unit (Système International d'Unités) for this quantity.
     */
    public static final Unit<Credits> UNIT = new BaseUnit<Credits>(" Cr");
}
