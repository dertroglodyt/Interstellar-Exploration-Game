/*
 *  Created by DerTroglodyt on 2016-11-10 10:32
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.market.DAWareTypeTree;
import de.hdc.commonlibrary.market.IDAWare;

/**
 *
 * @author martin
 */
@SuppressWarnings("serial")
public class DAWaresContainer extends DABasicModule {

    @Deprecated
    public DAWaresContainer() {
        super();
        content = null;
    }

    @Override
    public void init(DAWareTypeTree tree) {
        content.init(tree);
    }

    /**
     * Compacts the contained wares of a list of containers into a single list of unique wares.
     *
     * @param conti Container list.
     * @return Compiled list of wares.
     */
    public static ArrayList<IDAWare> toWareList(List<DAWaresContainer> conti) {
        ArrayList<IDAWare> v = new ArrayList<>(conti.size());
        for (DAWaresContainer wc : conti) {
            IDAWare wa = wc.content;
            if ((wa != null) && (wa.getAmount().isPositiv())) {
                boolean done = false;
                for (IDAWare a : v) {
                    if (a.equals(wa)) {
                        a.add(wa.getAmount());
                        done = true;
                        break;
                    }
                }
                if (! done) {
                    v.add(wa);
                }
            }
        }
        return v;
    }

    public DAWaresContainer(IDAWare content) {
        super();
        this.content = content;
    }


//    public DAWaresContainer(IDAWare ware, int aAmount, DABasicModuleClass aContainerClass, DATransform trans) {
//        super(aContainerClass, trans);
//        if (ware.getWareClass().getSize().getMaxVolume().isGreaterThan(wareClass.getSize().getMaxVolume())) {
//            DAResult.createFatal("Ware size <" + ware.getWareClass().getSize()
//                    + "> does not match container size <" + wareClass.getSize()
//                    + ">", "DABMWaresContainer()");
//        }
//        if (ware.getVolume().scale(aAmount).isGreaterThan(wareClass.getSize().getMaxVolume())) {
//            DAResult.createFatal("Amount of ware <" + ware.getWareClass().getSize()
//                    + "> does not match container size <" + wareClass.getSize()
//                    + ">", "DABMWaresContainer()");
//        }
//        content = new DAWareAmount(ware, new DAValue<Pieces>(aAmount, NewUnits.PIECES));
//        setName(new DALine("Conti"));
//    }

//    @Override
//    public String toString() {
//        return content.getClassID() + " " + content
//                + " (" + getActualAmount() + "/" + getMaxAmount() + ")";
//    }

//    @Override
//    public DAValue<Mass> getMass() {
//        return super.getMass().scale(getActualAmount().doubleValue(NewUnits.PIECES));
//    }
//
//
//    public DAResult<?> add(IDAWare w) {
//        if (w.isUnique() || content.getWare().isUnique()) {
//            return DAResult.createWarning("Ware is unique! Can not stack.", "DABMWaresContainer.add");
//        }
//        if (!w.getWare().equals(content.getWare())) {
//            return DAResult.createWarning("Wares do not match.", "DABMWaresContainer.add");
//        }
//        if (content.getAmount().add(w.getAmount()).isGreaterThan(getMaxAmount())) {
//            return DAResult.createWarning("Amount does not fit into conatiner!", "DABMWaresContainer.add");
//        }
//        content = content.add(w.getAmount());
//        return DAResult.createOK("ok", "DABMWaresContainer.add");
//    }

    @Override
    public boolean add(DAValue<Pieces> x) {
        return content.add(x);
    }

//    public DAResult<?> sub(DAValue<Pieces> x) {
//        if (!w.getWare().equals(content.getWare())) {
//            return DAResult.createWarning("Wares do not match.", "DABMWaresContainer.add");
//        }
//        if (content.getAmount().isLessThan(w.getAmount())) {
//            return DAResult.createWarning("Conatiner does not hold given amount!", "DABMWaresContainer.sub");
//        }
//        content = content.sub(w.getAmount());
//        return DAResult.createOK("ok", "DABMWaresContainer.sub");
//    }

    @Override
    public boolean sub(DAValue<Pieces> x) {
        return content.sub(x);
    }

//    /**
//     * Takes ware and packages it into containers.
//     * Tries to use containers with minimum size required.
//     * If that container size is to big for target storage the amount is split up into
//     * smaller containers that fit into the target storage.
//     * @param ware Ware to store in container
//     * @param a Amount of ware
//     * @param sto Target storage
//     * @return The containers holding the packaged ware.
//     */
//    public static DAVector<DAWaresContainer> packageWare(IDAWare ware, DAValue<Pieces> a, DAbmStorage sto) {
//        if (ware.isUnique() && (a.doubleValue(NewUnits.PIECES) != 1.0)) {
//            Log.warn(DAWaresContainer.class, "Ware is unique! Can not stack.", "DABMWaresContainer.packageWare");
//            return null;
//        }
//        DAWareClass.Size size = ware.getWareClass().getSize();
//        DAValue<Volume> vol = size.getMaxVolume().scale(a.doubleValue(NewUnits.PIECES));
//        // Maximum volume in storage
//        DAValue<Volume> volSto = sto.getMaxSpace();
//
//        // find smallest container for ware size that still fits into target storage
//        DABasicModuleClass cwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerMini.getWareClass();
//
//        if (!cwc.getSize().isBiggerOrEqual(vol)) {
//            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerSmall.getWareClass();
//            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
//                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
//                cwc = newcwc;
//            }
//        }
//        if (!cwc.getSize().isBiggerOrEqual(vol)) {
//            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerMedium.getWareClass();
//            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
//                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
//                cwc = newcwc;
//            }
//        }
//        if (!cwc.getSize().isBiggerOrEqual(vol)) {
//            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerLarge.getWareClass();
//            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
//                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
//                cwc = newcwc;
//            }
//        }
//        if (!cwc.getSize().isBiggerOrEqual(vol)) {
//            DABasicModuleClass newcwc = (DABasicModuleClass) AssetPool.AssetNameWareClass.StandardContainerGiant.getWareClass();
//            if (newcwc.getSize().getMaxVolume().isLessThan(volSto)
//                     && newcwc.getSize().isSmaller(sto.getWareClass().getSize())) {
//                cwc = newcwc;
//            }
//        }
//        // fill n containers until full amount ist packed
//        // max amount per container
//
//        int x = (int) Math.floor(cwc.getSize().getMaxVolume().doubleValueBase() / size.getMaxVolume().doubleValueBase());
//        int remaining = (int) a.doubleValueBase();
//        DAVector<DAWaresContainer> v = new DAVector<DAWaresContainer>(DAWaresContainer.class);
//        while (remaining > 0) {
//            int y = Math.min(x, remaining);
//            DAWaresContainer cont = new DAWaresContainer(ware, y, cwc, null);
//            remaining -= y;
//            v.add(cont);
//        }
//        return v;
//    }

//    @Override
//    public void resolveOther(DAModuleContainer aParentContainer) {
//        super.resolveOther(aParentContainer);
//        content.resolveOther(aParentContainer);
//    }

    public DAWareClass getContentType() {
        return content.getWareClass();
    }

    public boolean contains(Class<?> wc) {
        return (content.getWareClass().getClass().isAssignableFrom(wc));
    }

//    public IDAWare takeOne() {
//        if (content.getAmount().isZero()) {
//            return null;
//        }
//        content.sub(DAValue.<Pieces>create(1, NewUnits.PIECES));
//        return content;
//    }

    public IDAWare getContent() {
        return content;
    }

    public DAValue<Pieces> getActualAmount() {
        return content.getAmount();
    }

//    public DAValue<Pieces> getMaxAmount() {
//        DAValue<Volume> volC = wareClass.getSize().getMaxVolume();
//        DAValue<Volume> volW = getContentType().getSize().getMaxVolume();
//        int x = (int) (volC.doubleValueBase() / volW.doubleValueBase());
//        return new DAValue<Pieces>(x, NewUnits.PIECES);
//    }

//    public DAValue<Pieces> getFreeAmount() {
//        return getMaxAmount().sub(getActualAmount());
//    }

//    @Override
//    public DAbmWaresContainer clone() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

//    @Override
//    public void longTick(DADateTime actWorldTime, DAValue<Duration> t) {
//        super.longTick(actWorldTime, t);
//        if (getParentContainer() != null) {
//            if (getParentContainer().isOnline()) {
//                if (! isOnline()) {
//                    setOnline(true);
//                }
//            }
//        }
//    }

    @Override
    public void toStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);

        stream.writeUTF(content.getClass().getName());
        content.toStream(stream);
    }

    /**
     * Init() needs to be called after deserialization.
     */
    @Override
    public DABasicModule fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        IDAWare acontent = null;
        try {
            Class<?> c = Class.forName(stream.readUTF());
            acontent = (IDAWare) ((IDAWare) c.newInstance()).fromStream(stream);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //todo
        return new DAWaresContainer(acontent);
    }

    private static final byte VERSION = 1;

    private final IDAWare content;

}
