/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.util.Log;

/**
 * <DVCsmWareClassID, DADoubleFloat>
 * Volume flow of needed goods in [<UNIT>/s].
 * For Example: Air condition needed/produced.
 * For Example: Fuel needed/produced.
 * Positive values mean good is produced.
 *
 * @author martin
 */
public class DAGoodFlowList extends DataAtom {

    public static DAGoodFlowList create() {
        return new DAGoodFlowList(DAFlowMap.create());
    }

//    public DAGoodFlowList(DAGoodFlowList other) {
//        super();
//        goodFlow = new DAFlowMap(DAGoodFlow.class);
//        add(other);
//    }

    @Deprecated
    public DAGoodFlowList() {
        goodFlow = null;
    }

    public final void add(DAGoodFlowList flowList) {
        for (DAUniqueID wc : flowList.getKeySet()) {
            DAGoodFlow gfNew = flowList.get(wc);
            DAGoodFlow gf = goodFlow.get(wc);
            if (gf != null) {
                if (! gfNew.flow.getUnit().isCompatible(gf.flow.getUnit())) {
                    String s = "Unit <" + gf.flow.getUnit() + "> and <" + gfNew.flow.getUnit() + "> of "
                            + wc + " are incompatible!";
                    Log.fatal(DAGoodFlowList.class, s);
                    throw new IllegalArgumentException(s);
                }
                final DAValue v1 = gf.flow;
                final DAValue v2 = gfNew.flow;
                goodFlow.set(wc, DAGoodFlow.create(wc, v1.add(v2)));
            } else {
                goodFlow.set(wc, gfNew);
            }
        }
    }

//    public void subtract(DAGoodFlowList flowList) {
//        for (DAUniqueID wc : flowList.getKeySet()) {
//            DAGoodFlow gfNew = flowList.get(wc);
//            DAGoodFlow gf = goodFlow.getValue(wc);
//            if (gf != null) {
//                try {
//                    gf.getFlow().subtract(gfNew.getFlow());
//                } catch (DVCUnitMismatchException ex) {
//                    DVCErrorHandler.raiseError(DAResult.createWarning(ex.toString(), "DAGoodFlowList.add"));
//                } catch (DVCPrecisionMismatchException ex) {
//                    DVCErrorHandler.raiseError(DAResult.createWarning(ex.toString(), "DAGoodFlowList.add"));
//                }
//            } else {
//                goodFlow.addProperty(wc, gfNew.clone());
//            }
//        }
//        notifyListener(this);
//    }

//    public void set(DAValue<?> flow) {
//        try {
//            if (flow == null) {
//                DVCErrorHandler.raiseError(DAResult.createSerious("Flow is NULL!", "DVCsmGoodFlowList.set"));
//                return;
//            }
//            goodFlow.addProperty(flow.getClassID(), flow);
//            notifyListener(this);
//        } catch (Throwable t) {
//            Log.throwable(DAGoodFlowList.class, t, "set");
//        }
//    }

    public void set(DAWareClass wareClass, DAGoodFlow flow) {
        goodFlow.set(wareClass.id, flow);
    }

    public void removeAll() {
        goodFlow.removeAll();
    }

    public void remove(DAWareClass wareClass) {
        if (wareClass == null) {
            return;
        }
        goodFlow.remove(wareClass.id);
    }

    public void remove(DAUniqueID wareClassID) {
        if (wareClassID == null) {
            return;
        }
        goodFlow.remove(wareClassID);
    }

    public DAGoodFlow get(DAWareClass wareClass) {
        if (wareClass == null) {
            return null;
        }
        return goodFlow.get(wareClass.id);
    }

    public DAGoodFlow get(DAUniqueID wareClassID) {
        return goodFlow.get(wareClassID);
    }

    public Set<DAUniqueID> getKeySet() {
        return goodFlow.getKeySet();
    }

    public Collection<DAGoodFlow> getValueList() {
        return goodFlow.getValueList();
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        goodFlow.toStream(stream);
    }

    @Override
    public DAGoodFlowList fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAFlowMap goodFlow = new DAFlowMap().fromStream(stream);
        return new DAGoodFlowList(goodFlow);
    }

    private static final byte VERSION = 1;

    private final DAFlowMap goodFlow;

    private DAGoodFlowList(DAFlowMap goodFlow) {
        super();
        this.goodFlow = goodFlow;
    }

}
