/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.atom.DAVector;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.market.IDAWare;
import de.hdc.commonlibrary.moduleclass.DABasicModuleClass;
import de.hdc.commonlibrary.util.Log;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAbmWaresContainer extends DABasicModule {

    private static final long serialVersionUID = SerialUID.DAbmWaresContainer.value();
//    private static final String DEF_ICON = "/datavault/common/space/icon/DABMWaresContainer.gif";
//    static {
//        DVCBasicDataModel.register(DAbmWaresContainer.class, createImageIcon(DEF_ICON));
//    }
    private DAWareAmount content;

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

        if ((version < 1) || (version > 1)) {
            Log.fatal(DAbmWaresContainer.class, "readExternal: Wrong version number " + version);
        }
        content.readExternal(in);
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

        content.writeExternal(out);
    }

    @Deprecated
    public DAbmWaresContainer() {
        super();
        content = new DAWareAmount();
    }

    public DAbmWaresContainer(DAbmWaresContainerClass wcc, DATransform trans) {
        super(wcc, trans);

    }

    public DAbmWaresContainer(IDAWare ware, int aAmount, DABasicModuleClass aContainerClass, DATransform trans) {
        super(aContainerClass, trans);
        if (ware.getWareClass().getSize().getMaxVolume().isGreaterThan(wareClass.getSize().getMaxVolume())) {
            DAResult.createFatal("Ware size <" + ware.getWareClass().getSize()
                    + "> does not match container size <" + wareClass.getSize()
                    + ">", "DABMWaresContainer()");
        }
        if (ware.getVolume().scale(aAmount).isGreaterThan(wareClass.getSize().getMaxVolume())) {
            DAResult.createFatal("Amount of ware <" + ware.getWareClass().getSize()
                    + "> does not match container size <" + wareClass.getSize()
                    + ">", "DABMWaresContainer()");
        }
        content = new DAWareAmount(ware, new DAValue<Pieces>(aAmount, NewUnits.PIECES));
        setName(new DALine("Conti"));
    }

    @Override
    public String toString() {
        return wareClass + " " + content.getWare()
                + " (" + getActualAmount() + "/" + ((wareClass != null)?getMaxAmount():"?") + ")";
    }

    @Override
    public String toParseString(String levelTab) {
        return toString() + (isUnique()?" <" + getItemID() + ">":"");
    }

    @Override
    public DAbmWaresContainer parse(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DAValue<Mass> getMass() {
        return super.getMass().scale(getActualAmount().doubleValue(NewUnits.PIECES));
    }

//    @Override
//    public double getMassKG() {
//        return physical.getMass().getBaseValue()
//                + getActualAmount().getValue() * content.getWare().getWareClass().getKGperM3();
//    }

    public DAResult<?> add(DAWareAmount w) {
        if (w.getWare().isUnique() || content.getWare().isUnique()) {
            return DAResult.createWarning("Ware is unique! Can not stack.", "DABMWaresContainer.add");
        }
        if (!w.getWare().equals(content.getWare())) {
            return DAResult.createWarning("Wares do not match.", "DABMWaresContainer.add");
        }
        if (content.getAmount().add(w.getAmount()).isGreaterThan(getMaxAmount())) {
            return DAResult.createWarning("Amount does not fit into conatiner!", "DABMWaresContainer.add");
        }
        content = content.add(w.getAmount());
        return DAResult.createOK("ok", "DABMWaresContainer.add");
    }

    public DAResult<?> add(IDAWare w, DAValue<Pieces> x) {
        return add(new DAWareAmount(w, x));
    }

    public DAResult<?> sub(DAWareAmount w) {
        if (!w.getWare().equals(content.getWare())) {
            return DAResult.createWarning("Wares do not match.", "DABMWaresContainer.add");
        }
        if (content.getAmount().isLessThan(w.getAmount())) {
            return DAResult.createWarning("Conatiner does not hold given amount!", "DABMWaresContainer.sub");
        }
        content = content.sub(w.getAmount());
        return DAResult.createOK("ok", "DABMWaresContainer.sub");
    }

    public  DAResult<?> sub(IDAWare w, DAValue<Pieces> x) {
        return sub(new DAWareAmount(w, x));
    }

    /**
     * Takes ware and packages it into containers.
     * Tries to use containers with minimum size required.
     * If that container size is to big for target storage the amount is split up into
     * smaller containers that fit into the target storage.
     * @param ware Ware to store in container
     * @param a Amount of ware
     * @param sto Target storage
     * @return The containers holding the packaged ware.
     */
    public static DAVector<DAbmWaresContainer> packageWare(IDAWare ware, DAValue<Pieces> a, DAbmStorage sto) {
        if (ware.isUnique() && (a.doubleValue(NewUnits.PIECES) != 1.0)) {
            Log.warn(DAbmWaresContainer.class, "Ware is unique! Can not stack.", "DABMWaresContainer.packageWare");
            return null;
        }
        DAWareClass.Size size = ware.getWareClass().getSize();
        DAValue<Volume> vol = size.getMaxVolume().scale(a.doubleValue(NewUnits.PIECES));
        // Maximum volume in storage
        DAValue<Volume> volSto = sto.getMaxSpace();

        // find smallest container for ware size that still fits into target storage
        DABasicModuleClass cwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerMini.getWareClass();

        if (!cwc.getSize().isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerSmall.getWareClass();
            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
                cwc = newcwc;
            }
        }
        if (!cwc.getSize().isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerMedium.getWareClass();
            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
                cwc = newcwc;
            }
        }
        if (!cwc.getSize().isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerLarge.getWareClass();
            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
                cwc = newcwc;
            }
        }
        if (!cwc.getSize().isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerGiant.getWareClass();
            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
                cwc = newcwc;
            }
        }
        // fill n containers until full amount ist packed
        // max amount per container

        int x = (int) Math.floor(cwc.getSize().getMaxVolume().doubleValueBase() / size.getMaxVolume().doubleValueBase());
        int remaining = (int) a.doubleValueBase();
        DAVector<DAbmWaresContainer> v = new DAVector<DAbmWaresContainer>(DAbmWaresContainer.class);
        while (remaining > 0) {
            int y = Math.min(x, remaining);
            DAbmWaresContainer cont = new DAbmWaresContainer(ware, y, cwc, null);
            remaining -= y;
            v.add(cont);
        }
        return v;
    }

//    @Override
//    public void resolveOther(DAModuleContainer aParentContainer) {
//        super.resolveOther(aParentContainer);
//        content.resolveOther(aParentContainer);
//    }

    public DAWareClass getContentType() {
        return content.getWare().getWareClass();
    }

    public boolean contains(Class<?> wc) {
        return (content.getWare().getWareClass().getClass().isAssignableFrom(wc));
    }

    public IDAWare takeOne() {
        if (content.getAmount().doubleValue() == 0) {
            return null;
        }
        content.sub(new DAValue<Pieces>(1, NewUnits.PIECES));
        IDAWare w = content.getWare();
        if (w.isUnique()) {
            return w;
        } else {
            return content.getWare();
        }
    }

    public DAWareAmount getContent() {
        return content;
    }

    public DAValue<Pieces> getActualAmount() {
        return content.getAmount();
    }

    public DAValue<Pieces> getMaxAmount() {
        DAValue<Volume> volC = wareClass.getSize().getMaxVolume();
        DAValue<Volume> volW = getContentType().getSize().getMaxVolume();
        int x = (int) (volC.doubleValueBase() / volW.doubleValueBase());
        return new DAValue<Pieces>(x, NewUnits.PIECES);
    }

    public DAValue<Pieces> getFreeAmount() {
        return getMaxAmount().sub(getActualAmount());
    }

//    @Override
//    public DAbmWaresContainer clone() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
        super.longTick(actWorldTime, t);
        if (getParentContainer() != null) {
            if (getParentContainer().isOnline()) {
                if (! isOnline()) {
                    setOnline(true);
                }
            }
        }
    }

}
