/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.protocol;

import de.hdc.commonlibrary.data.atom.DAText;

/**
 *
 * @author martin
 */
public interface IParameterType {

    public DAText getName();
    public Class<?> getType();

}
