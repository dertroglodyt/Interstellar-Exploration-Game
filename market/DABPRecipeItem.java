/*
 * DABPRecipe.java
 *
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.dertroglodyt.iegcommon.AssetPool;
import de.dertroglodyt.iegcommon.astro.DATransform;
import de.dertroglodyt.iegcommon.module.DABasicModule;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.atom.DataAtom;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DABPRecipeItem extends DataAtom {

    private static final long serialVersionUID = SerialUIDPool.UID.DABPRecipeItem.value();

    /**
     * Wares needed for production of this product and where to place them.
     */
    private final DAUniqueID wareClassID;
    private final DAValue<Pieces> amount;
    private final DATransform pos;

    private transient DAWareClass wareClass;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            throw new IllegalStateException("readExternal: Unknown version number <" + version + ">.");
        }
        wareClassID.readExternal(in);
        amount.readExternal(in);
        pos.readExternal(in);
        wareClass = null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish changed read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are changed.
         */
        byte version = 1;
        out.writeByte(version);

        wareClassID.writeExternal(out);
        amount.writeExternal(out);
        pos.writeExternal(out);
    }

    /**
     * @deprecated To be used by serialisation only.
     */
    @Deprecated
    public DABPRecipeItem() {
        super();
        wareClassID = new DAUniqueID();
        amount = new DAValue<Pieces>();
        pos = new DATransform();
        wareClass = null;
    }

    public DABPRecipeItem(DAUniqueID aWareClassID, DAValue<Pieces> amount, DATransform trans) {
        super();
        wareClassID = aWareClassID;
        this.amount = amount;
        pos = trans;
        wareClass = null;
    }

    public DABPRecipeItem(DABasicModule module) {
        super();
        wareClassID = module.getClassID();
        amount = new DAValue<Pieces>(1, NewUnits.PIECES);
        pos = module.getTransform();
        wareClass = null;
    }

    @Override
    public String toString() {
        return amount.format(9, 0, true, ' ') + " " + getWareClass();
    }

    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DABPRecipeItem parse(String value) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public DABPRecipeItem clone() {
//        return new DABPRecipeItem(wareClassID, amount, pos);
//    }

    public DAUniqueID getWareClassID() {
        return wareClassID;
    }

    public DAWareClass getWareClass() {
        if (wareClass == null) {
            wareClass = AssetPool.waresTree.getWareClass(wareClassID);
        }
        return wareClass;
    }

    public DAValue<Pieces> getAmount() {
        return amount;
    }

    public DATransform getTransform() {
        return pos;
    }

    @Override
    public DataAtom getTestInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
