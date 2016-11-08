/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import de.dertroglodyt.iegcommon.AssetPool;
import de.dertroglodyt.iegcommon.AssetPool.AssetNameWareClass;
import de.dertroglodyt.iegcommon.Constants.UserDataKey;
import de.dertroglodyt.iegcommon.astro.DAThing;
import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DATreeNode;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.collection.DAText;
import de.hdc.commonlibrary.util.Log;


/**
 * Holds the invariable properties of a DAWare.
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWareClass extends DATreeNode<DAWaresType, DAWareClass, DAWareClass> {
//        implements IDVCActionHandler {

    public static enum Size {
        NONE("unbekannt", 0),          // Max. Seitenlänge
        MINI("mini", 0.008),           // 0.20m
        SMALL("klein", 1.0),           // 1.0m
        MEDIUM("mittel", 125.0),       // 5.0m
        LARGE("groß", 8000.0),         // 20.0m
        GIANT("riesig", 125000.0),     // 50.0m
        COLOSSAL("kolossal", 1e100),   // everything else. can not be carried / stored by normal means.
        ;

        private final String text;
        private final DAValue<Volume> vol;
        private final DAValue<Mass> mass;
        private final DAValue<Length> maxSide;

        Size(String aText, double m3) {
            text = aText;
            vol = new DAValue<Volume>(m3, SI.CUBIC_METRE);
            mass = new DAValue<Mass>(m3 * 200, SI.KILOGRAM);  // 200kg / m³  Hull-Only-Weight
            maxSide = new DAValue<Length>(Math.cbrt(m3), SI.METER);
        }

        public String toSerialString() {
            return super.name();
        }

        // Attention: MUST NOT be used in serialisation!
        @Override
        public String toString() {
            return text;
        }

        public static Size valueFrom(String aText) {
            for (Size s: values()) {
                if (s.toString().equals(aText)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("Cannot parse into a Size: " + aText);
        }

        public DAValue<Volume> getMaxVolume() {
            return vol;
        }

        public DAValue<Mass> getDefaultMass() {
            return mass;
        }

        public DAValue<Length> getMaxSide() {
            return maxSide;
        }

        public boolean isSmaller(Size s) {
            return (vol.isLessThan(s.getMaxVolume()));
        }

        public boolean isBiggerOrEqual(Size s) {
            return ! isSmaller(s);
        }

        public boolean isBiggerOrEqual(DAValue<Volume> v) {
            return ! (vol.isLessThan(v));
        }
    }
    public static enum State {
        /**
         * WareClass has been marked for deletion.
         */
        DELETED,
        /**
         * WareClasses available only to admins. (Alien ships etc.)
         */
        ADMIN,
        /**
         * Publi available WareClass.
         */
        PUBLIC
    }

    /**
//    public static enum Parameters implements IParameterType {
//
//        NONE(null),
//        ITEMID(DAUniqueID.class),
//        WARECLASS(DAWareClass.class),
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
//        SETWARECLASS(Parameters.NONE, Parameters.ITEMID, Parameters.WARECLASS),
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
*/

    @Deprecated
    public DAWareClass() {
        this(new DAUniqueID().toParseString(""), Size.SMALL, State.ADMIN, new DALine("<not named>")
                , new DAText("<Beschreibung>"), new DAValue<Mass>(1000.0, SI.KILOGRAM)
                , Size.SMALL.getMaxVolume(), NewUnits.PIECES, AssetPool.AssetNameWareClass.ElectricalPower);
    }

    public DAWareClass(String name) {
        this(new DAUniqueID().toParseString(""), Size.SMALL, State.ADMIN, new DALine(name)
                , new DAText("<Beschreibung>"), new DAValue<Mass>(1000.0, SI.KILOGRAM)
                , Size.SMALL.getMaxVolume(), NewUnits.PIECES, AssetPool.AssetNameWareClass.ElectricalPower);
    }

//    public DAWareClass(Size aSize, DALine aClassName, DAText aDescription, DAValue<Mass> aMass, Unit<?> aUnit
//            , AssetNameGrafic graficAssetName) {
//        this(null, aSize, State.PUBLIC, aClassName, aDescription, aMass, aSize.getMaxVolume(), aUnit, graficAssetName);
//    }

//    public DAWareClass(String idStr, Size aSize
//            , DALine aClassName, DAText aDescription, DAValue<Mass> aMass
//            , Unit<?> aUnit, AssetNameGrafic graficAssetName) {
//        this(idStr, aSize, aClassName, aDescription, aMass, aSize.getMaxVolume(), aUnit, graficAssetName);
//    }

    protected DAWareClass(String idStr, Size size, State state
            , DALine className, DAText description
            , DAValue<Mass> mass, DAValue<Volume> volume
            , Unit<?> unit, AssetNameWareClass graficAssetName) {
        super(DAWaresType.class, className, (idStr != null)?new DAUniqueID(idStr):DAUniqueID.createRandom());
        if (volume.isGreaterThan(size.getMaxVolume())) {
            Log.warn(DAWareClass.class, "Volume of ware is bigger than size allows!");
        }
        fDescription = description;
        fSize = size;
        fState = state;
        fAssetName = graficAssetName;
        fUnit = unit;
        fMass = mass;
        fVolume = volume;
        recalc();
    }

    public DAWareClass(AssetNameWareClass assetName) {
        super(DAWaresType.class, new DALine(assetName.toString()), DAUniqueID.createRandom());
        fAssetName = assetName;
        fThing = new DAThing(new DALine(assetName.toString()), DAThing.Type.FREEFALLING, assetName);
        // Initialize from asset user data.
        fDescription = new DAText(getAssetData(UserDataKey.Description, ""));
        fSize = Size.valueOf(getAssetData(UserDataKey.Size, Size.SMALL.name()));
        fState = State.valueOf(getAssetData(UserDataKey.State, State.ADMIN.toString()));
        fUnit = Unit.valueOf(getAssetData(UserDataKey.Unit, NewUnits.PIECES.toString()));
        fMass = getAssetData(UserDataKey.Mass, fSize.getDefaultMass());
        fVolume = getAssetData(UserDataKey.Volume, fSize.getMaxVolume());
        recalc();
    }

    public void setFrom(DAWareClass wc) {
        setName(wc.getName());
        fDescription = wc.getDescription();
        fSize = wc.getSize();
        fState = wc.getState();
        fAssetName = wc.getAssetName();
        fUnit = wc.getUnit();
        recalc();
    }

    @Override
    public String toString() {
        return getName().toString();
    }

    @Override
    public String toParseString(String levelTab) {
//        return getName() + " (" + fSize + ") " + getMass() + " " + getVolume();
        throw new UnsupportedOperationException();
    }

    @Override
    public DAWareClass parse(String value) {
        throw new UnsupportedOperationException();
    }

    public IDAWare getInstance() {
        DAWare w = new DAWare(this);
//        w.resolveOther(aParentContainer);
        return w;
    }

    public final <T extends Object> T getAssetData(UserDataKey key, T defaultValue) {
        return getAssetData(key.toString(), defaultValue);
    }

    public final <T extends Object> T getAssetData(String key, T defaultValue) {
        T t;
        if (defaultValue instanceof IDataAtom) {
            t = (T) ((IDataAtom) defaultValue).parse((String) AssetPool.getGraficalObject(fAssetName).getUserData(key));
        } else{
            t = AssetPool.getGraficalObject(fAssetName).getUserData(key);
        }
        if (t != null) {
            return t;
        } else {
            return defaultValue;
        }
    }

    public DAUniqueID getTypeID() {
        if (getParent() != null) {
            return getParent().getItemID();
        } else {
            return new DAUniqueID();
        }
    }

//    public void setTypeID(DAUniqueID id) {
//        typeID = id;
//    }

    public Unit<?> getUnit() {
        return fUnit;
    }

    public void setUnit(Unit<?> aUnit) {
        fUnit = aUnit;
        notifyListener(this);
    }

    public Size getSize() {
        return fSize;
    }

    public void setSize(Size value) {
//        DVCErrorHandler.createDebug("Size: " + fSize + " -> " + value, "DAWareClass");
        fSize = value;
        notifyListener(this);
    }

    public boolean isPublic() {
        return (fState == State.PUBLIC);
    }

    public State getState() {
        return fState;
    }

    public void setState(State value) {
        fState = value;
        notifyListener(this);
    }

    public DAText getDescription() {
        return fDescription;
    }

//    public BranchGroup getNode() {
//        return fAssetName.getGraficNode();
//    }

//    private static DAValue<Mass> getDefaultMass(double aCapacity, DVCsmMaterial aMaterial, Size aSize) {
//        return getDefaultMass(aCapacity, aMaterial, aSize.getMaxVolume().getValue());
//    }

//    private static DAValue<Mass> getDefaultMass(double aCapacity, DVCsmMaterial aMaterial, double aVolume) {
//        return new DAValue<Mass>((1.0 - aCapacity) * aMaterial.getDensity() * aVolume);
//    }

    public DAValue<Mass> getMass() {
        return fMass;
    }

    public void setMass(DAValue<Mass> m) {
        fMass = m;
        recalc();
    }

    public DAValue<Volume> getVolume() {
        return fVolume;
    }

    public void setVolume(DAValue<Volume> v) {
        fVolume = v;
        recalc();
    }

    public DAValue<VolumetricDensity> getKGperM3() {
        return kgPerM3;
    }

//    /**
//     *
//     * @return
//     * @deprecated Only to be used by DVCDlgEditModuleClass.
//     */
//    @Deprecated
//    public DVCsmObjectGroup getGroup() {
//        return fAssetName;
//    }

    public AssetNameWareClass getAssetName() {
        return fAssetName;
    }

//    @Override
//    public DAWareClass clone() {
//        DAWareClass wc = new DAWareClass(getItemID().toString(), fSize, fState
//                , getName().clone(), fDescription.clone()
//                , fMass, fVolume, fUnit, fAssetName);
//        return wc;
//    }

//    @Override
//    public DVCDataEditor getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCDataEditor de = new DVCseWareClass(this, editmode, user);
//        addListener(de);
//        return de;
//    }

//    public DVCDataEditor getGroupEditor(EditMode editmode, DVCAbstractUser user, boolean showPreview, boolean buildPreview, boolean previewOnly) {
//        return fAssetName.getEditor(editmode, user, showPreview, buildPreview, previewOnly);
//    }

//    @Override
//    public DVCdmTypedResult<DVCdmParameterList> handle(DVCdmOwningThread thread, DVCdmRemoteAction action
//            , boolean sourceIsServer) {
//        Action type = Action.valueOf(action.getName().toString());
//        if (type == null) {
//            return new DVCdmTypedResult<>("Unknown action type <" + action.getName() + ">!",
//                    DVCdmResult.ResultType.WARNING, null, "DVCsmWareClass.handle");
//        }
//        DVCdmParameterList pl = new DVCdmParameterList();
//        DVCdmParameterList parms = action.getParameters();
//        DAUniqueID itemID = (DAUniqueID) parms.get(Parameters.ITEMID);
//        if (itemID == null) {
//            return new DVCdmTypedResult<>("ItemID is NULL! <" + action + ">!",
//                    DVCdmResult.ResultType.WARNING, null, "DVCsmWareClass.handle");
//        }
//        if (! getItemID().equals(itemID)) {
//            return new DVCdmTypedResult<>("ItemID does not match! <" + action + ">!",
//                    DVCdmResult.ResultType.WARNING, null, "DVCsmWareClassv.handle");
//        }
//        switch (type) {
//            case SETWARECLASS: {
//                DAWareClass m = (DAWareClass) parms.get(Parameters.WARECLASS);
//                if (m == null) {
//                    return new DVCdmTypedResult<>("WareClass is NULL!"
//                            , DVCdmResult.ResultType.WARNING, null, "DVCsmWareClass.handle");
//                }
//                setFrom(m);
//                break;
//            }
//            default: {
//                return new DVCdmTypedResult<>("Unknown action <" + action + ">."
//                        , DVCdmResult.ResultType.WARNING, null, "DVCsmWareClass.handle");
//            }
//        }
//        return new DVCdmTypedResult<>("ok", DVCdmResult.ResultType.OK, pl, "DVCsmWareClass.handle");
//    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        removeAllChildren();

        super.readExternal(in);
        Log.debug(DAWareClass.class, "reading {0} {1}", this.getClass().getName(), this.getName());
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            Log.fatal(DAWareClass.class, "readExternal: Unknown version number <" + version + ">.");
        }
        fDescription.readExternal(in);
        fSize = Size.valueOf(in.readUTF());
        fState = State.valueOf(in.readUTF());
        fAssetName = AssetNameWareClass.valueOf(in.readUTF());
        fUnit = Unit.valueOf(in.readUTF());
        fMass.readExternal(in);
        fVolume.readExternal(in);
        if (getName().toString().trim().length() == 0) {
            setName(new DALine("<not named>"));
        }
        recalc();
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

        fDescription.writeExternal(out);
        out.writeUTF(fSize.toSerialString());
        out.writeUTF(fState.toString());
        out.writeUTF(fAssetName.toString());
        out.writeUTF(fUnit.toString());
        fMass.writeExternal(out);
        fVolume.writeExternal(out);
    }

    protected DAText fDescription;
    protected Size fSize;
    protected State fState;
    protected AssetNameWareClass fAssetName;
    protected Unit<?> fUnit;
    protected DAValue<Mass> fMass;
    protected DAValue<Volume> fVolume;

    protected transient DAValue<VolumetricDensity> kgPerM3;
    protected transient DAThing fThing;

    private static final long serialVersionUID = SerialUIDPool.UID.DAWareClass.value();

    public void recalc() {
//        fAssetName.recalc();
        if (getVolume().doubleValue() != 0.0) {
            kgPerM3 = fMass.div(fVolume).to(VolumetricDensity.UNIT);
        } else {
            kgPerM3 = new DAValue<VolumetricDensity>(0.0, VolumetricDensity.UNIT);
        }
    }

}
