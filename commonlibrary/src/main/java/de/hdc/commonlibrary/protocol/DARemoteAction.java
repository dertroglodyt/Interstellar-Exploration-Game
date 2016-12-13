/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.protocol;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DataAtom;

/**
 *
 * @author martin
 */
public class DARemoteAction extends DataAtom {

    public enum Type {
        UNKNOWN,
        ORGANISATION,
        SHIP,
        MARKET,
        ;
    }

    // TODO: Sender definieren...
    public final Type senderType;
    public final DAUniqueID senderID;
    public final Type destinationType;
    public final DAUniqueID destinationID;
    public final DAText actionName;
    public final DAParameterList parm;

    @Deprecated
    public DARemoteAction() {
        super();
        senderType = null;
        senderID = null;
        destinationType = null;
        destinationID = null;
        actionName = null;
        parm = null;
    }

    public DARemoteAction(Type sender, DAUniqueID senderID, Type destination, DAUniqueID destinationID
            , IRemoteActionType action, DAParameterList parms) {
        super();
        if (senderID == null) {
            throw new IllegalArgumentException("SenderID is NULL.");
        }
        if (destinationID == null) {
            throw new IllegalArgumentException("DestinationID is NULL.");
        }
        if (action.getInput().size() != parms.size()) {
            throw new IllegalArgumentException("Parameter count != action.parameter!");
        }
        if (! parms.validate(action)) {
            throw new IllegalArgumentException("Action parameter not in parameter list or of wrong type!");
        }
//        for (IParameterType pt : action.getInput()) {
//            if (parms.get(pt) == null) {
//                throw new IllegalArgumentException("Action parameter <" + pt.getName() + "> not in parameter list!");
//            }
//        }
        senderType = sender;
        this.senderID = senderID;
        destinationType = destination;
        this.destinationID = destinationID;
        actionName = action.getName();
        parm = parms;
    }

    @Override
    public String toString() {
        return senderType + "." + actionName + "." + destinationType;
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        stream.writeUTF(senderType.toString());
        senderID.toStream(stream);
        stream.writeUTF(destinationType.toString());
        destinationID.toStream(stream);
        actionName.toStream(stream);
        parm.toStream(stream);
    }

    @Override
    public DARemoteAction fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        Type aSenderType = Type.valueOf(stream.readUTF());
        DAUniqueID aSenderID = new DAUniqueID().fromStream(stream);
        Type aDestinationType = Type.valueOf(stream.readUTF());
        DAUniqueID aDestinationID = new DAUniqueID().fromStream(stream);
        DAText aName = new DAText().fromStream(stream);
        DAParameterList aParm = new DAParameterList().fromStream(stream);

        return new DARemoteAction(aSenderType, aSenderID, aDestinationType, aDestinationID, aName, aParm);
    }

    private static final byte VERSION = 1;

    private DARemoteAction(Type sender, DAUniqueID senderID, Type destination, DAUniqueID destinationID
            , DAText action, DAParameterList parms) {
        super();
        senderType = sender;
        this.senderID = senderID;
        destinationType = destination;
        this.destinationID = destinationID;
        actionName = action;
        parm = parms;
    }

}
