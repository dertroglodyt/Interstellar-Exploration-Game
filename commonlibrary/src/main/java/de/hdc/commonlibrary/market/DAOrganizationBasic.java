/*
 *  Created by DerTroglodyt on 2016-11-27 01:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import org.jscience.economics.money.Money;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAArray;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.util.Log;

public class DAOrganizationBasic extends DataAtom {

    public final DAUniqueID id;
    public final DAText name;
    private DAValue<Money> wallet;
    private final DAArray<DAMarketTransaction> transactionLog;

    public static DAOrganizationBasic create(DAUniqueID id, DAText name) {
        return new DAOrganizationBasic(id, name);
    }

    @Override
    public String toString() {
        return "DAOrganizationBasic{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    public DAResult<DAWare> transaction(DAMarketTransaction mt) {
        synchronized(wallet) {
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            if ((! ste[2].getClassName().contentEquals("datavault.common.space.model.market.DAMarket"))
                    && (! ste[2].getClassName().contentEquals("datavault.common.space.model.DAClan"))
                    && (! ste[2].getClassName().contentEquals("datavault.common.space.editor.DVCseClan"))
                    && (! ste[2].getClassName().contentEquals("datavault.common.space.model.market.module.DABMRentableStorage"))
                    && (! ste[2].getClassName().contentEquals("datavault.common.space.model.market.module.DABMHangar"))
                    ) {
                Log.warn(DAOrganizationBasic.class, "transaction: DAClan.transaction() was invoked by "
                        + ste[2].getClassName() + " Line: " + ste[2].getLineNumber() + " which is forbidden!");
                return DAResult.createWarning("Invocation allowed only by DAMarket!", "DAClan.transaction");
            }
            if ((mt.sourceID.compareTo(id) != 0) && (mt.targetID.compareTo(id) != 0)) {
                return DAResult.createFailed("We are neither source nor target of this transaction...!?", "DAClan.transaction");
            }
            try {
                DAResult<DAWare> r;
                switch (mt.type) {
                    case TAX_FROM:
                    case DONATION_FROM:
                    case SOLD_TO: {
                        // we get money. add price to wallet.
                        wallet = wallet.add(mt.price);
                        r = DAResult.createOK("", "DAClan.transaction");
                        break;
                    }
                    case TAX_TO:
                    case BOUGHT_FROM:
                    case DONATION_TO: {
                        // we loose money. deduct price from wallet.
                        if (wallet.isSmallerThan(mt.price)) {
                            return DAResult.createFailed("Not enough money! Needed: " + mt.price
                                    + " Available: " + wallet, "DAClan.transaction");
                        }
                        wallet = wallet.sub(mt.price);
                        r = DAResult.createOK("", "DAClan.transaction");
                        break;
                    }
                    case UNKNOWN:
                    default : r = DAResult.createFailed("", "DAClan.transaction");
                }
                transactionLog.add(mt);
                return r;
            } catch (Exception e) {
                return DAResult.createFailed(e.getMessage(), "DAClan.transaction");
            }
        }
    }

    public void undoTransaction(DAMarketTransaction mt) {
        synchronized(wallet) {
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            if ((! ste[2].getClassName().contentEquals("datavault.common.space.model.market.DAMarket"))
                    && (! ste[2].getClassName().contentEquals("datavault.common.space.model.DAClan"))
                    && (! ste[2].getClassName().contentEquals("datavault.common.space.editor.DVCseClan"))) {
                Log.warn(DAOrganizationBasic.class, "undoTransaction: DAClan.undoTransaction() was invoked by "
                        + ste[2].getClassName() + " Line: " + ste[2].getLineNumber() + " which is forbidden!");
            }
            if ((mt.sourceID.compareTo(id) != 0) && (mt.targetID.compareTo(id) != 0)) {
                Log.warn(DAOrganizationBasic.class, "undoTransaction: We are neither source nor target of this transaction...!?");
                return ;
            }
            switch (mt.type) {
                case TAX_FROM:
                case DONATION_FROM:
                case SOLD_TO: {
                    wallet = wallet.sub(mt.price);
//                        if (mt.getSourceID().compareTo(user.getUserID()) == 0) {
//                            wallet.subtract(mt.getPrice());
//                        } else {
//                            wallet.addTo(mt.getPrice());
//                        }
                    break;
                }
                case TAX_TO:
                case BOUGHT_FROM:
                case DONATION_TO: {
                    wallet = wallet.add(mt.price);
//                        if (mt.getSourceID().compareTo(user.getUserID()) == 0) {
//                            wallet.addTo(mt.getPrice());
//                        } else {
//                            wallet.subtract(mt.getPrice());
//                        }
                    break;
                }
                case UNKNOWN:
            }
            transactionLog.remove(mt);
        }
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        id.toStream(stream);
        name.toStream(stream);
    }

    @Override
    public DAOrganizationBasic fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAUniqueID id = new DAUniqueID().fromStream(stream);
        DAText name = new DAText().fromStream(stream);
        return new DAOrganizationBasic(id, name);
    }

    private static final byte VERSION = 1;

    private DAOrganizationBasic(DAUniqueID id, DAText name) {
        super();
        this.id = id;
        this.name = name;
        this.wallet = DAValue.<Money>create(0, NewUnits.CREDITS);
        this.transactionLog = DAArray.create();
    }

}
