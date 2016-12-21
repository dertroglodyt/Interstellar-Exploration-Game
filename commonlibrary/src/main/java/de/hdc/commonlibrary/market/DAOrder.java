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

import org.jscience.economics.money.Money;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
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
public class DAOrder extends DataAtom {

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
     * Ware amounts to buy/sell.
     */
    public final IDAWare ware;
    /** Clan which created this order. */
    public final DAUniqueID clanID;
    /** Storage where the ware is (sell) / will go (buy) */
    public final DAUniqueID storageID;
    /** Free text for this order */
    public final DAText description;

    public static final DAOrder create(Type aType, IDAWare aWare, DAValue<Money> aPrice
            , DADateTime aExpires, DAUniqueID aStorageID
            , DAUniqueID aClanID, DAText aDescription, DAValue<Pieces> refillMaxAmount
            , DAValue<Duration> aRefillTime, DADateTime aNextFillUp) {
        return new DAOrder(DAUniqueID.createRandom(), aType, aWare, aPrice, aExpires, aStorageID, aClanID
                , aDescription, refillMaxAmount, aRefillTime, aNextFillUp);
    }

//    public static DAOrder createBuyOrder(DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
//            , DAbmStorage aStorage, DAText aDescription) {
//        if (aStorage == null) {
//            Log.warn(DAOrder.class, "createBuyOrder: Storage is NULL!.");
//            return null;
//        }
//        return new DAOrder(Type.BUY_ORDER, aAmount, aPrice, aWareClass, null
//                , aStorage.getItemID(), aStorage.getRootContainer().getOwnerID(), aDescription);
//    }
//
//    public static DAOrder createRefillingBuyOrder(DAValue<Pieces> aAmount, DAValue<Money> aPrice, DAWareClass aWareClass
//            , DAbmStorage aStorage, DAText aDescription, DAValue<Pieces> aMaxAmount, DAValue<Duration> aRefillTime) {
//        if (aStorage == null) {
//            Log.warn(DAOrder.class, "createRefillingBuyOrder: Storage is NULL!.");
//            return null;
//        }
//        if (aMaxAmount == null) {
//            Log.warn(DAOrder.class, "createRefillingBuyOrder: MaxAmount is NULL!.");
//            return null;
//        }
//        if (aMaxAmount.is(DAValue.Sign.NEGATIVE_OR_ZERO)) {
//            Log.warn(DAOrder.class, "createRefillingBuyOrder: MaxAmount is <= 0!.");
//            return null;
//        }
//        return new DAOrder(Type.BUY_ORDER, aAmount, aPrice, aWareClass
//                , DAOrder.getExpirationDate(new DADateTime()), null
//                , aStorage.getItemID()
//                , aStorage.getRootContainer().getOwnerID(), aDescription, aMaxAmount, aRefillTime);
//    }
//
//    public static DAOrder createSellOrder(DAValue<Money> aPrice, DAWare aSellAmount
//            , DAbmStorage aStorage, DAText aDescription) {
//        if (aStorage == null) {
//            Log.warn(DAOrder.class, "createSellOrder: Storage is NULL!.");
//            return null;
//        }
//        return new DAOrder(Type.SELL_ORDER, null, aPrice, null, aSellAmount
//                , aStorage.getItemID(), aStorage.getRootContainer().getOwnerID(), aDescription);
//    }
//
//    public static DAOrder createRefillingSellOrder(DAValue<Money> aPrice, DAWare aSellAmount
//            , DAbmStorage aStorage, DAText aDescription, DAValue<Pieces> aMaxAmount, DAValue<Duration> aRefillTime) {
//        if (aStorage == null) {
//            Log.warn(DAOrder.class, "createRefillingSellOrder: Storage is NULL!.");
//            return null;
//        }
//        return new DAOrder(Type.SELL_ORDER, null, aPrice, null
//                , DAOrder.getExpirationDate(new DADateTime()), aSellAmount
//                , aStorage.getItemID(), aStorage.getRootContainer().getOwnerID()
//                , aDescription, aMaxAmount, aRefillTime);
//    }

    @Deprecated
    public DAOrder() {
        super();
        maxAmount = null;
        refillTime = null;
        nextFillup = null;
        orderID = null;
        type = null;
        created = null;
        expires = null;
        price = null;
        ware = null;
        clanID = null;
        storageID = null;
        description = null;
    }

    public void init(DAWareTypeTree tree) {
        ware.init(tree);
    }

    public static DADateTime getExpirationDate(DADateTime start) {
        return start.addDuration(DAValue.<Duration>create(30, NonSI.DAY));
    }

    public boolean isBuyOrder() {
        return (type == Type.BUY_ORDER);
    }

    public boolean isSellOrder() {
        return (type == Type.SELL_ORDER);
    }

    public boolean isRefilling() {
        return (maxAmount.isPositiv());
    }

    public String getWaresString() {
        if (ware.getAmount().isZero()) {
            return "<ERROR: Keine Ware>";
        } else {
            return "" + ware.toString();
        }
    }

    public boolean containsWare(IDAWare w) {
        if (ware.getItemID() != null) {
            return (w.getItemID().equals(ware.getItemID()));
        } else {
            return ware.equals(w);
        }
    }

    public boolean containsType(DAWareClass type) {
        return ware.getWareClass().equals(type);
    }

    public DAValue<Pieces> getAmount() {
        return ware.getAmount();
    }

    //todo
//    public void doFillup(DADateTime actWorldTime, DAModuleContainer aParentContainer) {
//        try {
//            if ((! maxAmount.is(DAValue.Sign.ZERO)) && (getAmount().isLessThan(maxAmount))) {
//                if (nextFillup == null) {
//                    nextFillup = actWorldTime.addDuration(refillTime, null);
//                }
//                if (actWorldTime.compareTo(nextFillup) >= 0) {
//                    if (type == Type.BUY_ORDER) {
//                        buyAmount = buyAmount.add(new DAValue<Pieces>(1, PIECES));
//                    } else {
//                        if (! ware.getWare().isUnique()) {
//                            ware.add(new DAValue<Pieces>(1, PIECES));
//                        }
//                    }
//                    nextFillup = actWorldTime.addDuration(refillTime, null);
//                    notifyListener(this);
//                }
//            }
//        } catch (Exception e) {
//            Log.warn(DAOrder.class, "doFillUp: DAOrder: <" + this.toParseString("") + "> " + e.toString());
//        }
//    }

    public void remove(DAValue<Pieces> a) {
        try {
            if (isBuyOrder()) {
                ware.add(a);
            } else {
                ware.sub(a);
            }
        } catch (Exception e) {
            Log.warn(DAOrder.class, "remove: DAOrder: <" + this.toString() + "> " + e);
        }
    }

    @Override
    public String toString() {
        if (isBuyOrder()) {
            return orderID + ": " + getAmount() + " " + ware + " " + price;
        } else {
            return orderID + ": " + getWaresString() + " " + price;
        }
    }

    @Override
    public int doCompare(IDataAtom o) {
//        if (!(o instanceof DAOrder)) {
//            return -1;
//        }
        return orderID.compareTo(((DAOrder) o).orderID);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        orderID.toStream(stream);
        stream.writeUTF(type.toString());
        created.toStream(stream);
        expires.toStream(stream);
        price.toStream(stream);
//        stream.writeBoolean(wareClassID != null);
//        if (wareClassID != null) {
//            buyAmount.toStream(stream);
//            wareClassID.toStream(stream);
//            wareClassString.toStream(stream);
//        }
        stream.writeUTF(ware.getClass().getName());
        ware.toStream(stream);
        storageID.toStream(stream);
        clanID.toStream(stream);
        description.toStream(stream);
        maxAmount.toStream(stream);
        refillTime.toStream(stream);
        nextFillup.toStream(stream);
    }

    @Override
    public DAOrder fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAUniqueID aorderID = new DAUniqueID().fromStream(stream);
        Type atype = Type.valueOf(stream.readUTF());
        DADateTime acreated = new DADateTime().fromStream(stream);
        DADateTime aexpires = new DADateTime().fromStream(stream);
        DAValue<Money> aprice = new DAValue<Money>().fromStream(stream);
//        DAValue<Pieces> abuyAmount = null;
//        DAUniqueID awareClassID = null;
//        DAText awareClassString = null;
//        if (stream.readBoolean()) {
//            abuyAmount = new DAValue<Pieces>().fromStream(stream);
//            awareClassID = new DAUniqueID().fromStream(stream);
//            awareClassString = new DAText().fromStream(stream);
//        }
        //todo rethrow
        IDAWare aware = null;
        try {
            final Class<?> c = Class.forName(stream.readUTF());
            aware = (IDAWare) ((IDAWare) c.newInstance()).fromStream(stream);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
        DAUniqueID astorageID = new DAUniqueID().fromStream(stream);
        DAUniqueID aclanID = new DAUniqueID().fromStream(stream);
        DAText adescription = new DAText().fromStream(stream);
        DAValue<Pieces> amaxAmount = new DAValue().fromStream(stream);
        DAValue<Duration> arefillTime = new DAValue().fromStream(stream);
        DADateTime anextFillUp = new DADateTime().fromStream(stream);

        return new DAOrder(aorderID, atype, aware, aprice, aexpires, astorageID, aclanID, adescription
                , amaxAmount, arefillTime, anextFillUp);
    }

    private static final byte VERSION = 1;

    private DAOrder(DAUniqueID aOrderID, Type aType, IDAWare aWare, DAValue<Money> aPrice
            , DADateTime aExpires, DAUniqueID aStorageID
            , DAUniqueID aClanID, DAText aDescription, DAValue<Pieces> refillMaxAmount
            , DAValue<Duration> aRefillTime, DADateTime aNextFillUp) {
        super();
//        if (aType == null) {
//            Log.warn(DAOrder.class, "Type is NULL!.");
//            return;
//        }
//        if (aExpires == null) {
//            Log.warn(DAOrder.class, "Expiration is NULL!.");
//            return;
//        }
//        if (aPrice == null) {
//            Log.warn(DAOrder.class, "Price is NULL!.");
//            return;
//        }
//        if (aPrice.sign() == -1) {
//            Log.warn(DAOrder.class, "Price is < 0!.");
//            return;
//        }
//        if (aStorageID == null) {
//            Log.warn(DAOrder.class, "StorageID is NULL!.");
//            return;
//        }
//        if (aClanID == null) {
//            Log.warn(DAOrder.class, "ClanID is NULL!.");
//            return;
//        }
//        if (aDescription == null) {
//            Log.warn(DAOrder.class, "Description is NULL!.");
//            return;
//        }
//        if (refillMaxAmount == null) {
//            Log.warn(DAOrder.class, "RefillMaxAmount is NULL!.");
//            return;
//        }
//        if (refillMaxAmount.sign() == -1) {
//            Log.warn(DAOrder.class, "RefillMaxAmount is < 0!.");
//            return;
//        }
//        if (aRefillTime == null) {
//            Log.warn(DAOrder.class, "RefillTime is NULL!.");
//            return;
//        }
//        if (aRefillTime.sign() <= 0) {
//            Log.warn(DAOrder.class, "RefillTime is <= 0!.");
//            return;
//        }
//        if (aWare != null) {
//            if ((! refillMaxAmount.isZero()) && (aWare.getAmount().isGreaterThan(refillMaxAmount))) {
//                Log.warn(DAOrder.class, "Amount > MaxAmount!.");
//                return;
//            }
//        }
        orderID = aOrderID;
        type = aType;
        ware = aWare;
        created = DADateTime.now();
        expires = aExpires;
        price = aPrice;
        storageID = aStorageID;
        clanID = aClanID;
        description = aDescription;
        maxAmount = refillMaxAmount;
        refillTime = aRefillTime;
        nextFillup = aNextFillUp;
    }

}
