package de.hdc.commonlibrary.market;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DAVector;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.module.DAWaresContainer;
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

    public MarketHandler(DAMarket market) {
        this.market = market;
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
            return DARemoteAction.create(action, DAResult.createFailed("MarketHandler.handle", "Action is invalid!" + action.toString()));
        }
        if (action.destinationType != DARemoteAction.Type.MARKET) {
            return DARemoteAction.create(action, DAResult.createFailed("MarketHandler.handle", "Action dest. type is not MARKET!" + action.toString()));
        }
        if (action.destinationID != market.id) {
            return DARemoteAction.create(action, DAResult.createFailed("MarketHandler.handle", "Action dest. is not this market!" + action.toString()));
        }
        switch (Action.valueOf(action.actionName.toString())) {
            case ADD_ORDERS: {
                return DARemoteAction.create(action, DAResult.createFailed("MarketHandler.handle", "Not implemented yet!" + action.toString()));
            }
        }
        return DARemoteAction.create(action, DAResult.createFailed("MarketHandler.handle", "Unknown error!" + action.toString()));
    }

    private final DAMarket market;

}
