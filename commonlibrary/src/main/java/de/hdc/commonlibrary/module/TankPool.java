/*
 *  Created by DerTroglodyt on 2016-11-10 09:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.util.Log;

/**
 * @author martin
 */
public class TankPool implements IDataAtom {

    public TankPool() {
        table = new HashMap<DAUniqueID, ArrayList<DAAbstractTank>>(0);
    }

    public void add(DAAbstractTank t, DAUniqueID typeID) {
        ArrayList<DAAbstractTank> v = table.get(typeID);
        if (v == null) {
            v = new ArrayList<DAAbstractTank>(1);
            table.put(typeID, v);
        }
        v.add(t);
    }

    public void remove(DAAbstractTank t, DAUniqueID typeID) {
        ArrayList<DAAbstractTank> v = table.get(typeID);
        if (v == null) {
            Log.warn(TankPool.class, "No such tank! <" + t + ">", "DVCWaresPool.remove");
        } else {
            v.remove(t);
        }
    }

    public DAResult<DAGoodFlow> manage(DAGoodFlow gf) {
        if (gf.flow.doubleValueBase() >= 0) {
            return put(gf);
        } else {
            return take(gf);
        }
    }

    /**
     * Stores the amount of ware in the pool if possible.
     * If there are tanks for this ware type each of them is asked to store the
     * amount of ware until the remaining amount reaches 0 or no more tanks are available.
     * Returns OK if ALL amount could be stored.
     * Returns FAILED and the remaining amount if only some or none could be stored.
     * Returns a WARNING and the input amount if there are no tanks for this type of ware.
     *
     * @param gf
     * @return The remaining amount.
     */
    public DAResult<DAGoodFlow> put(DAGoodFlow gf) {
        if (gf.flow.doubleValueBase() < 0) {
            return DAResult.createWarning("Can not store negative amount of ware." + gf, "DVCWaresPool.put");
        }
        ArrayList<DAAbstractTank> v = table.get(gf.wareClassID);
        if (v == null) {
            return DAResult.createWarning("No tank for ware <" + gf + ">.", "DVCWaresPool.put");
        }
        DAGoodFlow wa = gf;
        for (DAAbstractTank t : v) {
            DAResult<DAGoodFlow> r = t.change(wa);
            if (!r.isFailed()) {
                return r;
            }
            wa = r.getResult();
        }
        return new DAResult<DAGoodFlow>("Not enough space in tanks for  <" + gf + ">. Some amount could not be stored."
                , DAResult.ResultType.FAILED, wa, "DVCWaresPool");
    }

    /**
     * Takes the amount of ware from the pool if possible.
     * If there are tanks for this ware type each of them is asked to deliver the
     * amount of ware until the taken amount reaches the amount asked for or no more tanks are available.
     * Returns OK if ALL amount could be taken.
     * Returns FAILED and the already delivered amount if only some or none could be taken.
     * Returns a WARNING and the a 0 amount if there are no tanks for this type of ware.
     *
     * @param gf
     * @return The delivered amount.
     */
    public DAResult<DAGoodFlow> take(DAGoodFlow gf) {
        if (gf.flow.doubleValueBase() >= 0) {
            return DAResult.createWarning("Can not take positive amount of ware." + gf, "DVCWaresPool.take");
        }
        ArrayList<DAAbstractTank> v = table.get(gf.wareClassID);
        if (v == null) {
            return DAResult.createWarning("No tank for ware <" + gf + ">.", "DVCWaresPool.take");
        }
        DAGoodFlow wa = gf;
        for (DAAbstractTank t : v) {
            DAResult<DAGoodFlow> r = t.change(wa);
            if (!r.isFailed()) {
                return r;
            }
            wa = r.getResult();
        }
        return new DAResult<DAGoodFlow>("Not enough ware in tanks for  <" + gf + ">. Some amount could not be delivered."
                , DAResult.ResultType.FAILED, wa, "DVCWaresPool");
    }

    /**
     * Table of vectors which hold tank modules of a specific ware type.
     */
    private HashMap<DAUniqueID, ArrayList<DAAbstractTank>> table;

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        stream.writeInt(table.size());
        for (DAUniqueID id : table.keySet()) {
            id.toStream(stream);
            stream.writeInt(table.get(id).size());
            for (DAAbstractTank t : table.get(id)) {
                stream.writeUTF(t.getClass().toString());
                t.toStream(stream);
            }
        }
    }

    @Override
    public TankPool fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        int x = stream.readInt();
        HashMap<DAUniqueID, ArrayList<DAAbstractTank>> atable = new HashMap<>(x);
        for (int i = 0; i < x; i++) {
            DAUniqueID aid = new DAUniqueID().fromStream(stream);
            int y = stream.readInt();
            ArrayList<DAAbstractTank> al = new ArrayList<>(y);
            for (int k = 0; k < y; k++) {
                try {
                    final Class<?> c = Class.forName(stream.readUTF());
                    // todo: what happens if the tank saves own variables in addition to AbstractTanks???
                    final DAAbstractTank t = (DAAbstractTank) ((DAAbstractTank) c.newInstance()).fromStream(stream);
                    al.add(t);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            atable.put(aid, al);
        }
        return new TankPool();
    }

    private static final byte VERSION = 1;
}
