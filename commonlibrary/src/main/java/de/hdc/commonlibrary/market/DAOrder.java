/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import org.jscience.economics.money.Money;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.unit.NonSI;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DAVector;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.module.DAModuleContainer;
import de.hdc.commonlibrary.util.Log;

import static de.hdc.commonlibrary.data.quantity.NewUnits.PIECES;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public abstract class DAOrder extends DataAtom {

    public enum Type {SELL_ORDER, BUY_ORDER};

    /**
     * For refilling orders.
     * Max amount to be filled up.
     * A Value of 0 means this is not to be filled up (normal order).
     */
    public final DAValue<Pieces> maxAmount;
    /**
     * For refilling orders.
     * Time after which a new order (up to maxAmount) is created.
     */
    public final DAValue<Duration> refillTime;

    /**
     * UTC date when next fillup should happen.
     * Used by DVCMarket.longTick
     */
    public final transient DADateTime nextFillup;

    public final DAUniqueID orderID;
    public final Type type;
    public final DADateTime created;
    public final DADateTime expires;
    public final DAValue<Money> price;
    /**
     * Amount of ware to buy.
     * Null if SELL_ORDER.
     */
    public final DAValue<Pieces> buyAmount;
    /**
     * Type of ware to buy.
     * Null if SELL_ORDER.
     */
    public final DAUniqueID wareClassID;
    /**
     * Name of ware to buy.
     * Null if SELL_ORDER.
     */
    public final DAText wareClassString;
    /**
     * Ware amounts to sell.
     * Null if BUY_ORDER.
     */
    public final IDAWare list;
    /** Clan which created this order. */
    public final DAUniqueID clanID;
    /** Storage where the ware is (sell) / will go (buy) */
    public final DAUniqueID storageID;
    /** Free text for this order */
    public final DAText description;

    @Override
    @SuppressWarnings("deprecation")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            throw new IllegalStateException("readExternal: Wrong version number " + version);
        }
        orderID.readExternal(in);
        type = Type.valueOf(in.readUTF());
        created.readExternal(in);
        expires.readExternal(in);
        price.readExternal(in);
        if (in.readBoolean()) {
            buyAmount = new DAValue<Pieces>();
            buyAmount.readExternal(in);
            wareClassID = new DAUniqueID();
            wareClassID.readExternal(in);
            wareClassString = new DALine();
            wareClassString.readExternal(in);
        }
        if (in.readBoolean()) {
            if (version == 1) {
                DAVector<DAWare> v = new DAVector<DAWareAmount>(DAWareAmount.class);
                v.readExternal(in);
                list = new DAWare(v.firstElement().getWare(), v.firstElement().getAmount());
            } else {
                list = new DAWare();
                list.readExternal(in);
            }
        }
        storageID.readExternal(in);
        clanID.readExternal(in);
        description.readExternal(in);
        maxAmount.readExternal(in);
        refillTime.readExternal(in);
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

        orderID.writeExternal(out);
        out.writeUTF(type.toString());
        created.writeExternal(out);
        expires.writeExternal(out);
        price.writeExternal(out);
        out.writeBoolean((wareClassID != null));
        if (wareClassID != null) {
            buyAmount.writeExternal(out);
            wareClassID.writeExternal(out);
            wareClassString.writeExternal(out);
        }
        out.writeBoolean((list != null));
        if (list != null) {
            list.writeExternal(out);
        }
        storageID.writeExternal(out);
        clanID.writeExternal(out);
        description.writeExternal(out);
        maxAmount.writeExternal(out);
        refillTime.writeExternal(out);
    }

    public static DADateTime getExpirationDate(DADateTime start) {
        return start.addDuration(new DAValue<Duration>(30, NonSI.DAY), null);
    }

    public static DAOrder createBuyOrder(DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
            , DAbmStorage aStorage, DAText aDescription) {
        if (aStorage == null) {
            Log.warn(DAOrder.class, "createBuyOrder: Storage is NULL!.");
            return null;
        }
        return new DAOrder(Type.BUY_ORDER, aAmount, aPrice, aWareClass, null
                , aStorage.getItemID(), aStorage.getRootContainer().getOwnerID(), aDescription);
    }

    public static DAOrder createRefillingBuyOrder(DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
            , DAbmStorage aStorage, DAText aDescription, DAValue<Pieces> aMaxAmount, DAValue<Duration> aRefillTime) {
        if (aStorage == null) {
            Log.warn(DAOrder.class, "createRefillingBuyOrder: Storage is NULL!.");
            return null;
        }
        if (aMaxAmount == null) {
            Log.warn(DAOrder.class, "createRefillingBuyOrder: MaxAmount is NULL!.");
            return null;
        }
        if (aMaxAmount.is(DAValue.Sign.NEGATIVE_OR_ZERO)) {
            Log.warn(DAOrder.class, "createRefillingBuyOrder: MaxAmount is <= 0!.");
            return null;
        }
        return new DAOrder(Type.BUY_ORDER, aAmount, aPrice, aWareClass
                , DAOrder.getExpirationDate(new DADateTime()), null
                , aStorage.getItemID()
                , aStorage.getRootContainer().getOwnerID(), aDescription, aMaxAmount, aRefillTime);
    }

    public static DAOrder createSellOrder(DAValue<Money> aPrice, DAWare aSellAmount
            , DAbmStorage aStorage, DAText aDescription) {
        if (aStorage == null) {
            Log.warn(DAOrder.class, "createSellOrder: Storage is NULL!.");
            return null;
        }
        return new DAOrder(Type.SELL_ORDER, null, aPrice, null, aSellAmount
                , aStorage.getItemID(), aStorage.getRootContainer().getOwnerID(), aDescription);
    }

    public static DAOrder createRefillingSellOrder(DAValue<Money> aPrice, DAWare aSellAmount
            , DAbmStorage aStorage, DAText aDescription, DAValue<Pieces> aMaxAmount, DAValue<Duration> aRefillTime) {
        if (aStorage == null) {
            Log.warn(DAOrder.class, "createRefillingSellOrder: Storage is NULL!.");
            return null;
        }
        return new DAOrder(Type.SELL_ORDER, null, aPrice, null
                , DAOrder.getExpirationDate(new DADateTime()), aSellAmount
                , aStorage.getItemID(), aStorage.getRootContainer().getOwnerID()
                , aDescription, aMaxAmount, aRefillTime);
    }

//    public void resolveOther(DAModuleContainer aParentContainer) {
//        if (list != null) {
//            list.resolveOther(aParentContainer);
//        }
//    }

    @Override
    public DAOrder getTestInstance() {
        throw new UnsupportedOperationException();
    }

    public boolean isBuyOrder() {
        return (type == Type.BUY_ORDER);
    }

    public boolean isSellOrder() {
        return (type == Type.SELL_ORDER);
    }

    public boolean isRefilling() {
        return (maxAmount.is(DAValue.Sign.POISTIVE_NON_ZERO));
    }

    public DAWare getSellAmount() {
        return list;
    }

    public DAUniqueID getWaresClassID() {
        return wareClassID;
    }

    public String getWaresString() {
        if (isBuyOrder()) {
            return wareClassString.toString();
        }
        if (list.getAmount().is(DAValue.Sign.ZERO)) {
            return "<Keine Ware>";
        } else {
            return "" + list.toString();
        }
    }

    public boolean containsWare(IDAWare w) {
        if (list == null) {
            return (w.getClassID().equals(wareClassID));
        } else {
            return list.getWare().getWareClass().equals(w.getWareClass());
        }
    }

    public boolean containsType(DAWaresType wt) {
        if (list == null) {
            return false;
        } else {
            return list.getWare().getTypeID().equals(wt.getItemID());
        }
    }

    public DAValue<Pieces> getAmount() {
        if (isBuyOrder()) {
            return buyAmount;
        } else {
            return list.getAmount();
        }
    }

    public void setDescription(DAText val) {
        description.setText(val.toString());
        notifyListener(this);
    }

    public void setPrice(DAValue<Money> val) {
        price = val;
        notifyListener(this);
    }

    public void doFillup(DADateTime actWorldTime, DAModuleContainer aParentContainer) {
        try {
            if ((! maxAmount.is(DAValue.Sign.ZERO)) && (getAmount().isLessThan(maxAmount))) {
                if (nextFillup == null) {
                    nextFillup = actWorldTime.addDuration(refillTime, null);
                }
                if (actWorldTime.compareTo(nextFillup) >= 0) {
                    if (type == Type.BUY_ORDER) {
                        buyAmount = buyAmount.add(new DAValue<Pieces>(1, PIECES));
                    } else {
                        if (! list.getWare().isUnique()) {
                            list.add(new DAValue<Pieces>(1, PIECES));
                        }
                    }
                    nextFillup = actWorldTime.addDuration(refillTime, null);
                    notifyListener(this);
                }
            }
        } catch (Exception e) {
            Log.warn(DAOrder.class, "doFillUp: DAOrder: <" + this.toParseString("") + "> " + e.toString());
        }
    }

    public void remove(DAValue<Pieces> a) {
        try {
            if (isBuyOrder()) {
                buyAmount = buyAmount.sub(a);
            } else {
                list.sub(a);
            }
        } catch (Exception e) {
            Log.warn(DAOrder.class, "remove: DAOrder: <" + this.toParseString("") + "> " + e.toString());
        }
    }

    @Override
    public String toString() {
        if (isBuyOrder()) {
            return getAmount().toString() + " " + wareClassString + " " + price.toString();
        } else {
            return getWaresString() + " " + price.toString();
        }
    }

    @Override
    public int doCompare(IDataAtom o) {
        if (!(o instanceof DAOrder)) {
            return -1;
        }
        return orderID.compareTo(((DAOrder) o).orderID);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
//        stream.writeUTF(value);
    }

    public static DAOrder fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        return DAOrder.create(stream.readUTF());
    }

    private static final byte VERSION = 1;private DAOrder(Type aType, DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
            , DAWare aSellAmount, DAUniqueID aStorageID
            , DAUniqueID aClanID, DAText aDescription) {
        this(aType, aAmount, aPrice, aWareClass, DAOrder.getExpirationDate(new DADateTime())
                , aSellAmount, aStorageID, aClanID, aDescription);
    }

    private DAOrder(Type aType, DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
            , DADateTime aExpires, DAWare aSellAmount
            , DAUniqueID aStorageID, DAUniqueID aClanID, DAText aDescription) {
        this(aType, aAmount, aPrice, aWareClass, aExpires, aSellAmount, aStorageID, aClanID, aDescription
                , new DAValue<Pieces>(0, PIECES), new DAValue<Duration>(15, NonSI.MINUTE));
    }

    private DAOrder(Type aType, DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
            , DADateTime aExpires, DAWare aSellAmount
            , DAUniqueID aStorageID, DAUniqueID aClanID, DAText aDescription
            , DAValue<Pieces> refillMaxAmount, DAValue<Duration> aRefillTime) {
        super();
        if (aType == null) {
            Log.warn(DAOrder.class, "Type is NULL!.");
            return;
        }
        if (aExpires == null) {
            Log.warn(DAOrder.class, "Expiration is NULL!.");
            return;
        }
        if (aPrice == null) {
            Log.warn(DAOrder.class, "Price is NULL!.");
            return;
        }
        if (aPrice.sign() == -1) {
            Log.warn(DAOrder.class, "Price is < 0!.");
            return;
        }
        if (aStorageID == null) {
            Log.warn(DAOrder.class, "StorageID is NULL!.");
            return;
        }
        if (aClanID == null) {
            Log.warn(DAOrder.class, "ClanID is NULL!.");
            return;
        }
        if (aDescription == null) {
            Log.warn(DAOrder.class, "Description is NULL!.");
            return;
        }
        if (refillMaxAmount == null) {
            Log.warn(DAOrder.class, "RefillMaxAmount is NULL!.");
            return;
        }
        if (refillMaxAmount.sign() == -1) {
            Log.warn(DAOrder.class, "RefillMaxAmount is < 0!.");
            return;
        }
        if (aRefillTime == null) {
            Log.warn(DAOrder.class, "RefillTime is NULL!.");
            return;
        }
        if (aRefillTime.sign() <= 0) {
            Log.warn(DAOrder.class, "RefillTime is <= 0!.");
            return;
        }
        if (aAmount != null) {
            if ((! refillMaxAmount.isZero()) && (aAmount.doCompare(refillMaxAmount) > 0)) {
                Log.warn(DAOrder.class, "Amount > MaxAmount!.");
                return;
            }
        }
        orderID = DAUniqueID.createRandom();
        type = aType;
        // only to be 100% shure.
        if (aType == Type.BUY_ORDER) {
            if (aWareClass == null) {
                Log.warn(DAOrder.class, "WareClass is NULL!.");
                return;
            }
            if (aAmount == null) {
                Log.warn(DAOrder.class, "Amount is NULL!.");
                return;
            }
            if (aAmount.is(DAValue.Sign.NEGATIVE_OR_ZERO)) {
                Log.warn(DAOrder.class, "Amount is <= 0!.");
                return;
            }
        } else {
            if (aSellAmount == null) {
                Log.warn(DAOrder.class, "Sell WareAmount is NULL!.");
                return;
            }
        }
        created = new DADateTime();
        expires = aExpires;
        price = aPrice;
        if (aWareClass != null) {
            buyAmount = aAmount;
            wareClassID = aWareClass.getItemID();
            wareClassString = aWareClass.getName();
        } else {
            list = aSellAmount;
        }
        storageID = aStorageID;
        clanID = aClanID;
        description = aDescription;
        maxAmount = refillMaxAmount;
        refillTime = aRefillTime;
    }

}
