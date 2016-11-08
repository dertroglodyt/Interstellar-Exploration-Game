/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

import de.hdc.commonlibrary.data.ITreeNode;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DATreeNode;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.collection.DAIDMap;
import de.hdc.commonlibrary.data.types.collection.DAText;
import de.hdc.commonlibrary.util.Log;

/**
 * Types defined in DAWaresTree.DefWaresTree
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWaresType extends DATreeNode<DAWaresType, DAWaresType, DAWareClass> {
//        implements IDVCActionHandler {

    private static final long serialVersionUID = SerialUIDPool.UID.DAWaresType.value();

    /**
//    private transient static final DecimalFormat f = new DecimalFormat("00000000000000000000000000000000"
//                , new DecimalFormatSymbols(Locale.GERMANY));
//
//    public static enum Parameters implements IParameterType {
//
//        NONE(null),
//        ITEMID(DAUniqueID.class),
//        WARESTYPE(DAWaresType.class),
//        ;
//
//        private DALine name;
//        private Class<?> c;
//
//        Parameters(Class<?> aClass) {
//            name = new DALine(this.toString());
//            c = aClass;
//        }
//
//        @Override
//        public DALine getName() {
//            return name;
//        }
//
//        @Override
//        public Class<?> getType() {
//            return c;
//        }
//;
//
//    }
//
//    public static enum Action implements IRemoteActionType {
//        SETWARESTYPE(Parameters.NONE, Parameters.ITEMID, Parameters.WARESTYPE),
//        ;
//
//        private DALine name;
//        private ArrayList<IParameterType> input;
//        private IParameterType result;
//
//        Action(IParameterType r, IParameterType... in) {
//            name = new DALine(this.toString());
//            input = new ArrayList<IParameterType>(in.length);
//            input.addAll(Arrays.asList(in));
//            result = r;
//        }
//
//        @Override
//        public DALine getName() {
//            return name;
//        }
//
//        @Override
//        public ArrayList<IParameterType> getInputType() {
//            return input;
//        }
//
//        @Override
//        public IParameterType getResultType() {
//            return result;
//        }
//
//    }

//    public enum WaresTypeID {
//        UNKNOWN("f0000000000000000000000000000000"),
//        BLUEPRINT("f0000000000000000000000000000001"),
//        BLUEPRINT_HALF("f0000000000000000000000000000002"),
//        BLUEPRINT_FINISHED("f0000000000000000000000000000003"),
//        BLUEPRINT_MODULE("f0000000000000000000000000000004"),
//        BLUEPRINT_MODULE_MINI("f0000000000000000000000000000005"),
//        BLUEPRINT_MODULE_SMALL("f0000000000000000000000000000006"),
//        BLUEPRINT_MODULE_MEDIUM("f0000000000000000000000000000007"),
//        BLUEPRINT_MODULE_LARGE("f0000000000000000000000000000008"),
//        BLUEPRINT_MODULE_GIANT("f0000000000000000000000000000009"),
//        BLUEPRINT_MODULE_GROUP("f0000000000000000000000000000010"),
//        BLUEPRINT_MODULE_GROUP_MINI("f0000000000000000000000000000011"),
//        BLUEPRINT_MODULE_GROUP_SMALL("f0000000000000000000000000000012"),
//        BLUEPRINT_MODULE_GROUP_MEDIUM("f0000000000000000000000000000013"),
//        BLUEPRINT_MODULE_GROUP_LARGE("f0000000000000000000000000000014"),
//        BLUEPRINT_MODULE_GROUP_GIANT("f0000000000000000000000000000015"),
//        BLUEPRINT_SHIP("f0000000000000000000000000000016"),
//        BLUEPRINT_SHIP_DRONE("f0000000000000000000000000000017"),
//        BLUEPRINT_SHIP_SHUTTLE("f0000000000000000000000000000018"),
//        BLUEPRINT_SHIP_FRIGATE("f0000000000000000000000000000019"),
//        BLUEPRINT_SHIP_CORVETTE("f0000000000000000000000000000020"),
//        BLUEPRINT_SHIP_DESTROYER("f0000000000000000000000000000021"),
//        BLUEPRINT_SHIP_CRUISER("f0000000000000000000000000000022"),
//        BLUEPRINT_SHIP_BATTLESHIP("f0000000000000000000000000000023"),
//        BLUEPRINT_SHIP_CARRIER("f0000000000000000000000000000024"),
//        BLUEPRINT_SHIP_CAPITAL("f0000000000000000000000000000025"),
//        BLUEPRINT_STATION("f0000000000000000000000000000026"),
//        GOODS("f0000000000000000000000000000027"),
//        GOODS_RAW("f0000000000000000000000000000028"),
//        GOODS_RAW_GAS("f0000000000000000000000000000029"),
//        GOODS_RAW_LIQUID("f0000000000000000000000000000030"),
//        GOODS_RAW_SOLID("f0000000000000000000000000000031"),
//        GOODS_RAW_SOLID_ORE("f0000000000000000000000000000032"),
//        GOODS_MINERALS("f0000000000000000000000000000033"),
//        GOODS_HALF("f0000000000000000000000000000034"),
//        GOODS_FINISHED("f0000000000000000000000000000035"),
//        MODULE("f0000000000000000000000000000036"),
//        MODULE_MINI("f0000000000000000000000000000037"),
//        MODULE_SMALL("f0000000000000000000000000000038"),
//        MODULE_MEDIUM("f0000000000000000000000000000039"),
//        MODULE_LARGE("f0000000000000000000000000000040"),
//        MODULE_GIANT("f0000000000000000000000000000041"),
//        MODULE_GROUP("f0000000000000000000000000000042"),
//        MODULE_GROUP_MINI("f0000000000000000000000000000043"),
//        MODULE_GROUP_SMALL("f0000000000000000000000000000044"),
//        MODULE_GROUP_MEDIUM("f0000000000000000000000000000045"),
//        MODULE_GROUP_LARGE("f0000000000000000000000000000046"),
//        MODULE_GROUP_GIANT("f0000000000000000000000000000047"),
//        SHIP("f0000000000000000000000000000048"),
//        SHIP_DRONE("f0000000000000000000000000000049"),
//        SHIP_SHUTTLE("f0000000000000000000000000000050"),
//        SHIP_FRIGATE("f0000000000000000000000000000051"),
//        SHIP_CORVETTE("f0000000000000000000000000000052"),
//        SHIP_DESTROYER("f0000000000000000000000000000053"),
//        SHIP_CRUISER("f0000000000000000000000000000054"),
//        SHIP_BATTLESHIP("f0000000000000000000000000000055"),
//        SHIP_CARRIER("f0000000000000000000000000000056"),
//        SHIP_CAPITAL("f0000000000000000000000000000057"),
//        STATION("f0000000000000000000000000000058"),
//             ;
//
//        private final DAUniqueID id;
//
//        WaresTypeID() {
//            id = new DAUniqueID();
//        }
//
//        WaresTypeID(String aID) {
//            id = new DAUniqueID(aID);
//        }
//
//        public DAUniqueID getID() {
//            return id;
//        }
//
//        public static WaresTypeID byID(DAUniqueID id) {
//            for (WaresTypeID wt : values()) {
//                if (wt.getID().equals(id)) {
//                    return wt;
//                }
//            }
//            return null;
//        }
//    }
*/

    private DALine fDisplayName;
    private DAText fDescription;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        removeAllChildren();

        super.readExternal(in);
        Log.debug(DAWaresType.class, "reading " + this.getClass().getName() + " " + this.getName());
        byte version = in.readByte();
        // Do something different here if old version demands it

        fDisplayName.readExternal(in);
        fDescription.readExternal(in);
        //wareClassList.removeAll();
        //wareClassList.readExternal(in);
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

        fDisplayName.writeExternal(out);
        fDescription.writeExternal(out);
        //wareClassList.writeExternal(out);
    }

    @Deprecated
    public DAWaresType() {
        super();
        fDisplayName = new DALine();
        fDescription = new DAText();
        //wareClassList = new DAWareClassList();
    }

//    public DAWaresType(WaresTypeID aType, String aHierarchy1, String aHierarchy2, String aHierarchy3
//            , String aHierarchy4, String aHierarchy5, int i, String aName, String aDescription) {
//        this(aType, aName, aDescription, aHierarchy1, aHierarchy2, aHierarchy3, aHierarchy4, aHierarchy5);
//    }
//
//    public DAWaresType(WaresTypeID aType, String aName, String aDescription,  String... aHierarchy) {
//        super(DAWaresType.class, DAWaresType.class, DAWaresType.class, new DALine(aName));
//        fDescription = new DAText(aDescription);
//        wareClassList = new DAWareClassList();
//    }

//    public DAWaresType(WaresTypeID aType, String aName, String aDescription) {
//        super(DAWaresType.class, DAWaresType.class, DAWareClass.class, new DALine(aName), aType.getID());
//        description = new DAText(aDescription);
//        //wareClassList = new DAWareClassList();
//    }

    public DAWaresType(String name, String displayName, String description, DAUniqueID id) {
        super(DAWaresType.class, DAWaresType.class, DAWareClass.class, new DALine(name), id);
        fDisplayName = new DALine(displayName);
        fDescription = new DAText(description);
    }

    @Override
    public String toString() {
        return getName().toString();
    }

    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DAWaresType parse(String value) {
        throw new UnsupportedOperationException();
    }

//    public void setFrom(DAWaresType wt) {
//        setName(wt.getName());
//        fDescription = wt.getDescription().clone();
//    }

//    public WaresTypeID getType() {
//        return WaresTypeID.byID(getItemID());
//    }

    public DAText getDescription() {
        return fDescription;
    }

    public DAIDMap<DAWareClass> getWareClasses() {
        DAIDMap<DAWareClass> wcl = new DAIDMap<DAWareClass>(DAWareClass.class);
        Iterator<ITreeNode> i = leafIterator();
        while (i.hasNext()) {
            DAWareClass wc = (DAWareClass) i.next();
            wcl.addProperty(wc.getItemID(), wc);
        }
        return wcl;
    }

//    public void init(PhysicsSpace physicsSpace) {
//        Iterator<ITreeNode> i = branchIterator();
//        while (i.hasNext()) {
//            DAWaresType wt = (DAWaresType) i.next();
//            wt.init(physicsSpace);
//        }
//        i = leafIterator();
//        while (i.hasNext()) {
//            DAWareClass wc = (DAWareClass) i.next();
//            wc.init(physicsSpace);
//        }
//    }

//    public void resolve(int lvl, IDVCsmGraficNode parent) {
//        Iterator<ITreeNode> i = branchIterator();
//        while (i.hasNext()) {
//            DAWaresType wt = (DAWaresType) i.next();
//            wt.resolve(lvl+1, parent);
//        }
//        i = leafIterator();
//        while (i.hasNext()) {
//            DAWareClass wc = (DAWareClass) i.next();
//            wc.resolve(lvl+1);
//        }
//    }

//    public void resolveOther() {
//        Iterator<IDATreeNode> i = branchIterator();
//        while (i.hasNext()) {
//            DAWaresType wt = (DAWaresType) i.next();
//            wt.resolveOther();
//        }
//        i = leafIterator();
//        while (i.hasNext()) {
//            DAWareClass wc = (DAWareClass) i.next();
//            wc.resolveOther();
//        }
//    }

    public DAWareClass getWareClass(DAUniqueID wareClassID) {
        ITreeNode tn = get(wareClassID);
        if (tn instanceof DAWareClass) {
            return (DAWareClass) tn;
        } else {
            return null;
        }
    }

    public void add(DAWareClass wc) {
        //wc.setTypeID(getItemID());
        //wareClassList.addProperty(wc.getClassID(), wc);
        super.add(wc);
        notifyListener(this);
    }

    public void removeAll() {
        //wareClassList.removeAll();
        Iterator<ITreeNode> i = leafIterator();
        while (i.hasNext()) {
            i.remove();
        }
        notifyListener(this);
    }

    public void remove(DAWareClass wc) {
        //wareClassList.remove(wc.getClassID());
        super.remove(wc);
        notifyListener(this);
    }

//    @Override
//    public boolean parse(String value) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

//    @Deprecated
//    @Override
//    public DVCBasicDataModel clone() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

//    @Override
//    public DVCseWaresType getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseWaresType de = new DVCseWaresType(this, editmode, user);
//        addListener(de);
//        return de;
//    }

//    public String[] getPathArray(String newPath) {
//        String[] s = new String[0];
//        Vector<String> vs = new Vector<String>();
//        for (int i=0; i < hierarchy.fSize(); i++) {
//            String sh = hierarchy.elementAt(i).toString();
//            if (sh.length() > 0) {
//                vs.add(sh);
//            }
//        }
//        if ((newPath != null) && (newPath.length() > 0)) {
//            vs.add(newPath);
//        }
//        return vs.toArray(s);
//    }

//    @Override
//    public DATypedResult<DAParameterList> handle(DAOwningThread thread, DARemoteAction action
//            , boolean sourceIsServer) {
//        Action type = Action.valueOf(action.getName().toString());
//        if (type == null) {
//            return new DATypedResult<DAParameterList>("Unknown action type <" + action.getName() + ">!",
//                    DAResult.ResultType.WARNING, null, "DVCsmWaresType.handle");
//        }
//        DAParameterList pl = new DAParameterList();
//        DAParameterList parms = action.getParameters();
//        DAUniqueID itemID = (DAUniqueID) parms.get(Parameters.ITEMID);
//        if (itemID == null) {
//            return new DATypedResult<DAParameterList>("ItemID is NULL! <" + action + ">!",
//                    DAResult.ResultType.WARNING, null, "DVCsmWaresType.handle");
//        }
//        if (! getItemID().equals(itemID)) {
//            return new DATypedResult<DAParameterList>("ItemID does not match! <" + action + ">!",
//                    DAResult.ResultType.WARNING, null, "DVCsmWaresType.handle");
//        }
//        switch (type) {
//            case SETWARESTYPE: {
//                DAWaresType m = (DAWaresType) parms.get(Parameters.WARESTYPE);
//                if (m == null) {
//                    return new DATypedResult<DAParameterList>("Wares type is NULL!"
//                            , DAResult.ResultType.WARNING, null, "DVCsmWaresType.handle");
//                }
//                setFrom(m);
//                break;
//            }
//            default: {
//                return new DATypedResult<DAParameterList>("Unknown action <" + action + ">."
//                        , DAResult.ResultType.WARNING, null, "DVCsmWaresType.handle");
//            }
//        }
//        return new DATypedResult<DAParameterList>("ok", DAResult.ResultType.OK, pl, "DVCsmWaresType.handle");
//    }

//    @Override
//    public DAWaresType clone() {
////        DAWaresType wt = new DAWaresType(getName().toString(), getDescription().toString(), getItemID());
////        return wt;
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

}
