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
import de.hdc.commonlibrary.data.compound.DAResult;

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
    public final DAResult result;

    @Deprecated
    public DARemoteAction() {
        super();
        senderType = null;
        senderID = null;
        destinationType = null;
        destinationID = null;
        actionName = null;
        parm = null;
        result = null;
    }

    public static DARemoteAction create(DARemoteAction action, DAResult result) {
        return new DARemoteAction(action.destinationType, action.destinationID, action.senderType, action.senderID
                , action.actionName, action.parm, result);
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
        result = null;
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
        stream.writeBoolean(result != null);
        if (result != null) {
            result.toStream(stream);
        }
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
        DAResult aResult = null;
        DAParameterList aParm = new DAParameterList().fromStream(stream);
        if (stream.readBoolean()) {
            aResult = new DAResult<>().fromStream(stream);
        }

        return new DARemoteAction(aSenderType, aSenderID, aDestinationType, aDestinationID, aName, aParm, aResult);
    }

    private static final byte VERSION = 1;

    private DARemoteAction(Type sender, DAUniqueID senderID, Type destination, DAUniqueID destinationID
            , DAText action, DAParameterList parms, DAResult result) {
        super();
        senderType = sender;
        this.senderID = senderID;
        destinationType = destination;
        this.destinationID = destinationID;
        actionName = action;
        parm = parms;
        this.result = result;
    }

}
