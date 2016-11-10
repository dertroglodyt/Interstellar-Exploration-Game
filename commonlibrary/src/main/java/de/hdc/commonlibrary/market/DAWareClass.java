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

/**
 * Holds the invariable properties of a DAWare.
 * @author martin
 */
public class DAWareClass extends DataAtom {

    public static final DAWareClass ELECTRICAL_POWER = new DAWareClass(
            DAUniqueID.parse("00000000000000000000000000000001")
            , DAText.create("Electrical Power"), DAText.create("Needs description"), Size.NONE
            , State.PUBLIC, SI.JOULE, DAValue.<Mass>create(0, SI.KILOGRAM), DAValue.<Volume>create(0, SI.CUBIC_METRE)
            , DAText.create("Electrical Power"));

    public static enum Size {
        NONE("unbekannt", 0),          // Max. Seitenlänge
        MINI("mini", 0.008),           // 0.20m
        SMALL("klein", 1.0),           // 1.0m
        MEDIUM("mittel", 125.0),       // 5.0m
        LARGE("groß", 8000.0),         // 20.0m
        GIANT("riesig", 125000.0),     // 50.0m
        COLOSSAL("kolossal", 1e100),   // everything else. can not be carried / stored by normal means.
        ;

        public final DAText text;
        public final DAValue<Volume> vol;
        public final DAValue<Mass> mass;
        public final DAValue<Length> maxSide;

        Size(String aText, double m3) {
            text = DAText.create(aText);
            vol = DAValue.<Volume>create(m3, SI.CUBIC_METRE);
            mass = DAValue.<Mass>create(m3 * 200, SI.KILOGRAM);  // 200kg / m³  Hull-Only-Weight
            maxSide = DAValue.<Length>create(Math.cbrt(m3), SI.METER);
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

        public boolean isSmaller(Size s) {
            return vol.isSmallerThan(s.vol);
        }

        public boolean isBiggerOrEqual(Size s) {
            return ! isSmaller(s);
        }

        public boolean isBiggerOrEqual(DAValue<Volume> v) {
            return ! vol.isSmallerThan(v);
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
    public final DAText name;
    public final DAText description;
    public final Size size;
    public final State state;
    public final Unit<?> unit;
    public final DAValue<Mass> mass;
    public final DAValue<Volume> volume;
    public final DAText assetName;
    public final transient DAValue<VolumetricDensity> kgPerM3;

    public DAWareClass create(DAUniqueID id, DAText name, DAText description, Size size, State state
            , Unit<?> unit, DAValue<Mass> mass, DAValue<Volume> volume, DAText assetName) {
            return new DAWareClass(id, name, description, size, state, unit, mass, volume, assetName);
    }

    @Deprecated
    public DAWareClass() {
        super();
        this.id = null;
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
        final DAUniqueID id = new DAUniqueID().fromStream(stream);
        final DAText name = new DAText().fromStream(stream);
        final DAText description = new DAText().fromStream(stream);
        final Size size = Size.valueFrom(stream.readUTF());
        final State state = State.valueOf(stream.readUTF());
        final Unit<?> unit = Unit.valueOf(stream.readUTF());
        final DAValue<Mass> mass = new DAValue().fromStream(stream);
        final DAValue<Volume> volume = new DAValue().fromStream(stream);
        final DAText assetName = new DAText().fromStream(stream);

        return new DAWareClass(id, name, description, size, state, unit, mass, volume, assetName);
    }

    private static final byte VERSION = 1;

    private DAWareClass(DAUniqueID id, DAText name, DAText description, Size size, State state
            , Unit<?> unit, DAValue<Mass> mass, DAValue<Volume> volume, DAText assetName) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.size = size;
        this.state = state;
        this.unit = unit;
        this.mass = mass;
        this.volume = volume;
        this.assetName = assetName;
        if (volume.isZero()) {
            final DAValue<VolumetricDensity> d = (DAValue<VolumetricDensity>) mass.div(volume);
            kgPerM3 = d.to(VolumetricDensity.UNIT);
        } else {
            kgPerM3 = DAValue.<VolumetricDensity>create(0.0, VolumetricDensity.UNIT);
        }
    }

}
