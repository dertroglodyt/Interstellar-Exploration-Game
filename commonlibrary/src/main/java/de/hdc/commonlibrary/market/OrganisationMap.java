/*
 *  Created by DerTroglodyt on 2016-11-28 11:04
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import java.util.concurrent.ConcurrentHashMap;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;

public class OrganisationMap {

    /**
     * todo Only for testing.
     *
     * @param id
     */
    public static IDAOwner getOrganisation(DAUniqueID id) {
        return DAOwnerBasic.create(id, DAText.create(id.toString()));
    }

    private final ConcurrentHashMap<DAUniqueID, IDAOwner> map;

    private OrganisationMap() {
        map = null;
    }
}
