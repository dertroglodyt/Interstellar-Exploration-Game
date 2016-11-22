/*
 *  Created by DerTroglodyt on 2016-11-21 14:23
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DataAtom;

public class DAWareTypeTree extends DataAtom {

    public static DAWareTypeTree create() {
        return new DAWareTypeTree(DAWareTypeTreeNode.create(null, DAText.create("WareTypeTree")
                , DAText.create("You should never read this...")));
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public DAWareTypeTreeNode getWareType(DAUniqueID id) {
        return root.findChild(id);
    }

    public DAWareClass getWareClass(DAUniqueID id) {
        return root.findWareClass(id);
    }

    public DAWareTypeTreeNode getWareClassType(DAUniqueID id) {
        return root.findWareClassType(id);
    }

    @Override
    public void toStream(@NonNull final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        root.toStream(stream);
    }

    @Override
    public DAWareTypeTree fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAWareTypeTreeNode r = new DAWareTypeTreeNode().fromStream(stream);
        return new DAWareTypeTree(r);
    }

    private static final byte VERSION = 1;
    private final DAWareTypeTreeNode root;

    private DAWareTypeTree(DAWareTypeTreeNode root) {
        super();
        this.root = root;
    }

    @Deprecated
    public DAWareTypeTree() {
        super();
        root = null;
    }
}
