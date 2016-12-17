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

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.NonSI;

import de.hdc.commonlibrary.data.atom.DAArray;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.data.world.DASubjectMap;
import de.hdc.commonlibrary.module.DABasicModule;
import de.hdc.commonlibrary.module.DAModuleContainer;
import de.hdc.commonlibrary.module.DAShip;
import de.hdc.commonlibrary.module.DAStorage;
import de.hdc.commonlibrary.module.DAWaresContainer;
import de.hdc.commonlibrary.util.Log;

/**
 * A market is located at a station and run by a clanID.
 *
 * @author martin
 */
public class DAMarket extends DataAtom {

    public final DAUniqueID id;
    public final DAText marketName;
    public final DAArray<DAOrder> orders;
    /**
     * Percent tax for placing buy or sell orders.
     */
    public final DAValue<Dimensionless> tax;
    /**
     * Ship where this market is placed upon.
     * The market is owned by the ship.
     * The ship sets this to point back after deserialization of market.
     */
    private transient DAShip ship;

    public static final DAMarket create(DAShip parent, DAUniqueID id, DAText name, DAValue<Dimensionless> tax
            , DAArray<DAOrder> orders) {
        return new DAMarket(parent, id, name, tax, orders);
    }

    public static final DAMarket create(DAShip parent, DAUniqueID id, DAText name, DAValue<Dimensionless> tax) {
        return new DAMarket(parent, id, name, tax, DAArray.create());
    }

    @Deprecated
    public DAMarket() {
        super();
        id = null;
        marketName = null;
        orders = null;
        tax = null;
        ship = null;
    }

    public DAMarket(DAText aStationName, DAShip ship) {
        super();
        id = DAUniqueID.createRandom();
        this.marketName = aStationName;
        orders = DAArray.create();
        tax = DAValue.create(10, NonSI.PERCENT);
        this.ship = ship;
    }

    @Override
    public String toString() {
        return marketName.toString();
    }

    public void init(DAShip parent, DAWareTypeTree tree) {
        for (DAOrder o : orders) {
            o.init(tree);
        }
        ship = parent;
    }

    public DABasicModule getStorage(DAUniqueID moduleID) {
        return ship.getModule(moduleID);
    }

    public DAText getMarketName() {
        return marketName;
    }

    public DAValue<Dimensionless> getTax() {
        return tax;
    }

    public DAModuleContainer getParentContainer() {
        return ship;
    }

    public DAUniqueID getOwnerID() {
        return ship.itemID;
    }

//    public void longTick(DADateTime actWorldTime) {
//        for (DAOrder o : orders) {
//            o.doFillup(actWorldTime, ship);
//        }
//    }

    public void addOrder(DAOrder order) {
        if (order == null) {
            DAResult r = DAResult.createWarning("Order is NULL!", "DAMarket.addOrder");
            Log.result(r);
        }
        orders.add(order);
    }
//
    public void addOrders(DAArray<DAOrder> vo) {
        if (vo == null) {
            final DAResult<?> r = DAResult.createWarning("Orders are NULL!", "DAMarket.addOrders");
            Log.result(r);
        }
        for (final DAOrder o : vo) {
            orders.add(o);
        }
    }

    public void removeOrder(DAOrder order) {
        orders.remove(order);
    }

    public void removeOrders(DAArray<DAOrder> vo) {
        for (final DAOrder o : vo) {
            orders.remove(o);
        }
    }

    public DAArray<DAOrder> getBuying() {
        DAArray<DAOrder> vo = DAArray.<DAOrder>create();
        for (DAOrder o : orders) {
            if (o.isBuyOrder()) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAArray<DAOrder> getSelling() {
        final DAArray<DAOrder> vo = DAArray.<DAOrder>create();
        for (DAOrder o : orders) {
            if (o.isSellOrder()) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAArray<DAOrder> getBuying(IDAWare w) {
        final DAArray<DAOrder> vo = DAArray.<DAOrder>create();
        for (DAOrder o : orders) {
            if (o.isBuyOrder() && o.containsWare(w)) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAArray<DAOrder> getSelling(IDAWare w) {
        final DAArray<DAOrder> vo = DAArray.<DAOrder>create();
        for (DAOrder o : orders) {
            if (o.isSellOrder() && o.containsWare(w)) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAArray<DAOrder> getBuying(DAWareClass wareClass) {
        final DAArray<DAOrder> vo = DAArray.<DAOrder>create();
        for (DAOrder o : orders) {
            if (o.isBuyOrder() && o.containsType(wareClass)) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAArray<DAOrder> getSelling(DAWareClass wareClass) {
        final DAArray<DAOrder> vo = DAArray.<DAOrder>create();
        for (DAOrder o : orders) {
            if (o.isSellOrder() && o.containsType(wareClass)) {
                vo.add(o);
            }
        }
        return vo;
    }

    private DAOrder find(DAOrder order) {
        for (DAOrder o : orders) {
            if (o.isBuyOrder() && (o.compareTo(order) == 0)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Processes a sell order.
     * @param targetStorage
     * @param so
     * @param amount
     * @param scriptID
     * @return
     */
    public DAResult<IDAWare> buy(DAStorage targetStorage, DAOrder so, DAValue<Pieces> amount
            , DAUniqueID scriptID, DASubjectMap userMap) {
        synchronized (orders) {
            if (so == null) {
                return DAResult.createWarning("Order is NULL!.", "DAMarket.buy");
            }
            if (targetStorage == null) {
                return DAResult.createWarning("Storage is NULL!.", "DAMarket.buy");
            }
            if (amount == null) {
                return DAResult.createWarning("Amount is NULL!.", "DAMarket.buy");
            }
            if (! so.isSellOrder()) {
                return DAResult.createWarning("Not a sell order!.", "DAMarket.buy");
            }
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            boolean scripted = false;
            for (StackTraceElement e : ste) {
                if (e.getClassName().contains("DAScriptCPU")) {
                    scripted = true;
                }
                if (scripted) {
                    Log.warn(DAMarket.class, "buy: DAClan.transaction() was invoced by " + ste[2].getClassName()
                            + " Line: " + ste[2].getLineNumber() + " which is forbidden!");
                    return DAResult.createWarning("Invocation allowed only by DAMarket!", "DAMarket.buy");
                }
            }
            try {
                if (ship.getModule(targetStorage.getItemID()) == null) {
                    return DAResult.createWarning("Target storage is not a local storage.", "DAMarket.buy");
                }
                if (! amount.isPositiv()) {
                    return DAResult.createWarning("No valid amount <" + amount.toString() + ">.", "DAMarket.buy");
                }
                if (amount.isGreaterThan(so.getAmount())) {
                    return DAResult.createWarning("Amount bigger than sell order amount.", "DAMarket.buy");
                }
                DAWare wa = DAWare.create(so.ware, amount);
                DAResult<DAWare> rf = targetStorage.canAddAmount(wa);
                if (!rf.isOK()) {
                    return DAResult.createFailed(rf.getMessage(), "DAMarket.buy");
                }
                DAOwner buyer = userMap.get(targetStorage.getLeaserID());
                if (buyer == null) {
                    return DAResult.createWarning("No valid buyer found in WorldNode.", "DAMarket.buy");
                }
                DAStorage sto = (DAStorage) ship.getModule(so.storageID);
                if (sto == null) {
                    return DAResult.createWarning("Order source storage not found in ship.", "DAMarket.buy");
                }
                DAOwner owner = userMap.get(sto.getLeaserID());
                if (owner == null) {
                    return DAResult.createWarning("No valid order owner found in WorldNode.", "DAMarket.buy");
                }
                DAOrder order = find(so);
                if (order == null) {
                    return DAResult.createFailed("Could not find requested ware in selling ware.", "DAMarket.buy");
                }
                DAValue<Money> price = so.price.scale(amount);
                DAMarketTransaction smt = DAMarketTransaction.createBill(owner, buyer, so.toString(), price);
                DAMarketTransaction bmt = DAMarketTransaction.createReceipt(owner, buyer, so.toString(), price);
                // Wares are not stored in an actual storage any more!
                // Instead the order holds the wares.
//                if (! sto.remove(so.getSellAmount())) {
//                    return DAResult.createFailed("Could not take requested ware from storage.", "DAMarket.buy");
//                }
                DAResult<IDAWare> r = owner.transaction(smt);
                if (! r.isOK()) {
//                    DAResult r2 = sto.add(so.getSellAmount());
//                    if (! r2.isOK()) {
//                        DVCErrorHandler.raiseError(DAResult.createWarning("Container lost: " + so.getSellAmount()
//                                , "DAMarket.buy"));
//                    }
                    return r;
                }
                r = buyer.transaction(bmt);
                if (! r.isOK()) {
//                    DAResult r2 = sto.add(so.getSellAmount());
//                    if (! r2.isOK()) {
//                        DVCErrorHandler.raiseError(DAResult.createWarning("Container lost(2): " + so.getSellAmount()
//                                , "DAMarket.buy"));
//                    }
                    owner.undoTransaction(smt);
                    return r;
                }
                final DAResult<IDAWare> r2 = targetStorage.addAmount(wa);
                if (! r2.isOK()) {
                    order.remove(amount.sub(r2.getResult().getAmount()));
                    Log.warn(DAMarket.class, "buy: Can not add wares to storage: " + so.ware.getAmount());
                    return r2;
                }
                // TODO
//                buyer.addProperty(r2.getResult());
//                owner.removeProperty(amount());
                order.remove(amount);
                if ((! order.getAmount().isPositiv()) && (! order.isRefilling())) {
                    orders.remove(order);
                }
                return DAResult.createOK("You bought " + amount + " of " + so.getWaresString() + ".", "DAMarket.buy");
            } catch (Exception e) {
                return DAResult.createFailed(e.toString(), "DAMarket.buy");
            }
        }
    }

    /**
     * Processes a buy order.
     * @param sourceStorage
     * @param bo
     * @param contis
     * @param scriptID
     * @return
     */
    public DAResult sell(DAStorage sourceStorage, DAOrder bo, DAArray<DAWaresContainer> contis
            , DAUniqueID scriptID, DASubjectMap userMap) {
        synchronized (orders) {
            if (bo == null) {
                return DAResult.createWarning("Order is NULL!.", "DAMarket.sell");
            }
            if (sourceStorage == null) {
                return DAResult.createWarning("Storage is NULL!.", "DAMarket.sell");
            }
            if (contis == null) {
                return DAResult.createWarning("Amount is NULL!.", "DAMarket.sell");
            }
            if (! bo.isBuyOrder()) {
                return DAResult.createWarning("Not a buy order!.", "DAMarket.sell");
            }
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            boolean scripted = false;
            for (StackTraceElement e : ste) {
                if (e.getClassName().contains("DAScriptCPU")) {
                    scripted = true;
                }
                if (scripted) {
                    Log.warn(DAMarket.class, "sell: DAOrganisation.transaction() was invoced by " + ste[2].getClassName()
                            + " Line: " + ste[2].getLineNumber() + " which is forbidden!");
                    return DAResult.createWarning("Invocation allowed only by DAMarket!", "DAMarket.sell");
                }
            }
            try {
                if (ship.getModule(sourceStorage.getItemID()) == null) {
                    return DAResult.createWarning("Source storage is not a local storage.", "DAMarket.sell");
                }
                DAStorage sto = (DAStorage) ship.getModule(bo.storageID);
                if (sto == null) {
                    return DAResult.createWarning("Order storage not found in ship.", "DAMarket.sell");
                }
                if (contis.getSize() <= 0) {
                    return DAResult.createWarning("No source containers.", "DAMarket.sell");
                }
                DAValue<Pieces> x = DAValue.create(0, NewUnits.PIECES);
                for (DAWaresContainer wc : contis) {
                    if (! wc.getContentType().id.equals(bo.ware.getClassID())) {
                        return DAResult.createFailed("Container content does not match order.", "DAMarket.sell");
                    }
                    x = x.add(wc.getActualAmount());
                }
                if (x.isZero()) {
                    return DAResult.createFailed("Container does not hold any wares.", "DAMarket.sell");
                }
                if (x.isGreaterThan(bo.getAmount())) {
                    return DAResult.createFailed("Container hold more wares than ordered.", "DAMarket.sell");
                }
                if (! sto.isOnline()) {
                    return DAResult.createFailed("Target storage is not online.", "DAMarket.sell");
                }
                DAResult rf = sto.canAdd(contis);
                if (!rf.isOK()) {
                    return DAResult.createFailed("Not enough space in destination storage.", "DAMarket.sell");
                }
                DAOwner seller = userMap.get(sourceStorage.getLeaserID());
                if (seller == null) {
                    return DAResult.createWarning("No valid seller found in WorldNode.", "DAMarket.sell");
                }
                DAOwner owner = userMap.get(sto.getLeaserID());
                if (owner == null) {
                    return DAResult.createWarning("No valid order owner found in WorldNode.", "DAMarket.sell");
                }
                DAOrder w = find(bo);
                if (w == null) {
                    return DAResult.createFailed("Could not find requested ware in buying ware.", "DAMarket.sell");
                }
                DAValue<Money> price = bo.price.scale(x);
                DAMarketTransaction smt = DAMarketTransaction.createBill(seller, owner, bo.toString(), price);
                DAMarketTransaction bmt = DAMarketTransaction.createReceipt(seller, owner, bo.toString(), price);
                DAResult<DAArray<DAWaresContainer>> rr = sourceStorage.remove(contis);
                if (! rr.isOK()) {
                    // try partial rollback for partial remove
                    sourceStorage.add(rr.getResult());
                    return DAResult.createFailed("Could not take requested ware from storage.", "DAMarket.sell");
                }
                DAResult r = owner.transaction(bmt);
                if (! r.isOK()) {
                    DAResult<DAArray<DAWaresContainer>> r2 = sourceStorage.add(contis);
                    if (! r2.isOK()) {
                        // try complete rollback
                        sourceStorage.add(contis);
                        Log.warn(DAMarket.class, "sell: Could not add ware to storage: " + contis.toString());
                    }
                    return r;
                }
                r = seller.transaction(smt);
                if (! r.isOK()) {
                    // try complete rollback
                    DAResult r2 = sourceStorage.add(contis);
                    if (! r2.isOK()) {
                        // it's dead jim...
                        Log.warn(DAMarket.class, "sell: Container lost: " + contis.toString());
                    }
                    owner.undoTransaction(bmt);
                    return r;
                }
                DAResult<DAArray<DAWaresContainer>> r2 = sto.add(contis);
                if (! r2.isOK()) {
                    // try partial rollback after partial add
                    sourceStorage.add(r2.getResult());
                    Log.warn(DAMarket.class, "sell: Could not add all wares to storage: " + contis.toString());
                    return r2;
                }
                // TODO
//                owner.addProperty(r2.getResult());
//                seller.removeProperty(contis);
                if ((bo.getAmount().isGreaterThan(x)) || (bo.isRefilling())) {
                    find(bo).remove(x);
                } else {
                    orders.remove(w);
                }
                sto.compact();
                return DAResult.createOK("You sold " + x + " of " + bo.getWaresString() + ".", "DAMarket.sell");
            } catch (Exception e) {
                return DAResult.createFailed(e.getMessage(), "DAMarket.sell");
            }
        }
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        id.toStream(stream);
        marketName.toStream(stream);
        tax.toStream(stream);
        orders.toStream(stream);
    }

    /**
     * Init() needs to be called after deserialization.
     */
    @Override
    public DAMarket fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DAUniqueID mid = new DAUniqueID().fromStream(stream);
        final DAText mmarketName = new DAText().fromStream(stream);
        final DAValue<Dimensionless> mtax = new DAValue().fromStream(stream);
        final DAArray<DAOrder> morders = new DAArray<DAOrder>().fromStream(stream);
        return new DAMarket(mid, mmarketName, mtax, morders);
    }

    private static final byte VERSION = 1;

    private DAMarket(DAUniqueID id, DAText name, DAValue<Dimensionless> tax, DAArray<DAOrder> orders) {
        super();
        this.id = id;
        this.marketName = name;
        this.tax = tax;
        this.orders = orders;
        this.ship = null;
    }

    private DAMarket(DAShip parent, DAUniqueID id, DAText name, DAValue<Dimensionless> tax, DAArray<DAOrder> orders) {
        super();
        this.id = id;
        this.marketName = name;
        this.tax = tax;
        this.orders = orders;
        this.ship = parent;
    }

}
