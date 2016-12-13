/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import org.jscience.economics.money.Money;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.NonSI;

import de.dertroglodyt.common.protocol.IParameterType;
import de.dertroglodyt.common.protocol.IRemoteActionType;
import de.dertroglodyt.iegcommon.DAClan;
import de.dertroglodyt.iegcommon.module.DAModuleContainer;
import de.dertroglodyt.iegcommon.module.DAbmHangar;
import de.dertroglodyt.iegcommon.module.DAbmStorage;
import de.dertroglodyt.iegcommon.module.DAbmWaresContainer;
import de.dertroglodyt.iegcommon.module.DAmcShip;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.data.types.atom.DADateTime;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.atom.DataAtom;
import de.hdc.commonlibrary.data.types.collection.DAVector;
import de.hdc.commonlibrary.data.types.compound.DAResult;
import de.hdc.commonlibrary.util.Log;

/**
 * A market is located at a station and run by a clanID.
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAMarket extends DataAtom { //implements IActionHandler {

    private static final long serialVersionUID = SerialUIDPool.UID.DAMarket.value();

    public static enum Parameters implements IParameterType {

        NONE(null),
        SHIPID(DAUniqueID.class),
        ORDERS(DAVector.class),
        WARE(DAWare.class),
        WARESTYPE(DAWaresType.class),
        STORAGEID(DAUniqueID.class),
        ORDER(DAOrder.class),
        AMOUNT(DAValue.class),
        CONTAINER(DAbmWaresContainer.class),
        CONTAINERLIST(DAVector.class),
        SCRIPT_ID(DAUniqueID.class),
        ;

        private DALine name;
        private Class<? extends DataAtom> c;

        Parameters(Class<? extends DataAtom> aClass) {
            name = new DALine(this.toString());
            c = aClass;
        }

        @Override
        public DALine getName() {
            return name;
        }

        @Override
        public Class<? extends DataAtom> getType() {
            return c;
        }

    }

    public static enum Action implements IRemoteActionType {
        ADD_ORDERS(Parameters.NONE, Parameters.SHIPID, Parameters.ORDERS),
        REMOVE_ORDERS(Parameters.NONE, Parameters.SHIPID, Parameters.ORDERS),
        GET_BUYING(Parameters.ORDERS, Parameters.SHIPID),
        GET_SELLING(Parameters.ORDERS, Parameters.SHIPID),
        GET_BUYING_W(Parameters.ORDERS, Parameters.SHIPID, Parameters.WARE),
        GET_SELLING_W(Parameters.ORDERS, Parameters.SHIPID, Parameters.WARE),
        GET_BUYING_WT(Parameters.ORDERS, Parameters.SHIPID, Parameters.WARESTYPE),
        GET_SELLING_WT(Parameters.ORDERS, Parameters.SHIPID, Parameters.WARESTYPE),
        BUY(Parameters.NONE, Parameters.SHIPID, Parameters.STORAGEID, Parameters.ORDER, Parameters.AMOUNT, Parameters.SCRIPT_ID),
        SELL(Parameters.NONE, Parameters.SHIPID, Parameters.STORAGEID, Parameters.ORDER, Parameters.CONTAINERLIST, Parameters.SCRIPT_ID),
        SPLIT(Parameters.CONTAINER, Parameters.SHIPID, Parameters.STORAGEID, Parameters.CONTAINER, Parameters.AMOUNT),
        ;

        private DALine name;
        private ArrayList<IParameterType> input;
        private IParameterType result;

        Action(IParameterType r, IParameterType... in) {
            name = new DALine(this.toString());
            input = new ArrayList<IParameterType>(in.length);
            input.addAll(Arrays.asList(in));
            result = r;
        }

        @Override
        public DALine getName() {
            return name;
        }

        @Override
        public int getInputTypeSize() {
            return input.size();
        }

        @Override
        public Iterator<IParameterType> getInputType() {
            return input.iterator();
        }

        @Override
        public IParameterType getResultType() {
            return result;
        }

    }

    protected DALine marketName;
    protected final DAVector<DAOrder> orders;
    /**
     * Percent tax for placing buy or sell orders.
     */
    protected DAValue<Dimensionless> tax;

    /**
     * Ship where this market is placed upon.
     */
    protected transient DAmcShip ship;
//    private transient HashSet<IDVCRemoteDataModel> remoteListener;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            throw new IllegalStateException("readExternal: Unknown version number <" + version + ">.");
        }
        marketName.readExternal(in);
        orders.readExternal(in);
        tax.readExternal(in);
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

        marketName.writeExternal(out);
        orders.writeExternal(out);
        tax.writeExternal(out);
    }

    @Deprecated
    public DAMarket() {
        this(new DALine("<unknown>"));
    }

    public DAMarket(DALine aStationName) {
        super();
        this.marketName = aStationName;
        orders = new DAVector<DAOrder>(DAOrder.class);
        tax = new DAValue<Dimensionless>(10, NonSI.PERCENT);
//        remoteListener = new HashSet<IDVCRemoteDataModel>(0);
    }

    @Override
    public DAMarket getTestInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return marketName.toString();
    }

    @Override
    public String toParseString(String levelTab) {
        return levelTab + toString();
    }

    @Override
    public DAbmHangar parse(String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void init(DAmcShip aShip) {
        if (aShip == null) {
            Log.warn(DAMarket.class, "init: Parent ship is NULL!");
        }
        ship = aShip;
//        for (DAOrder o : orders) {
//            o.resolveOther(ship);
//        }
    }

    public DALine getMarketName() {
        return marketName;
    }

    public DAModuleContainer getParentContainer() {
        return ship;
    }

    public DAUniqueID getOwnerID() {
        return ship.getOwnerID();
    }

    public void longTick(DADateTime actWorldTime) {
        for (DAOrder o : orders) {
            o.doFillup(actWorldTime, ship);
        }
    }

    public DAResult<?> addOrder(DAOrder order) {
        if (order == null) {
            DAResult r = DAResult.createWarning("Order is NULL!", "DAMarket.addOrder");
            Log.result(r);
            return r;
        }
        orders.add(order);
        notifyListener(this);
        return DAResult.createOK("ok", "DAMarket.addOrder");
    }

    public DAResult addOrders(DAVector<DAOrder> vo) {
        if (vo == null) {
            DAResult r = DAResult.createWarning("Orders are NULL!", "DAMarket.addOrders");
            Log.result(r);
            return r;
        }
        orders.addAll(vo);
        notifyListener(this);
        return DAResult.createOK("ok", "DAMarket.addOrders");
    }

    public DAResult removeOrder(DAOrder order) {
        orders.remove(order);
        notifyListener(this);
        return DAResult.createOK("ok", "DAMarket.removeOrder");
    }

    public DAResult removeOrders(DAVector<DAOrder> vo) {
        orders.removeAll(vo);
        notifyListener(this);
        return DAResult.createOK("ok", "DAMarket.removeOrders");
    }

    public DAVector<DAOrder> getBuying() {
        DAVector<DAOrder> vo = new DAVector<DAOrder>(DAOrder.class);
        for (DAOrder o : orders) {
            if (o.isBuyOrder()) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAVector<DAOrder> getSelling() {
        DAVector<DAOrder> vo = new DAVector<DAOrder>(DAOrder.class);
        for (DAOrder o : orders) {
            if (o.isSellOrder()) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAVector<DAOrder> getBuying(IDAWare w) {
        DAVector<DAOrder> vo = new DAVector<DAOrder>(DAOrder.class);
        for (DAOrder o : orders) {
            if (o.isBuyOrder() && o.containsWare(w)) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAVector<DAOrder> getSelling(IDAWare w) {
        DAVector<DAOrder> vo = new DAVector<DAOrder>(DAOrder.class);
        for (DAOrder o : orders) {
            if (o.isSellOrder() && o.containsWare(w)) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAVector<DAOrder> getBuying(DAWaresType wt) {
        DAVector<DAOrder> vo = new DAVector<DAOrder>(DAOrder.class);
        for (DAOrder o : orders) {
            if (o.isBuyOrder() && o.containsType(wt)) {
                vo.add(o);
            }
        }
        return vo;
    }

    public DAVector<DAOrder> getSelling(DAWaresType wt) {
        DAVector<DAOrder> vo = new DAVector<DAOrder>(DAOrder.class);
        for (DAOrder o : orders) {
            if (o.isSellOrder() && o.containsType(wt)) {
                vo.add(o);
            }
        }
        return vo;
    }

    private DAOrder findBuying(DAOrder order) {
        for (DAOrder o : orders) {
            if (o.isBuyOrder() && (o.compareTo(order) == 0)) {
                return o;
            }
        }
        return null;
    }

    private DAOrder findSelling(DAOrder order) {
        for (DAOrder o : orders) {
            if (o.isSellOrder() && (o.compareTo(order) == 0)) {
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
    public DAResult buy(DAbmStorage targetStorage, DAOrder so, DAValue<Pieces> amount, DAUniqueID scriptID) {
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
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            boolean scripted = false;
            for (StackTraceElement e : ste) {
                if (e.getClassName().contains("DAScriptCPU")) {
                    scripted = true;
                }
                if (scripted) {
                    Log.warn(DAbmHangar.class, "buy: DAClan.transaction() was invoced by " + ste[2].getClassName()
                            + " Line: " + ste[2].getLineNumber() + " which is forbidden!");
                    return DAResult.createWarning("Invocation allowed only by DAMarket!", "DAMarket.buy");
                }
            }
            try {
                if (ship.getModule(targetStorage.getItemID()) == null) {
                    return DAResult.createWarning("Target storage is not a local storage.", "DAMarket.buy");
                }
                if (amount.is(DAValue.Sign.NEGATIVE_OR_ZERO)) {
                    return DAResult.createWarning("No valid amount <" + amount.toString() + ">.", "DAMarket.buy");
                }
                if (amount.isGreaterThan(so.getAmount())) {
                    return DAResult.createWarning("Amount bigger than sell order amount.", "DAMarket.buy");
                }
                DAWareAmount wa = new DAWareAmount(so.getSellAmount().getWare(), amount);
                DAResult rf = targetStorage.canAddAmount(wa);
                if (!rf.isOK()) {
                    return DAResult.createFailed(rf.getMessage(), "DAMarket.buy");
                }
                DAClan buyer = targetStorage.getLeaser();
                if (buyer == null) {
                    return DAResult.createWarning("No valid buyer found in WorldNode.", "DAMarket.buy");
                }
                DAbmStorage sto = (DAbmStorage) ship.getModule(so.getStorageID());
                if (sto == null) {
                    return DAResult.createWarning("Order source storage not found in ship.", "DAMarket.buy");
                }
                DAClan owner = sto.getLeaser();
                if (owner == null) {
                    return DAResult.createWarning("No valid order owner found in WorldNode.", "DAMarket.buy");
                }
                DAOrder order = findSelling(so);
                if (order == null) {
                    return DAResult.createFailed("Could not find requested ware in selling list.", "DAMarket.buy");
                }
                DAValue<Money> price = so.getPrice().scale(amount);
                DAMarketTransaction smt = DAMarketTransaction.createBill(owner, buyer, so.toString(), price);
                DAMarketTransaction bmt = DAMarketTransaction.createReceipt(owner, buyer, so.toString(), price);
                // Wares are not stored in an actual storage any more!
                // Instead the order holds the wares.
//                if (! sto.remove(so.getSellAmount())) {
//                    return DAResult.createFailed("Could not take requested ware from storage.", "DAMarket.buy");
//                }
                DAResult r = owner.transaction(smt);
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
                DAResult<DAWareAmount> r2 = targetStorage.addAmount(wa);
                if (! r2.isOK()) {
                    order.remove(amount.sub(r2.getResult().getAmount()));
                    Log.warn(DAbmHangar.class, "buy: Can not add wares to storage: " + so.getSellAmount());
                    return r2;
                }
                // TODO
//                buyer.addProperty(r2.getResult());
//                owner.removeProperty(amount());
                order.remove(amount);
                if ((order.getAmount().is(DAValue.Sign.NEGATIVE_OR_ZERO)) && (! order.isRefilling())) {
                    orders.remove(order);
                }
                notifyListener(this);
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
    public DAResult sell(DAbmStorage sourceStorage, DAOrder bo, DAVector<DAbmWaresContainer> contis
            , DAUniqueID scriptID) {
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
                    Log.warn(DAbmHangar.class, "sell: DAClan.transaction() was invoced by " + ste[2].getClassName()
                            + " Line: " + ste[2].getLineNumber() + " which is forbidden!");
                    return DAResult.createWarning("Invocation allowed only by DAMarket!", "DAMarket.sell");
                }
            }
            try {
                if (ship.getModule(sourceStorage.getItemID()) == null) {
                    return DAResult.createWarning("Source storage is not a local storage.", "DAMarket.sell");
                }
                DAbmStorage sto = (DAbmStorage) ship.getModule(bo.getStorageID());
                if (sto == null) {
                    return DAResult.createWarning("Order storage not found in ship.", "DAMarket.sell");
                }
                if (contis.size() <= 0) {
                    return DAResult.createWarning("No source containers.", "DAMarket.sell");
                }
                DAValue<Pieces> x = new DAValue<Pieces>(0, NewUnits.PIECES);
                for (DAbmWaresContainer wc : contis) {
                    if (! wc.getContentType().getItemID().equals(bo.getWaresClassID())) {
                        return DAResult.createFailed("Container content does not match order.", "DAMarket.sell");
                    }
                    x = x.add(wc.getActualAmount());
                }
                if (x.is(DAValue.Sign.ZERO)) {
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
                DAClan seller = sourceStorage.getLeaser();
                if (seller == null) {
                    return DAResult.createWarning("No valid seller found in WorldNode.", "DAMarket.sell");
                }
                DAClan owner = sto.getLeaser();
                if (owner == null) {
                    return DAResult.createWarning("No valid order owner found in WorldNode.", "DAMarket.sell");
                }
                DAOrder w = findBuying(bo);
                if (w == null) {
                    return DAResult.createFailed("Could not find requested ware in buying list.", "DAMarket.sell");
                }
                DAValue<Money> price = bo.getPrice().scale(x);
                DAMarketTransaction smt = DAMarketTransaction.createBill(seller, owner, bo.toString(), price);
                DAMarketTransaction bmt = DAMarketTransaction.createReceipt(seller, owner, bo.toString(), price);
                DAResult<DAVector<DAbmWaresContainer>> rr = sourceStorage.remove(contis);
                if (! rr.isOK()) {
                    // try partial rollback for partial remove
                    sourceStorage.add(rr.getResult());
                    return DAResult.createFailed("Could not take requested ware from storage.", "DAMarket.sell");
                }
                DAResult r = owner.transaction(bmt);
                if (! r.isOK()) {
                    DAResult<DAVector<DAbmWaresContainer>> r2 = sourceStorage.add(contis);
                    if (! r2.isOK()) {
                        // try complete rollback
                        sourceStorage.add(contis);
                        Log.warn(DAbmHangar.class, "sell: Could not add ware to storage: " + contis.toParseString(""));
                    }
                    return r;
                }
                r = seller.transaction(smt);
                if (! r.isOK()) {
                    // try complete rollback
                    DAResult r2 = sourceStorage.add(contis);
                    if (! r2.isOK()) {
                        // it's dead jim...
                        Log.warn(DAbmHangar.class, "sell: Container lost: " + contis.toParseString(""));
                    }
                    owner.undoTransaction(bmt);
                    return r;
                }
                DAResult<DAVector<DAbmWaresContainer>> r2 = sto.add(contis);
                if (! r2.isOK()) {
                    // try partial rollback after partial add
                    sourceStorage.add(r2.getResult());
                    Log.warn(DAbmHangar.class, "sell: Could not add all wares to storage: " + contis.toParseString(""));
                    return r2;
                }
                // TODO
//                owner.addProperty(r2.getResult());
//                seller.removeProperty(contis);
                if ((bo.getAmount().isGreaterThan(x)) || (bo.isRefilling())) {
                    findBuying(bo).remove(x);
                } else {
                    orders.remove(w);
                }
                sto.compact();
                notifyListener(this);
                return DAResult.createOK("You sold " + x + " of " + bo.getWaresString() + ".", "DAMarket.sell");
            } catch (Exception e) {
                return DAResult.createFailed(e.getMessage(), "DAMarket.sell");
            }
        }
    }

//    //TODO: return the real warestree
//    public DAWaresTree getTree() {
//        DAWaresTree t = new DAWaresTree("");
//        return t;
//    }

//    @Override
//    public DVCBasicDataModel clone() {
//        DVCErrorHandler.raiseError(DAResult.createWarning("Not supported yet.", "DAMarket.parse"));
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public DVCseMarket getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseMarket de = new DVCseMarket(this, editmode, user);
//        addListener(de);
//        return de;
//    }
//
//    @Override
//    public DVCrsmMarket getRemoteDataModel(DAOwningThread thread, IDVCActionDispatcher dispatcher) {
//        DVCrsmMarket rc = new DVCrsmMarket(thread, this);
//        rc.setActionDispatcher(dispatcher);
//        addRemoteListener(rc);
//        return rc;
//    }

//    @Override
//    public DATypedResult<DAParameterList> handle(DAOwningThread thread, DARemoteAction action, boolean sourceIsServer) {
//        if (action.getSender() != DARemoteAction.RemoteObject.MARKET) {
//            return new DATypedResult<DAParameterList>("Unknown action sender <" + action + ">!",
//                    DAResult.ResultType.WARNING, null, "DVCosmClan.handle");
//        }
//        Action type = Action.valueOf(action.getName().toString());
//        if (type == null) {
//            return new DATypedResult<DAParameterList>("Unknown action type <" + action.getName() + ">!",
//                    DAResult.ResultType.WARNING, null, "DVCosmClan.handle");
//        }
//        DAParameterList pl = new DAParameterList();
//        DAParameterList parms = action.getParameters();
//        DAUniqueID id = (DAUniqueID) parms.get(Parameters.SHIPID);
//        if (!id.equals(this.getParentContainer().getRootContainer().getItemID())) {
//            return new DATypedResult<DAParameterList>("Wrong channeled action <" + action + ">!",
//                    DAResult.ResultType.WARNING, null, "DVCosmClan.handle");
//        }
//        switch (type) {
//            case ADD_ORDERS: {
//            @SuppressWarnings("unchecked")
//                DAVector<DAOrder> v = (DAVector<DAOrder>) parms.get(Parameters.ORDERS);
//                DAResult r = addOrders(v);
//                return new DATypedResult<DAParameterList>(r.getMessage(),
//                        r.getResultType(), pl, "DVCosmClan.handle");
//            }
//            case REMOVE_ORDERS: {
//            @SuppressWarnings("unchecked")
//                DAVector<DAOrder> v = (DAVector<DAOrder>) parms.get(Parameters.ORDERS);
//                DAResult r = removeOrders(v);
//                return new DATypedResult<DAParameterList>(r.getMessage(),
//                        r.getResultType(), pl, "DVCosmClan.handle");
//            }
//            case BUY: {
//                DAUniqueID stoID = (DAUniqueID) parms.get(Parameters.STORAGEID);
//                DAbmStorage sto = (DAbmStorage) getParentContainer().getModule(stoID);
//                DAOrder so = (DAOrder) parms.get(Parameters.ORDER);
//                DAAmount amount = (DAAmount) parms.get(Parameters.AMOUNT);
//                DAUniqueID sid = (DAUniqueID) parms.get(Parameters.SCRIPT_ID);
//                if (! sid.isValid()) { // special case because we can not transfer null values
//                    sid = null;
//                }
//                DAResult r = buy(sto, so, amount, sid);
//                return new DATypedResult<DAParameterList>(r.getMessage(),
//                        r.getResultType(), pl, "DVCosmClan.handle");
//            }
//            case SELL: {
//                DAUniqueID stoID = (DAUniqueID) parms.get(Parameters.STORAGEID);
//                DAbmStorage sto = (DAbmStorage) getParentContainer().getModule(stoID);
//                DAOrder bo = (DAOrder) parms.get(Parameters.ORDER);
//            @SuppressWarnings("unchecked")
//                DAVector<DAbmWaresContainer> cont = (DAVector<DAbmWaresContainer>) parms.get(Parameters.CONTAINERLIST);
//                DAUniqueID sid = (DAUniqueID) parms.get(Parameters.SCRIPT_ID);
//                if (! sid.isValid()) { // special case because we can not transfer null values
//                    sid = null;
//                }
//                DAResult r = sell(sto, bo, cont, sid);
//                return new DATypedResult<DAParameterList>(r.getMessage(),
//                        r.getResultType(), pl, "DVCosmClan.handle");
//            }
//            case GET_BUYING: {
//                DAVector<DAOrder> v = getBuying();
//                pl.add(Parameters.ORDERS, v);
//                break;
//            }
//            case GET_SELLING: {
//                DAVector<DAOrder> v = getSelling();
//                pl.add(Parameters.ORDERS, v);
//                break;
//            }
//            case GET_BUYING_W: {
//                DAWare w = (DAWare) parms.get(Parameters.WARE);
//                DAVector<DAOrder> v = getBuying(w);
//                pl.add(Parameters.ORDERS, v);
//                break;
//            }
//            case GET_SELLING_W: {
//                DAWare w = (DAWare) parms.get(Parameters.WARE);
//                DAVector<DAOrder> v = getSelling(w);
//                pl.add(Parameters.ORDERS, v);
//                break;
//            }
//            case GET_BUYING_WT: {
//                DAWaresType wt = (DAWaresType) parms.get(Parameters.WARESTYPE);
//                DAVector<DAOrder> v = getBuying(wt);
//                pl.add(Parameters.ORDERS, v);
//                break;
//            }
//            case GET_SELLING_WT: {
//                DAWaresType wt = (DAWaresType) parms.get(Parameters.WARESTYPE);
//                DAVector<DAOrder> v = getSelling(wt);
//                pl.add(Parameters.ORDERS, v);
//                break;
//            }
//            case SPLIT: {
//                DAUniqueID stoID = (DAUniqueID) parms.get(Parameters.STORAGEID);
//                DAbmStorage sto = (DAbmStorage) getParentContainer().getModule(stoID);
//                DAbmWaresContainer wc = (DAbmWaresContainer) parms.get(Parameters.CONTAINER);
//                DAAmount a = (DAAmount) parms.get(Parameters.AMOUNT);
//                DATypedResult<DAbmWaresContainer> r = split(sto, wc, a);
//                pl.add(Parameters.CONTAINER, r.getResult());
//                return new DATypedResult<DAParameterList>(r.getMessage(),
//                        r.getResultType(), pl, "DVCosmClan.handle");
//                //break;
//            }
//
//            default: {
//                return new DATypedResult<DAParameterList>("Unknown action type <" + action.getName() + ">!",
//                    DAResult.ResultType.WARNING, null, "DAMarket.handle");
//            }
//        }
//        return new DATypedResult<DAParameterList>("ok",
//                DAResult.ResultType.OK, pl, "DVCosmClan.handle");
//    }
//
//    @Override
//    public void addRemoteListener(IDVCRemoteDataModel rl) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("remoteListener not initialized!", "DAMarket.addRemoteListener"));
//            return;
//        }
//        remoteListener.add(rl);
//    }
//
//    @Override
//    public void removeRemoteListener(IDVCRemoteDataModel rl) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("remoteListener not initialized!", "DAMarket.removeRemoteListener"));
//            return;
//        }
//        remoteListener.remove(rl);
//    }
//
//    @Override
//    public void notifyRemoteListener(DAOwningThread newOwner) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("remoteListener not initialized!", "DAMarket.notifyRemoteListener"));
//            return;
//        }
//        for (IDVCRemoteDataModel rd : remoteListener) {
//            rd.ownerChanged(newOwner);
//        }
//    }
//
//    @Override
//    public void notifyRemoteListener(DVCBasicDataModel newData) {
//        if (remoteListener == null) {
//            DVCErrorHandler.raiseError(DAResult.createWarning("remoteListener not initialized!", "DAMarket.notifyRemoteListener"));
//            return;
//        }
//        for (IDVCRemoteDataModel rd : remoteListener) {
//            rd.dataChanged(newData);
//        }
//    }

    public DAResult<DAbmWaresContainer> split(DAbmStorage sto, DAbmWaresContainer wc, DAValue<Pieces> a) {
        return sto.split(wc, a);
    }

}
