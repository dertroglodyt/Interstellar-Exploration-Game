/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Energy;
import javax.measure.unit.SI;

import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DADamage extends DataAtom {

    public class DamageResult {
        public final DADamage fLeftDamage;
        public final DAValue<Energy> fLeftHP;
        public DamageResult(DADamage leftDamage, DAValue<Energy> leftHP) {
            fLeftDamage = leftDamage;
            fLeftHP = leftHP;
        }
    }

    public static final DAValue<Energy> NULL_ENERGY = DAValue.create(0, SI.JOULE);
    public static final DADamage NULL_DAMAGE = new DADamage(NULL_ENERGY, NULL_ENERGY, NULL_ENERGY);

    public final DAValue<Energy> damageEM;
    public final DAValue<Energy> damageKINETIC;
    public final DAValue<Energy> damageTHERMAL;

    @Deprecated
    public DADamage() {
        super();
        damageEM = null;
        damageKINETIC = null;
        damageTHERMAL = null;
    }

    public DADamage(DAValue<Energy> em, DAValue<Energy> kinetic, DAValue<Energy> thermal) {
        super();
        damageEM = em;
        damageKINETIC = kinetic;
        damageTHERMAL = thermal;
    }

    @Override
    public String toString() {
        return "[EM: " + damageEM
                + " Kin: " + damageKINETIC
                + " Therm: " + damageTHERMAL
                + "]";
    }

    public boolean isZero() {
        return (damageEM.isZero() && damageTHERMAL.isZero() && damageKINETIC.isZero());
    }

//    public DAValue<Energy> getReversed(Type type) {
//        if (! get(type).getUnit().isConvertible(DAUnit.KnownUnit.percent.getUnit())) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("Cannot reverse non-percent value!", "DADamage.getReversed"));
//            return new DAValue<Energy>(0.0, DAUnit.KnownUnit.percent);
//        }
//        return new DAValue<Energy>(100.0 * (1.0 - get(type).getBaseValue()), DAUnit.KnownUnit.percent);
//    }

    /**
     * Returns the new value of hit points.
     * @param hitPoints
     * @param resistance
     * @return
     */
    public DamageResult applyDamage(DAValue<Energy> hitPoints, DADamage resistance) {
        DAValue<Energy> leftEM;
        DAValue<Energy> leftKin;
        DAValue<Energy> leftTherm;

        DAValue<Energy> resultHP = hitPoints;

        // Calculate effective EM damage done (damage - resistance)
        DAValue<Energy> dmg = damageEM.sub(resistance.damageEM);
        // Resistance strong enough. No damage.
        if (dmg.doubleValueBase() < 0.0) {
            dmg = NULL_ENERGY;
        }
        // more damage than hit points?
        if (resultHP.compareTo(dmg) == -1) {
            // module destroyed
            resultHP = NULL_ENERGY;
            // subtract absorbed damage to get remaining damage
            leftEM = dmg.sub(resultHP);
        } else {
            resultHP = resultHP.sub(dmg);
            // no remaining damage
            leftEM = NULL_ENERGY;
        }

        // Calculate effective Kin damage done (damage - resistance)
        dmg = damageKINETIC.sub(resistance.damageKINETIC);
        // Resistance strong enough. No damage.
        if (dmg.doubleValueBase() < 0.0) {
            dmg = NULL_ENERGY;
        }
        // more damage than hit points?
        if (resultHP.compareTo(dmg) == -1) {
            // module destroyed
            resultHP = NULL_ENERGY;
            // subtract absorbed damage to get remaining damage
            leftKin = dmg.sub(resultHP);
        } else {
            resultHP = resultHP.sub(dmg);
            // no remaining damage
            leftKin = NULL_ENERGY;
        }

        // Calculate effective thermnal damage done (damage - resistance)
        dmg = damageTHERMAL.sub(resistance.damageTHERMAL);
        // Resistance strong enough. No damage.
        if (dmg.doubleValueBase() < 0.0) {
            dmg = NULL_ENERGY;
        }
        // more damage than hit points?
        if (resultHP.compareTo(dmg) == -1) {
            // module destroyed
            resultHP = NULL_ENERGY;
            // subtract absorbed damage to get remaining damage
            leftTherm = dmg.sub(resultHP);
        } else {
            resultHP = resultHP.sub(dmg);
            // no remaining damage
            leftTherm = NULL_ENERGY;
        }
        return new DamageResult(new DADamage(leftEM, leftKin, leftTherm), resultHP);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        damageEM.toStream(stream);
        damageKINETIC.toStream(stream);
        damageTHERMAL.toStream(stream);
    }

    @Override
    public DADamage fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }

        DAValue<Energy> em = new DAValue().fromStream(stream);
        DAValue<Energy> kinetic = new DAValue().fromStream(stream);
        DAValue<Energy> thermal = new DAValue().fromStream(stream);
        return new DADamage(em, kinetic, thermal);
    }

    private static final byte VERSION = 1;

}
