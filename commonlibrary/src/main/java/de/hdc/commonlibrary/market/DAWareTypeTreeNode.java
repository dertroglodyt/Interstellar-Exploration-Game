/*
 *  Created by DerTroglodyt on 2016-11-21 14:23
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DataAtom;

public class DAWareTypeTreeNode extends DataAtom {

    public static DAWareTypeTreeNode create(DAWareTypeTreeNode parent, DAText name, DAText description) {
        return new DAWareTypeTreeNode(name, description, parent);
    }

    /**
     * Used by DAWareTypeTreeBootstrap.
     * @param parent
     * @param name
     * @param description
     * @param id
     * @return
     */
    @Deprecated
    public static DAWareTypeTreeNode create(DAWareTypeTreeNode parent, DAText name, DAText description
            , DAUniqueID id) {
        return new DAWareTypeTreeNode(name, description, parent, id);
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public DAUniqueID getID() {
        return id;
    }

    public void addChild(DAWareTypeTreeNode child) {
        children.add(child);
    }

    public DAWareTypeTreeNode removeChild(DAWareTypeTreeNode child) {
        for (DAWareTypeTreeNode n : children) {
            if (n.compareTo(child) == 0) {
                wareClasses.remove(n);
                return n;
            }
        }
        return null;
    }

    public DAWareTypeTreeNode findChild(DAUniqueID id) {
        for (DAWareTypeTreeNode tn : children) {
            if (tn.getID().compareTo(id) == 0) {
                return tn;
            }
            final DAWareTypeTreeNode n = tn.findChild(id);
            if (n != null) {
                return n;
            }
        }
        return null;
    }

    /**
     * Used by DAWareTypeTreeBootstrap.
     * Gets first direct child of this node with given name.
     * @param name
     * @return
     */
    public DAWareTypeTreeNode findChildByName(DAText name) {
        for (DAWareTypeTreeNode tn : children) {
            if (tn.getDisplayName().compareTo(name) == 0) {
                return tn;
            }
        }
        return null;
    }

    public void addWareClass(DAWareClass wareClass) {
        wareClasses.add(wareClass);
    }

    public DAWareClass removeWareClass(DAWareClass wareClass) {
        for (DAWareClass w : wareClasses) {
            if (w.compareTo(wareClass) == 0) {
                wareClasses.remove(w);
                return w;
            }
        }
        return null;
    }

    public DAWareClass findWareClass(DAUniqueID wareClassID) {
        for (DAWareClass wc : wareClasses) {
            if (wc.id.compareTo(wareClassID) == 0) {
                return wc;
            }
        }
        for (DAWareTypeTreeNode tn : children) {
            DAWareClass wc = tn.findWareClass(wareClassID);
            if (wc != null) {
                return wc;
            }
        }
        return null;
    }

    public DAWareTypeTreeNode findWareClassType(DAUniqueID wareClassID) {
        for (DAWareClass wc : wareClasses) {
            if (wc.id.compareTo(wareClassID) == 0) {
                return this;
            }
        }
        for (DAWareTypeTreeNode tn : children) {
            DAWareTypeTreeNode wc = tn.findWareClassType(wareClassID);
            if (wc != null) {
                return wc;
            }
        }
        return null;
    }

    public DAText getDisplayName() {
        return name;
    }

    public DAText getDescription() {
        return description;
    }

    @Override
    public void toStream(final DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        name.toStream(stream);
        description.toStream(stream);
        parent.toStream(stream);
        stream.writeInt(children.size());
        for (DAWareTypeTreeNode wn : children) {
            wn.toStream(stream);
        }
        stream.writeInt(wareClasses.size());
        for (DAWareClass wc : wareClasses) {
            wc.toStream(stream);
        }
    }

    @Override
    public DAWareTypeTreeNode fromStream(final DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        DAText aname = new DAText().fromStream(stream);
        DAText adescription = new DAText().fromStream(stream);
        DAWareTypeTreeNode aparent = new DAWareTypeTreeNode().fromStream(stream);
        DAWareTypeTreeNode n = new DAWareTypeTreeNode(aname, adescription, aparent);
        int x = stream.readInt();
        for (int i = 0; i < x; i++) {
            n.addChild(new DAWareTypeTreeNode().fromStream(stream));
        }
        x = stream.readInt();
        for (int i = 0; i < x; i++) {
            DAWareClass wc = new DAWareClass().fromStream(stream);
            n.addWareClass(wc);
        }
        return n;
    }

    private static final byte VERSION = 1;

    private final DAUniqueID id;
    private DAText name;
    private DAText description;
    private DAWareTypeTreeNode parent;
    private final ArrayList<DAWareTypeTreeNode> children;
    private final ArrayList<DAWareClass> wareClasses;

    private DAWareTypeTreeNode(DAText name, DAText description, DAWareTypeTreeNode parent) {
        this(name, description, parent, DAUniqueID.createRandom());
    }

    private DAWareTypeTreeNode(DAText name, DAText description, DAWareTypeTreeNode parent, DAUniqueID id) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.children = new ArrayList<>(0);
        this.wareClasses = new ArrayList<>(0);
    }

    @Deprecated
    public DAWareTypeTreeNode() {
        super();
        id = null;
        name = null;
        description = null;
        parent = null;
        children = null;
        wareClasses = null;
    }
}
