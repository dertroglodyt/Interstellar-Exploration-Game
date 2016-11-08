/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.converter.ConversionException;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.util.Log;

/**
 * Instance of a DAWareClass.
 * Holds instance variables of a ware (i.e. a module) if needed.
 * All invariant properties of this ware are held in the wareClass.
 * @author martin
 */
//@SuppressWarnings("serial")
public abstract class DAWare extends DataAtom implements IDAWare {

    @Deprecated
    public DAWare() {
        classID = null;
        itemID = null;
        itemName = null;
        amount = null;
    }

//    public static DAWare create(DAWareClass wareClass) {
//        return new DAWare(wareClass.id, null, DAText.create(""));
//    }

//    public static DAWare createUnique(DAWareClass aWareClass, DAText name) {
//        return new DAWare(aWareClass.id, DAUniqueID.createRandom(), name);
//    }

    public void init(DAWareClassMap map) {
        wareClass = map.get(classID);
        if (wareClass == null) {
            throw new IllegalArgumentException("DAWare: Unknown DAWareClass!");
        }
    }

    @Override
    public String toString() {
        return (isUnique()?itemName + " (" : "") + classID + (isUnique()?" : )" + itemID : "");
    }

    @Override
    public DAUniqueID getClassID() {
        return classID;
    }

    @Override
    public DAUniqueID getItemID() {
        return itemID;
    }

    @Override
    public DAText getName() {
        return itemName;
    }

    @Override
    public abstract DAWare setName(DAText newName);

    @Override
    public boolean isUnique() {
        return (itemID != null);
    }

    @Override
    public abstract DAWare makeUnique(DAText name);
//    {
//        if (isUnique()) {
//            Log.warn(DAWareClass.class, "makeUnique: Ware is already unique!");
//            return this;
//        }
//        return new DAWare(classID, DAUniqueID.createRandom(), name);
//    }

    @Override
    public DAWareClass getWareClass() {
        return wareClass;
    }

    @Override
    public DAValue<Mass> getMass() {
        return wareClass.mass;
    }

    @Override
    public Unit<?> getUnit() {
        return wareClass.unit;
    }

    @Override
    public DAValue<Volume> getVolume() {
        return wareClass.volume;
    }

    @Override
    public DAValue<Pieces> getAmount() {
        return amount;
    }

    @Override
    public int doCompare(IDataAtom o) {
        if (itemID != null) {
            return itemID.compareTo(((DAWare) o).itemID);
        }
        return classID.compareTo(((DAWare) o).classID);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        classID.toStream(stream);
        stream.writeBoolean((itemID == null));
        if (itemID != null) {
            itemID.toStream(stream);
            itemName.toStream(stream);
        }
        amount.toStream(stream);
    }

    /**
     * Init() needs to be called after deserialization.
     */
    @Override
    public DAWare fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        classID = new DAUniqueID().fromStream(stream);
        DAUniqueID itemID = null;
        DAText itemName = null;
        if (stream.readBoolean()) {
            itemID = new DAUniqueID().fromStream(stream);
            itemName = new DAText().fromStream(stream);
        }

        return null;
    }

    //    public static ArrayList<DAWareAmount> toAmountList(DAVector<DAbmWaresContainer> conti) {
//        ArrayList<DAWareAmount> v = new DAVector<DAWareAmount>(DAWareAmount.class);
//        for (DAbmWaresContainer wc : conti) {
//            DAWareAmount wa = wc.getContent();
//            if ((wa != null) && (wa.amount.doubleValue(Pieces.UNIT) > 0)) {
//                boolean done = false;
//                for (DAWareAmount a : v) {
//                    if (a.ware.equals(wa.ware)) {
//                        a.add(wa.amount);
//                        done = true;
//                        break;
//                    }
//                }
//                if (! done) {
//                    v.add(wa);
//                }
//            }
//        }
//        return v;
//    }

    public boolean add(DAValue<Pieces> value) {
        try {
            if (isUnique()) {
                Log.warn(DAWare.class, "add: Increase of unique ware not allowed!");
                return false;
            }
            amount = amount.add(value);
            if (amount.doubleValueBase() < 0.0) {
                amount = DAValue.<Pieces>create(0, Pieces.UNIT);
            }
            return true;
        } catch (ConversionException ex) {
            Log.throwable(DAWare.class, ex, "add");
            return false;
        }
    }

    public boolean sub(DAValue<Pieces> value) {
        try {
            if (isUnique()) {
                Log.warn(DAWare.class, "sub: Decrease of unique ware not allowed!");
                return false;
            }
            amount = amount.sub(value);
            if (amount.doubleValue(Pieces.UNIT) < 0.0) {
                amount = DAValue.<Pieces>create(0, Pieces.UNIT);
            }
            return true;
        } catch (ConversionException ex) {
            Log.throwable(DAWare.class, ex, "sub");
            return false;
        }
    }

    protected DAWare(DAUniqueID wareClassID, DAUniqueID itemID, DAText name, DAValue<Pieces> amount) {
        super();
        this.classID = wareClassID;
        this.itemID = itemID;
        this.itemName = name;
        this.amount = amount;
        if (isUnique() && (amount.doubleValue(NewUnits.PIECES) != 1.0)) {
            Log.warn(DAWare.class, "Amount != 1 of unique ware not allowed!");
            throw new IllegalArgumentException("Amount != 1 of unique ware not allowed!");
        }
    }

    private static final byte VERSION = 1;

    /**
     * Needed to restore ware class after serialisation
     */
    private DAUniqueID classID;
    /**
     * Unique items have an ID. Otherwise multiple items of this ware are stackable.
     */
    private DAUniqueID itemID;
    /**
     * Unique items have a name.
     */
    private DAText itemName;
    /**
     * Gets populated by init().
     * Init() needs to be called after deserialization and Constructor.
     */
    private transient DAWareClass wareClass;
    private DAValue<Pieces> amount;

}
