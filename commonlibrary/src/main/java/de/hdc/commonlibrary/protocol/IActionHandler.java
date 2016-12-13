package de.hdc.commonlibrary.protocol;

/**
 * Created by DerTroglodyt on 2016-12-13 20:10.
 * Email example@gmail.com
 * Copyright by HDC, Germany
 */

public interface IActionHandler {

    public DARemoteAction handle(DARemoteAction action);
}
