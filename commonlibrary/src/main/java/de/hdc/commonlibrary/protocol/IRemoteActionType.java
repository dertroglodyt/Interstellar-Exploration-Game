/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.protocol;

import java.util.List;

import de.hdc.commonlibrary.data.atom.DAText;

/**
 *
 * @author martin
 */
public interface IRemoteActionType {

    public DAText getName();
    public List<IParameterType> getInput();
    public IParameterType getResultType();

}
