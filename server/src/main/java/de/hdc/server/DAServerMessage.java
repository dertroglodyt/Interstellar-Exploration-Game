package de.hdc.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAInetAddress;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.protocol.DAParameterList;
import de.hdc.commonlibrary.protocol.IParameterType;
import de.hdc.commonlibrary.protocol.IRemoteActionType;

/**
 * Created by DerTroglodyt on 2016-12-19 09:40.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public class DAServerMessage implements IDataAtom {
    /**
     * Valid parameters in a remote action.
     */
    public enum Parms implements IParameterType {

        NONE(null),
        OK(DAText.class),
        SENDER_IP(DAInetAddress.class),
        DEST_IP(DAInetAddress.class),
        ;

        private DAText name;
        private Class<? extends IDataAtom> c;

        Parms(Class<? extends IDataAtom> aClass) {
            name = DAText.create(this.toString());
            c = aClass;
        }

        @Override
        public DAText getName() {
            return name;
        }

        @Override
        public Class<? extends IDataAtom> getType() {
            return c;
        }

    }

    public final DAParameterList parameterList;
    public final Action action;
    public DAParameterList response;

    /**
     * Valid remote actions and their parameters.
     */
    public enum Action implements IRemoteActionType {
        REGISTER_LOG_SERVER(Parms.OK, Parms.SENDER_IP),
        UNREGISTER_LOG_SERVER(Parms.OK, Parms.SENDER_IP),
        GET_LOG_SERVER(Parms.DEST_IP, Parms.SENDER_IP),
        ;

        @Override
        public DAText getName() {
            return name;
        }

        @Override
        public List<IParameterType> getInput() {
            return input;
        }

        @Override
        public IParameterType getResultType() {
            return result;
        }

        private final DAText name;
        private final List<IParameterType> input;
        private final IParameterType result;

        Action(IParameterType r, IParameterType... in) {
            name = DAText.create(this.toString());
            input = Collections.unmodifiableList(Arrays.asList(in));
            result = r;
        }

    }

    public DAServerMessage(Action action, DAParameterList pl) {
        if (! pl.validate(action)) {
            throw new RuntimeException("Paraneter list not matching action!");
        }
        this.parameterList = pl;
        this.action = action;
        response = null;
    }

    @Deprecated
    public DAServerMessage() {
        parameterList = null;
        action = null;
        response = null;
    }

    @Override
    public void toStream(final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        parameterList.toStream(stream);
        stream.writeUTF(action.toString());
        stream.writeBoolean((response != null));
        if (response != null) {
            response.toStream(stream);
        }
    }

    @Override
    public DAServerMessage fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAParameterList aparm = new DAParameterList().fromStream(stream);
        Action aaction = Action.valueOf(stream.readUTF());
        DAParameterList aresp = null;
        if (stream.readBoolean()) {
            aresp = new DAParameterList().fromStream(stream);
        }
        return new DAServerMessage(aaction, aparm, aresp);
    }

    private static final byte VERSION = 1;

    private DAServerMessage(Action action, DAParameterList pl, DAParameterList response) {
        if (! pl.validate(action)) {
            throw new RuntimeException("Paraneter list not matching action!");
        }
        this.parameterList = pl;
        this.action = action;
        this.response = response;
    }

}
