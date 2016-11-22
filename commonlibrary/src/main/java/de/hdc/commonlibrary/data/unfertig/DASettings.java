/*
 * Copyright DerTroglodyt
 */

package de.hdc.commonlibrary.data.unfertig;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DAVector;
import de.hdc.commonlibrary.data.compound.DAMap;

/**
 * An enumeration of properties (key-value pairs).
 *
 * todo unfertig
 *
 * @author dertroglodyt
 */
public class DASettings extends DAMap<DAText, DAText> {

    public enum System {
        APPLICATION_PATH, COFIG_PATH;

        public final DAText key;
        System() {
            key = DAText.create("System." + name());
        }
    }

    public enum Grafics {
        SCREEN_SIZE, FONT_SIZE;

        public final DAText key;
        Grafics() {
            key = DAText.create("Grafics." + name());
        }
    }

    /**
     * Create default settings.
     */
    public DASettings create(String applicationPath) {
        DASettings s = new DASettings();
        s.set(System.APPLICATION_PATH.key, DAText.create(applicationPath));
        s.set(System.COFIG_PATH.key, DAText.create(applicationPath + File.separator + "config"));
        DAVector<? extends Quantity> v = DAVector.create(NonSI.PIXEL, 1024.0, 800.0);
        s.set(Grafics.SCREEN_SIZE.key, DAText.create(v.toString()));
        DAValue<? extends Quantity> vv = DAValue.create(12, NonSI.PIXEL);
        s.set(Grafics.FONT_SIZE.key, DAText.create(vv.toString()));
        return s;
    }

//    public DASettings(DATextMap grafics) {
//        super();
//        this.table = grafics;
//    }

    public IDataAtom get(Grafics key) {
        return get(key.key);
    }

    public void set(Grafics key, IDataAtom value) {
        set(key.key, DAText.create(value.toString()));
    }

    /**
    public static DASettings load() throws IOException {
        File f = new File(new File(".").getCanonicalPath() + File.separator + "settings.conf");
        return fromJSON(JSONObject.read(new FileReader(f)));
        return null;
    }

    public void save() throws IOException {
        File f = new File(new File(".").getCanonicalPath() + File.separator + "settings.conf");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(toJSON("", null));
        bw.close();
    }
     */

    @Override
    public String toString() {
        return "DASettings{" + super.toString() + '}';
    }
    //todo implement doCompare

    @Override
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeInt(table.size());
        for (Map.Entry<DAText, DAText> entry : table.entrySet()) {
            entry.getKey().toStream(stream);
            entry.getValue().toStream(stream);
        }
    }

    @Override
    public DASettings fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte(); // version
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        final DASettings t = new DASettings();
        final int x = stream.readInt();
        for (int i = 0; i < x; i++) {
            final DAText key = new DAText().fromStream(stream);
            final DAText a = new DAText().fromStream(stream);
            t.set(key, a);
        }
        return t;
    }

    private static final byte VERSION = 1;

    @Deprecated
    public DASettings() {
        super();
    }

}
