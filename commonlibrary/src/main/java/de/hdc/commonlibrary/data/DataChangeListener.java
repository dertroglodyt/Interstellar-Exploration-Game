/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DataChangeListener.java
 *
 * Created on 6. Mai 2005, 02:47
 */

package de.hdc.commonlibrary.data;

/**
 * Interface that allows components to register as a listener for
 * change of the data of a DVCDataModel.
 *
 * @author Martin
 */
@FunctionalInterface
public interface DataChangeListener {
    /** Called by Model to signal change of data
     * @param sender The sending DataAtom.
     */
    public void changeNotify(IDataAtom sender);
}
