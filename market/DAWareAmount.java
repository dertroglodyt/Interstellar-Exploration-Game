/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.converter.ConversionException;

import de.dertroglodyt.common.protocol.DAParameterList;
import de.dertroglodyt.iegcommon.module.DAbmWaresContainer;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.atom.DataAtom;
import de.hdc.commonlibrary.data.types.collection.DAVector;
import de.hdc.commonlibrary.util.Log;

/**
 * Caution: This class owns the ware it holds!
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWareAmount extends DataAtom {

    public static DAVector<DAWareAmount> toAmountList(DAVector<DAbmWaresContainer> conti) {
        DAVector<DAWareAmount> v = new DAVector<DAWareAmount>(DAWareAmount.class);
        for (DAbmWaresContainer wc : conti) {
            DAWareAmount wa = wc.getContent();
            if ((wa != null) && (wa.getAmount().doubleValue(NewUnits.PIECES) > 0)) {
                boolean done = false;
                for (DAWareAmount a : v) {
                    if (a.getWare().equals(wa.getWare())) {
                        a.add(wa.getAmount());
                        done = true;
                        break;
                    }
                }
                if (! done) {
                    v.add(wa);
                }
            }
        }
        return v;
    }

    @Deprecated
    public DAWareAmount() {
        super();
        ware = new DAWare();
        amount = new DAValue<Pieces>(0, NewUnits.PIECES);
    }

    public DAWareAmount(IDAWare aWare, DAValue<Pieces> aAmount) {
        super();
        if (aWare == null) {
            Log.warn(DAWareAmount.class, "Ware is NULL!");
            throw new IllegalArgumentException("Ware is NULL!");
        }
        if (aWare.isUnique() && (aAmount.doubleValue(NewUnits.PIECES) != 1.0)) {
            Log.warn(DAWareAmount.class, "Amount != 1 of unique ware not allowed!");
            throw new IllegalArgumentException("Amount != 1 of unique ware not allowed!");
        }
        ware = aWare;
        amount = aAmount;
    }

//    public void resolveOther(DAModuleContainer aParentContainer) {
//        ware.resolveOther(aParentContainer);
//    }

    public DAWareAmount add(DAValue<Pieces> value) {
        try {
            if (ware.isUnique()) {
                Log.warn(DAWareAmount.class, "add: Increase of unique ware not allowed!");
                return this;
            }
            DAValue<Pieces> result = amount.add(value);
            if (result.doubleValue(NewUnits.PIECES) < 0) {
                result = new DAValue<Pieces>(0, NewUnits.PIECES);
            }
            return new DAWareAmount(ware, result);
        } catch (ConversionException ex) {
            Log.throwable(DAWareAmount.class, ex, "add");
        }
        return this;
    }

    public DAWareAmount sub(DAValue<Pieces> value) {
        try {
            if (ware.isUnique()) {
                Log.warn(DAWareAmount.class, "sub: Decrease of unique ware not allowed!");
                return this;
            }
            DAValue<Pieces> result = amount.sub(value);
            if (result.doubleValue(NewUnits.PIECES) < 0) {
                result = new DAValue<Pieces>(0, NewUnits.PIECES);
            }
        } catch (ConversionException ex) {
            Log.throwable(DAWareAmount.class, ex, "sub");
        }
        return this;
    }

    public IDAWare getWare() {
        return ware;
    }

    public DAValue<Pieces> getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return amount + " " + ware;
    }

    @Override
    public DataAtom getTestInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DAParameterList parse(String value) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public DAWareAmount clone() {
//        return new DAWareAmount(ware.clone(), amount);
//    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            Log.fatal(DAWareAmount.class, "Unknown version number <" + version + ">.", "DAWareAmount.readExternal");
        }
        ware.readExternal(in);
        amount.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish isChanged read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are isChanged.
         */
        byte version = 1;
        out.writeByte(version);

        ware.writeExternal(out);
        amount.writeExternal(out);
    }

    private static final long serialVersionUID = SerialUIDPool.UID.DAWareAmount.value();

    private IDAWare ware;
    private DAValue<Pieces> amount;

}
