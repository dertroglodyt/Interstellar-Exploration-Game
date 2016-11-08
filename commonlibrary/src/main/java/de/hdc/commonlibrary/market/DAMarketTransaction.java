/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import org.jscience.economics.money.Money;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.util.Log;

/**
 *
 * @author martin
 */
public class DAMarketTransaction extends DataAtom {

    public enum Type {
        UNKNOWN, SOLD_TO, BOUGHT_FROM, DONATION_TO, DONATION_FROM, TAX_TO, TAX_FROM
    }

    public final DADateTime time;
    public final DAUniqueID sourceID;
    public final DAText sourceName;
    public final DAUniqueID targetID;
    public final DAText targetName;
    public final Type type;
    public final DAValue<Pieces> amount;
    public final DAText what;
    public final DAValue<Money> price;

    public static DAMarketTransaction createBill(DAOrganizationBasic billing, DAOrganizationBasic paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(billing, paying, what, price, Type.SOLD_TO);
    }

    public static DAMarketTransaction createReceipt(DAOrganizationBasic billing, DAOrganizationBasic paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(billing, paying, what, price, Type.BOUGHT_FROM);
    }

    public static DAMarketTransaction createTaxBill(DAOrganizationBasic billing, DAOrganizationBasic paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(billing, paying, what, price, Type.TAX_TO);
    }

    public static DAMarketTransaction createTaxReceipt(DAOrganizationBasic billing, DAOrganizationBasic paying, String what, DAValue<Money> price) {
        return new DAMarketTransaction(billing, paying, what, price, Type.TAX_FROM);
    }

    public static DAMarketTransaction createDonationTo(DAOrganizationBasic receiving, DAOrganizationBasic donating, String what, DAValue<Money> price) {
        return new DAMarketTransaction(receiving, donating, what, price, Type.DONATION_TO);
    }

    public static DAMarketTransaction createDonationFrom(DAOrganizationBasic receiving, DAOrganizationBasic donating, String what, DAValue<Money> price) {
        return new DAMarketTransaction(receiving, donating, what, price, Type.DONATION_FROM);
    }

    @Override
    public String toString() {
        String s = time + " " + sourceName + " " + type + " " +
                amount + " " + what + " to " + targetName +
                " for " + price + ".";
        switch (type) {
            case SOLD_TO: {
                s = time + "   " + price + " " + amount
                        + " " + what + " verkauft an " + targetName  + ".";
                break;
            }
            case BOUGHT_FROM: {
                s = time + "   " + price + " " + amount
                        + " " + what + " gekauft von " + sourceName + ".";
                break;
            }
            case DONATION_TO: {
                s = time + "   " + price + " gespendet an "
                        + targetName + " (" + what + ").";
                break;
            }
            case DONATION_FROM: {
                s = time + "   " + price + " gespendet von "
                        + sourceName + " (" + what + ").";
                break;
            }
            case TAX_TO: {
                s = time + "   " + price + " Gebühren an "
                        + targetName + " (" + what + ").";
                break;
            }
            case TAX_FROM: {
                s = time + "   " + price + " Gebühren von "
                        + sourceName + " (" + what + ").";
                break;
            }
            case UNKNOWN: {
                s = time + " " + sourceName + " " + type + " " +
                amount + " " + what + " to " + targetName +
                " for " + price + ".";
                break;
            }
        }
        return s;
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        time.toStream(stream);
        sourceID.toStream(stream);
        targetID.toStream(stream);
        sourceName.toStream(stream);
        targetName.toStream(stream);
        stream.writeUTF(type.toString());
        amount.toStream(stream);
        what.toStream(stream);
        price.toStream(stream);
    }

    @Override
    public DAMarketTransaction fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DADateTime atime = new DADateTime().fromStream(stream);
        final DAUniqueID asourceID = new DAUniqueID().fromStream(stream);
        final DAUniqueID atargetID = new DAUniqueID().fromStream(stream);
        final DAText asourceName = new DAText().fromStream(stream);
        final DAText atargetName = new DAText().fromStream(stream);
        Type atype = Type.UNKNOWN;
        try {
            atype = Type.valueOf(stream.readUTF());
        } catch (Exception e) {
            atype = Type.UNKNOWN;
        }
        final DAValue<Pieces> aamount = new DAValue().fromStream(stream);
        final DAText awhat = new DAText().fromStream(stream);
        final DAValue<Money> aprice = new DAValue().fromStream(stream);
        return new DAMarketTransaction(atime, atargetID, asourceID, atargetName, asourceName, aamount, awhat, aprice, atype);
    }

    private static final byte VERSION = 1;

    //todo warum kein amount?
    private DAMarketTransaction(DAOrganizationBasic target, DAOrganizationBasic source,
                                String what, DAValue<Money> price, Type type) {
        this(DADateTime.now(), target.id, source.id, target.name, source.name
                , DAValue.<Pieces>create(1, NewUnits.PIECES), DAText.create(what), price, type);
    }

    private DAMarketTransaction(DADateTime time, DAUniqueID target, DAUniqueID source
            , DAText targetName, DAText sourceName, DAValue<Pieces> amount, DAText what
            , DAValue<Money> price, Type type) {
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
        this.time = time;
        this.sourceID = source;
        this.targetID = target;
        this.targetName = targetName;
        this.sourceName = sourceName;
        this.type = type;
        this.amount = amount;
        this.what = what;
        this.price = price;
    }

}
