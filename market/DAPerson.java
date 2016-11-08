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

import de.dertroglodyt.iegcommon.AssetPool;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.collection.DASet;

/**
 * Represent either a character or a group of anonymous persons.
 * @author martin
 */
@SuppressWarnings("serial")
public class DAPerson extends DAWare {

    private static final long serialVersionUID = SerialUIDPool.UID.DAPerson.value();

    private int count;
    private final DASet<DAPatent> patent;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        count = in.readInt();
        patent.readExternal(in);
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

        out.writeInt(count);
        patent.writeExternal(out);
    }

    @Deprecated
    public DAPerson() {
        super();
        count = 0;
        patent = new DASet<DAPatent>(DAPatent.class);
    }

    public DAPerson(DALine aName, int aCount) {
        super(AssetPool.AssetNameWareClass.Person.getWareClass());
        count = aCount;
        patent = new DASet<DAPatent>(DAPatent.class);
        super.makeUnique(aName);
    }

    public DAPerson(DALine aName, int aCount, DAPatent.Patent... aPatent) {
        super(AssetPool.AssetNameWareClass.Person.getWareClass());
        count = aCount;
        patent = new DASet<DAPatent>(DAPatent.class);
        for (int i=0; i < aPatent.length; i++) {
            patent.add(aPatent[i].getPatent());
        }
        super.makeUnique(aName);
    }

    @Override
    public String toString() {
        return getName() + " " + patent;
    }

    public int getCount() {
        return count;
    }

    public DAUniqueID getID() {
        return super.getItemID();
    }

    @Override
    public DAValue<Mass> getMass() {
        return wareClass.getMass().scale(count);
    }

    @Override
    public DAValue<Volume> getVolume() {
        return wareClass.getVolume().scale(count);
    }

    @Override
    public DALine getName() {
        return itemName;
    }

//    @Override
//    public DAResult<DALine> setName(DALine s) {
//        DAResult<DALine> r = new DAResult<DALine>("previous name '" + itemName + "' successfully changed to new name '"
//                + s + "'" , DAResult.ResultType.OK, itemName, "setName");
//        if (character != null) {
//            Log.warn(DAPerson.class, "setItemName: Itemname of a character can not be set here.");
//        } else {
//            itemName = s;
//            return r;
//        }
//    }

    public DASet<DAPatent> getPatentList() {
        return patent;
    }

    public void addPatent(DAPatent.Patent aPatent) {
        patent.add(aPatent.getPatent());
    }

    public void removePatent(DAPatent.Patent aPatent) {
        patent.remove(aPatent.getPatent());
    }

    public boolean hasPatent(DAPatent.Patent aPatent) {
        return patent.contains(aPatent.getPatent());
    }

//    public void resolve(DAWorldNode n) {
//        if (characterID != null) {
//            DACharacter ch = n.getCharacter(characterID);
//            if (ch == null) {
//                Log.warn(DAPerson.class, "Character from crew not found in world node!", "DAPerson.resolve");
//            }
//            setCharacter(ch);
//        }
//    }

//    @Override
//    public DAWare clone() {
//        if (character != null) {
//            throw new UnsupportedOperationException();
//        }
//        DAPerson p = new DAPerson(getName(), count);
//        for (int i=0; i < patent.size(); i++) {
//            p.patent.add(patent.elementAt(i));
//        }
//        if (itemID != null) {
//            p.itemID = itemID.clone();
//        }
//        return p;
//    }

    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DAPerson parse(String value) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public DVCDataEditor getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCsePerson de = new DVCsePerson(this, editmode, user);
//        addListener(de);
//        return de;
//    }

}
