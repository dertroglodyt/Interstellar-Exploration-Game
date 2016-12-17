/*
 *  Created by DerTroglodyt on 2016-11-27 01:27
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.compound.DAResult;

public class DAOwner implements IDASubject {

    public final DAUniqueID id;
    public final DAText name;
    public final DAOwner.Type type;
    private DAWallet wallet;

    public static DAOwner create(DAUniqueID id, DAText name, DAOwner.Type type) {
        return new DAOwner(id, name, type, DAWallet.create());
    }

    @Override
    public String toString() {
        return "DAOrganizationBasic{" +
                "id=" + id +
                ", actionName=" + name +
                '}';
    }

    @Override
    public DAUniqueID getId() {
        return id;
    }

    @Override
    public DAText getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    public DAResult<IDAWare> transaction(DAMarketTransaction mt) {
        return wallet.transaction(mt);
    }

    public void undoTransaction(DAMarketTransaction mt) {
        wallet.undoTransaction(mt);
    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        id.toStream(stream);
        name.toStream(stream);
        stream.writeUTF(type.toString());
        wallet.toStream(stream);
    }

    @Override
    public DAOwner fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAUniqueID aid = new DAUniqueID().fromStream(stream);
        DAText aname = new DAText().fromStream(stream);
        DAOwner.Type atype = DAOwner.Type.valueOf(stream.readUTF());
        DAWallet awallet = new DAWallet().fromStream(stream);
        return new DAOwner(aid, aname, atype, awallet);
    }

    private static final byte VERSION = 1;

    private DAOwner(DAUniqueID id, DAText name, DAOwner.Type type, DAWallet wallet) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.wallet = wallet;
    }

}
