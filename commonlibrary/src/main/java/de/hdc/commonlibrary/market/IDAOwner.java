/*
 *  Created by DerTroglodyt on 2016-11-28 11:12
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.compound.DAResult;

public interface IDAOwner extends IDataAtom {

    public DAUniqueID getId();

    public DAText getName();

    public DAResult<IDAWare> transaction(DAMarketTransaction mt);

    public void undoTransaction(DAMarketTransaction mt);


}
