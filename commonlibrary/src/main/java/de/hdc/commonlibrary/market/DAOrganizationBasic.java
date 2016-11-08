package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DataAtom;

public class DAOrganizationBasic extends DataAtom {

    public final DAUniqueID id;
    public final DAText name;

    public static DAOrganizationBasic create(DAUniqueID id, DAText name) {
        return new DAOrganizationBasic(id, name);
    }
    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        id.toStream(stream);
        name.toStream(stream);
    }

    @Override
    public DAOrganizationBasic fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAUniqueID id = new DAUniqueID().fromStream(stream);
        DAText name = new DAText().fromStream(stream);
        return new DAOrganizationBasic(id, name);
    }

    private static final byte VERSION = 1;

    private DAOrganizationBasic(DAUniqueID id, DAText name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "DAOrganizationBasic{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
