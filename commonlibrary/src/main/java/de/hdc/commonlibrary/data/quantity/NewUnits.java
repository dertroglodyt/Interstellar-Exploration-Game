/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.data.quantity;

import org.jscience.economics.money.Currency;
import java.util.List;
import java.util.Vector;
import javax.measure.quantity.Angle;
import javax.measure.quantity.AngularVelocity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author martin
 */
public class NewUnits {

    public static final Unit<Pieces> PIECES = Pieces.UNIT;
//    public static final Unit<HitPoints> HITPOINTS = HitPoints.UNIT;
    public static final Unit<Angle> HOUR_ANGLE = NonSI.REVOLUTION.divide(24);
    public static final Unit<AngularVelocity> RADIAN_PER_SECOND = SI.RADIAN.divide(SI.SECOND).asType(AngularVelocity.class);
    public static final Unit<AngularVelocity> RADIAN_PER_DAY = SI.RADIAN.divide(NonSI.DAY).asType(AngularVelocity.class);
    public static final Currency CREDITS = new Currency("Cr");

    public static Unit<?> valueOf(String unit) {
        try {
            return Unit.valueOf(unit);
        } catch (IllegalArgumentException e) {
            for (Unit<?> u : LIST) {
                if (u.toString().equals(unit)) {
                    return u;
                }
            }
        }
        throw new IllegalArgumentException("Unknown unit <" + unit + ">");
    }

    private NewUnits() {
    }

    private static final List<Unit<?>> LIST = new Vector<Unit<?>>();

    static {
        LIST.add(PIECES);
//      LIST.add(HITPOINTS);
        LIST.add(HOUR_ANGLE);
        LIST.add(RADIAN_PER_SECOND);
        LIST.add(RADIAN_PER_DAY);
        LIST.add(CREDITS);
    }

}
