/*
 *  Created by DerTroglodyt on 2016-11-28 11:12
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;

public interface IDASubject extends IDataAtom {

    enum Type {
        AI, NPC, PLAYER, ORGANISATION
    }

    DAUniqueID getId();

    DAText getName();

    Type getType();

}
