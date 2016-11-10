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

/**
 *
 * @author martin
 */
public class DAShip extends DAModuleContainer {
//        implements IActionHandler
//
//    private static final long serialVersionUID = SerialUID.DAShip.value();
////    static {
////        DVCBasicDataModel.register(DAmcShip.class, createImageIcon("/datavault/common/space/icon/DAShip.gif"));
////    }
//
//    public static enum Parameters implements IParameterType {
//        NONE(null),
//        SHIPID(DAUniqueID.class),
//        SHIP(DAShip.class),
//        SHIPLIST(DAVector.class),  //<DAShip>
//        NAME(DAText.class),
//        DOCKINGSHIPID(DAUniqueID.class),
//        THRUSTER_TYPE(DAText.class),
//        THRUSTER_SPEED(DAText.class),
//        PRESET_TYPE(DAText.class),
//        CLANID(DAUniqueID.class),
//        STORAGE(DAbmStorage.class),
//        WARES_CONTAINER(DAbmWaresContainer.class),
//        CLAN(DAClan.class),
//        STORAGE_FROM_ID(DAUniqueID.class),
//        STORAGE_TO_ID(DAUniqueID.class),
//        STORAGE_TOSHIP_ID(DAUniqueID.class),
//        CARGOLIST(DAVector.class),  //<DAbmWaresContainer>
//        STORAGELIST(DAVector.class),  //<DAbmStorage>
//        DESTINATION_ID(DAUniqueID.class),
//        LOCATION(DAVector.class),
//        CAPTAINID(DAUniqueID.class),
//        ;
//
//        private DAText name;
//        private Class<?> c;
//
//        Parameters(Class<?> aClass) {
//            name = new DAText(this.toString());
//            c = aClass;
//        }
//
//        @Override
//        public DAText getName() {
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
//        /**
//         * Retrieves the minimal initialized clan stub. This is used by DVCrsmClan for lazy initialisation.
//         */
//        GET_STUB(Parameters.SHIP, Parameters.SHIPID),
//        SET_NAME(Parameters.NONE, Parameters.SHIPID, Parameters.NAME),
//        RENT_STORAGE(Parameters.STORAGE, Parameters.SHIPID, Parameters.CLANID),
//        UNPACK(Parameters.SHIP, Parameters.SHIPID, Parameters.WARES_CONTAINER, Parameters.STORAGE),
//        RESOLVE_CLAN(Parameters.CLAN, Parameters.SHIPID, Parameters.CLANID),
//        GET_DOCKED_TO(Parameters.SHIP, Parameters.SHIPID),
//        GET_DOCKED_SHIPS(Parameters.SHIPLIST, Parameters.SHIPID, Parameters.CLANID),
//        TRANSFER_CARGO(Parameters.NONE, Parameters.SHIPID, Parameters.CARGOLIST, Parameters.STORAGE_FROM_ID
//                , Parameters.STORAGE_TO_ID, Parameters.STORAGE_TOSHIP_ID),
//        GET_STORAGES(Parameters.STORAGELIST, Parameters.SHIPID),
//        PLACE_AT(Parameters.NONE, Parameters.SHIPID, Parameters.DESTINATION_ID),
//
//        // flight commands
//        DOCK(Parameters.NONE, Parameters.SHIPID, Parameters.DOCKINGSHIPID),
//        UNDOCK(Parameters.NONE, Parameters.SHIPID, Parameters.DOCKINGSHIPID),
//        SET_THRUST(Parameters.NONE, Parameters.SHIPID, Parameters.THRUSTER_SPEED, Parameters.THRUSTER_TYPE),
//        ALL_ENGINES_OFF(Parameters.NONE, Parameters.SHIPID),
//        INC_THRUST(Parameters.NONE, Parameters.SHIPID, Parameters.PRESET_TYPE),
//        DEC_THRUST(Parameters.NONE, Parameters.SHIPID, Parameters.PRESET_TYPE),
//        SET_DESTINATION(Parameters.NONE, Parameters.SHIPID, Parameters.DESTINATION_ID),
//        ALIGN_TO_DEST(Parameters.NONE, Parameters.SHIPID),
//        ALIGN_TO_LOC(Parameters.NONE, Parameters.SHIPID, Parameters.LOCATION),
//        FLY_TO_DEST(Parameters.NONE, Parameters.SHIPID),
//        SET_CAPTAIN(Parameters.NONE, Parameters.SHIPID, Parameters.CAPTAINID),
//        ;
//
//        private DAText name;
//        private ArrayList<IParameterType> input;
//        private IParameterType result;
//
//        Action(IParameterType r, IParameterType... in) {
//            name = new DAText(this.toString());
//            input = new ArrayList<IParameterType>(in.length);
//            input.addAll(Arrays.asList(in));
//            result = r;
//        }
//
//        @Override
//        public DAText getName() {
//            return name;
//        }
//
//        @Override
//        public int getInputTypeSize() {
//            return input.size();
//        }
//
//        @Override
//        public Iterator<IParameterType> getInputType() {
//            return input.iterator();
//        }
//
//        @Override
//        public IParameterType getResultType() {
//            return result;
//        }
//
//    }
//
//    public static enum State {
//        /**
//         * Dead as a brick. No life support!
//         */
//        OFFLINE("(offline)"),
//        /**
//         * Minimal power consumption mode.
//         */
//        IDLE("(idle)"),
//        /**
//         * Same as IDLE, only docked.
//         */
//        DOCKED("(docked)"),
//        /**
//         * Auto piloting in orbit.
//         */
//        ORBITING("(orbit)"),
//        /**
//         * Manual flight
//         */
//        MANUAL_FLIGHT("(manual)"),
//        MANUAL_FLIGHT_STOP_ROL("(manual)"),
//        MANUAL_FLIGHT_STOP_STRAFE("(manual)"),
//        MANUAL_FLIGHT_FULL_STOP("(manual)"),
//        /**
//         * Auto piloting
//         */
//        TRAVEL_APPROACH_ALIGN("(auto)"),
//        TRAVEL_APPROACH("(auto)"),
//        TRAVEL_APPROACH_ACCEL("(auto)"),
//        TRAVEL_APPROACH_DEACCEL("(auto)"),
//
//        TRAVEL_SOL_ALIGN("(sol)"),
//        TRAVEL_SOL_ACCEL("(sol)"),
//        TRAVEL_SOL_JUMP("(sol)"),
//        TRAVEL_SOL_DEACCEL("(sol)"),
//
//        TRAVEL_WARP_ALIGN("(warp)"),
//        TRAVEL_WARP_ACCEL("(warp)"),
//        TRAVEL_WARP_JUMP("(warp)"),
//        TRAVEL_WARP_DEACCEL("(warp)");
//
//        private final String desc;
//        private State(String s) {
//            desc = s;
//        }
//        public String getDescriptiveString() {
//            return desc;
//        }
//    }
//
//    public static enum Aim {
//        /**
//         * No aim for auto pilot to achieve.
//         */
//        NONE("-"),
//        /**
//         * Bring systems into idle state.
//         * Online systems which are offline.
//         * Stop engines, factories, etc.
//         */
//        TO_IDLE("Bereitschaft"),
//        TO_IDLE_2("Bereitschaft"),
//        /**
//         * Shut down all system.
//         */
//        TO_OFFLINE("Ausschalten"),
//        /**
//         * Start engines (if necessary) and aproach docking point of hangar.
//         * When reached stop and try to dock.
//         */
//        DOCK("Anlegen"),
//        /**
//         * Start engines and aproach undock point of hangar.
//         * When reached keep direction and speed.
//         */
//        UNDOCK("Ablegen"),
//        /**
//         * Align ship to point to a specified direction or destination.
//         * Does not change main thrust.
//         */
//        ALIGN_TO("Ausrichten"),
//        /**
//         * Try to reach destination.
//         * - align to destination
//         * - achieve cruising speed
//         * - activate jump drive
//         * - deactivate jump drive
//         * - deaccelerate and stop at destination
//         */
//        FLY_TO("Anfliegen"),
//        /**
//         * Fire thrusters until ship stops rolling.
//         */
//        STOP_ROLL("Stop Rotation"),
//        /**
//         * Fire thrusters until ship stops strafing.
//         */
//        STOP_STRAFE("Stop Gleiten"),
//        /**
//         * Bring ship to full stop. Including rolling.
//         */
//        FULL_STOP("Stoppen"),
//        ;
//
//        private final String desc;
//        private Aim(String s) {
//            desc = s;
//        }
//        public String getDescriptiveString() {
//            return desc;
//        }
//    }
//
//    public static enum Preset {
//        BW_FULL,
//        BW_THREE_QUARTER,
//        BW_HALF,
//        BW_QUARTER,
//        STOP,
//        QUARTER,
//        HALF,
//        THREE_QUARTER,
//        FULL,
//        ;
//
//        Preset inc() {
//            switch (this) {
//                case BW_FULL: return BW_THREE_QUARTER;
//                case BW_THREE_QUARTER: return BW_HALF;
//                case BW_HALF: return BW_QUARTER;
//                case BW_QUARTER: return STOP;
//                case STOP: return QUARTER;
//                case QUARTER: return HALF;
//                case HALF: return THREE_QUARTER;
//                case THREE_QUARTER: return FULL;
//                case FULL: return FULL;
//                default: return STOP;
//            }
//        }
//
//        Preset dec() {
//            switch (this) {
//                case FULL: return THREE_QUARTER;
//                case THREE_QUARTER: return HALF;
//                case HALF: return QUARTER;
//                case QUARTER: return STOP;
//                case STOP: return BW_QUARTER;
//                case BW_QUARTER: return BW_HALF;
//                case BW_HALF: return BW_THREE_QUARTER;
//                case BW_THREE_QUARTER: return BW_FULL;
//                case BW_FULL: return BW_FULL;
//                default: return STOP;
//            }
//        }
//
//        boolean isForward() {
//            switch (this) {
//                case STOP: return true;
//                case QUARTER: return true;
//                case HALF: return true;
//                case THREE_QUARTER: return true;
//                case FULL: return true;
//                default: return false;
//            }
//        }
//
//        boolean isBackward() {
//            switch (this) {
//                case STOP: return true;
//                case BW_QUARTER: return true;
//                case BW_HALF: return true;
//                case BW_THREE_QUARTER: return true;
//                case BW_FULL: return true;
//                default: return false;
//            }
//        }
//
//        Speed toSpeed() {
//            switch (this) {
//                case BW_FULL: return Speed.FULL;
//                case BW_THREE_QUARTER: return Speed.THREEQUARTER;
//                case BW_HALF: return Speed.HALF;
//                case BW_QUARTER: return Speed.QUARTER;
//                case STOP: return Speed.STOP;
//                case QUARTER: return Speed.QUARTER;
//                case HALF: return Speed.HALF;
//                case THREE_QUARTER: return Speed.THREEQUARTER;
//                case FULL: return Speed.FULL;
//                default: return Speed.STOP;
//            }
//        }
//    }
//
//    public static enum PresetType {
//        MAIN,
//        FORWARD,
//        HORIZONTAL,
//        VERTICAL,
//        YAW,
//        PITCH,
//        ROLL,
//        ;
//
//        ArrayList<ThrusterType> getThrusterType(Preset p) {
//            ArrayList<ThrusterType> v = new ArrayList<ThrusterType>(0);
//            switch (this) {
//                case MAIN: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.MainThrust);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.MainBreak);
//                    }
//                    break;
//                }
//                case FORWARD: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.StrafeForth);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.StrafeBack);
//                    }
//                    break;
//                }
//                case HORIZONTAL: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.StrafeRight);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.StrafeLetft);
//                    }
//                    break;
//                }
//                case VERTICAL: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.StrafeUp);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.StrafeDown);
//                    }
//                    break;
//                }
//                case YAW: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.YawPlus);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.YawMinus);
//                    }
//                    break;
//                }
//                case PITCH: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.PitchPlus);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.PitchMinus);
//                    }
//                    break;
//                }
//                case ROLL: {
//                    if (p.isForward()) {
//                        v.add(ThrusterType.RollPlus);
//                    }
//                    if (p.isBackward()) {
//                        v.add(ThrusterType.RollMinus);
//                    }
//                    break;
//                }
//            }
//            return v;
//        }
//    }
//
//    public static enum DefaultViewPoint {
//        MainHall("vp_mainhall"),
//        Hangar("vp_hangar"),
//        Storage("vp_storage"),
//        Market("vp_market"),
//        ;
//
//        public final String metaName;
//        DefaultViewPoint(String s) {
//            metaName = s;
//        }
//    }
//
//    /**
//     * Actual state of ship.
//     */
//    protected State fState;
//    /**
//     * What to achive next.
//     */
//    protected Aim fAim;
//    /**
//     * Where to travel next
//     */
//    protected DAUniqueID destID;
//    protected DAMarket market;
//    /**
//     * ID of owning clan
//     */
//    protected DAUniqueID ownerID;
//    protected DAUniqueID captainID;
//    private DAVector<DALogItem<DAPerson>> crew;
//    private DAVector<DALogItem<DAPerson>> passengers = new DAVector<DALogItem<DAPerson>>(DALogItem.class);
//    private EnumMap<PresetType, Preset> propulsionPreset;
//    /**
//     * Indicates if this is m_s_s public station.
//     * Public stations broadcast their position and can be navigated to at any time.
//     */
//    private boolean isPublicStation;
//    private final DALog fLog;
//
//
//    protected transient DACharacter captain;
//    /**
//     * Set by station / ship this ship is docked to.
//     */
//    protected transient de.dertroglodyt.iegcommon.module.DAShip dockedTo;
//    /**
//     * Course set to this destination.
//     */
//    protected transient DALocation destination;
//    protected transient DAClan owner;
//    protected transient final EnumMap<DAbmPropulsion.ThrusterType, DAVector<DAbmPropulsion>> thruster;
//    /**
//     * Actual point of reference.
//     */
//    protected transient DAWorldNode worldNode;
//    protected transient DAReactionControlSystem rcs;
//    /**
//     * Maximum acceleration from auxiliary thrusters
//     * [m/s²]
//     */
//    protected transient double maxAuxThrusterTrans;
//    /**
//     * Maximum angular acceleration from auxiliary thrusters
//     * [°/s²]
//     */
//    protected transient double maxAuxThrusterRot;
//
//    public de.dertroglodyt.iegcommon.module.DAShip getReference() {
//        return worldNode.getStation();
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        super.readExternal(in);
//        byte version = in.readByte();
//        // Do something different here if old version demands it
//
//        if ((version < 1) || (version > 1)) {
//            throw new IllegalStateException("readExternal: Unknown version number <" + version + ">.");
//        }
//        fState = State.valueOf(in.readUTF());
//        fAim = Aim.valueOf(in.readUTF());
//
//        DAVector3d<Dimensionless> heading = new DAVector3d<Dimensionless>();
//        heading.readExternal(in);
//        rcs.presetHeading(heading);
//        DAValue<Velocity> v = new DAValue<Velocity>();
//        v.readExternal(in);
//        rcs.presetVelocity(v);
//
//        if (in.readBoolean()) {
//            destID = new DAUniqueID();
//            destID.readExternal(in);
//        } else {
//            destID = null;
//            destination = null;
//        }
//        if (in.readBoolean()) {
//            market = new DAMarket();
//            market.readExternal(in);
//        }
//        ownerID.readExternal(in);
//        if (in.readBoolean()) {
//            captainID = new DAUniqueID();
//            captainID.readExternal(in);
//            captain = null;
//        } else {
//            captainID = null;
//            captain = null;
//        }
//        crew.readExternal(in);
//        passengers.readExternal(in);
//        for (PresetType t : PresetType.values()) {
//            propulsionPreset.put(t, Preset.STOP);
//        }
//        int x = in.readInt();
//        for (int i=0; i < x; i++) {
//            String s = in.readUTF();
//            PresetType pt = PresetType.valueOf(s);
//            s = in.readUTF();
//            Preset p = Preset.valueOf(s);
//            propulsionPreset.put(pt, p);
//        }
//        isPublicStation = in.readBoolean();
//        if (version >= 3) {
//            fLog.readExternal(in);
//        }
//    }
//
//    @Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        super.writeExternal(out);
//        /**
//         * The version number of the class to help distinguish isChanged read/write data formats.
//         * It should be set in every "writeExternal" of every class.
//         * It's value should only change if write-/readExternal are isChanged.
//         */
//        byte version = 1;
//        out.writeByte(version);
//
//        out.writeUTF(fState.toString());
//        out.writeUTF(fAim.toString());
//
//        rcs.getPresetHeading().writeExternal(out);
//        rcs.getPresetVelocity().writeExternal(out);
//
//        out.writeBoolean((destID != null));
//        if (destID != null) {
//            destID.writeExternal(out);
//        }
//        out.writeBoolean((market != null));
//        if (market != null) {
//            market.writeExternal(out);
//        }
//        ownerID.writeExternal(out);
//        out.writeBoolean((captainID != null));
//        if (captainID != null) {
//            captainID.writeExternal(out);
//        }
//        crew.writeExternal(out);
//        passengers.writeExternal(out);
//        out.writeInt(propulsionPreset.size());
//        for (PresetType pt : propulsionPreset.keySet()) {
//            out.writeUTF(pt.toString());
//            out.writeUTF(propulsionPreset.get(pt).toString());
//        }
//        out.writeBoolean(isPublicStation);
//        fLog.writeExternal(out);
//    }
//
//    @Deprecated
//    public DAShip() {
//        super();
//        //itemID = new DAUniqueID();
//        super.setName(new DAText("<not initialized>"));
//        dockedTo = null;
//        fState = State.OFFLINE;
//        fAim = Aim.NONE;
//        ownerID = new DAUniqueID();
//        captain = null;
//        crew = new DAVector<DALogItem<DAPerson>>(DALogItem.class);
//        passengers = new DAVector<DALogItem<DAPerson>>(DALogItem.class);
////        scriptListener = null;
//        market = null;
//        thruster = new EnumMap<ThrusterType, DAVector<DAbmPropulsion>>(DAbmPropulsion.ThrusterType.class);
//        propulsionPreset = new EnumMap<PresetType, Preset>(PresetType.class);
//        rcs = new DAReactionControlSystem(this);
//        isPublicStation = false;
//        fLog = new DALog();
//    }
//
//    public DAShip(DABasicModuleClass aWareClass, DATransform trans) {
//        this(aWareClass, trans, new DAText("New " + aWareClass), new DAUniqueID());
//    }
//
//    public DAShip(DABasicModuleClass aWareClass, DATransform trans, DAText aName, DAUniqueID aOwnerID) {
////            , IDVCScriptChangeListener scl) {
//        super(aWareClass, trans);
//        super.setName(aName);
//        dockedTo = null;
//        fState = State.OFFLINE;
//        fAim = Aim.NONE;
//        ownerID = aOwnerID;
//        captain = null;
//        crew = new DAVector<DALogItem<DAPerson>>(DALogItem.class);
//        passengers = new DAVector<DALogItem<DAPerson>>(DALogItem.class);
////        scriptListener = scl;
//        market = null;
//        thruster = new EnumMap<ThrusterType, DAVector<DAbmPropulsion>>(DAbmPropulsion.ThrusterType.class);
//        propulsionPreset = new EnumMap<PresetType, Preset>(PresetType.class);
//        rcs = new DAReactionControlSystem(this);
//        isPublicStation = false;
//        fLog = new DALog();
//        fLog.add(this.getItemID(), new DAText(aName + " gebaut."));
//    }
//
//    @Override
//    public String toString() {
//        return ((getItemID() != null)?getName().toString():"<" + wareClass + ">")
//                + " " + ((fAim != null)?fAim.getDescriptiveString():"<not init>")
//                + " " + ((fState != null)?fState.getDescriptiveString():"<not init>");
//    }
//
//    @Override
//    public String toParseString(String levelTab) {
//        return toString() + " " + ((getItemID() != null)?getItemID().toString():"<never used>");
//    }
//
//    @Override
//    public de.dertroglodyt.iegcommon.module.DAShip parse(String value) {
//        throw new UnsupportedOperationException();
//    }
//
////    @Override
////    public DAmcShip clone() {
////        DAmcShip s = new DAmcShip(wareClass, getName(), ownerID, scriptListener);
//////        s.setID(getItemID());
////        for (DABasicModule bm : allModules) {
////            s.addModule(bm.clone());
////        }
//////        s.resolve(worldNode);
////        return s;
////    }
//
////    @Override
////    @Override
////    public DVCrsmShip getRemoteDataModel(DAOwningThread thread, IDVCActionDispatcher dispatcher) {
////        // TODO: implement stubonly == true. and lazy data fetching in rsmShip
////        DVCrsmShip rs = new DVCrsmShip(thread, this, false, dispatcher);
////        addRemoteListener(rs);
////        return rs;
////    }
//
////    protected void notifyScriptListener() {
////        if (scriptListener != null) {
////            if (captain != null) {
////                scriptListener.scriptChanged(this, captain.getScript(), new Ship(this));
////            } else {
////                scriptListener.scriptChanged(this, null);
////            }
////        }
////    }
//
////    @Override
////    public void resolve(int lvl) {
////        super.resolve(lvl);
////    }
////
////    @Override
////    @Deprecated
////    public void resolveOther(DAModuleContainer aParentContainer) {
////        super.resolveOther(aParentContainer);
////    }
//
//    /**
//     * Resolve things after readExternal.
//     * @param n
//     */
//    public void init(DAWorldNode n) {
//        worldNode = n;
//        // resove characters in captain and crew
//        captain = worldNode.getCharacter(captainID);
//        if (ownerID.isValid()) {
//            owner = n.getClan(ownerID);
//            if (owner != null) {
////                    owner.setActionDispatcher(actionDispatcher);
//            } else {
//                Log.warn(de.dertroglodyt.iegcommon.module.DAShip.class, "init: Owner not found for ship: " + this);
//            }
//        } else {
//            owner = null;
//        }
//        if (market != null) {
//            Log.debug(de.dertroglodyt.iegcommon.module.DAShip.class, "init: Market: " + market);
//            market.init(this);
//        }
//        if (destID != null) {
//            destination = findOG(destID);
//            if (destination == null) {
//                Log.warn(de.dertroglodyt.iegcommon.module.DAShip.class, "init: " + this + ": Destination ID unknown <" + destID + ">");
//            }
//        }
//        if ((fState != State.OFFLINE) &&
//                (destination == null) &&
//                (fState != State.DOCKED) &&
//                (fState != State.IDLE) &&
//                (fState != State.MANUAL_FLIGHT) &&
//                (fState != State.ORBITING)) {
//            setAimIdle();
//            //state = State.IDLE;
////                aim = Aim.TO_IDLE;
//            Log.warn(de.dertroglodyt.iegcommon.module.DAShip.class, "init: Ship was on auto pilot but no destination resolved!");
//        }
//        for (DAbmHangar h : hangars) {
//            h.resolveDocked(n, this);
//        }
//        recalcThrusterTypes();
//    }
//
//    private void recalcThrusterTypes() {
//        synchronized(thruster) {
//            synchronized(engines) {
//                thruster.clear();
//                // sort out engines by type
//                for (DAbmPropulsion p : engines) {
//                    p.addListener(this);
//                    for (DAbmPropulsion.ThrusterType t : p.getThrusterType()) {
//                        DAVector<DAbmPropulsion> v = thruster.get(t);
//                        if (v == null) {
//                            v = new DAVector<DAbmPropulsion>(DAbmPropulsion.class);
//                            thruster.put(t, v);
//                        }
//                        v.add(p);
//                    }
//                }
//            }
//        }
////        // calculate maximum accelerations from auxiliary thrusters
////        DAVector<DAbmPropulsion> vp = getEngines(ThrusterType.Auxiliary);
////        if ((vp != null) && (vp.elementAt(0) != null)) {
////            // A = F / M
////            maxAuxThrusterTrans = vp.elementAt(0).getMaxThrustValue() / getMassKG();
////            Vector3d im = physical.getInertiaMoments().getVector3d();
////            // A = F * r / I
////            Point3d p1 = new Point3d();
////            Point3d p2 = new Point3d();
////            // get hull dimensions
////            getBoundingBox().getLower(p1);
////            getBoundingBox().getUpper(p2);
////            Vector3d r = new Vector3d(Math.abs(p1.x) + Math.abs(p2.x)
////                    , Math.abs(p1.y) + Math.abs(p2.y)
////                    , Math.abs(p1.z) + Math.abs(p2.z));
////            double ax = (im.x != 0.0) ? r.x * vp.elementAt(0).getMaxThrustValue() / im.x : 0.0;
////            double ay = (im.y != 0.0) ? r.y * vp.elementAt(0).getMaxThrustValue() / im.y : 0.0;
////            double az = (im.z != 0.0) ? r.z * vp.elementAt(0).getMaxThrustValue() / im.z : 0.0;
////            maxAuxThrusterRot = ((ax + ay + az) / 3.0);
////        } else {
//            maxAuxThrusterTrans = 0.0;
//            maxAuxThrusterRot = 0.0;
////        }
//    }
//
//    public double getAuxTrans() {
//        return maxAuxThrusterTrans;
//    }
//
//    public double getAuxRot() {
//        return maxAuxThrusterRot;
//    }
//
////    public void setShipState(State s) {
////        synchronized(state) {
////            state = s;
////            // Set Module state too
////            super.setOnline((state != State.OFFLINE));
////        }
////    }
//
////    /**
////     * Should not be used except from DAbmHangar.
////     * If previously offline:
////     * Switch on reactor, boot computer and put ship to IDLE.
////     * If previously online:
////     * Switch off all modules (like engines, weapons, etc.) and put ship to IDLE.
////     * @return
////     */
////    private DAResult setIdle() {
////        for (DAbmPropulsion p : getEngines()) {
////            p.setThrustValue(0);
////        }
////        for (DABasicModule b : getModules()) {
////            if (b instanceof DAbmWeapon) {
////                b.setOnline(false);
////            } else {
////                //TODO switch on computer
////                b.setOnline(true);
////            }
////        }
////        setShipState(State.IDLE);
////        return DAResult.createOK("ok", "DAmcShip.setIdle");
////    }
//
////    /**
////     * Should not be used except from DAbmHangar.
////     * Turn off all the lights...
////     * @return
////     */
////    public DAResult setOffline() {
////        for (DABasicModule b : getModules()) {
////            b.setOnline(false);
////        }
////        setShipState(State.OFFLINE);
////        return DAResult.createOK("ok", "DAmcShip.setOffline");
////    }
//
////    @Override
////    public void setOnline(boolean on) {
////        if (state != State.OFFLINE) {
////            DVCErrorHandler.raiseError(DAResult.createWarning("Was not offline!", "DAmcShip.setOnline"));
////        }
////        aim = Aim.TO_IDLE;
////    }
//
//    /**
//     * Used by DAbmHangar.
//     * Do NOT use directly to dock / undock.
//     * @param station
//     * @param resolving
//     * @return
//     */
//    public DAResult<?> setDockedTo(de.dertroglodyt.iegcommon.module.DAShip station, boolean resolving, DATransform newPosition) {
//        if (worldNode == null) {
//            Log.warn(de.dertroglodyt.iegcommon.module.DAShip.class, "setDockedTo: WorldNode not set in ship <" + this + ">. Cannot set dockedTo.");
//            return null;
//        }
//        clearAngularVelocity();
//        clearVelocity();
//        setFrom(newPosition);
////        if (station == null) {
////            return DAResult.createWarning("Station was null!", "DAmcShip.setDockedTo");
////        }
////        DAResult r = setIdle();
////        if (! r.isOK()) {
////            return r;
////        }
//        dockedTo = station;
//        if (getParent() != null) {
//            getParent().remove(this);
//        }
//        if (station != null) {
//            //setTransformParent(station.physical);
//            worldNode.removeShip(this);
////            station.add(this);
//            // Don't litter log on startup
//            if (! resolving) {
//                setState(State.DOCKED);
//            } else {
//                fState = State.DOCKED;
//            }
//    //        // abs pos of station
//    //        Vector3d v = getRootContainer().getAbsolutePos();
//    //        // Add module position in group
//    //        v.add(getPos());
//        } else {
//            //setTransformParent(worldNode.getAstroObjects());
//            worldNode.addShip(this);
////            worldNode.getAstroObjects().add(this);
//            // Don't litter log on startup
//            if (! resolving) {
//                setState(State.IDLE);
//            } else {
//                fState = State.IDLE;
//            }
//        }
//        // Don't litter log on startup
//        if (! resolving) {
//            if (station != null) {
//                fLog.add(station.getItemID(), new DAText("Angedockt an " + station.getName() + "."));
//            } else {
//                fLog.add(this.getItemID(), new DAText("Abgedockt."));
//            }
//        }
//        notifyListener(this);
//        return DAResult.createOK("ok", "DAShip.setDockedTo");
//    }
//
//    public de.dertroglodyt.iegcommon.module.DAShip getDockedTo() {
//        return dockedTo;
//    }
//
//    @Override
//    public DABasicModule.State getState() {
//        return (isOnline()?DABasicModule.State.ONLINE:DABasicModule.State.OFFLINE);
//    }
//
//    public State getShipState() {
//        return State.valueOf(fState.toString());
//    }
//
//    public DAmcShipClass.Type getType() {
//        return ((DAmcShipClass) wareClass).getType();
//    }
//
//    @Deprecated
//    @Override
//    public DAmcShipClass getWareClass() {
//        return (DAmcShipClass) wareClass;
//    }
//
//    @Override
//    public boolean isOnline() {
//        return (fState != State.OFFLINE);
//    }
//
//    public boolean isTravelling() {
//        return (fState == State.TRAVEL_APPROACH)
//                || (fState == State.TRAVEL_APPROACH_ACCEL)
//                || (fState == State.TRAVEL_APPROACH_ALIGN)
//                || (fState == State.TRAVEL_APPROACH_DEACCEL)
//                || (fState == State.TRAVEL_SOL_ACCEL)
//                || (fState == State.TRAVEL_SOL_ALIGN)
//                || (fState == State.TRAVEL_SOL_DEACCEL)
//                || (fState == State.TRAVEL_SOL_JUMP)
//                || (fState == State.TRAVEL_WARP_ACCEL)
//                || (fState == State.TRAVEL_WARP_ALIGN)
//                || (fState == State.TRAVEL_WARP_DEACCEL)
//                || (fState == State.TRAVEL_WARP_JUMP)
//                ;
//    }
//
//    public boolean isWarping() {
//        return (fState == State.TRAVEL_SOL_ACCEL)
//                || (fState == State.TRAVEL_SOL_ALIGN)
//                || (fState == State.TRAVEL_SOL_DEACCEL)
//                || (fState == State.TRAVEL_SOL_JUMP)
//                || (fState == State.TRAVEL_WARP_ACCEL)
//                || (fState == State.TRAVEL_WARP_ALIGN)
//                || (fState == State.TRAVEL_WARP_DEACCEL)
//                || (fState == State.TRAVEL_WARP_JUMP)
//                ;
//    }
//
//    public boolean isManuallFlight() {
//        return (fState == State.IDLE)
//                || (fState == State.MANUAL_FLIGHT)
//                || (fState == State.MANUAL_FLIGHT_FULL_STOP)
//                || (fState == State.MANUAL_FLIGHT_STOP_ROL)
//                || (fState == State.MANUAL_FLIGHT_STOP_STRAFE);
//    }
//
//    public DALocation getDestination() {
//        return destination;
//    }
//
//    public void setDestination(DALocation dest) {
//        destination = dest;
//    }
//
////    public void setDestination(DAUniqueID dest) {
////        destination = findOG(dest);
////        if (destination != null) {
////            destID = dest;
////        } else {
////            destID = null;
////        }
////        fLog.add(destID, new DAText("Ziel gesetzt: " + destination));
////    }
////
////    public void setDestination(DANewtonian dest) {
////        destination = dest;
////        if (destination != null) {
////            destID = dest.getItemID();
////        } else {
////            destID = null;
////        }
////        fLog.add(destID, new DAText("Ziel gesetzt: " + destination));
////    }
//
//    public boolean isPublicStation() {
//        return isPublicStation;
//    }
//
//    public void setPublicStation(boolean isPublic) {
//        isPublicStation = isPublic;
//        if (isPublicStation) {
//            fLog.add(this.getItemID(), new DAText("Schiff wurde als öffentliche Station deklariert."));
//        } else {
//            fLog.add(this.getItemID(), new DAText("Schiff ist keine öffentliche Station mehr."));
//        }
//    }
//
//    /**
//     * Dock a smaller ship at this one.
//     * @param ship
//     * @return
//     */
//    public DAResult dock(de.dertroglodyt.iegcommon.module.DAShip ship) {
//        if (ship.equals(this)) {
//            return DAResult.createFailed("Schiff kann nicht an sich selbst andocken!", "DAShip.dock");
//        }
//        if (ship.getDockedTo() != null) {
//            return DAResult.createFailed("Schiff ist bereits woanders angedockt!", "DAShip.dock");
//        }
//        if (hangars.isEmpty()) {
//            return DAResult.createFailed("Keine Hangars vorhanden!", "DAShip.dock");
//        }
//        synchronized(hangars) {
//            DAbmHangar hr = null;
//            for (DAbmHangar h : hangars) {
//                if (h.canDock(ship)) {
//                    hr = h;
//                    break;
//                }
//            }
//            if (hr == null) {
//                return DAResult.createFailed("Kein Hangarplatz verfügbar / in Andock-Reichweite.", "DAShip.dock");
//            }
//            DAResult r = hangars.elementAt(0).dock(ship);
//            if (!r.isOK()) {
//                return r;
//            }
//            // Players are placed inside station as passengers.
//            if (ship.getCaptain() != null) {
//                if (ship.getCaptain().isPlayer()) {
//                    addToPassenger(ship.getCaptain());
//                }
//            }
////            ship.setDockedTo(this);
//        }
//        notifyListener(this);
//        fLog.add(ship.getItemID(), new DAText("Neues Schiff angedockt: " + ship));
//        return DAResult.createOK("Schiff angedockt.", "DAShip.dock");
//    }
//
//    /**
//     * Undock a smaller ship from this one.
//     * @param ship
//     * @return
//     */
//    public DAResult undock(de.dertroglodyt.iegcommon.module.DAShip ship) {
//        if (ship.equals(this)) {
//            return DAResult.createFailed("Schiff kann nicht von sich selbst abdocken!", "DAShip.undock");
//        }
//        if (hangars.isEmpty()) {
//            return DAResult.createFailed("Keine Hangars vorhanden!", "DAShip.dock");
//        }
//        synchronized(hangars) {
//            DAbmHangar hr = null;
//            for (DAbmHangar h : hangars) {
//                if (h.isDocked(ship)) {
//                    hr = h;
//                    break;
//                }
//            }
//            if (hr == null) {
//                return DAResult.createFailed("Ship is not docked here.", "DAShip.undock");
//            }
//            DAResult r = hr.undock(ship);
//            if (!r.isOK()) {
//                return r;
//            }
////            ship.setGroupPosition(this.getGroupPosition());
////            ship.setDockedTo(null);
//        }
//        notifyListener(this);
//        fLog.add(ship.getItemID(), new DAText("Schiff aus Hangar abgedockt: " + ship));
//        return DAResult.createOK("Schiff abgedockt.", "DAShip.undock");
//    }
//
//    public DAResult<DAbmStorage> rentStorage(DAUniqueID leaserID) {
//        if (rentStorages == null) {
//            return new DAResult<DAbmStorage>("Keine Mietlagerräume vorhanden.", DAResult.ResultType.FAILED
//                    , null, "DAShip.rentStorage");
//        }
//        DAClan c = worldNode.getClan(leaserID);
//        if (c == null) {
//            return new DAResult<DAbmStorage>("Unbekannter clan.", DAResult.ResultType.FAILED
//                    , null, "DAShip.rentStorage");
//        }
//        synchronized(rentStorages) {
//            // does clan already have a storage here?
//            for (DAbmRentableStorage rs : rentStorages) {
//                DAbmStorage s = rs.getStorageByLeaser(leaserID);
//                if (s != null) {
////                    s.resolveOther(this);
//                    return new DAResult<DAbmStorage>("Vorhandener Mietlagerraum gefunden.", DAResult.ResultType.OK
//                        , s, "DAShip.rentStorage");
//                }
//            }
//            // rent a new one
//            for (DAbmRentableStorage rs : rentStorages) {
//                DAbmStorage s = rs.rentStorage(c);
//                if (s != null) {
////                    s.resolveOther(this);
//                    fLog.add(leaserID, new DAText("Lagerraum gemietet von: " + c));
//                    return new DAResult<DAbmStorage>("Neuer Lagerraum angemietet.", DAResult.ResultType.OK
//                        , s, "DAShip.rentStorage");
//                }
//            }
//            return new DAResult<DAbmStorage>("Keine Mietlagerräume mehr frei.", DAResult.ResultType.FAILED
//                    , null, "DAShip.rentStorage");
//        }
//    }
//
//    public void setOrbit(DAOrbit orbit, DANewtonian central) {
//        if (orbit != null) {
//            setState(State.ORBITING);
//            worldNode.removeShip(this);
//            fLog.add(central.getItemID(), new DAText("Orbit gesetzt um " + central + "."));
//        } else {
//            setState(State.IDLE);
//            worldNode.addShip(this);
//            fLog.add(central.getItemID(), new DAText("Orbit um " + central + " verlassen."));
//        }
//        notifyListener(this);
//    }
//
////    public void clearOrbit() {
////        if (worldNode == null) {
////            Log.warn(DAmcShip.class, "clearOrbit: WorldNode not set in ship <" + this + ">. Cannot clear Orbit.");
////            return;
////        }
////        if (central != null) {
////            fLog.add(central.getItemID(), new DAText("Orbit um " + central + " verlassen."));
////        }
////        physical.setOrbit(null, null);
////        centralID = null;
////        if (getParent() != null) {
////            getParent().remove(this);
////        }
////        worldNode.getAstroObjects().add(this);
////        setState(State.IDLE);
////    }
//
//    public DAClan getOwningClan() {
//        return owner;
//    }
//
//    public synchronized void setOwner(DAClan c) {
//        if (owner != null) {
//            owner.removeProperty(this);
//        }
//        owner = c;
//        ownerID = c.getUserID();
//        fLog.add(c.getUserID(), new DAText("Neuer Besitzer: " + c + "."));
//        if (owner != null) {
//            owner.addProperty(this);
//        }
//    }
//
////    /**
////     * Used by DAWorldNode.readExternal
////     * @param central
////     */
////    public void setOrbitParent(DASolarObjectGroup central) {
////        physical.setOrbitParent(central);
////    }
//
////    public DAOrbit getOrbit() {
////        if (worldNode.getStation().equals(this)) {
////            return worldNode.getOrbit();
////        } else {
////            return null;
////        }
////    }
//
////    public DANewtonian getOrbitCentral() {
////        return central;
////    }
//
////    public DASolarSystem getOrigin() {
////        return worldNode.getSolarSystem();
////    }
//
////    public DAObjectGroup getOrbitParent() {
////        return physical.getOrbitParent();
////    }
////
////    public DAUniqueID getOrbitParentID() {
////        return physical.getOrbitParentID();
////    }
//
//    public DAUniqueID getOwnerID() {
//        return ownerID;
//    }
//
//    public void createMarket(DAWorldNode worldNode) {
//        // market needs a storage!
//        if (storages.size() <= 0) {
//            JOptionPane.showMessageDialog(null, "No Storage found! Market needs a storage.");
//            return;
////            storages.add(new DAbmStorage(new DAbmStorageClass(DAWaresTree.GOODS
////                    , DAWareClass.Size.GIANT
////                    , "Riesige Lagerhalle", "Lagerhalle in Stationen."
////                    , new DAPVMass(10000000), DAWareClass.Size.GIANT.getMaxVolume()*0.9), ownerID));
//        }
//        if (market != null) {
//            throw new IllegalStateException();
//        }
//        market = new DAMarket(new DAText(toString() + " Market"));
//        synchronized(market) {
//            market.init(this);
//        }
//    }
//
//    public DAMarket getMarket() {
//        return market;
//    }
//
//    public DAVector<de.dertroglodyt.iegcommon.module.DAShip> getDockedShips(DAClan clan) {
//        DAVector<de.dertroglodyt.iegcommon.module.DAShip> v = new DAVector<de.dertroglodyt.iegcommon.module.DAShip>(de.dertroglodyt.iegcommon.module.DAShip.class);
//        for (DAbmHangar h : hangars) {
//            v.addAll(h.getDockedShips(clan));
//        }
//        return v;
//    }
//
////    public void setScriptListener(IDVCScriptChangeListener scl) {
////        if (scriptListener != null) {
////            synchronized(scriptListener) {
////                scriptListener = scl;
////            }
////        } else {
////            scriptListener = scl;
////        }
////        if (captain != null) {
////            captain.setScriptListener(scl);
////        }
////        for (DALogItem<DAPerson> p : crew) {
////            if (p.getData().getCharacter() != null) {
////                p.getData().getCharacter().setScriptListener(scl);
////            }
////        }
////        for (DALogItem<DAPerson> p : passengers) {
////            if (p.getData().getCharacter() != null) {
////                p.getData().getCharacter().setScriptListener(scl);
////            }
////        }
////        //notifyScriptListener();
////        notifyListener(this);
////    }
//
//    public void setCaptain(DACharacter val) {
//        captain = val;
//        if (val != null) {
//            captainID = val.getID();
//            de.dertroglodyt.iegcommon.module.DAShip lastVessel = val.getVessel();
//            if (lastVessel != null) {
//                DAResult r = lastVessel.remove(val);
//                if (! r.isOK()) {
//                    throw new IllegalStateException("setCaptain: Can not remove from last vessel. " + lastVessel);
//                }
//            }
//            // might have become NULL when removing from lastVessel
//            captain = val;
//            captainID = val.getID();
////            captain.setScriptListener(scriptListener);
//            val.setVessel(this);
//            addToCrew(val);
//            fLog.add(val.getID(), new DAText("Kapitän " + val + " an Bord."));
//        } else {
//            captainID = null;
//            fLog.add(null, new DAText("Kapitän " + val + " von Bord."));
//        }
//        //notifyScriptListener();
//        notifyListener(this);
//    }
//
//    public DACharacter getCaptain() {
//        return captain;
//    }
//
//    public DASet<DAPatent> getPrerequisites() {
//        return wareClass.getPrerequisites();
//    }
//
//    public DAVector<DAbmPropulsion> getEngines(ThrusterType type) {
//        return thruster.get(type);
//    }
//
//    public boolean hasThruster(DAbmPropulsion.ThrusterType type) {
//        DAVector<DAbmPropulsion> v = getEngines(type);
//        return ((v != null) && (v.size() > 0));
//    }
//
//    public DAResult setEngineSpeed(ThrusterType type, Speed speed) {
//        synchronized(thruster) {
////            if (! isManuallFlight()) {
////                return DAResult.createFailed("Not in manual flight mode!", "DAmcShip.setEngineSpeed");
////            }
//            if (! hasThruster(type)) {
//                if (speed != Speed.STOP) {
//                    return DAResult.createFailed("No thrusters of type <" + type + "> available.", "DAShip.setEngineSpeed");
//                } else {
//                    return DAResult.createOK("ok.", "DAShip.setEngineSpeed");
//                }
//            }
//            for (DAbmPropulsion p : getEngines(type)) {
//                p.setSpeed(speed);
//            }
//            //fLog.add(this.getItemID(), new DAText("Triebwerk " + type + " gesetzt auf " + speed + "."));
//            return DAResult.createOK("ok.", "DAShip.setEngineSpeed");
//        }
//    }
//
//    public void allEnginesOff() {
////        if (! isManuallFlight()) {
////            return;
////        }
//        for (ThrusterType tt : thruster.keySet()) {
//            setEngineSpeed(tt, Speed.STOP);
//        }
//        for (PresetType t : PresetType.values()) {
//            propulsionPreset.put(t, Preset.STOP);
//        }
//        fLog.add(this.getItemID(), new DAText("Alle Triebwerke aus."));
//    }
//
//    public DAResult incThrust(PresetType type) {
//        if (! isManuallFlight()) {
//            return DAResult.createFailed("Not in manual flight mode!", "DAShip.incThrust");
//        }
//        DAResult r = DAResult.createOK("ok", "DAShip.incThrust");
//        Preset p = propulsionPreset.get(type);
//        for (ThrusterType tt : type.getThrusterType(p.inc())) {
//            r = setEngineSpeed(tt, p.inc().toSpeed());
//            if (!r.isOK()) {
//                return r;
//            }
//        }
//        propulsionPreset.put(type, p.inc());
//        return r;
//    }
//
//    public DAResult decThrust(PresetType type) {
//        if (! isManuallFlight()) {
//            return DAResult.createFailed("Not in manual flight mode!", "DAShip.decThrust");
//        }
//        DAResult r = DAResult.createOK("ok", "DAShip.decThrust");
//        Preset p = propulsionPreset.get(type);
//        for (ThrusterType tt : type.getThrusterType(p.dec())) {
//            r = setEngineSpeed(tt, p.dec().toSpeed());
//            if (!r.isOK()) {
//                return r;
//            }
//        }
//        propulsionPreset.put(type, p.dec());
//        return r;
//    }
//
//    public EnumMap<PresetType, Preset> getThrustPreset() {
//        return propulsionPreset;
//    }
//
//    public DAClan resolveClan(DAUniqueID clanID) {
//        return worldNode.getClan(clanID);
////        DAParameterList pl = new DAParameterList();
////        pl.add(DAmcShip.Parameters.SHIPID, getItemID());
////        pl.add(DAmcShip.Parameters.CLANID, clanID);
////        DAResult<DAParameterList> r = worldNode.dispatchAction(worldNode.getOwningThread()
////                , new DARemoteAction(DARemoteAction.Type.SHIP
////                , getOwnerID(), DAmcShip.Action.RESOLVE_CLAN, pl), true);
////        if (r.isOK()) {
////            return (DAClan) r.getResult().getValue(DAmcShip.Parameters.CLAN.getName());
////        } else {
////            return null;
////        }
//    }
//
//    private DAResult<DAVector<DAbmWaresContainer>> loadCargo(DAVector<DAbmWaresContainer> wares
//            , DAUniqueID stoID) {
//        DAbmStorage sto = (DAbmStorage) getModule(stoID);
//        if (sto == null) {
//            return new DAResult<DAVector<DAbmWaresContainer>>("Storage not found!",
//                DAResult.ResultType.WARNING, null, "DAShip.handle");
//        }
//        return sto.add(wares);
//    }
//
//    private DAResult<DAVector<DAbmWaresContainer>> unloadCargo(DAVector<DAbmWaresContainer> wares,
//            DAUniqueID stoID) {
//        DAbmStorage sto = (DAbmStorage) getModule(stoID);
//        if (sto == null) {
//            return new DAResult<DAVector<DAbmWaresContainer>>("Storage not found!",
//                DAResult.ResultType.WARNING, null, "DAShip.handle");
//        }
//        return sto.remove(wares);
//    }
//
//    public DAResult<DAVector<DAbmWaresContainer>> transferCargo(DAVector<DAbmWaresContainer> wares
//            , DAUniqueID stoFromID, DAUniqueID stoToID, de.dertroglodyt.iegcommon.module.DAShip toShip) {
//        DAResult<DAVector<DAbmWaresContainer>> rf = unloadCargo(wares, stoFromID);
//        if (! rf.isOK()) {
//            // rollback partial remove
//            loadCargo(rf.getResult(), stoFromID);
//            return rf;
//        }
//        DAResult<DAVector<DAbmWaresContainer>> rt = toShip.loadCargo(wares, stoToID);
//        if (! rt.isOK()) {
//            // rollback partial add
//            loadCargo(rt.getResult(), stoFromID);
//            return rt;
//        }
//        fLog.add(toShip.getItemID(), new DAText("Waren verladen auf Schiff " + toShip + "."));
//        return rt;
//    }
//
//    public void setState(State s) {
//        fState = s;
//        fLog.add(this.getItemID(), new DAText("Status: " + s + "."));
//    }
//
//    public Aim getAim() {
//        return Aim.valueOf(fAim.toString());
//    }
//
//    public void setAimNone() {
//        fAim = Aim.NONE;
//        fLog.add(this.getItemID(), new DAText("Befehl: Keiner."));
//    }
//
//    public void setAimIdle() {
//        if (fAim != Aim.TO_IDLE_2) {
//            fAim = Aim.TO_IDLE;
//            fLog.add(this.getItemID(), new DAText("Befehl: Alle Module auf Bereitschaft..."));
//        }
//    }
//
//    public void setAimOffline() {
//        fAim = Aim.TO_OFFLINE;
//        fLog.add(this.getItemID(), new DAText("Befehl: Alle Module aus..."));
//    }
//
//    public void setAimUndock() {
//        fAim = Aim.UNDOCK;
//        fLog.add(this.getItemID(), new DAText("Befehl: Abdocksequenz einleiten..."));
//    }
//
//    public DALocation getLocation() {
//        return new DALocation(this);
//    }
//
//    public DAWorldNode getWorldNode() {
//        return worldNode;
//    }
//
//    public void alignTo(DAVector3d<Dimensionless> dir) {
//        if (isWarping()) {
//            return;
//        }
//        fAim = Aim.ALIGN_TO;
//        rcs.presetHeading(dir);
//        fLog.add(this.getItemID(), new DAText("Befehl: Ausrichten nach " + dir + "..."));
//    }
//
//    public void alignTo(DALocation loc) {
//        fLog.add(this.getItemID(), new DAText("Befehl: Ausrichten auf " + loc + "..."));
//        alignTo(getLocation().getDirection(loc));
//    }
//
//    public void alignToDestination() {
//        if (isWarping() || (destination == null)) {
//            return;
//        }
//        rcs.presetHeading(getLocation().getDirection(destination));
//        fAim = Aim.ALIGN_TO;
//        fLog.add(this.getItemID(), new DAText("Befehl: Ausrichten auf Ziel..."));
//    }
//
//    private DALocation findOG(DAUniqueID ogID) {
//        DALocation dest = null;
//        if (ogID != null) {
//            DANewtonian newt = worldNode.getSolarSystem().get(ogID);
//            if (newt != null) {
//                dest = newt.getLocation();
//            } else {
//                de.dertroglodyt.iegcommon.module.DAShip sh = worldNode.getShip(ogID);
//                if (sh != null) {
//                    dest = sh.getLocation();
//                }
//            }
//        }
//        return dest;
//    }
//
//    public void flyToDestination(DAUniqueID destID) {
//        DALocation dest = findOG(destID);
//        if (dest == null) {
//            Log.warn(de.dertroglodyt.iegcommon.module.DAShip.class, "flyToDestination: " + this + ": Destination ID unknown <" + destID + ">");
//            return;
//        }
//        flyToDestination(dest);
//    }
//
//    public void flyToDestination(DALocation loc) {
//        setDestination(loc);
//        fLog.add(loc.getWorldNodeID(), new DAText("Befehl: Ziel " + loc + " anfliegen..."));
//        flyToDestination();
//    }
//
//    public void flyToDestination() {
//        fAim = Aim.FLY_TO;
//        fLog.add(this.getItemID(), new DAText("Befehl: Ziel anfliegen..."));
//    }
//
////    public void placeAt(DAUniqueID destID) {
////        DANewtonian dest = findOG(destID);
////        if (dest == null) {
////            Log.warn(DAmcShip.class, "placeAt: " + this + ": Destination ID unknown <" + destID + ">");
////            return;
////        }
////
////        physical.setOrbit(null, null);
////        if (getParent() != null) {
////            getParent().remove(this);
////        }
////        Vector3d v = null;
////        physical.setVelocity(dest.getVelocity());
////        v = dest.getAbsolutePos();
////        destination = null;
////        try {
////            v.x += dest.getMaxDist().getBaseValue() * 3.0;
////        } catch (Exception e) {
////            v.x += 100;
////        }
////        setPosition(v);
////        DVCErrorHandler.createDebug(toString() + " NewPosition: " + dest, "DAShip.placeAt");
////        fLog.add(destID, new DAText("Schiff plaziert bei " + dest + "."));
////    }
//
//    public void stopRoll() {
//        if (isWarping()) {
//            return;
//        }
//        fAim = Aim.STOP_ROLL;
//        fLog.add(this.getItemID(), new DAText("Befehl: Stoppe Rollbewegung..."));
//    }
//
//    public void stopStopStrafe() {
//        if (isWarping()) {
//            return;
//        }
//        fAim = Aim.STOP_STRAFE;
//        fLog.add(this.getItemID(), new DAText("Befehl: Stoppe Translation..."));
//    }
//
//    public void fullStop() {
//        fAim = Aim.FULL_STOP;
//        fLog.add(this.getItemID(), new DAText("Befehl: Kompletter Stopp..."));
//    }
//
//    @Override
//    public synchronized void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
//        super.longTick(actWorldTime, t);
//        fLog.add(this.getItemID(), new DAText("LongTick."));
//        if (market != null) {
//            synchronized(market) {
//                market.longTick(actWorldTime);
//            }
//        }
//    }
//
////    /**
////     * Calculates distance of point to plane.
////     * @param x point
////     * @param p vector to plane
////     * @param n normalized(!) vector orthogonal on plane
////     * @return distance to given plane
////     */
////    private double distToPlane(Vector3d x, Vector3d p, Vector3d n) {
////        // E: n * (x - p) = 0  (Hessesche Normalenform der Ebene)
////        // d = n * (x - p)
////        return (x.x - p.x) * n.x + (x.y - p.y) * n.y + (x.z - p.z) * n.z;
////    }
////
////    private void collide(DAObjectGroup og) {
////        Vector3d sogPos = og.getAbsolutePos();
////        // line between the two mass centers of the objects
////        // from this ship to object
////        Vector3d vla = getAbsolutePos();
////        vla.sub(sogPos);
////        // Vector of force on this line
////        vla.normalize();
////        if (Double.isNaN(vla.x) || Double.isNaN(vla.y) || Double.isNaN(vla.z)) {
////            return;
////        }
////        if ((vla.length() != 0.0) && (getNextVelo().length() !=  0.0)) {
////            // scale by fraction of velocity along this line
////            vla.scale(getNextVelo().length() * Math.cos(vla.angle(getNextVelo())));
////        }
////
////        // same from other object
////        Vector3d vlb = new Vector3d(sogPos);
////        vlb.sub(getAbsolutePos());
////        // Vector of force on this line
////        vlb.normalize();
////        if (Double.isNaN(vlb.x) || Double.isNaN(vlb.y) || Double.isNaN(vlb.z)) {
////            return;
////        }
////        double f = og.getNextVelo().length();
////        if ((vlb.length() != 0.0) && (f !=  0.0)) {
////            // scale by fraction of velocity along this line
////            vlb.scale(f * Math.cos(vlb.angle(og.getNextVelo())));
////        }
////
////        // Stosszahl k
////        // k = 0 -> Knete
////        // k = 1 -> Stahl
////        double k = 0.5;  // choosen arbitrarily
////        // v1' = (m1*v1 + m2*v2 - m2(v1-v2)*k) / (m1+m2)
////        // v2' = (m1*v1 + m2*v2 - m1(v2-v1)*k) / (m1+m2)
////
////        // resulting velocity vectors
////        double m1 = getMassKG();
////        double m2 = og.getMassKG();
////        if (m1 + m2 == 0.0) {
////            // cant compute for two non-masses
////            return;
////        }
////        double v1 = vla.length();
////        double v2 = vlb.length();
////        Vector3d vra = new Vector3d(vla);
////        vra.normalize();
////        if (Double.isNaN(vra.x) || Double.isNaN(vra.y) || Double.isNaN(vra.z)) {
////            return;
////        }
////        vra.scale((m1*v1 + m2*v2 - m2*(v1-v2)*k) / (m1+m2));
////        Vector3d vrb = new Vector3d(vlb);
////        vrb.normalize();
////        if (Double.isNaN(vrb.x) || Double.isNaN(vrb.y) || Double.isNaN(vrb.z)) {
////            return;
////        }
////        vrb.scale((m1*v1 + m2*v2 - m1*(v2-v1)*k) / (m1+m2));
////
////        // total velocity along line
////        Vector3d vges = new Vector3d(vla);
////        vges.sub(vlb);
////        // total absorbed kinetic impact energy
////        // == kinetic energy converted into deformational energy
////        double ke = (1 - (k*k)) * vges.length() * vges.length()
////                * (m1 * m2 / (2.0 * (m1 + m2)));
////        DADoubleFloat e = new DADoubleFloat(ke, KnownUnit.J);
////        DADamage d = new DADamage(DADamage.NULL_ENERGY, e, DADamage.NULL_ENERGY);
////
////        // for each module:
////        // apply damage until energy is used up or all modules destroyed
////        // repeat:
////        // find two nearest modules that are not destroyed and apply damage
////        // nearest -> smallest distance (can be negativ!) from touching plane
////
////        // Verbindunglinie zwischen den beiden massezentren
////        Vector3d ef = getAbsolutePos();
////        ef.sub(sogPos);
////        // normalenvektor der ebene
////        Vector3d en = new Vector3d(ef);
////        en.normalize();
////        // fusspunkt der ebene
////        ef.scale(0.5);
////        ef.add(getAbsolutePos());
////
////        while (! d.isZero()) {
////            // find nearest modul A thats not destroyed
////            DABasicModule bmA = null;
////            double minDist = 1e10;
////            for (DABasicModule bm : allModules) {
////                // for each module calculate distance to touching plane
////                if (! bm.isDestroyed()) {
////                    double dist = distToPlane(bm.getAbsolutePos(), ef, en);
////                    if (dist < minDist) {
////                        minDist = dist;
////                        bmA = bm;
////                    }
////                }
////            }
////            if (bmA == null) {
////                break;
////            }
////            if (d.isZero()) {
////                break;
////            }
////            // apply damage to A decrementing damage energy
////            bmA.applyDamage(d);
////            // TODO: apply force vector B for rotation
////
////            // if other object is ship:
////            if (og instanceof DAmcShip) {
////                // find nearest modul B thats not destroyed
////                DABasicModule bmB = null;
////                minDist = 1e10;
////                for (DABasicModule bm : allModules) {
////                    // for each module calculate distance to touching plane
////                    if (! bm.isDestroyed()) {
////                        double dist = distToPlane(bm.getAbsolutePos(), ef, en);
////                        if (dist < minDist) {
////                            minDist = dist;
////                            bmB = bm;
////                        }
////                    }
////                }
////                if (bmB == null) {
////                    break;
////                }
////                // apply damage to B decrementing damage energy
////                bmB.applyDamage(d);
////                // TODO: apply force vector A for trans and rot
////            }
////        }
////        // apply resulting velocity vector to both objects
////        physical.setVelocity(new DAPVVelocity(vra, KnownUnit.m_s
////                , DVCCoordinateSystem.Type.CARTESIAN_RIGHT_HANDED_XZ));
////        if (og instanceof DAmcShip) {
////            og.setVelocity(new DAPVVelocity(vra, KnownUnit.m_s
////                , DVCCoordinateSystem.Type.CARTESIAN_RIGHT_HANDED_XZ));
////        }
////    }
//
//    @Override
//    public synchronized void preTick(DAValue<Duration> t) {
//        // add gravitational forces to ship
////            gravitate(planet);
//        // apply forces from modules
//        super.preTick(t);
//
////        // collision handling
////        // calculate what new position would be without collision
////        physical.preTick(t, new Vector3d(maxAuxThrusterTrans, maxAuxThrusterTrans, maxAuxThrusterTrans)
////                , new Vector3d(maxAuxThrusterRot, maxAuxThrusterRot, maxAuxThrusterRot));
////        // ask world node if colliding with something
////        Object o = worldNode.getColliding(this);
////        if (o != null) {
////            DVCErrorHandler.createDebug(this + " colliding with " + o, "DAShip.preTick");
////            if (o instanceof DAmcShip) {
////                collide((DAmcShip) o);
////            } else {
////                if (o instanceof DAObjectGroup) {
////                    collide((DAObjectGroup) o);
////                } else {
////                    DVCErrorHandler.raiseError(DAResult.createWarning("Unknown collision object! <" + o + ">"
////                            , "DAShip.preTick"));
////                }
////            }
////        }
//
//        switch (fAim) {
//            case NONE: {  // Manual flight
//                if (isWarping()) {
//                    // todo
//                    // stop warp engine
//                    fLog.add(this.getItemID(), new DAText("Befehl: KEINER. Todo: Stoppe warp."));
//                }
//                break;
//            }
//            case TO_IDLE: {
//                super.setOnline(true);
//                for (DABasicModule b : getModules()) {
//                    if (b instanceof DAbmWeapon) {
//                        b.setOnline(false);
//                    } else {
//                        if (b.getState() == DABasicModule.State.OFFLINE) {
//                            b.setOnline(true);
//                        }
//                    }
//                }
//                fAim = Aim.TO_IDLE_2;
//                break;
//            }
//            case TO_IDLE_2: {
//                boolean ison = true;
//                for (DABasicModule b : getModules()) {
//                    if (b.getState() == DABasicModule.State.GOING_ONLINE) {
//                        ison = false;
//                        break;
//                    }
//                }
//                if (ison) {
//                    setAimNone();
////                    aim = Aim.NONE;
//                    if (fState != State.DOCKED) {
//                        setState(State.IDLE);
//                    }
//                    fLog.add(this.getItemID(), new DAText("Alle Maschinen sind betriebsbereit."));
//                }
//                break;
//            }
//            case TO_OFFLINE: {
//                boolean isoff = true;
//                for (DABasicModule b : getModules()) {
//                    if ((b.getState() == DABasicModule.State.ONLINE)
//                            || (b.getState() == DABasicModule.State.ERROR)) {
//                        b.setOnline(false);
//                    }
//                    if (b.getState() == DABasicModule.State.GOING_OFFLINE) {
//                        isoff = false;
//                    }
//                }
//                if (isoff) {
//                    setAimNone();
////                    aim = Aim.NONE;
//                    setState(State.OFFLINE);
//                    super.setOnline(false);
//                    fLog.add(this.getItemID(), new DAText("Alle Maschinen sind aus."));
//                }
//                break;
//            }
//            case UNDOCK: {
////                // align to undock point of hangar
////                // todo
//////                if (! alignedTo(destination)) {
//////                    fLog.add(this.getItemID(), new DAText("TODO: Ausrichten auf Abdockposition."));
//////                    alignTo();
//////                } else {
////                    // Undock point reached? (dist < 20m)
////                    Vector3d v = getDestinationPos();
////                    v.sub(getAbsolutePos());
////                    if (v.length() < 20) {
////                        setAimNone();
//////                        aim = Aim.ALIGN_TO.NONE;
////                        setState(DAmcShip.State.IDLE);
////                        fLog.add(this.getItemID(), new DAText("Abdockposition erreicht."));
////                        break;
////                    }
////                    // Start engines to accelerate if necessary
////                    if (getVelocity().isSmallerThan(DAbmHangar.UNDOCK_VELO)) {
////                        DAResult r = setEngineSpeed(ThrusterType.StrafeForth, Speed.HALF);
////                        if (! r.isOK()) {
////                            DVCErrorHandler.raiseError(r);
////                            setAimNone();
//////                            aim = Aim.ALIGN_TO.NONE;
////                            setState(DAmcShip.State.IDLE);
////                            break;
////                        }
////                    } else {
////                        DAResult r = setEngineSpeed(ThrusterType.StrafeForth, Speed.STOP);
////                    }
//////                }
//                fly_to();
//                break;
//            }
//            case DOCK: {
//                // todo
//                break;
//            }
//            case ALIGN_TO: {
//                // should be done by RCS
//                break;
//            }
//            case FLY_TO: {
//                fly_to();
//                break;
//            }
//            case FULL_STOP: {
//                // todo
//                break;
//            }
//            case STOP_ROLL: {
//                // todo
//                break;
//            }
//            case STOP_STRAFE: {
//                // todo
//                break;
//            }
//        }
//
//        if (fState != State.MANUAL_FLIGHT) {
//            DAResult r= rcs.tick(t);
//            if (! r.isOK()) {
//                fLog.add(this.getItemID(), new DAText(toString() + " FEHLER in RCS: " + r.toString()));
//                super.setErrorState(r);
//                setAimNone();
//                setState(de.dertroglodyt.iegcommon.module.DAShip.State.IDLE);
////                aim = Aim.NONE;
//            }
//        }
//    }
//
////    /**
////     * Finally apply all forces to the object and calculate new position and velocity
////     * @param t
////     */
////    @Override
////    public synchronized void postTick(DAValue<Duration> t) {
////        physical.postTick(t, new Vector3d(maxAuxThrusterTrans, maxAuxThrusterTrans, maxAuxThrusterTrans)
////                , new Vector3d(maxAuxThrusterRot, maxAuxThrusterRot, maxAuxThrusterRot));
////    }
//
//    public synchronized void addLog(DAUniqueID sourceID, DAText message) {
//        fLog.add(sourceID, message);
//    }
//
//    public DALog getLog() {
//        return fLog;
//    }
//
////    @Override
////    public DVCDataEditor getEditor(EditMode editmode, DVCAbstractUser user) {
////        DVCDataEditor de;
////        if (editmode == EditMode.ADMINMODE) {
////            de = new DVCseShipAdmin(this, editmode, user);
////        } else {
////            de = new DVCseShip(this, editmode, user);
////        }
////        addListener(de);
////        return de;
////    }
////
////    public static DVCseModuleContainer getParentEditor(DAmcShip model, EditMode editmode, DVCAbstractUser user) {
////        DVCseModuleContainer de = new DVCseModuleContainer((DAModuleContainer) model, editmode, user);
////        model.addListener(de);
////        return de;
////    }
//
//    @Override
//    public void changeNotify(IDataAtom sender) {
//        if (sender instanceof DAbmPropulsion) {
//            DAbmPropulsion p = (DAbmPropulsion) sender;
//            // if thruster type was changed... we do not know old type to remove it.
//            // so just rebuild the map.
//            for (ThrusterType tt : p.getThrusterType()) {
//                if (thruster.containsKey(tt)) {
//                    synchronized(thruster) {
//                        recalcThrusterTypes();
//                    }
//                    break;
//                }
//            }
//        }
//    }
//
////    @Override
////    public synchronized DAResult<DAParameterList> handle(DAOwningThread thread
////            , DARemoteAction action, boolean sourceIsServer) {
////        if (action.getSender() == DARemoteAction.RemoteObject.MARKET) {
////            if (market != null) {
////                return market.handle(thread, action, sourceIsServer);
////            } else {
////                return new DAResult<DAParameterList>("Market is NULL! <" + action + ">!",
////                    DAResult.ResultType.WARNING, null, "DAShip.handle");
////            }
////        }
////        if (action.getSender() != DARemoteAction.Type.SHIP) {
////            return new DAResult<DAParameterList>("Unknown action sender <" + action + ">!",
////                    DAResult.ResultType.WARNING, null, "DAShip.handle");
////        }
////        Action type = Action.valueOf(action.getName().toString());
////        if (type == null) {
////            return new DAResult<DAParameterList>("Unknown action type <" + action.getName() + ">!",
////                    DAResult.ResultType.WARNING, null, "DAShip.handle");
////        }
////        DAParameterList pl = new DAParameterList();
////        DAParameterList parms = action.getParameters();
////        DAUniqueID id = (DAUniqueID) parms.get(Parameters.SHIPID);
////        if (!id.equals(this.getItemID())) {
////            return new DAResult<DAParameterList>("Wrong channeled action <" + action + ">!",
////                    DAResult.ResultType.WARNING, null, "DAShip.handle");
////        }
//////        DAUniqueID capID = (DAUniqueID) parms.get(Parameters.CAPTAINID);
//////        if ((type != Action.GET_STUB) && ((capID == null) || (! capID.equals(captainID)))) {
//////            return new DATypedResult<DAParameterList>(DAResult.createFailed("Not allowed! You are not the cpatain of this vessel.", "DAmcShip.handle"));
//////        }
////        switch (type) {
////            case GET_STUB: {
////                //DAShip cc = new DAmcShip(getWareClass(), getItemName(), getItemID(), scriptListener);
////                pl.add(Parameters.SHIP, new DAmcShip(wareClass, getName(), getItemID(), scriptListener));
////                break;
////            }
////            case SET_NAME: {
////                try {
////                    DAText n = (DAText) parms.get(Parameters.NAME);
////                    if (n == null) {
////                        return new DAResult<DAParameterList>(DAResult.createFailed("Failed.", "DAShip.handle"));
////                    }
////                    setName(n);
////                } catch (Exception e) {
////                    return new DAResult<DAParameterList>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                break;
////            }
////            case GET_DOCKED_TO: {
////                if (dockedTo != null) {
////                    pl.add(Parameters.SHIP, dockedTo);
////                    return new DATypedResult<>("ok",
////                            DAResult.ResultType.OK, pl, "DAShip.handle");
////                } else {
////                    return new DATypedResult<>("Not docked!",
////                            DAResult.ResultType.OK, pl, "DAShip.handle");
////                }
////                //break;
////            }
////            case GET_DOCKED_SHIPS: {
////                DAUniqueID clanID = (DAUniqueID) parms.get(Parameters.CLANID);
////                if (clanID == null) {
////                    return new DATypedResult<>("No clan given!",
////                            DAResult.ResultType.OK, pl, "DAShip.handle");
////                }
////                DAClan c = worldNode.getClan(clanID);
////                if (c == null) {
////                    return new DATypedResult<>("Unknown clan!",
////                            DAResult.ResultType.OK, pl, "DAShip.handle");
////                }
////                pl.add(Parameters.SHIPLIST, getDockedShips(c));
////                return new DATypedResult<>("ok",
////                        DAResult.ResultType.OK, pl, "DAShip.handle");
////                //break;
////            }
////            case GET_STORAGES: {
////                pl.add(Parameters.STORAGELIST, getStorages());
////                return new DATypedResult<>("ok",
////                        DAResult.ResultType.OK, pl, "DAShip.handle");
////                //break;
////            }
////            case PLACE_AT: {
////                try {
////                    DAUniqueID did = (DAUniqueID) parms.get(Parameters.DESTINATION_ID);
////                    if (did == null) {
////                        return new DATypedResult<>(DAResult.createFailed("Failed. No destination parameter!", "DAShip.handle"));
////                    }
////                    placeAt(did);
////                    setAimIdle();
//////                    aim = Aim.TO_IDLE;
////                    return new DATypedResult<>("OK", DAResult.ResultType.OK, pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case SET_DESTINATION: {
////                try {
////                    DAUniqueID did = (DAUniqueID) parms.get(Parameters.DESTINATION_ID);
////                    if (did == null) {
////                        return new DATypedResult<>(DAResult.createFailed("Failed. No destination parameter!", "DAShip.handle"));
////                    }
////                    setDestination(did);
////                    return new DATypedResult<>("OK", DAResult.ResultType.OK, pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case FLY_TO_DEST: {
////                try {
////                    flyToDestination();
////                    return new DATypedResult<>("OK", DAResult.ResultType.OK, pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case DOCK: {
////                try {
////                    DAUniqueID sid = (DAUniqueID) parms.get(Parameters.DOCKINGSHIPID);
////                    DAmcShip s = worldNode.getVessel(sid);
////                    if (s == null) {
////                        return new DATypedResult<>(DAResult.createFailed("Failed. No ship parameter!", "DAShip.handle"));
////                    }
////                    // todo
//////                    s.getMeta(dvc)
//////                    if (! in andock-reichweite) {
//////                        aim = Aim.DOCK;
//////                        return new DATypedResult<DAParameterList>(
//////                                DAResult.createFailed("Not in docking range! Aproaching target...", "DAmcShip.handle"));
//////                    }
////                    DAResult r = dock(s);
////                    if (r.isOK()) {
////                        setAimIdle();
//////                        aim = Aim.TO_IDLE;
////                    }
////                    return new DATypedResult<>(r.getMessage(),
////                        r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case UNDOCK: {
////                try {
////                    DAUniqueID sid = (DAUniqueID) parms.get(Parameters.DOCKINGSHIPID);
////                    DAmcShip s = worldNode.getVessel(sid);
////                    if (s == null) {
////                        return new DATypedResult<>(DAResult.createFailed("Failed. No ship parameter!", "DAShip.handle"));
////                    }
////                    DAResult r = undock(s);
//////                    if (r.isOK()) {
//////                        setAimIdle();
//////                        aim = Aim.UNDOCK;
//////                    }
////                    return new DATypedResult<>(r.getMessage(),
////                        r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case SET_THRUST: {
////                try {
////                    ThrusterType tt = ThrusterType.valueOf(((DAText) parms.get(Parameters.THRUSTER_TYPE)).toString());
////                    Speed ts = Speed.valueOf(((DAText) parms.get(Parameters.THRUSTER_TYPE)).toString());
////                    DAResult r = setEngineSpeed(tt, ts);
////                    return new DATypedResult<>(r.getMessage(),
////                        r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case ALL_ENGINES_OFF: {
////                try {
////                    allEnginesOff();
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                break;
////            }
////            case INC_THRUST: {
////                try {
////                    PresetType pt = PresetType.valueOf(((DAText) parms.get(Parameters.PRESET_TYPE)).toString());
////                    DAResult r = incThrust(pt);
////                    return new DATypedResult<>(r.getMessage(),
////                        r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case DEC_THRUST: {
////                try {
////                    PresetType pt = PresetType.valueOf(((DAText) parms.get(Parameters.PRESET_TYPE)).toString());
////                    DAResult r = decThrust(pt);
////                    return new DATypedResult<>(r.getMessage(),
////                        r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case RENT_STORAGE: {
////                try {
////                    DAUniqueID clanID = (DAUniqueID) parms.get(Parameters.CLANID);
////                    DATypedResult<DAbmStorage> r = rentStorage(clanID);
////                    if ((! r.isOK()) || (r.getResult() == null)) {
////                        return new DATypedResult<>(r);
////                    }
////                    pl.add(Parameters.STORAGE, r.getResult());
////                    return new DATypedResult<>(r.getMessage(),
////                        r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case UNPACK: {
////                try {
////                    DAUniqueID stoID = ((DAbmStorage) parms.get(Parameters.STORAGE)).getItemID();
////                    DAbmStorage sto = (DAbmStorage) getModule(stoID);
////                    if (sto == null) {
////                        return new DATypedResult<>("Storage not found!",
////                            DAResult.ResultType.FAILED, pl, "DAShip.handle");
////                    }
////                    DAUniqueID wcID = ((DAbmWaresContainer) parms.get(Parameters.WARES_CONTAINER)).getItemID();
////                    DAbmWaresContainer wc = sto.getContainer(wcID);
////                    if (wc == null) {
////                        return new DATypedResult<>("Wares container not found!",
////                            DAResult.ResultType.FAILED, pl, "DAShip.handle");
////                    }
////                    DATypedResult<DAmcShip> r = unpack(sto, wc);
////                    if (! r.isOK()) {
////                        return new DATypedResult<>(r.getMessage(),
////                            r.getResultType(), pl, "DAShip.handle");
////                    }
////                    pl.add(Parameters.SHIP, r.getResult());
////                    return new DATypedResult<>("ok",
////                        DAResult.ResultType.OK, pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            case TRANSFER_CARGO: {
////                try {
////                    DAUniqueID stoFromID = (DAUniqueID) parms.get(Parameters.STORAGE_FROM_ID);
////                    DAUniqueID stoToID = (DAUniqueID) parms.get(Parameters.STORAGE_TO_ID);
////                    DAUniqueID stoToShipID = (DAUniqueID) parms.get(Parameters.STORAGE_TOSHIP_ID);
////                    DAmcShip ts = worldNode.getVessel(stoToShipID);
////                    if (ts == null) {
////                        return new DATypedResult<>("Unknown target ship!",
////                            DAResult.ResultType.FAILED, pl, "DAShip.handle");
////                    }
////                    DAVector<DAbmWaresContainer> wc = (DAVector<DAbmWaresContainer>) parms.get(Parameters.CARGOLIST);
////                    if (wc == null) {
////                        return new DATypedResult<>("Wares ware not found!",
////                            DAResult.ResultType.FAILED, pl, "DAShip.handle");
////                    }
////                    DAResult r = transferCargo(wc, stoFromID, stoToID, ts);
////                    return new DATypedResult<>(r.getMessage()
////                            , r.getResultType(), pl, "DAShip.handle");
////                } catch (Exception e) {
////                    return new DATypedResult<>(DAResult.createFailed(e.toString(), "DAShip.handle"));
////                }
////                //break;
////            }
////            default: {
////                return new DATypedResult<>("Unknown action type <" + action.getName() + ">!",
////                    DAResult.ResultType.WARNING, null, "DAShip.handle");
////            }
////        }
////        return new DATypedResult<>("ok",
////                DAResult.ResultType.OK, pl, "DAShip.handle");
////    }
//
//    public DAResult<de.dertroglodyt.iegcommon.module.DAShip> unpack(DAbmStorage sto, DAbmWaresContainer wc) {
//        if (sto == null) {
//            return new DAResult<de.dertroglodyt.iegcommon.module.DAShip>("Unknown storage!",
//                DAResult.ResultType.WARNING, null, "DAShip.unpack");
//        }
//        if (wc == null) {
//            return new DAResult<de.dertroglodyt.iegcommon.module.DAShip>("Unknown WaresContainer!",
//                DAResult.ResultType.WARNING, null, "DAShip.unpack");
//        }
//        // find empty hangar that can handle ship
//        DAbmHangar st = null;
//        for (DAbmHangar h : getHangars()) {
//            if (h.canDock((de.dertroglodyt.iegcommon.module.DAShip) wc.getContent().getWare())) {
//                st = h;
//                break;
//            }
//        }
//        if (st == null) {
//            return new DAResult<de.dertroglodyt.iegcommon.module.DAShip>("No empty hangar found for ship type!",
//                DAResult.ResultType.WARNING, null, "DAShip.unpack");
//        }
//        DAResult<de.dertroglodyt.iegcommon.module.DAShip> rs = st.unpackShip(sto, wc, worldNode);
//        // delete container if empty
//        if (wc.getActualAmount().doubleValue() <= 0) {
//            sto.remove(wc);
//        }
//        if (! rs.isOK()) {
//            return new DAResult<de.dertroglodyt.iegcommon.module.DAShip>(rs);
//        }
//        de.dertroglodyt.iegcommon.module.DAShip s = rs.getResult();
//        if (s == null) {
//            return new DAResult<de.dertroglodyt.iegcommon.module.DAShip>("Ship is null!",
//                DAResult.ResultType.WARNING, null, "DAShip.unpack");
//        }
//        dock(s);
//        return new DAResult<de.dertroglodyt.iegcommon.module.DAShip>("ok", DAResult.ResultType.OK, s, "DAShip.unpack");
//    }
//
//    private void fly_to() {
//        // TODO
////        if ((! isTravelling()) && (fState != State.IDLE)) {
////            fLog.add(this.getItemID(), new DAText("Fehler bei Zielanflug: Schiff ist nicht bereit. Gehe in Bereitschaft."));
////            Log.debug(DAmcShip.class, "fly_to: " + this + " FlyTo: Ship not ready. Going to idle mode...");
////            setAimIdle();
////            return;
////        }
////        if (destination == null) {
////            fLog.add(this.getItemID(), new DAText("Fehler bei Zielanflug: Kein Ziel gesetzt. Gehe in Bereitschaft."));
////            Log.debug(DAmcShip.class, "fly_to: " + this + " FlyTo: No destination set..."
////                    , "DAShip.fly_to");
////            setAimIdle();
//////            aim = Aim.TO_IDLE;
////            return;
////        }
////        // Leave orbit...
////        if (physical.getOrbit() != null) {
//////            Vector3d actPos = getGroupPosition();
//////            actPos.add(physical.getOrbitParent().getAbsolutePos());
//////            setGroupPosition(actPos);
//////            physical.setOrbit(null, null, this);
////            physical.setOrbit(null, null);
//////            this.getParent().remove(this);
////            fLog.add(this.getItemID(), new DAText("Zielanflug: Verlasse Orbit..."));
////            Log.debug(DAmcShip.class, "fly_to: " + this + " Leaving orbit..."
////                    , "DAShip.fly_to");
////        }
////        if ((fState != State.TRAVEL_SOL_ACCEL) &&
////                (fState != State.TRAVEL_SOL_ALIGN) &&
////                (fState != State.TRAVEL_SOL_DEACCEL) &&
////                (fState != State.TRAVEL_SOL_JUMP)) {
////            setState(State.TRAVEL_SOL_ALIGN);
////        }
////        DAValue<Velocity> v = physical.getVelocity();
////        DAVector3d<Dimensionless> dist = getLocation().getDirection(destination.getLocation());
////        double r = 3 * Math.max(this.getMaxDist().getBaseValue(), destination.getMaxDist().getBaseValue());
////        fLog.add(this.getItemID(), new DAText("Zielanflug: Entfernung " + dist.length() + "m."));
////        if (dist.length() < r) {
////            v.set(new Vector3d(), DVCCoordinateSystem.Type.CARTESIAN_RIGHT_HANDED_XZ);
////            fLog.add(this.getItemID(), new DAText("Zielanflug: Ziel erreicht. Gehe in Bereitschaft."));
////            Log.debug(DAmcShip.class, "fly_to: " + this + " Dest: " + destination + " reached.", "DAShip.fly_to");
////            destination = null;
////            setAimIdle();
//////            aim = Aim.TO_IDLE;
////            return;
////        }
////        switch (fState) {
////            case TRAVEL_SOL_ALIGN: {
////                dist.normalize();
////                dist.scale(10);
////                v.set(dist, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                setState(State.TRAVEL_SOL_ACCEL);
////                break;
////            }
////            case TRAVEL_SOL_ACCEL: {
////                dist.normalize();
////                dist.scale(2.0);
////                v.applyAcceleration(dist, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                if (Math.abs(v.getLength().getBaseValue()) >= 1000) {
////                    setState(State.TRAVEL_SOL_JUMP);
////                }
////                break;
////            }
////            case TRAVEL_SOL_JUMP: {
//////                double r = 0;
//////                if (destination instanceof IDAObjectGroup) {
//////                    r = ((IDAObjectGroup) destination).getMaxDist().getBaseValue();
//////                } else {
//////                    r = ((DAmcShip) destination).getObjectGroup().getMaxDist().getBaseValue();
//////                }
////                if (Math.abs(dist.length()) < 1E5) {
////                    dist.normalize();
////                    dist.scale(1000);
////                    v.set(dist, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                    setState(State.TRAVEL_SOL_DEACCEL);
////                }
////                if (Math.abs(dist.length()) < 1e12) {
////                    double z = dist.length() / 3.0;
////                    dist.normalize();
////                    dist.scale(z);
////                } else {
////                    dist.normalize();
////                    dist.scale(4.5e11);
////                }
////
////                v.set(dist, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                break;
////            }
////            case TRAVEL_SOL_DEACCEL: {
////                DAPVVelocity vel = getDestinationVel();
//////                double r = getDestinationGroup().getMaxDist().getBaseValue();
////                if ((Math.abs(v.getLength().getBaseValue()) < 10.0) || (Math.abs(dist.length()) < r)) {
////                    fLog.add(this.getItemID(), new DAText("Zielanflug: Ziel erreicht. Gehe in Bereitschaft."));
////                    Log.debug(DAmcShip.class, "fly_to: " + this + " Dest: " + destination + " reached.", "DAShip.fly_to");
////                    destination = null;
////                    setState(State.IDLE);
////                    Vector3d vv = vel.getVector(CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                    v.set(vv, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                } else {
////    //                    dist.scale(0.01);
////    //                    v.set(dist);
////                    dist.normalize();
////                    dist.scale(-2.0);
////                    v.applyAcceleration(dist, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                    if (v.getLength().getBaseValue() < vel.getLength().getBaseValue() * 1.1) {
////                        Vector3d vv = vel.getVector(CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                        vv.scale(1.1);
////                        v.set(vv, CoordinateSystem.CARTESIAN_RIGHT_HANDED_XZ);
////                    }
////                }
////                break;
////            }
////        }
////        fLog.add(this.getItemID(), new DAText(toString() + " Anflug. (" + fState + ")"));
////        physical.setVelocity(v);
//////            dist = this.getGroupPosition();
//////            dist.add(v.getVector().toVector3d());
//////            this.setGroupPosition(dist);
//////        notifyListener(this);
//    }
//
//    public DAValue<Length> getDistToDestination() {
//        if (destination != null) {
//            return getLocation().getDistance(destination);
//        } else {
//            return new DAValue<Length>(0, SI.METER);
//        }
//    }
//
////    private void miniStopRoll() {
////        aim = Aim.STOP_ROLL;
////    }
////
////    private void miniStopStrafe() {
////        aim = Aim.STOP_STRAFE;
////    }
////
////    private void miniFullStop() {
////        aim = Aim.FULL_STOP;
////    }
////
////    private void miniAlignTo(DAPVDirection p) {
////        Vector3d h = getAbsoluteHeading().getVector3d();
////        Vector3d a = p.getVector();
////        if (h.angle(a) < Math.toRadians(0.1)) {
////            return;
////        }
////        // transform vectors to ships local coordinate system
////        Transform3D t = new Transform3D();
////        t.lookAt(new Point3d(), new Point3d(h.x, h.y, h.z), new Vector3d(0, 1, 0));
////        t.invert();
////        Transform3D tp = new Transform3D();
////        tp.set(a);
////        tp.mul(t);
////        Vector3d r = new Vector3d();
////        tp.get(r);
////        // yaw
////        // pitch
////        // roll
////    }
//
//    /**
//     * Used to anounce a new (unpacked) ship to the corresponding world node.
//     * @param s
//     * @return
//     */
//    public DAResult newShip(de.dertroglodyt.iegcommon.module.DAShip s) {
//        try {
//            worldNode.addShip(s);
//            return DAResult.createOK("ok", "DAShip.newShip");
//        } catch (Exception e) {
//            return DAResult.createWarning(e.toString(), "DAShip.newShip");
//        }
//    }
//
////    @Override
////    public void setRemoteTransformParent(DAPhysical aParent) {
////        aParent.setTransformParent(physical.getTransformParent());
////    }
//
////    public void setAngularVelocity(DAVector3d<AngularVelocity> v) {
////        physical.setAngularVelocity(v);
////    }
//
//    // TODO: crew & passenger
//    public DAVector<DALogItem<DAPerson>> getCrew() {
//        return crew;
//    }
//
//    public DAVector<DALogItem<DAPerson>> getPassengers() {
//        return passengers;
//    }
//
//    public boolean isCrewMember(DAPerson p) {
//        for (DALogItem<DAPerson> li : crew) {
//            if (li.getData() != null) {
//                if (li.getData().equals(p)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isPassenger(DAPerson p) {
//        for (DALogItem<DAPerson> li : passengers) {
//            if (li.getData() != null) {
//                if (li.getData().equals(p)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isCrewMember(DACharacter ch) {
//        for (DALogItem<DAPerson> li : crew) {
//            if (li.getData() instanceof DACharacter) {
//                if (((DACharacter) li.getData()).equals(ch)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean isPassenger(DACharacter ch) {
//        for (DALogItem<DAPerson> li : passengers) {
//            if (li.getData() instanceof DACharacter) {
//                if (((DACharacter) li.getData()).equals(ch)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public DAResult<?> remove(DACharacter ch) {
//        boolean wasCaptain = false;
//        if ((captain != null) && (captain.equals(ch))) {
//            captainID = null;
//            captain = null;
//            wasCaptain = true;
////            return DAResult.createOK("Removed captain <" + ch + ">.", "DAmcShip.remove");
//        }
//        DAResult<?> r = removeFromCrew(ch);
//        if (r.isOK()) {
//            return r;
//        }
//        r = removeFromPassenger(ch);
//        if (r.isOK()) {
//            return r;
//        }
//        if (wasCaptain) {
//            return DAResult.createOK("Removed captain <" + ch + ">.", "DAShip.remove");
//        }
//        return DAResult.createFailed("<" + ch + "> was not found on board of <" + toString() + ">!", "DAShip.remove");
//    }
//
//    public DAResult<?> addToCrew(DAPerson p) {
//        if (isCrewMember(p)) {
//            return DAResult.createOK("ok. already was in crew.", "DAShip.addToCrew");
//        }
//        if (crew.add(new DALogItem<DAPerson>(p))) {
//            fLog.add(p.getID(), new DAText("Crew: " + p + " an Bord."));
//            return DAResult.createOK("ok", "DAShip.addToCrew");
//        } else {
//            return DAResult.createFailed("Could not add <" + p + "> to crew on board of <" + toString() + ">.", "DAShip.addToCrew");
//        }
//    }
//
//    public DAResult removeFromCrew(DACharacter ch) {
//        for (DALogItem<DAPerson> li : crew) {
//            if (li.getData() instanceof DACharacter) {
//                if (((DACharacter) li.getData()).equals(ch)) {
//                    crew.remove(li);
//                    fLog.add(ch.getID(), new DAText("Crew: " + ch + " von Bord."));
//                    return DAResult.createOK("ok", "DAShip.removeFromCrew");
//                }
//            }
//        }
//        return DAResult.createFailed("Could not remove <" + ch + "> from crew on board of <" + toString() + ">.", "DAShip.removeFromCrew");
//    }
//
//    public DAResult removeFromCrew(DAPerson p) {
//        for (DALogItem<DAPerson> li : crew) {
//            if (li.getData().equals(p)) {
//                crew.remove(li);
//                fLog.add(p.getID(), new DAText("Crew: " + p + " von Bord."));
//                return DAResult.createOK("ok", "DAShip.removeFromCrew");
//            }
//        }
//        return DAResult.createFailed("Could not remove <" + p + "> from crew on board of <" + toString() + ">.", "DAShip.removeFromCrew");
//    }
//
//    public DAResult addToPassenger(DAPerson p) {
//        if (isPassenger(p)) {
//            return DAResult.createOK("ok. already was in passengers.", "DAShip.addToPassenger");
//        }
//        if (passengers.add(new DALogItem<DAPerson>(p))) {
//            fLog.add(p.getID(), new DAText("Passagier: " + p + " an Bord."));
//            return DAResult.createOK("ok", "DAShip.addToPassenger");
//        } else {
//            return DAResult.createFailed("Could not add <" + p + "> to passengers on board of <" + toString() + ">.", "DAShip.addToPassenger");
//        }
//    }
//
//    public DAResult removeFromPassenger(DACharacter ch) {
//        for (DALogItem<DAPerson> li : passengers) {
//            if (li.getData() instanceof DACharacter) {
//                if (((DACharacter) li.getData()).equals(ch)) {
//                    passengers.remove(li);
//                    fLog.add(ch.getID(), new DAText("Passagier: " + ch + " von Bord."));
//                    return DAResult.createOK("ok", "DAShip.removeFromPassenger");
//                }
//            }
//        }
//        return DAResult.createFailed("Could not remove <" + ch + "> from crew on board of <" + toString() + ">.", "DAShip.removeFromPassenger");
//    }
//
//    public DAResult removeFromPassenger(DAPerson p) {
//        for (DALogItem<DAPerson> li : passengers) {
//            if (li.getData().equals(p)) {
//                passengers.remove(li);
//                fLog.add(p.getID(), new DAText("Passagier: " + p + " von Bord."));
//                return DAResult.createOK("ok", "DAShip.removeFromPassenger");
//            }
//        }
//        return DAResult.createFailed("Could not remove <" + p + "> from crew on board of <" + toString() + ">.", "DAShip.removeFromPassenger");
//    }
//
////
////    public DAResult canAdd(DAVector<DAPerson> passengers) {
////        if (! isOnline()) {
////            return DAResult.createFailed("Kabine ist nicht online.", "DAbmCabin.canAdd");
////        }
////        int x = 0;
////        for (DAPerson p : passengers) {
////            x += p.getCount();
////        }
////        if (getFreeNumber() < x) {
////            return DAResult.createFailed("Not enough free space.", "DAbmCabin.canAdd");
////        }
////        return DAResult.createOK("ok", "DAbmCabin.canAdd");
////    }
////
////    public DAResult canAdd(DAPerson passenger) {
////        if (! isOnline()) {
////            return DAResult.createFailed("Kabine ist nicht online.", "DAbmCabin.canAdd");
////        }
////        if (getFreeNumber() < passenger.getCount()) {
////            return DAResult.createFailed("Not enough free space.", "DAbmCabin.canAdd");
////        }
////        return DAResult.createOK("ok", "DAbmCabin.canAdd");
////    }
////
////    /**
////     * Tries to add the given amount of persons to the storage.
////     * @param passenger
////     * @return
////     */
////    public DAResult add(DAPerson passenger) {
////        DAResult r = canAdd(passenger);
////        if (! r.isOK()) {
////            return r;
////        } else {
////            boolean b = persons.add(new DALogItem<DAPerson>(passenger));
////            if (b) {
////                return DAResult.createOK("OK", "DAbmCabin");
////            } else {
////                return DAResult.createFailed("Something strange happened.", "DAbmCabin");
////            }
////        }
////    }
////
////    public DALogItem<DAPerson> get(DAPerson p) {
////        for (DALogItem<DAPerson> li : persons) {
////            if (li.getData().equals(p)) {
////                return li;
////            }
////        }
////        return null;
////    }
////
////    public synchronized void removeAllChildren() {
////        persons.clear();
////    }
////
////    /**
////     * Removes all container on ware from this storage.
////     * If not all container can be removed the returned vector holds the containers already removed on return.
////     * @param passengers
////     * @return
////     */
////    public synchronized DATypedResult<DAVector<DAPerson>> remove(DAVector<DAPerson> passengers) {
////        DAVector<DAPerson> rc = new DAVector<DAPerson>(DAPerson.class);
////        rc.addAll(passengers);
////        try {
////            Iterator<DAPerson> i = passengers.iterator();
////            while (i.hasNext()) {
////                DAPerson p = i.next();
////                if (! persons.remove(get(p))) {
////                    return new DATypedResult<DAVector<DAPerson>>("Can not remove person from cabin"
////                            , DAResult.ResultType.WARNING, rc, "DAbmCabin.remove");
////                } else {
////                    rc.remove(p);
////                }
////            }
////            notifyListener(this);
////            return new DATypedResult<DAVector<DAPerson>>("ok", DAResult.ResultType.OK
////                , rc, "DAbmCabin.remove");
////        } catch (Throwable t) {
////            DAResult r = DAResult.createWarning(t.toString(), "DAbmCabin.remove");
////            DVCErrorHandler.raiseError(r);
////            return new DATypedResult<DAVector<DAPerson>>(r.getMessage(), r.getResultType()
////                            , rc, "DAbmCabin.remove");
////        }
////    }
////
////    public synchronized boolean remove(DAPerson person) {
////        boolean r = persons.remove(get(person));
////        notifyListener(this);
////        return r;
////    }
////
////    public DAVector<DALogItem<DAPerson>> getList() {
////        return persons;
////    }
////
////    public DAPerson getByID(DAUniqueID id) {
////        if (id == null) {
////            return null;
////        }
////        for (DALogItem<DAPerson> li : persons) {
////            if (id.equals(li.getData().getItemID())) {
////                return li.getData();
////            }
////        }
////        return null;
////    }
////
////    public DAVector<DAPerson> getPersons() {
////        DAVector<DAPerson> v = new DAVector<DAPerson>(DAPerson.class);
////        for (DALogItem<DAPerson> li : persons) {
////            v.add(li.getData());
////        }
////        return v;
////    }
//
//    public DAVector3d<Dimensionless> getHeading() {
//        // TODO
//        return DAVector3d.StandardDirections.NORTH.getDirection();
//    }
//
}
