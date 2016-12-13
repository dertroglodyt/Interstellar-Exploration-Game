/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.protocol;

import de.hdc.commonlibrary.data.compound.DAResult;

/**
 * Dispatches DARemoteActions to the corresponding DataAtom.
 * @author martin
 */
public interface IActionDispatcher {

    /**
     * Executes the given action command on this.
     * @param action The request data.
     * @return The answer data.
     */
    public abstract DAResult<DAParameterList> dispatch(DARemoteAction action);

}
