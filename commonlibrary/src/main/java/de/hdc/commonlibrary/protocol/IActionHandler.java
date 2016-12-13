package de.hdc.commonlibrary.protocol;

import de.hdc.commonlibrary.data.compound.DAResult;

/**
 * Created by DerTroglodyt on 2016-12-13 20:10.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public interface IActionHandler {

    public DAResult<?> handle(DARemoteAction action);
}
