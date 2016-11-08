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
public interface Pieces extends Dimensionless {

    /**
     * Holds the SI unit (Système International d'Unités) for this quantity.
     */
    public final static Unit<Pieces> UNIT = new BaseUnit<Pieces>("pcs");
}
