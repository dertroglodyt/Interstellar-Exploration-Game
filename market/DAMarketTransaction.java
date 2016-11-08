/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import org.jscience.economics.money.Money;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.dertroglodyt.iegcommon.DAClan;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.data.types.atom.DADateTime;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.atom.DataAtom;
import de.hdc.commonlibrary.util.Log;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAMarketTransaction extends DataAtom {

    private static final long serialVersionUID = SerialUIDPool.UID.DAMarketTransaction.value();

    public static enum Type {
        unknown, sold_to, bought_from, donation_to, donation_from, tax_to, tax_from
    };

    private DADateTime time;
    private DAUniqueID sourceID;  // ClanID
    private DALine sourceName;
    private DAUniqueID targetID;  // ClanID
    private DALine targetName;
    private Type type;
    private DAValue<Pieces> amount;
    private DALine what;
    private DAValue<Money> price;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        time.readExternal(in);
        sourceID.readExternal(in);
        targetID.readExternal(in);
        sourceName.readExternal(in);
        targetName.readExternal(in);
        try {
            type = Type.valueOf(in.readUTF());
        } catch (Exception e) {
            type = Type.unknown;
        }
        amount.readExternal(in);
        what.readExternal(in);
        price.readExternal(in);
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

        time.writeExternal(out);
        sourceID.writeExternal(out);
        targetID.writeExternal(out);
        sourceName.writeExternal(out);
        targetName.writeExternal(out);
        out.writeUTF(type.toString());
        amount.writeExternal(out);
        what.writeExternal(out);
        price.writeExternal(out);
    }

    @Deprecated
    public DAMarketTransaction() {
        super();
        this.time = new DADateTime();
        this.sourceID = new DAUniqueID();
        this.targetID = new DAUniqueID();
        this.sourceName = new DALine("<none>");
        this.targetName = new DALine("<none>");
        this.type = Type.tax_to;
        this.amount = new DAValue<Pieces>();
        this.what = new DALine("?");
        this.price = new DAValue<Money>();
    }

//    private DAMarketTransaction(DAOrder order, DVCrsmClan sourceClan, DVCrsmClan targetClan
//            , DAValue<Pieces> amount, Type type) {
//        this(sourceClan, targetClan, type, amount,
//                order.getWaresString(), order.getPrice());
//    }
//
//    private DAMarketTransaction(DVCrsmClan sourceClan, DVCrsmClan targetClan,
//            Type type, DAValue<Pieces> amount, String what, DAValue<Money> price) {
//        super();
//        this.time = new DADateTime();
//        this.sourceID = sourceClan.getUserID();
//        this.targetID = targetClan.getUserID();
//        this.sourceName = new DALine(sourceClan.toString());
//        this.targetName = new DALine(targetClan.toString());
//        this.type = type;
//        this.amount = amount;
//        this.what = new DALine(what);
//        this.price = price;
//    }

    private DAMarketTransaction(DAClan source, DAClan target,
            String what, DAValue<Money> price, Type type) {
        super();
        if (source == null) {
            Log.warn(DAMarketTransaction.class, "Source is NULL!");
        }
        if (target == null) {
            Log.warn(DAMarketTransaction.class, "Target is NULL!");
        }
        if (what == null) {
            Log.warn(DAMarketTransaction.class, "What is NULL!");
        }
        if (price == null) {
            Log.warn(DAMarketTransaction.class, "Price is NULL!");
        }
        if (type == null) {
            Log.warn(DAMarketTransaction.class, "Type is NULL!");
        }
        this.time = new DADateTime();
        this.sourceID = source.getUserID();
        this.targetID = target.getUserID();
        this.sourceName = new DALine(source.toString());
        this.targetName = new DALine(target.toString());
        this.type = type;
        this.amount = new DAValue<Pieces>(1, NewUnits.PIECES);
        this.what = new DALine(what);
        this.price = price;
    }

    public static DAMarketTransaction createBill(DAClan billing, DAClan paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(paying, billing, what, price, Type.sold_to);
    }

    public static DAMarketTransaction createReceipt(DAClan billing, DAClan paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(paying, billing, what, price, Type.bought_from);
    }

    public static DAMarketTransaction createTaxBill(DAClan billing, DAClan paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(paying, billing, what, price, Type.tax_to);
    }

    public static DAMarketTransaction createTaxReceipt(DAClan billing, DAClan paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(paying, billing, what, price, Type.tax_from);
    }

    public static DAMarketTransaction createDonationTo(DAClan donating, DAClan receiving, String what, DAValue<Money> price) {
        return new DAMarketTransaction(donating, receiving, what, price, Type.donation_to);
    }

    public static DAMarketTransaction createDonationFrom(DAClan donating, DAClan receiving, String what, DAValue<Money> price) {
        return new DAMarketTransaction(donating, receiving, what, price, Type.donation_from);
    }

    @Override
    public String toString() {
        switch (type) {
            case sold_to: {
                return time.toString() + "   " + price + " " + amount.toString()
                        + " " + what.toString() + " verkauft an " + targetName  + ".";
            }
            case bought_from: {
                return time.toString() + "   " + price + " " + amount.toString()
                        + " " + what.toString() + " gekauft von " + sourceName + ".";
            }
            case donation_to: {
                return time.toString() + "   " + price + " gespendet an "
                        + targetName + " (" + what.toString() + ").";
            }
            case donation_from: {
                return time.toString() + "   " + price + " gespendet von "
                        + sourceName + " (" + what.toString() + ").";
            }
            case tax_to: {
                return time.toString() + "   " + price + " Gebühren an "
                        + targetName + " (" + what.toString() + ").";
            }
            case tax_from: {
                return time.toString() + "   " + price + " Gebühren von "
                        + sourceName + " (" + what.toString() + ").";
            }
            default : {
                return time.toString() + " " + sourceName + " " + type.toString() + " " +
                amount.toString() + " " + what.toString() + " to " + targetName +
                " for " + price.toString() + ".";
            }
        }
    }

    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DAMarketTransaction parse(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataAtom getTestInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DADateTime getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public DAValue<Money> getPrice() {
        return price;
    }

    public DAUniqueID getSourceID() {
        return sourceID;
    }

    public DAUniqueID getTargetID() {
        return targetID;
    }

//    @Override
//    public DAMarketTransaction clone() {
//        DAMarketTransaction mt = new DAMarketTransaction();
//        mt.time = time.clone();
//        mt.sourceID = sourceID.clone();
//        mt.sourceName = sourceName.clone();
//        mt.targetID = targetID.clone();
//        mt.targetName = targetName.clone();
//        mt.type = type;
//        mt.amount = amount.clone();
//        mt.what = what.clone();
//        mt.price = price.clone();
//        return mt;
//    }

//    @Override
//    public DVCseMarketTransaction getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseMarketTransaction de = new DVCseMarketTransaction(this, editmode, user);
//        addListener(de);
//        return de;
//    }

}
