/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAGoodFlow extends DataAtom {

    /**
     * <DAWareClassID, DAValue>
     * Volume flow of needed goods in [<UNIT>/s].
     * For Example: Air condition needed/produced.
     * For Example: Fuel needed/produced.
     * Positive values mean good is produced.
     */
    public final DAValue<?> flow;
    public final DAUniqueID wareClassID;

    @Deprecated
    public DAGoodFlow() {
        flow = null;
        wareClassID = null;
    }

    public static DAGoodFlow create(DAUniqueID aWareClassID, DAValue<?> aFlow) {
        return new DAGoodFlow(aFlow, aWareClassID);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + flow;
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        flow.toStream(stream);
        wareClassID.toStream(stream);
    }

    @Override
    public DAGoodFlow fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DAValue<?> flow = new DAValue().fromStream(stream);
        final DAUniqueID wareClassID = new DAUniqueID().fromStream(stream);
        return new DAGoodFlow(flow, wareClassID);
    }

    private static final byte VERSION = 1;

    private DAGoodFlow(DAValue<?> aFlow, DAUniqueID aWareClassID) {
        super();
        flow = aFlow;
        wareClassID = aWareClassID;
    }

}
