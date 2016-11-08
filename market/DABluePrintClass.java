/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import de.dertroglodyt.iegcommon.AssetPool;
import de.dertroglodyt.iegcommon.AssetPool.AssetNameWareClass;
import de.dertroglodyt.iegcommon.astro.DATransform;
import de.dertroglodyt.iegcommon.astro.DAWorldNode;
import de.dertroglodyt.iegcommon.module.DABasicModule;
import de.dertroglodyt.iegcommon.module.DAModuleContainer;
import de.dertroglodyt.iegcommon.module.DAmcShip;
import de.dertroglodyt.iegcommon.moduleclass.DABasicModuleClass;
import de.dertroglodyt.iegcommon.moduleclass.DAModuleContainerClass;
import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAUniqueID;
import de.hdc.commonlibrary.data.types.atom.DAValue;
import de.hdc.commonlibrary.data.types.collection.DAText;
import de.hdc.commonlibrary.data.types.collection.DAVector;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DABluePrintClass extends DAWareClass {

    private static final long serialVersionUID = SerialUIDPool.UID.DABluePrintClass.value();
//    private static final String DEF_ICON = "/datavault/common/space/icon/DABlueprint.gif";
//    static {
//        DVCBasicDataModel.register(DABluePrintClass.class, createImageIcon(DEF_ICON));
//    }

    /** Ware class id of product produced by this blueprint */
    private DAUniqueID productClassID;
    /**
     * Wares needed for production of this product.
     */
    private final DAVector<DABPRecipeItem> components;
    private boolean isContainer;

    /**
     * Calculated time [sec] to produce product in standard factory / shipyard.
     * For ships: Accumulated times to put all components into place.
     */
    private transient final DAValue<Duration> time;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        productClassID.readExternal(in);
        components.readExternal(in);
        isContainer = in.readBoolean();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish changed read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are changed.
         */
        byte version = 1;
        out.writeByte(version);

        productClassID.writeExternal(out);
        components.writeExternal(out);
        out.writeBoolean(isContainer);
    }

    /**
     * @deprecated To be used by serialisation only.
     */
    @Deprecated
    public DABluePrintClass() {
        super();
        productClassID = new DAUniqueID();
        components = new DAVector<DABPRecipeItem>();
        time = new DAValue<Duration>();
    }

    public DABluePrintClass(DALine aName, DAText aDescription, DAWareClass wc) {
        this(aName, aDescription, wc.getItemID());
    }

    private DABluePrintClass(DALine aName, DAText aDescription, DAUniqueID id) {
        super(AssetNameWareClass.BluePrint);
        productClassID = id;
        components = new DAVector<DABPRecipeItem>(DABPRecipeItem.class);
        isContainer = false;
        time = new DAValue<Duration>(0.0, NonSI.MINUTE);
        recalc();
    }

    public DABluePrintClass(DALine aName, DAText aDescription, DAModuleContainer container) {
        super(AssetNameWareClass.BluePrint);
        productClassID = container.getClassID();
        components = new DAVector<DABPRecipeItem>(DABPRecipeItem.class);
        isContainer = true;
        for (DABasicModule m : container.getModules()) {
            components.add(new DABPRecipeItem(m));
        }
        time = new DAValue<Duration>(0.0, NonSI.MINUTE);
        recalc();
    }

//    public DABluePrintClass(DALine aName, DAText aDescription, DAUniqueID aProductClassID
//            , DAVector<DABPRecipeItem> compList, boolean isContainer) {
//        super(AssetNameWareClass.BluePrint);
//        super.setDes
//        productClassID = aProductClassID;
//        components = compList;
//        this.isContainer = isContainer;
//        time = new DAValue<Duration>(0.0, NonSI.MINUTE);
//        recalc();
//    }

    @Override
    public String toString() {
        return getName().toString();
    }


//    @Override
//    public DABluePrintClass clone() {
//        return new DABluePrintClass(getName(), description.clone(), productClassID.clone(), components.clone(), isContainer);
//    }

    public DAUniqueID getProductClassID() {
        return productClassID;
    }

    public void setProductClass(DAWareClass wc) {
        productClassID = wc.getItemID();
        components.clear();
        isContainer = (wc instanceof DAModuleContainerClass);
        recalc();
    }

    public boolean isContainer() {
        return isContainer;
    }

    public DAValue<Pieces> getAmount(DAUniqueID wareClassID) {
        DAValue<Pieces> x = new DAValue<Pieces>(0, NewUnits.PIECES);
        for (DABPRecipeItem i : components) {
            DAUniqueID wa = i.getWareClassID();
            if (wa.equals(wareClassID)) {
                x = x.add(i.getAmount());
            }
        }
        return x;
    }

    public DAValue<Duration> getProductionTime() {
        return time;
    }

    public DAVector<DABPRecipeItem> getComponents() {
        return components;
    }

    public void add(DAWareClass wc, DAValue<Pieces> amount) {
        components.add(new DABPRecipeItem(wc.getItemID(), amount, new DATransform()));
    }

    @Override
    @Deprecated
    public IDAWare getInstance() {
        return null;
    }

//    public DAModuleContainer getModuleContainer() {
//        if (!isContainer) {
//            return null;
//        }
//        DAWareClass wc = DAResourcePool.getWareClass(productClassID);
//        DAModuleContainer mc = (DAModuleContainer) wc.getInstance();
//
//        for (DABPRecipeItem i : components) {
//            mc.addModule((DABasicModule) i.getWareClass().getInstance());
//        }
//        return mc;
//    }

    public IDAWare getProduct(DAWorldNode wn) {
        DAWareClass wc = AssetPool.waresTree.getWareClass(productClassID);
        IDAWare w = wc.getInstance();

        if (isContainer) {
            if ((wn != null) && (w instanceof DAmcShip)) {
                ((DAmcShip) w).init(wn);
            }
            for (DABPRecipeItem i : components) {
                DABasicModule bm = ((DABasicModuleClass) i.getWareClass()).getInstance(i.getTransform());
                ((DAModuleContainer) w).addModule(bm);
            }
        }
        return w;
    }

//    @Override
//    public DVCseBlueprintClass getEditor(EditMode editmode, DVCAbstractUser user) {
//        DVCseBlueprintClass de = new DVCseBlueprintClass(this, editmode, user);
//        addListener(de);
//        return de;
//    }
//
//    public static DVCseWareClass getParentEditor(DAWareClass model, EditMode editmode, DVCAbstractUser user) {
//        DVCseWareClass de = new DVCseWareClass(model, editmode, user);
//        model.addListener(de);
//        return de;
//    }

    public void setFrom(DAModuleContainer cont) {
        productClassID = cont.getClassID();
        components.clear();
        for (DABasicModule bm : cont.getModules()) {
            components.add(new DABPRecipeItem(bm));
        }
        isContainer = true;
        recalc();
    }

//    public final void recalc() {
//        double t = 10; // 10 minutes minimum;
//        for (DABPRecipeItem i : components) {
//            t += i.getWareClass().getSize().getMaxSide().getBaseValue();
//        }
//        // add 10% of final product size
//        t += getSize().getMaxVolume().getBaseValue() * 0.1;
//        time = new DAValue<Duration>(t, NonSI.MINUTE);
//        notifyListener(this);
//    }

//    @Override
//    public void init(int lvl) {
//        super.init(lvl);
//        recalc();
//    }


}
