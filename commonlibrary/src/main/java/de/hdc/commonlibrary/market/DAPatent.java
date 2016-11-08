/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAPatent extends DataAtom {

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
        private final DAPatentSet prerequisite;

        Patent(String n, Patent... p) {
            text = n;
            prerequisite = DAPatentSet.create();
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

        public static ArrayList<DAText> valueList() {
            final ArrayList<DAText> v = new ArrayList<>();
            for (Patent p : values()) {
                v.add(DAText.create(p.toString()));
            }
            return v;
        }

        public DAPatent getPatent() {
            return new DAPatent(this);
        }
    }

    public final Patent patent;

    public DAPatent create(Patent p) {
        return new DAPatent(p);
    }

    @Override
    public String toString() {
        return patent.toString();
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        stream.writeUTF(patent.toSerialString());
    }

    @Override
    public DAPatent fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final Patent p = Patent.valueFrom(stream.readUTF());
        return new DAPatent(p);
    }

    private static final byte VERSION = 1;

    private DAPatent(Patent p) {
        super();
        patent = Patent.NOT_INITIALZED;
    }

    @Deprecated
    public DAPatent() {
        super();
        patent = null;
    }

}
