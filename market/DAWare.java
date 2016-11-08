/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.Unit;

import de.dertroglodyt.iegcommon.AssetPool;
import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.atom.DataAtom;
import de.hdc.commonlibrary.data.types.compound.DAResult;
import de.hdc.commonlibrary.util.Log;

/**
 * Instance of a DAWareClass.
 * Holds instance variables of a ware (i.e. a module) if needed.
 * All invariant properties of this ware are held in the ware class.
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWare extends DataAtom implements IDAWare {

    public static final DALine NOT_NAMED = new DALine("<not named>");

    @Deprecated
    public DAWare() {
        super();
        classID = new DAUniqueID();
        itemID = null;
        itemName = null;
    }

    public DAWare(DAWareClass aWareClass) {
        super();
        try {
            if (aWareClass == null) {
                Log.fatal(DAWareClass.class, "WareClass is NULL!");
                return;
            }
            classID = aWareClass.getItemID();
            itemID = null;
            itemName = null;
            wareClass = aWareClass;
    //        wareClass.setOwner(this);
        } catch (Throwable t) {
            Log.throwable(DAWareClass.class, t, "()");
        }
    }

//    public static ImageIcon getDefaultIcon() {
//        return createImageIcon(DEF_ICON);
//    }

//    @Override
//    public void resolveOther(DAModuleContainer aParentContainer) {
//        // ware class may be unknown if this ware is in a warestree which was not read completely yet.
//        if (wareClass == null) {
//            wareClass = DAResourcePool.getWareClass(classID);
//            if (wareClass == null) {
//                DVCErrorHandler.raiseError(DVCdmResult.createWarning("No wareClass resolved for <" + this.toParseString() + ">"
//                        , "DAWare.resolveOther"));
//                wareClass = DAResourcePool.DefaultWareClasses.ELECTRICAL_POWER.getWareClass();
//            }
//        }
//    }

    @Override
    public String toString() {
        if (getWareClass() != null) {
            return (isUnique()?itemName + " (":"") + getWareClass() + (isUnique()?")":"");
        } else {
            Log.fatal(DAWareClass.class, "toString: No wareClass set for <" + getClass().getName() + ">");
            return "<no ware class>";
        }
    }

    @Override
    public DataAtom getTestInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DAWare parse(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DAUniqueID getTypeID() {
        return getWareClass().getTypeID();
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
    public DALine getName() {
        return itemName;
    }

    @Override
    public DAResult<DALine> setName(DALine newName) {
        DAResult<DALine> r = new DAResult<DALine>(
            "previous name '" + itemName + "' successfully changed to new name '"
            + newName + "'" , DAResult.ResultType.OK, itemName, "DAWare.setName");
        itemName = new DALine(newName.toString());
        return r;
    }

    @Override
    public DAWareClass getWareClass() {
        // ware class may be unknown if this ware is in a warestree which was not read completely yet.
        if (wareClass == null) {
            wareClass = AssetPool.waresTree.getWareClass(classID);
            if (wareClass == null) {
                Log.warn(DAWareClass.class, "getWareClass: No wareClass resolved for <" + this.toString() + ">");
                wareClass = AssetPool.AssetNameWareClass.ElectricalPower.getWareClass();
            }
        }
        return wareClass;
    }

//    protected BranchGroup getNode() {
//        return wareClass.getNode();
//    }

    @Override
    public DAValue<Mass> getMass() {
        return getWareClass().getMass();
    }

    @Override
    public Unit<?> getUnit() {
        return getWareClass().getUnit();
    }

    @Override
    public DAValue<Volume> getVolume() {
        return getWareClass().getVolume();
    }

    @Override
    public boolean isUnique() {
        return (itemID != null);
    }

    @Override
    public void makeUnique(DALine name) {
        if (isUnique()) {
            Log.warn(DAWareClass.class, "makeUnique: Ware is already unique!");
            return;
        }
        itemID = DAUniqueID.createRandom();
        itemName = name;
    }

//    @Override
//    public DAWare clone() {
//        DAWare w = new DAWare(getWareClass());
//        w.itemID = itemID;
//        w.itemName = itemName;
//        return w;
//    }

    @Override
    public int doCompare(IDataAtom o) {
        return classID.compareTo(((DAWare) o).classID);
    }

    @Override
    public int hashCode()  {
        int hash = 7;
        hash = 89 * hash + classID.hashCode();
        return hash;
    }

//    @Override
//    public DVCDataEditor getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseWare de = new DVCseWare(this, editmode, user);
//        addListener(de);
//        return de;
//    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        Log.debug(DAWareClass.class, "reading {0} {1}", this.getClass().getName(), this.getName());
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            Log.fatal(DAWareClass.class, "Unknown version number <" + version + ">.", "DAWare.readExternal");
        }
        classID.readExternal(in);
        if (in.readBoolean()) {
            itemID = new DAUniqueID();
            itemID.readExternal(in);
            itemName = new DALine();
            itemName.readExternal(in);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish isChanged read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are isChanged.
         */
        byte version = 1;
        out.writeByte(version);

        classID.writeExternal(out);
        out.writeBoolean((itemID != null));
        if (itemID != null) {
            itemID.writeExternal(out);
            itemName.writeExternal(out);
        }
    }

    /**
     * Needed to restore ware class after serialisation
     */
    protected DAUniqueID classID;
    /**
     * Unique items have an ID. Otherwise multiple items of this ware are stackable.
     */
    protected DAUniqueID itemID;
    /**
     * Unique items have a name.
     */
    protected DALine itemName;

    /**
     * lazy init
     */
    protected transient DAWareClass wareClass;

    private static final long serialVersionUID = SerialUIDPool.UID.DAWare.value();
//    private static final String DEF_ICON = "/datavault/common/space/icon/DAWare.gif";
//    static {
//        DVCBasicDataModel.register(DAWare.class, createImageIcon(DEF_ICON));
//    }


}
