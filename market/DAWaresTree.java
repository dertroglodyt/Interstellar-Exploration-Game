/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

import de.hdc.commonlibrary.data.ITreeIterator;
import de.hdc.commonlibrary.data.ITreeNode;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DATreeNode;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.compound.DAResult;

/**
 * Tree of all WareTypes (which hold all WareClasses).
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWaresTree extends DATreeNode<DAWaresType, DAWaresType, DAWareClass> {

    private static final long serialVersionUID = SerialUIDPool.UID.DAWaresTree.value();

        public static enum DefWaresTree {
        Blueprint("f0000000000000000000000000000001", "Blaupause", ""),
        Blueprint_Half("f0000000000000000000000000000002", "Halbfertige Güter", "Güter für die Weiterverarbeitung."),
        Blueprint_Finished("f0000000000000000000000000000003", "Fertigwaren", "Alle Arten von Fertigwaren."),

        Blueprint_Module("f0000000000000000000000000000004", "Module", ""),
        Blueprint_Module_Mini("f0000000000000000000000000000005", "Mini", "Die kleinste Sorte Module mit einem Volumen von bis zu 8 Litern. (Kleiner als ein Müllkorb)"),
        Blueprint_Module_Small("f0000000000000000000000000000006", "Small", "Kleine Module mit einem Volumen von bis zu 1 m³. (Mülltonne)"),
        Blueprint_Module_Medium("f0000000000000000000000000000007", "Medium", "Mittlere Module mit einem Volumen von bis zu 125 m³. (Omnibus)"),
        Blueprint_Module_Large("f0000000000000000000000000000008", "Large", "Große Module mit einem Volumen von bis zu 8.000 m³. (Öltanker)"),
        Blueprint_Module_Giant("f0000000000000000000000000000009", "Giant", "Riesige Module mit einem Volumen von bis zu 125.000 m³. (Graf Zeppelin)"),

        Blueprint_ModuleGroup("f0000000000000000000000000000010", "Modulgruppe", ""),
        Blueprint_ModuleGroup_Mini("f0000000000000000000000000000011", "Mini", "Die kleinste Sorte Modulgruppe mit einem Volumen von weniger als 1 m³. (Kleiner als ein Müllkorb)"),
        Blueprint_ModuleGroup_Small("f0000000000000000000000000000012", "Small", "Kleine Modulgruppe mit einem Volumen von mehr als 1 m³. (Mülltonne)"),
        Blueprint_ModuleGroup_Medium("f0000000000000000000000000000013", "Medium", "Mittlere Modulgruppe mit einem Volumen von mehr als 125 m³. (Omnibus)"),
        Blueprint_ModuleGroup_Large("f0000000000000000000000000000014", "Large", "Große Modulgruppe mit einem Volumen von mehr als 8.000 m³. (Öltanker)"),
        Blueprint_ModuleGroup_Giant("f0000000000000000000000000000015", "Giant", "Riesige Modulgruppe mit einem Volumen von mehr als 125.000 m³. (Graf Zeppelin)"),

        Blueprint_Ship("f0000000000000000000000000000016", "Schiff", ""),
        Blueprint_Ship_Drone("f0000000000000000000000000000017", "Drone", "Kleinste Sorte Raumschiff mit einem Volumen von weniger als 8 m³ (Minibus). Zu klein für menschliche Besatzung."),
        Blueprint_Ship_Shuttle("f0000000000000000000000000000018", "Shuttle", "Raumschiffe bis 50 m³. (Kleinbus/Van)"),
        Blueprint_Ship_Corvette("f0000000000000000000000000000019", "Korvette", "Raumschiffe bis 200 m³. (Omnibus)"),
        Blueprint_Ship_Frigate("f0000000000000000000000000000020", "Fregatte", "Raumschiffe bis 1.000 m³. (Boeing 777)"),
        Blueprint_Ship_Destroyer("f0000000000000000000000000000021", "Zerstörer", "Raumschiffe bis 5.000 m³. (Antonow An-255)"),
        Blueprint_Ship_Cruiser("f0000000000000000000000000000022", "Kreuzer", "Raumschiffe bis 20.000 m³. (Öltanker)"),
        Blueprint_Ship_Battleship("f0000000000000000000000000000023", "Schlachtschiff", "Raumschiffe bis 100.000 m³. (Graf Zeppelin)"),
        Blueprint_Ship_Carrier("f0000000000000000000000000000024", "Trägerschiff", "Raumschiffe bis 1.000.000 m³. (Pyramide)"),
        Blueprint_Ship_Capital("f0000000000000000000000000000025", "Großkampfschiff", "Raumschiffe bis 10.000.000 m³. (...)"),
        Blueprint_Station("f0000000000000000000000000000026", "Station", "Alles, was sich in ein Orbit um einen Himmelskörper bringen lässt. Die Größe variiert von einem kleinen Vorposten / Forschungsstation bis zu gigantischen Handelszentren."),

        Goods("f0000000000000000000000000000027", "Güter", ""),
        Goods_Raw("f0000000000000000000000000000028", "Rohstoffe", ""),
        Goods_Gas("f0000000000000000000000000000029", "Gas", "Alle Arten Gase, wie Sauerstoff, Wasserstoff, Luft, etc."),
        Goods_Liquid("f0000000000000000000000000000030", "Flüssig", "Alle Arten Flüssigkeiten, wie Wasser, Öl, etc."),
        Goods_Solid("f0000000000000000000000000000031", "Fest", "Alle Arten Feststoffe, wie Erde, etc."),
        Goods_Ore("f0000000000000000000000000000032", "Fest (Erz)", "Alle Arten Erze aus Asteroiden."),
        Goods_Minerals("f0000000000000000000000000000033", "Mineralien", "Alle Arten Mineralien, wie Gold, Edelsteine, Metalle, etc."),
        Goods_Half("f0000000000000000000000000000034", "Halbfertig", "Alle Arten Vorprodukte für die Herstellung von Fertigwaren."),
        Goods_Finished("f0000000000000000000000000000035", "Fertig", "Alle Arten Fertigprodukte."),

        Module("f0000000000000000000000000000036", "Module", ""),
        Module_Mini("f0000000000000000000000000000037", "Mini", "Die kleinste Sorte Module mit einem Volumen von weniger als 1 m³. (Kleiner als ein Müllkorb)"),
        Module_Small("f0000000000000000000000000000038", "Small", "Kleine Module mit einem Volumen von mehr als 1 m³. (Mülltonne)"),
        Module_Medium("f0000000000000000000000000000039", "Medium", "Mittlere Module mit einem Volumen von mehr als 125 m³. (Omnibus)"),
        Module_Large("f0000000000000000000000000000040", "Large", "Große Module mit einem Volumen von mehr als 8.000 m³. (Öltanker)"),
        Module_Giant("f0000000000000000000000000000041", "Giant", "Riesige Module mit einem Volumen von mehr als 125.000 m³. (Graf Zeppelin)"),

        ModuleGroup("f0000000000000000000000000000042", "Modulgruppe", ""),
        ModuleGroup_Mini("f0000000000000000000000000000043", "Mini", "Die kleinste Sorte Modulgruppe mit einem Volumen von weniger als 1 m³. (Kleiner als ein Müllkorb)"),
        ModuleGroup_Small("f0000000000000000000000000000044", "Small", "Kleine Modulgruppe mit einem Volumen von mehr als 1 m³. (Mülltonne)"),
        ModuleGroup_Medium("f0000000000000000000000000000045", "Medium", "Mittlere Modulgruppe mit einem Volumen von mehr als 125 m³. (Omnibus)"),
        ModuleGroup_Large("f0000000000000000000000000000046", "Large", "Große Modulgruppe mit einem Volumen von mehr als 8.000 m³. (Öltanker)"),
        ModuleGroup_Giant("f0000000000000000000000000000047", "Giant", "Riesige Modulgruppe mit einem Volumen von mehr als 125.000 m³. (Graf Zeppelin)"),

        Ship("f0000000000000000000000000000048", "Schiff", ""),
        Ship_Drone("f0000000000000000000000000000049", "Drone", "Kleinste Sorte Raumschiff mit einem Volumen von weniger als 8 m³ (Minibus). Zu klein für menschliche Besatzung."),
        Ship_Shuttle("f0000000000000000000000000000050", "Shuttle", "Raumschiffe bis 50 m³. (Kleinbus/Van)"),
        Ship_Corvette("f0000000000000000000000000000051", "Korvette", "Raumschiffe bis 200 m³. (Omnibus)"),
        Ship_Frigate("f0000000000000000000000000000052", "Fregatte", "Raumschiffe bis 1.000 m³. (Boeing 777)"),
        Ship_Destroyer("f0000000000000000000000000000053", "Zerstörer", "Raumschiffe bis 5.000 m³. (Antonow An-255)"),
        Ship_Cruiser("f0000000000000000000000000000054", "Kreuzer", "Raumschiffe bis 20.000 m³. (Öltanker)"),
        Ship_Battleship("f0000000000000000000000000000055", "Schlachtschiff", "Raumschiffe bis 100.000 m³. (Graf Zeppelin)"),
        Ship_Carrier("f0000000000000000000000000000056", "Trägerschiff", "Raumschiffe bis 1.000.000 m³. (Pyramide)"),
        Ship_Capital("f0000000000000000000000000000057", "Großkampfschiff", "Raumschiffe bis 10.000.000 m³. (...)"),
        Station("f0000000000000000000000000000058", "Station", "Alles, was sich in ein Orbit um einen Himmelskörper bringen lässt. Die Größe variiert von einem kleinen Vorposten / Forschungsstation bis zu gigantischen Handelszentren."),
        ;

        public final DAWaresType fWaresType;
        private String path;

        DefWaresTree(String id, String displayName, String description) {
            String name = name();
            String wholePath = name().replace("_", "/");
            if (wholePath.lastIndexOf('/') > 0) {
                path = "/" + wholePath.substring(0, wholePath.lastIndexOf('/')+1);
                name = wholePath.substring(wholePath.lastIndexOf('/')+1);
            } else {
                path = "";
            }
            fWaresType = new DAWaresType(name, displayName, description, new DAUniqueID(id));
        }

        public static DefWaresTree byName(String name) {
            return valueOf(name.trim().replace(" ", "_"));
        }

        public static DAUniqueID idByName(String name) {
            return valueOf(name.trim().replace(" ", "_")).fWaresType.getItemID();
        }

    };

    public static final DAWaresType UNKNOWN = new DAWaresType("Unknown", "unknown", "NOT a valid DVCsmWaresType!"
            , new DAUniqueID());
    /**
//    public static enum Root {
//        Blueprints(-1, -2, -3, -4),
//        Goods(-5, -6),
//        Modules(-7, -8),
//        Ships(-9);
//
//        private int[] rootIDs;
//        Root(int... id) {
//            rootIDs = new int[id.length];
//            for (int i=0; i < id.length; i++) {
//                rootIDs[i] = id[i];
//            }
//        }
//
//        public int[] getRootIDs() {
//            return rootIDs;
//        }
//
//        public static Vector<DAInteger> combine(Root... roots) {
//            Vector<DAInteger> r = new Vector<DAInteger>();
//            for (int i=0; i < roots.length; i++) {
//                int[] z = roots[i].getRootIDs();
//                for (int k=0; k < z.length; k++) {
//                    r.add(new DAInteger(z[k]));
//                }
//            }
//            return r;
//        }
//    };
     */

    public DAWaresTree() {
        super(DAWaresType.class, DAWaresType.class, DAWareClass.class, new DALine("WaresTree"));
        for (DefWaresTree dw : DefWaresTree.values()) {
            add(dw.fWaresType, dw.path);
        }
    }

    public DAWaresType getTypeByID(DAUniqueID id) {
        return (DAWaresType) getRoot().get(id);
    }

    public DAResult<?> addWareClass(DAWareClass wc, DAUniqueID typeID) {
        DAWaresType wt = getTypeByID(typeID);
        if (wt == null) {
            DAResult<?> r = DAResult.createWarning("WaresType <" + wc.getTypeID() + "> for "
                    + wc + " not found in Tree!", "DVCsmWaresTree.addWareClass");
            r.log();
            return r;
        }
        wt.add(wc);
        notifyListener(this);
        return DAResult.createOK("ok", "DVCsmWaresTree.addWareClass");
    }

    public DAWareClass getWareClass(DALine wareClassName) {
        ITreeIterator i = getRoot().treeIterator();
        while (i.hasNext()) {
            ITreeNode tn = i.next();
            if (tn instanceof DAWareClass) {
                if (((DAWareClass) tn).getName().compareTo(wareClassName) == 0) {
                    return (DAWareClass) tn;
                }
            }
        }
        return null;
    }

    public DAWareClass getWareClass(DAUniqueID wareClassID) {
        ITreeNode tn = getRoot().get(wareClassID);
        if (tn instanceof DAWareClass) {
            return (DAWareClass) tn;
        } else {
            return null;
        }
    }

    public DAWaresType getWaresType(DAUniqueID typeID) {
        ITreeNode tn = getRoot().get(typeID);
        if (tn instanceof DAWaresType) {
            return (DAWaresType) tn;
        } else {
            return null;
        }
    }

    /**
     * Removes all nodes not instanceof a given class.
     * @param c
     * @param onlyPublic
     */
    public void removeOtherThan(Class<?> c, boolean onlyPublic) {
        Iterator<ITreeNode> i = getRoot().treeIterator();
        while (i.hasNext()) {
            ITreeNode tn = i.next();
            if (! (tn instanceof DAWaresType)) {
                continue;
            }
            DAWaresType w = (DAWaresType) tn;
            if (w.getItemID().isValid()) {
                for (DAWareClass wc : w.getWareClasses().values()) {
                    if ((! c.isAssignableFrom(wc.getClass()))
                            || (onlyPublic && (! wc.isPublic()))) {
//                        DVCErrorHandler.createDebug("Removing WC " + wc + " in WT " + w, "DAWaresTree.removeOtherThan");
                        w.remove(wc);
                    }
                }
            }
        }
        // Delete all tree branches without children.
        boolean deleted = true;
        while (deleted) {
            deleted = false;
            i = getRoot().treeIterator();
            while (i.hasNext()) {
                ITreeNode tn = i.next();
                if (! (tn instanceof DAWaresType)) {
                    continue;
                }
                DAWaresType w = (DAWaresType) tn;
                if ((w.getChildCount() == 0) && (! w.isRoot())) {
//                    DVCErrorHandler.createDebug("Removing WT " + w, "DAWaresTree.removeOtherThan");
                    i.remove();
                    deleted = true;
                }
            }
        }
        notifyListener(this);
    }

//    /**
//     * Reomves all nodes not having at least one WareClass in a market transaction.
//     * @param m
//     */
//    public void removeUnused(DAMarket m) {
//        Iterator<ITreeNode> i = getRoot().treeIterator();
//        while (i.hasNext()) {
//            DAWaresType w = (DAWaresType) i.next();
//            if (w.getItemID().isValid()
//                    && (m.getBuying(w).isEmpty())
//                    && (m.getSelling(w).isEmpty())
//                    && (! w.isRoot())) {
//                i.remove();
//            }
//        }
//        // Delete all tree branches without children.
//        boolean deleted = true;
//        while (deleted) {
//            deleted = false;
//            i = getRoot().treeIterator();
//            while (i.hasNext()) {
//                DAWaresType w = (DAWaresType) i.next();
//                if ((w.getChildCount() == 0) && (! w.isRoot())) {
//                    i.remove();
//                    deleted = true;
//                }
//            }
//        }
//        notifyListener(this);
//    }

    @Deprecated
    @Override
    public String toParseString(String levelTab) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DAWaresTree parse(String value) {
        throw new UnsupportedOperationException();
    }

    // TreeModel interface
    @Deprecated
    @Override
    public DATreeNode<DAWaresType, DAWaresType, DAWareClass> getRoot() {
        return (DATreeNode<DAWaresType, DAWaresType, DAWareClass>) super.getRoot();
    }

//    @Override
//    public DAWaresTree clone() {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        DVCErrorHandler.raiseError(DAResult.createWarning("Not supported yet.", "DVCsmWareTree.readExternal"));
//        throw new UnsupportedOperationException();
        super.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
//        DVCErrorHandler.raiseError(DAResult.createWarning("Not supported yet.", "DVCsmWareTree.writeExternal"));
//        throw new UnsupportedOperationException();
        super.writeExternal(out);
    }

    private DAResult<?> add(DAWaresType item, String path) {
        DAResult<?> r = null;
        DAWaresType wt = getTypeByID(item.getItemID());
        if (wt != null) {
            return DAResult.createOK("ok. was already there.", "DVCsmWaresType.add");
        }
        r = getRoot().add(new DALine(path), item, false);
        notifyListener(this);
        return r;
    }

}
