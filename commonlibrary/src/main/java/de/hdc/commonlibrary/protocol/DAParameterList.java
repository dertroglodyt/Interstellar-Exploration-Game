/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.protocol;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.compound.DAMap;
import de.hdc.commonlibrary.util.Log;

/**
 * List of Parameters in a DARemoteAction.
 * @author martin
 */
@SuppressWarnings("serial")
public class DAParameterList extends DataAtom {

    @Deprecated
    public DAParameterList() {
        super();
        parms = null;
    }

    @Override
    public String toString() {
        return parms.toString();
    }

    public void add(IParameterType type, IDataAtom data) {
        if ((type.getType().equals(data.getClass()))
                || (type.getType().isAssignableFrom(data.getClass()))) {
            parms.set(type.getName(), data);
        } else {
            Log.warn(DAParameterList.class, "add: Parameter <{0}> type does not match data type<{1}>! {2}"
                    , type.getType(), data.getClass(), this);
        }
    }

    public IDataAtom get(IParameterType type) {
        return parms.get(type.getName());
    }

    public int size() {
        return parms.size();
    }

    public boolean contains(Class<IDataAtom> c) {
        for (IDataAtom bdm : parms.getValueList()) {
            if (bdm.getClass().getName().equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates that all parameters for the given parameter type are in parameter list.
     * @param rat
     * @return
     */
    public boolean validate(IRemoteActionType rat) {
        for (IParameterType pt : rat.getInput()) {
            final IDataAtom da = get(pt);
            if ((da == null) && (! pt.getType().getName().equals(pt.getType().getName()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        parms.toStream(stream);
    }

    @Override
    public DAParameterList fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }

        DAMap<DAText, IDataAtom> aparm = new DAMap<>();
        aparm.fromStream(stream);

        return new DAParameterList(parms);
    }

    private static final byte VERSION = 1;
    private static enum DummyParameters implements IParameterType {

        NONE(null),
        NAME(DAText.class),
        ;

        private DAText name;
        private Class<? extends IDataAtom> c;

        DummyParameters(Class<? extends IDataAtom> aClass) {
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

    private final DAMap<DAText, IDataAtom> parms;

    private DAParameterList(DAMap<DAText, IDataAtom> map) {
        super();
        parms = map;
    }

}
