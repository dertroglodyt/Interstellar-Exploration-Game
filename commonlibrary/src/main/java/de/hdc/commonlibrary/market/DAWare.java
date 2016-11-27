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

import android.support.annotation.Nullable;

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
 * Holds instance variables of a ware (i.e. a module) if needed.
 * All invariant properties of this ware are held in the wareClass.
 *
 * 'add' and 'sub' make this class into non-immutable!
 *
 * @author martin
 */
//@SuppressWarnings("serial")
public class DAWare extends DataAtom implements IDAWare {

    @Deprecated
    public DAWare() {
        classID = null;
        itemID = null;
        name = null;
        amount = null;
        wareClass = null;
    }

    public static DAWare create(IDAWare ware, DAValue<Pieces> amount) {
        return new DAWare(ware.getWareClass(), ware.getItemID(), ware.getName(), amount);
    }

    public static DAWare create(DAWareClass wareClass, DAValue<Pieces> amount) {
        return new DAWare(wareClass, null, DAText.create(""), amount);
    }

    public static DAWare createUnique(DAWareClass aWareClass, DAText name, DAValue<Pieces> amount) {
        return new DAWare(aWareClass, DAUniqueID.createRandom(), name, amount);
    }

    @Override
    public void init(DAWareTypeTree tree) {
        wareClass = tree.getWareClass(classID);
        if (wareClass == null) {
            throw new IllegalArgumentException("DAWare: Unknown DAWareClass!");
        }
    }

    @Override
    public String toString() {
        return (isUnique()? name + " (" : "") + classID + (isUnique()?" : )" + itemID : "")
                + amount;
    }

    @Override
    public int doCompare(IDataAtom o) {
        if (itemID != null) {
            return itemID.compareTo(((DAWare) o).itemID);
        }
        return classID.compareTo(((DAWare) o).classID);
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
        return name;
    }

    @Override
    public final boolean isUnique() {
        return (itemID != null);
    }

    @Override
    public DAWare makeUnique(DAText name) {
        if (isUnique()) {
            Log.warn(DAWareClass.class, "makeUnique: Ware is already unique!");
            return this;
        }
        return new DAWare(classID, DAUniqueID.createRandom(), name, amount);
    }

    @Nullable
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
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        classID.toStream(stream);
        stream.writeBoolean((itemID != null));
        if (itemID != null) {
            itemID.toStream(stream);
            name.toStream(stream);
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
        final DAUniqueID aclassID = new DAUniqueID().fromStream(stream);
        DAUniqueID aitemID = null;
        DAText aitemName = null;
        if (stream.readBoolean()) {
            aitemID = new DAUniqueID().fromStream(stream);
            aitemName = new DAText().fromStream(stream);
        }
        final DAValue<Pieces> aamount = new DAValue<Pieces>().fromStream(stream);

        return new DAWare(aclassID, aitemID, aitemName, aamount);
    }

    @Override
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

    @Override
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

    /**
     * For deserialization only.
     * @param wareClassID
     * @param itemID
     * @param name
     * @param amount
     */
    private DAWare(DAUniqueID wareClassID, DAUniqueID itemID, DAText name, DAValue<Pieces> amount) {
        super();
        this.classID = wareClassID;
        this.itemID = itemID;
        this.name = name;
        this.amount = amount;
        if (isUnique() && (amount.doubleValue(NewUnits.PIECES) != 1.0)) {
            Log.warn(DAWare.class, "Amount != 1 of unique ware not allowed!");
            throw new IllegalArgumentException("Amount != 1 of unique ware not allowed!");
        }
        wareClass = null;
    }

    private DAWare(DAWareClass wareClass, DAUniqueID itemID, DAText name, DAValue<Pieces> amount) {
        super();
        this.classID = wareClass.id;
        this.wareClass = wareClass;
        this.itemID = itemID;
        this.name = name;
        this.amount = amount;
        if (isUnique() && (amount.doubleValue(NewUnits.PIECES) != 1.0)) {
            Log.warn(DAWare.class, "Amount != 1 of unique ware not allowed!");
            throw new IllegalArgumentException("Amount != 1 of unique ware not allowed!");
        }
        this.wareClass = null;
    }

    private static final byte VERSION = 1;

    /**
     * Needed to restore ware class after serialisation
     */
    private final DAUniqueID classID;
    /**
     * Unique items have an ID. Otherwise multiple items of this ware are stackable.
     */
    private final DAUniqueID itemID;
    /**
     * Unique items have a name.
     */
    private final DAText name;
    /**
     * Gets populated by init().
     * Init() needs to be called after deserialization and Constructor.
     */
    @Nullable
    private transient DAWareClass wareClass;
    private DAValue<Pieces> amount;

}
