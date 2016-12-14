package de.hdc.commonlibrary.market;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAArray;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DAVector;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.module.DAStorage;
import de.hdc.commonlibrary.module.DAWaresContainer;
import de.hdc.commonlibrary.protocol.DAParameterList;
import de.hdc.commonlibrary.protocol.DARemoteAction;
import de.hdc.commonlibrary.protocol.IActionHandler;
import de.hdc.commonlibrary.protocol.IParameterType;
import de.hdc.commonlibrary.protocol.IRemoteActionType;

/**
 * Created by DerTroglodyt on 2016-12-13 18:58.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class MarketHandler implements IActionHandler {

    public MarketHandler(DAMarket market, OrganisationMap userMap) {
        this.market = market;
        this.userMap = userMap;
    }

    /**
     * Valid parameters in a remote action.
     */
    public enum Parms implements IParameterType {

        NONE(null),
        SHIPID(DAUniqueID.class),
        ORDERS(DAVector.class),
        WARE(DAWare.class),
        WARESTYPE(DAWareTypeTreeNode.class),
        STORAGEID(DAUniqueID.class),
        ORDER(DAOrder.class),
        AMOUNT(DAValue.class),
        CONTAINER(DAWaresContainer.class),
        CONTAINERLIST(DAVector.class),
        SCRIPT_ID(DAUniqueID.class),
        ;

        private DAText name;
        private Class<? extends IDataAtom> c;

        Parms(Class<? extends IDataAtom> aClass) {
            name = DAText.create(this.toString());
            c = aClass;
        }

        @Override
        public DAText getName() {
            return name;
        }

        @Override
        public Class<? extends IDataAtom> getType() {
            return c;
        }

    }

    /**
     * Valid remote actions and their parameters.
     */
    public enum Action implements IRemoteActionType {
        ADD_ORDERS(Parms.NONE, Parms.SHIPID, Parms.ORDERS),
        REMOVE_ORDERS(Parms.NONE, Parms.SHIPID, Parms.ORDERS),
        GET_BUYING(Parms.ORDERS, Parms.SHIPID),
        GET_SELLING(Parms.ORDERS, Parms.SHIPID),
        GET_BUYING_W(Parms.ORDERS, Parms.SHIPID, Parms.WARE),
        GET_SELLING_W(Parms.ORDERS, Parms.SHIPID, Parms.WARE),
        GET_BUYING_WT(Parms.ORDERS, Parms.SHIPID, Parms.WARESTYPE),
        GET_SELLING_WT(Parms.ORDERS, Parms.SHIPID, Parms.WARESTYPE),
        BUY(Parms.NONE, Parms.SHIPID, Parms.STORAGEID, Parms.ORDER, Parms.AMOUNT, Parms.SCRIPT_ID),
        SELL(Parms.NONE, Parms.SHIPID, Parms.STORAGEID, Parms.ORDER, Parms.CONTAINERLIST, Parms.SCRIPT_ID),
        SPLIT(Parms.CONTAINER, Parms.SHIPID, Parms.STORAGEID, Parms.CONTAINER, Parms.AMOUNT),;

        @Override
        public DAText getName() {
            return name;
        }

        @Override
        public List<IParameterType> getInput() {
            return input;
        }

        @Override
        public IParameterType getResultType() {
            return result;
        }

        private final DAText name;
        private final List<IParameterType> input;
        private final IParameterType result;

        Action(IParameterType r, IParameterType... in) {
            name = DAText.create(this.toString());
            input = Collections.unmodifiableList(Arrays.asList(in));
            result = r;
        }



    }

    /**
     * Handles an action and gives a result message back to sender.
     * @param action The action that should be executed by this handler.
     * @return The result as a message returned to the sender.
     */
    @Override
    public DARemoteAction handle(DARemoteAction action) {
        if (! action.parm.validate(Action.valueOf(action.actionName.toString()))) {
            return DARemoteAction.create(action, DAResult.createFailed("Action is invalid!" + action.toString(), "MarketHandler.handle"));
        }
        if (action.destinationType != DARemoteAction.Type.MARKET) {
            return DARemoteAction.create(action, DAResult.createFailed("Action dest. type is not MARKET!" + action.toString(), "MarketHandler.handle"));
        }
        if (action.destinationID != market.id) {
            return DARemoteAction.create(action, DAResult.createFailed("Action dest. is not this market!" + action.toString(), "MarketHandler.handle"));
        }
        final DAParameterList pl = DAParameterList.create();
        switch (Action.valueOf(action.actionName.toString())) {
            case ADD_ORDERS: {
                DAArray<DAOrder> v = (DAArray<DAOrder>) action.parm.get(Parms.ORDERS);
                market.addOrders(v);
                return DARemoteAction.create(action, DAResult.createOK("Order(s) added.", "MarketHandler.handle", pl));
            }
            case REMOVE_ORDERS: {
            @SuppressWarnings("unchecked")
            DAArray<DAOrder> v = (DAArray<DAOrder>) action.parm.get(Parms.ORDERS);
                market.removeOrders(v);
                return DARemoteAction.create(action, DAResult.createOK("Removed order(s).", "MarketHandler.handle", pl));
            }
            case BUY: {
                DAUniqueID stoID = (DAUniqueID) action.parm.get(Parms.STORAGEID);
                DAStorage sto = (DAStorage) market.getStorage(stoID);
                DAOrder so = (DAOrder) action.parm.get(Parms.ORDER);
                DAValue<Pieces> amount = (DAValue<Pieces>) action.parm.get(Parms.AMOUNT);
                DAUniqueID sid = (DAUniqueID) action.parm.get(Parms.SCRIPT_ID);
                if (! sid.isValid()) { // special case because we can not transfer null values
                    sid = null;
                }
                DAResult r = market.buy(sto, so, amount, sid, userMap);
                return DARemoteAction.create(action, new DAResult<DAParameterList>(r.getMessage(),
                        r.getResultType(), pl, "DVCosmClan.handle"));
            }
            case SELL: {
                DAUniqueID stoID = (DAUniqueID) action.parm.get(Parms.STORAGEID);
                DAStorage sto = (DAStorage) market.getStorage(stoID);
                DAOrder bo = (DAOrder) action.parm.get(Parms.ORDER);
            @SuppressWarnings("unchecked")
                DAArray<DAWaresContainer> cont = (DAArray<DAWaresContainer>) action.parm.get(Parms.CONTAINERLIST);
                DAUniqueID sid = (DAUniqueID) action.parm.get(Parms.SCRIPT_ID);
                if (! sid.isValid()) { // special case because we can not transfer null values
                    sid = null;
                }
                DAResult r = market.sell(sto, bo, cont, sid, userMap);
                return DARemoteAction.create(action, new DAResult<DAParameterList>(r.getMessage(),
                        r.getResultType(), pl, "DVCosmClan.handle"));
            }
            case GET_BUYING: {
                DAArray<DAOrder> v = market.getBuying();
                pl.add(Parms.ORDERS, v);
                break;
            }
            case GET_SELLING: {
                DAArray<DAOrder> v = market.getSelling();
                pl.add(Parms.ORDERS, v);
                break;
            }
            case GET_BUYING_W: {
                DAWare w = (DAWare) action.parm.get(Parms.WARE);
                DAArray<DAOrder> v = market.getBuying(w);
                pl.add(Parms.ORDERS, v);
                break;
            }
            case GET_SELLING_W: {
                DAWare w = (DAWare) action.parm.get(Parms.WARE);
                DAArray<DAOrder> v = market.getSelling(w);
                pl.add(Parms.ORDERS, v);
                break;
            }
//            case GET_BUYING_WT: {
//                DAWaresType wt = (DAWaresType) action.parm.get(Parms.WARESTYPE);
//                DAArray<DAOrder> v = market.getBuying(wt);
//                pl.add(Parms.ORDERS, v);
//                break;
//            }
//            case GET_SELLING_WT: {
//                DAWaresType wt = (DAWaresType) action.parm.get(Parms.WARESTYPE);
//                DAArray<DAOrder> v = market.getSelling(wt);
//                pl.add(Parms.ORDERS, v);
//                break;
//            }
////            case SPLIT: {
////                DAUniqueID stoID = (DAUniqueID) action.parm.get(Parameters.STORAGEID);
////                DAbmStorage sto = (DAbmStorage) getParentContainer().getModule(stoID);
////                DAbmWaresContainer wc = (DAbmWaresContainer) action.parm.get(Parameters.CONTAINER);
////                DAAmount a = (DAAmount) action.parm.get(Parameters.AMOUNT);
////                DATypedResult<DAbmWaresContainer> r = split(sto, wc, a);
////                pl.add(Parameters.CONTAINER, r.getResult());
////                return new DATypedResult<DAParameterList>(r.getMessage(),
////                        r.getResultType(), pl, "DVCosmClan.handle");
////                //break;
////            }
            default: {
                return DARemoteAction.create(action, DAResult.createFailed("Unknown action <" + action.toString() + ">!"
                        , "MarketHandler.handle"));
            }
        }
        return DARemoteAction.create(action, DAResult.createOK("Handled action " + action.toString()
                , "MarketHandler.handle", pl));
    }

    private final DAMarket market;
    private final OrganisationMap userMap;

}
