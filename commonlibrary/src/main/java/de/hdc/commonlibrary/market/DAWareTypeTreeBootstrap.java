/*
 *  Created by DerTroglodyt on 2016-11-22 10:09
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;

/**
 * Initial tree of all WareTypes (which hold all WareClasses).
 * Used to bootstrap.
 * @author martin
 */
public final class DAWareTypeTreeBootstrap {

        private static enum DefWaresTree {
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

            public final DAUniqueID id;
            public final DAText displayName;
            public final DAText description;
            public final String path;

        DefWaresTree(String id, String displayName, String description) {
            this.id = DAUniqueID.parse(id);
            String dp = name();
            if (displayName.lastIndexOf(' ') > 0) {
                dp = displayName.substring(displayName.lastIndexOf('/')+1);
            }
            this.displayName = DAText.create(dp);
            this.description = DAText.create(description);

            String name = name();
            String wholePath = name().replace("_", "/");
            if (wholePath.lastIndexOf('/') > 0) {
                path = "/" + wholePath.substring(0, wholePath.lastIndexOf('/')+1);
                name = wholePath.substring(wholePath.lastIndexOf('/')+1);
            } else {
                path = "";
            }
        }

    }

    private enum DefWareClass {
        Electrical_Power(DefWaresTree.Goods_Raw, "00000000000000000000000000000001"),
        Anti_Matter(DefWaresTree.Goods_Raw, "00000000000000000000000000000002"),
        Blueprint(DefWaresTree.Goods, "00000000000000000000000000000003"),
        Person(DefWaresTree.Goods, "00000000000000000000000000000004"),

        Standard_Container_Mini(DefWaresTree.Goods, "00000000000000000000000000000100"),
        Standard_Container_Small(DefWaresTree.Goods, "00000000000000000000000000000101"),
        Standard_Container_Medium(DefWaresTree.Goods, "00000000000000000000000000000102"),
        Standard_Container_Large(DefWaresTree.Goods, "00000000000000000000000000000103"),
        Standard_Container_Giant(DefWaresTree.Goods, "00000000000000000000000000000104"),

        O2(DefWaresTree.Goods_Gas, "00000000000000000000000000000200"),
        H2(DefWaresTree.Goods_Gas, "00000000000000000000000000000201"),
        N2(DefWaresTree.Goods_Gas, "00000000000000000000000000000202"),
        He(DefWaresTree.Goods_Gas, "00000000000000000000000000000203"),
        Xe(DefWaresTree.Goods_Gas, "00000000000000000000000000000204"),
        Air(DefWaresTree.Goods_Gas, "00000000000000000000000000000205"),

        Ice(DefWaresTree.Goods_Liquid, "00000000000000000000000000000300"),
        Dest_Water(DefWaresTree.Goods_Liquid, "00000000000000000000000000000301"),
        Drink_Water(DefWaresTree.Goods_Liquid, "00000000000000000000000000000302"),

        Olivin(DefWaresTree.Goods_Ore, "00000000000000000000000000000400"),
        Pyroxen(DefWaresTree.Goods_Ore, "00000000000000000000000000000401"),
        Plagioklas(DefWaresTree.Goods_Ore, "00000000000000000000000000000402"),
        Iron_Meteorite(DefWaresTree.Goods_Ore, "00000000000000000000000000000403"),
        Ataxit(DefWaresTree.Goods_Ore, "00000000000000000000000000000404"),

        Trade_Goods(DefWaresTree.Goods_Finished, "00000000000000000000000000001000"),
        Dry_Food(DefWaresTree.Goods_Finished, "00000000000000000000000000001001"),
        Frozen_Food(DefWaresTree.Goods_Finished, "00000000000000000000000000001002"),
        Fresh_Food(DefWaresTree.Goods_Finished, "00000000000000000000000000001003"),
        Soft_Drinks(DefWaresTree.Goods_Finished, "00000000000000000000000000001004"),
        Spirits(DefWaresTree.Goods_Finished, "00000000000000000000000000001005"),

        Standard_Station(DefWaresTree.Station, "00000000000000000000000000010000"),

        Standard_Corvette(DefWaresTree.Ship_Corvette, "00000000000000000000000000011000"),

        Factory_Frigate(DefWaresTree.Module_Giant, "00000000000000000000000000020001"),
        Station_Hangar(DefWaresTree.Module_Giant, "00000000000000000000000000020002"),
        Station_Storage(DefWaresTree.Module_Giant, "00000000000000000000000000020003"),
        Station_Rentable_Storage(DefWaresTree.Module_Giant, "00000000000000000000000000020004"),;

        public final DAText displayName;
        public final DAUniqueID fClassID;
        public final DAUniqueID typeID;

        DefWareClass(DefWaresTree wtID, String classID) {
            displayName = DAText.create(name().replace('_', ' '));
            fClassID = DAUniqueID.parse(classID);
            typeID = wtID.id;
        }
    }

    public static final DAWareTypeTreeNode UNKNOWN = DAWareTypeTreeNode.create(null, DAText.create("Unknown")
            , DAText.create("NOT a valid DVCsmWaresType!"));

    public static DAWareTypeTreeNode create() {
        final DAWareTypeTreeNode root = DAWareTypeTreeNode.create(null, DAText.create("Root node.")
                , DAText.create("You should never see this!"));
        for (DefWaresTree dwf : DefWaresTree.values()) {
//            System.out.println(dwf.name() + "###" + dwf.path);
            if (dwf.path.isEmpty()) {
                root.addChild(DAWareTypeTreeNode.create(root, dwf.displayName, dwf.description, dwf.id));
            } else {
                String pn = dwf.path.substring(1);
                pn = pn.substring(0, pn.indexOf('/'));
                final DAText parentName = DAText.create(pn);
                final DAWareTypeTreeNode parent = root.findChildByName(parentName);
                parent.addChild(DAWareTypeTreeNode.create(parent, dwf.displayName, dwf.description, dwf.id));
            }
        }
        for (DefWareClass t : DefWareClass.values()) {
//            System.out.println(t.displayName);
            DAWareTypeTreeNode parent = root.findChild(t.typeID);
            parent.addWareClass(DAWareClass.create(t.fClassID, t.typeID, t.displayName));
        }
        return root;
    }

    private DAWareTypeTreeBootstrap() {
    }
}
