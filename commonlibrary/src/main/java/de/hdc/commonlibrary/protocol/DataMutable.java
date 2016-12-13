/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.protocol;

import java.util.ArrayList;
import java.util.List;

import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.data.compound.DAResult;

/**
 *
 * @author martin
 */
public abstract class DataMutable extends DataAtom {

    /**
     * Executes the given action command on this.
     * @param action The request data.
     * @return The answer data.
     */
    public abstract DAResult<DAParameterList> handle(DARemoteAction action);

    public DataMutable() {
        super();
        fActionDispatcher = null;
        fRemoteListeners = new ArrayList<DataMutable>(0);
    }

    public boolean isOriginal() {
        return (fActionDispatcher == null);
    }

    public void setActionDispatcher(IActionDispatcher dispatcher) {
        fActionDispatcher = dispatcher;
    }

    public IActionDispatcher getActionDispatcher() {
        return fActionDispatcher;
    }

    public void addRemoteListener(DataMutable listener) {
        if (fActionDispatcher != null) {
            throw new IllegalStateException("Remote object can not have remote listeners.");
        }
        fRemoteListeners.add(listener);
    }

    /**
     * Send actions to this dispatcher.
     * If NULL this is the original (server side) object that reports changes to its listeners.
     */
    private transient IActionDispatcher fActionDispatcher;
    /**
     * Report changes to this dispatcher.
     * If NULL this is a remote object.
     */
    private transient final List<DataMutable> fRemoteListeners;

}
