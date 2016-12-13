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

    public static enum Parameters implements IParameterType {

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

        Parameters(Class<? extends IDataAtom> aClass) {
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
        SPLIT(Parameters.CONTAINER, Parameters.SHIPID, Parameters.STORAGEID, Parameters.CONTAINER, Parameters.AMOUNT),;

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
//            input = new ArrayList<IParameterType>(in.length);
//            input.addAll(Arrays.asList(in));
            input = Collections.unmodifiableList(Arrays.asList(in));
            result = r;
        }



    }

    @Override
    public DAResult<?> handle(DARemoteAction action) {
        action.parm.validate(Action.valueOf(action.actionName.toString()));
        return DAResult.createFailed("MarketHandler.handle", "Unknown error");
    }

    private final DAMarket market;

}
