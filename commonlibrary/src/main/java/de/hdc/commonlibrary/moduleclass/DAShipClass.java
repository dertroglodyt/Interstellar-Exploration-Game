/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.moduleclass;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.hdc.commonlibrary.market.DAWareClass.Size;
import de.hdc.commonlibrary.module.DAShip;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAShipClass extends DAModuleContainerClass {

    public static enum Type {                                            // Max.Length  Sides          Hull-Weight (max)
        DRONE("Drone", 2.0, Size.MEDIUM, Size.SMALL),                    //    4m  2m x 2m x 2m            1.6t
        SHUTTLE("Shuttle", 5.0, Size.MEDIUM, Size.SMALL),                //   10m  5m x 5m x 5m             25t
        CORVETTE("Korvette", 12.5, Size.LARGE, Size.MEDIUM),             //   25m  12.5m x 12.5m x 12.5m   390t
        FRIGATE("Fregatte", 25.0, Size.GIANT, Size.MEDIUM),              //   50m  25m x 25m x 25m        3125t
        DESTROYER("Zerstörer", 50.0, Size.GIANT, Size.LARGE),            //  100m  50m x 50m x 50m       25000t
        CRUISER("Kreuzer", 100.0, Size.COLOSSAL, Size.LARGE),            //  200m  100m x 100m x 100m   200000t
        BATTLESHIP("Schlachtschiff", 200.0, Size.COLOSSAL, Size.GIANT),  //  400m  200m x 200m x 200m    1.6e6t
        CARRIER("Träger", 400, Size.COLOSSAL, Size.GIANT),               //  800m  400m x 400m x 400m   12.8e6t
        CAPITAL("Flaggschiff", 800, Size.COLOSSAL, Size.GIANT),          // 1600m  800m x 800m x 800m  102.0e6t
        LEVIATHAN("Leviathan", 1e100, Size.COLOSSAL, Size.COLOSSAL);     // everything else...

        private final String text;
        private double side;
        private Size size;
        private Size moduleSize;
        Type(String aText, double aSide, Size aSize, Size aModuleSize) {
            text = aText;
            side = aSide;
            size = aSize;
            moduleSize = aModuleSize;
        }

        public String toSerialString() {
            return name();
        }

        // Attention: MUST NOT be used in serialisation!
        @Override
        public String toString() {
            return text;
        }

        public static Type valueFrom(String aText) {
            for (Type p: values()) {
                if (p.toString().equals(aText)) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Cannot parse into a Type: " + aText);
        }

        public double getMaxVolume() {
            return side * side * side;
        }

        public double getMaxSide() {
            return 2.0 * side;
        }

        public double getSmallSide(double faktor) {
            return side / faktor;
        }

        public Size getShipSize() {
            return size;
        }

        public Size getMaxModuleSize() {
            return moduleSize;
        }
    }

    private Type type;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        type = Type.valueOf(in.readUTF());
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

        out.writeUTF(type.toSerialString());
    }

    public DAShipClass() {
        super("Schiff");
    }

    public DAShipClass(AssetPool.AssetNameWareClass graficAssetName) {
        super(graficAssetName);
        type = getAssetData(Constants.UserDataKey.ShipType, Type.SHUTTLE);
    }

//    public DAShipClass(String idStr, DALine name, DAText description, Type aShipType, AssetPool.AssetNameWareClass graficAssetName) {
//        super(idStr, aShipType.getMaxModuleSize(), name, description, graficAssetName);
//        type = aShipType;
//        getPrerequisites().add(DAPatent.Patent.SHUTTLE_PILOT.getPatent());
//    }

//    @Override
//    public DAShipClass clone() {
//        DAShipClass pc = new DAShipClass(getItemID().toString(), getClassName().clone()
//                , fDescription.clone(), getType(), fAssetName);
//        return pc;
//    }

    @Override
    public DAShip create() {
        return new DAShip(this, null);
    }

    @Override
    public DAShip create(DATransform trans) {
        return new DAShip(this, trans);
    }

    public DAShip create(DAWorldNode wn, DATransform trans) {
        DAShip s = new DAShip(this, trans);
        s.init(wn);
        return s;
    }

    public Size getShipTypeSize() {
        return type.getShipSize();
    }

    public Size getMaxModuleSize() {
        return type.getMaxModuleSize();
    }

    public Type getType() {
        return type;
    }

}
