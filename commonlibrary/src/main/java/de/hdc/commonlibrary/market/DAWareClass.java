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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.quantity.Pieces;

/**
 * Holds the invariable properties of a DAWare.
 * @author martin
 */
public class DAWareClass extends DataAtom {

    public static enum Size {
        NONE("unbekannt", 0, 0),          // Max. Seitenlänge
        MINI("mini", 0.008, 0.2),           // 0.20m
        SMALL("klein", 1.0, 1.0),           // 1.0m
        MEDIUM("mittel", 125.0, 5.0),       // 5.0m
        LARGE("groß", 8000.0, 20.0),         // 20.0m
        GIANT("riesig", 125000.0, 50.0),     // 50.0m
        COLOSSAL("kolossal", 1e99, 1e33),   // everything else. can not be carried / stored by normal means.
        ;

        public final DAText text;
        public final DAValue<Volume> volume;
        public final DAValue<Mass> mass;
        public final DAValue<Length> maxSide;

        Size(String aText, double m3, double m) {
            text = DAText.create(aText);
            volume = DAValue.create(m3, SI.CUBIC_METRE);
            mass = DAValue.create(m3 * 200, SI.KILOGRAM);  // 200kg / m³  Hull-Only-Weight
            maxSide = DAValue.create(m3, SI.METER);
        }

        public String toSerialString() {
            return super.name();
        }

        // Attention: MUST NOT be used in serialisation!
        @Override
        public String toString() {
            return text.toString();
        }

        public static Size valueFrom(String aText) {
            for (Size s: values()) {
                if (s.toString().equals(aText)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("Cannot parse into a Size: " + aText);
        }

        public DAValue<Length> getMaxSide() {
            return maxSide;
        }

        public DAValue<Volume> getVolume() {
            return volume;
        }

        public boolean isSmaller(Size s) {
            return volume.isSmallerThan(s.volume);
        }

        public boolean isBiggerOrEqual(Size s) {
            return ! isSmaller(s);
        }

        public boolean isBiggerOrEqual(DAValue<Volume> v) {
            return ! volume.isSmallerThan(v);
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

    public final DAUniqueID id;
    public final DAUniqueID typeID;
    public final DAText name;
    public final DAText description;
    public final Size size;
    public final State state;
    public final Unit<?> unit;
    public final DAValue<Mass> mass;
    /**
     * The volume which this component encloses.
     */
    public final DAValue<Volume> volume;
    public final DAText assetName;
    public final transient DAValue<VolumetricDensity> kgPerM3;

    public static DAWareClass create(DAUniqueID id, DAUniqueID typeID, DAText name, DAText description, Size size, State state
            , Unit<?> unit, DAValue<Mass> mass, DAValue<Volume> volume, DAText assetName) {
            return new DAWareClass(id, typeID, name, description, size, state, unit, mass, volume, assetName);
    }

    /**
     * Used by DAWareTypeTreeBootstrap.
     */
    @Deprecated
    public static DAWareClass create(DAUniqueID id, DAUniqueID typeID, DAText name) {
        return new DAWareClass(id, typeID, name, DAText.create(""), Size.NONE, State.ADMIN, Pieces.UNIT
                , DAValue.create(1, SI.KILOGRAM), DAValue.create(1, SI.CUBIC_METRE), DAText.create(""));
    }

    @Deprecated
    public DAWareClass() {
        super();
        this.id = null;
        this.typeID = null;
        this.name = null;
        this.description = null;
        this.size = null;
        this.state = null;
        this.unit = null;
        this.mass = null;
        this.volume = null;
        this.assetName = null;
        this.kgPerM3 = null;
    }

    @Override
    public String toString() {
        return name + "(" + state+ ", " + size + ", " + unit
                + ", " + mass + ", " + volume + ")";
    }

    public boolean isPublic() {
        return (state == State.PUBLIC);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        id.toStream(stream);
        typeID.toStream(stream);
        name.toStream(stream);
        description.toStream(stream);
        stream.writeUTF(size.toSerialString());
        stream.writeUTF(state.toString());
        stream.writeUTF(unit.toString());
        mass.toStream(stream);
        volume.toStream(stream);
        assetName.toStream(stream);
    }

    @Override
    public DAWareClass fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DAUniqueID aid = new DAUniqueID().fromStream(stream);
        final DAUniqueID atypeID = new DAUniqueID().fromStream(stream);
        final DAText aname = new DAText().fromStream(stream);
        final DAText adescription = new DAText().fromStream(stream);
        final Size asize = Size.valueFrom(stream.readUTF());
        final State astate = State.valueOf(stream.readUTF());
        final Unit<?> aunit = Unit.valueOf(stream.readUTF());
        final DAValue<Mass> amass = new DAValue<Mass>().fromStream(stream);
        final DAValue<Volume> avolume = new DAValue<Volume>().fromStream(stream);
        final DAText aassetName = new DAText().fromStream(stream);

        return new DAWareClass(aid, atypeID, aname, adescription, asize, astate, aunit, amass, avolume, aassetName);
    }

    private static final byte VERSION = 1;

    protected DAWareClass(DAWareClass wc) {
        super();
        this.id = wc.id;
        this.typeID = wc.typeID;
        this.name = wc.name;
        this.description = wc.description;
        this.size = wc.size;
        this.state = wc.state;
        this.unit = wc.unit;
        this.mass = wc.mass;
        this.volume = wc.volume;
        this.assetName = wc.assetName;
        if (! volume.isZero()) {
            final DAValue<VolumetricDensity> d = (DAValue<VolumetricDensity>) mass.div(volume);
            kgPerM3 = d.to(VolumetricDensity.UNIT);
        } else {
            kgPerM3 = DAValue.create(0.0, VolumetricDensity.UNIT);
        }
    }

    protected DAWareClass(DAUniqueID id, DAUniqueID typeID, DAText name, DAText description, Size size, State state
            , Unit<?> unit, DAValue<Mass> mass, DAValue<Volume> volume, DAText assetName) {
        super();
        this.id = id;
        this.typeID = typeID;
        this.name = name;
        this.description = description;
        this.size = size;
        this.state = state;
        this.unit = unit;
        this.mass = mass;
        this.volume = volume;
        this.assetName = assetName;
        if (! volume.isZero()) {
            final DAValue<VolumetricDensity> d = (DAValue<VolumetricDensity>) mass.div(volume);
            kgPerM3 = d.to(VolumetricDensity.UNIT);
        } else {
            kgPerM3 = DAValue.<VolumetricDensity>create(0.0, VolumetricDensity.UNIT);
        }
    }

}
