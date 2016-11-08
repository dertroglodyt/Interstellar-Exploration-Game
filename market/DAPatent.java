/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DataAtom;
import de.hdc.commonlibrary.data.types.collection.DASet;
import de.hdc.commonlibrary.data.types.collection.DAVector;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAPatent extends DataAtom {

    private static final long serialVersionUID = SerialUIDPool.UID.DAPatent.value();

    public static enum Patent {
        NOT_INITIALZED("<not initalized>"),
        // Antrieb & Wartung
        SCHIFFS_MECHANIKER("Schiffsmechaniker"),
        SCHIFFS_INGENIEUR("Schiffsingenieur", SCHIFFS_MECHANIKER),
        LTDSCHIFFS_INGENIEUR("Leitender Schiffsingenieur", SCHIFFS_INGENIEUR),
        SCHIFFS_BETRIEBSOFFIZIER("Schiff-Betriebsoffizier", LTDSCHIFFS_INGENIEUR),
        // Bergbau
        BERGBAU_MECHANIKER("Bergbaumechaniker"),
        BERGBAU_INGENIEUR("Bergbauingenieur", BERGBAU_MECHANIKER),
        LTDBERGBAU_INGENIEUR("Leitender Bergbauingenieur", BERGBAU_INGENIEUR),
        BERGBAU_BETRIEBSOFFIZIER("Bergbau-Betriebsoffizier", LTDBERGBAU_INGENIEUR),
        // Waffen
        WAFFEN_MECHANIKER("Waffenmechaniker"),
        WAFFEN_INGENIEUR("Waffeningenieur", WAFFEN_MECHANIKER),
        LTDWAFFEN_INGENIEUR("Leitender Waffeningenieur", WAFFEN_INGENIEUR),
        WAFFEN_BETRIEBSOFFIZIER("Waffen-Betriebsoffizier", LTDWAFFEN_INGENIEUR),
        // Hangar
        HANGAR_MECHANIKER("Hangarmechaniker"),
        HANGAR_INGENIEUR("Hangaringenieur", HANGAR_MECHANIKER),
        LTDHANGAR_INGENIEUR("Leitender Hangaringenieur", HANGAR_INGENIEUR),
        HANGAR_BETRIEBSOFFIZIER("Hangar-Betriebsoffizier", LTDHANGAR_INGENIEUR),
        // Waffen
        DROHNENOPERATOR("Drohnenoperator"),
        FLO_SHUTTLE("Fliegerleitoffizier Shuttle", DROHNENOPERATOR),
        FLO_KORVETTEN("Fliegerleitoffizier Korvetten", FLO_SHUTTLE),
        FLO_FREGATTEN("Fliegerleitoffizier Fregatten", FLO_KORVETTEN),
        // Krankenstation
        ASSISTENZARZT("Assistenzarzt"),
        FACHARZT("Facharzt", ASSISTENZARZT),
        OBERARZT("Oberarzt", FACHARZT),
        CHEFARZT("Chefarzt", OBERARZT),
        // Navigation (Sprungantrieb)
        SOLAR_NAVIGATOR("Solarnavigator"),
        STELLAR_NAVIGATOR("Stellarnavigator", SOLAR_NAVIGATOR),
        // Pilot
        SHUTTLE_PILOT("Shuttlepilot"),
        KORVETTEN_PILOT("Korvettenpilot", SHUTTLE_PILOT),
        FREGATTEN_PILOT("Fregattenpilot", KORVETTEN_PILOT),
        ZERSTÖRER_PILOT("Zerstörerpilot", FREGATTEN_PILOT),
        KREUZER_PILOT("Kreuzerpilot", ZERSTÖRER_PILOT),
        SCHLACHTSCHIFF_PILOT("Schlachtschiffpilot", KREUZER_PILOT),
        TRÄGER_PILOT("Trägerpilot", SCHLACHTSCHIFF_PILOT),
        GKS_PILOT("Grosskampfschiffpilot", TRÄGER_PILOT),
        ;

        private final String text;
        private final DASet<DAPatent> prerequisite;

        Patent(String n, Patent... p) {
            text = n;
            prerequisite = new DASet<DAPatent>(DAPatent.class);
            for (int i=0; i < p.length; i++) {
                prerequisite.add(new DAPatent(p[i]));
            }
        }

        public String toSerialString() {
            return name();
        }

        // Attention: MUST NOT be used in serialisation!
        @Override
        public String toString() {
            return text;
        }

        public static Patent valueFrom(String aText) {
            for (Patent p: values()) {
                if (p.toString().equals(aText)) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Cannot parse into a Patent: " + aText);
        }

        public static DAVector<DALine> valueList() {
            DAVector<DALine> v = new DAVector<DALine>(DALine.class);
            for (Patent p : values()) {
                v.add(new DALine(p.toString()));
            }
            return v;
        }

        public DAPatent getPatent() {
            return new DAPatent(this);
        }
    }

    private Patent patent;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        patent = Patent.valueOf(in.readUTF());
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

        out.writeUTF(patent.toSerialString());
    }

    @Deprecated
    public DAPatent() {
        super();
        patent = Patent.NOT_INITIALZED;
    }

    private DAPatent(Patent p) {
        patent = p;
    }

    @Override
    public String toString() {
        return patent.toString();
    }

//    @Override
//    public DAPatent clone() {
//        return new DAPatent(patent);
//    }

    @Override
    public DataAtom getTestInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toParseString(String levelTab) {
        return toString();
    }

    @Override
    public DAPatent parse(String s) {
        try {
            return new DAPatent(Patent.valueFrom(s));
        } catch (IllegalArgumentException iae) {
            return new DAPatent(Patent.valueOf(s));
        }
    }

//    @Override
//    public DVCDataEditor getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCsePatent de = new DVCsePatent(this, editmode, user);
//        addListener(de);
//        return de;
//    }

}
