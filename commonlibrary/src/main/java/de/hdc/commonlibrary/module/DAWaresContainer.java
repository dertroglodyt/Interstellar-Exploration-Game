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

import javax.measure.quantity.Volume;

import de.hdc.commonlibrary.data.atom.DAArray;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.compound.DAResult;
import de.hdc.commonlibrary.data.quantity.NewUnits;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.market.DAWare;
import de.hdc.commonlibrary.market.DAWareClass;
import de.hdc.commonlibrary.market.DAWareTypeTree;
import de.hdc.commonlibrary.market.DAWareTypeTreeBootstrap;
import de.hdc.commonlibrary.market.IDAWare;
import de.hdc.commonlibrary.moduleclass.DABasicModuleClass;
import de.hdc.commonlibrary.util.Log;

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


    public DAWaresContainer(IDAWare ware, int aAmount, DABasicModuleClass aContainerClass) {
        super(aContainerClass, ModuleType.STORAGE, DAText.create("Conti"));
        if (ware.getWareClass().size.getVolume().isGreaterThan(getWareClass().size.getVolume())) {
            DAResult.createFatal("Ware size <" + ware.getWareClass().size
                    + "> does not match container size <" + getWareClass().size
                    + ">", "DABMWaresContainer()");
        }
        if (ware.getVolume().scale(aAmount).isGreaterThan(getWareClass().size.getVolume())) {
            DAResult.createFatal("Amount of ware <" + ware.getWareClass().size
                    + "> does not match container size <" + getWareClass().size
                    + ">", "DABMWaresContainer()");
        }
        content = DAWare.create(ware, DAValue.create(aAmount, NewUnits.PIECES));
    }

    @Override
    public String toString() {
        return content.getClassID() + " " + content
                + " (" + getActualAmount() + "/" + getMaxAmount() + ")";
    }

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

//    @Override
//    public boolean add(DAValue<Pieces> x) {
//        return content.add(x);
//    }

//    public DAResult<?> sub(DAValue<Pieces> x) {
//        if (!w.getWare().equals(content.getWare())) {
//            return DAResult.createWarning("Wares do not match.", "DABMWaresContainer.add");
//        }
//        if (content.getAmount().isSmallerThan(w.getAmount())) {
//            return DAResult.createWarning("Conatiner does not hold given amount!", "DABMWaresContainer.sub");
//        }
//        content = content.sub(w.getAmount());
//        return DAResult.createOK("ok", "DABMWaresContainer.sub");
//    }

//    @Override
//    public boolean sub(DAValue<Pieces> x) {
//        return content.sub(x);
//    }

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
    public static DAArray<DAWaresContainer> packageWare(IDAWare ware, DAValue<Pieces> a, DAStorage sto) {
        if (ware.isUnique() && (a.doubleValue(NewUnits.PIECES) != 1.0)) {
            Log.warn(DAWaresContainer.class, "Ware is unique! Can not stack.", "DABMWaresContainer.packageWare");
            return null;
        }
        final DAWareClass.Size size = ware.getWareClass().size;
        final DAValue<Volume> vol = size.getVolume().scale(a.doubleValue(NewUnits.PIECES));
        // Maximum volume in storage
        final DAValue<Volume> volSto = sto.getMaxCargoSpace();

        // find smallest container for ware size that still fits into target storage
        DABasicModuleClass cwc = DAWareTypeTreeBootstrap.CONTAINER_MINI;

        if (!cwc.size.isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = DAWareTypeTreeBootstrap.CONTAINER_SMALL;
            if (newcwc.size.getVolume().isSmallerThan(volSto)
                     && newcwc.size.isSmaller(sto.getWareClass().size)) {
                cwc = newcwc;
            }
        }
        if (!cwc.size.isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = DAWareTypeTreeBootstrap.CONTAINER_MEDIUM;
            if (newcwc.size.getVolume().isSmallerThan(volSto)
                     && newcwc.size.isSmaller(sto.getWareClass().size)) {
                cwc = newcwc;
            }
        }
        if (!cwc.size.isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = DAWareTypeTreeBootstrap.CONTAINER_LARGE;
            if (newcwc.size.getVolume().isSmallerThan(volSto)
                     && newcwc.size.isSmaller(sto.getWareClass().size)) {
                cwc = newcwc;
            }
        }
        if (!cwc.size.isBiggerOrEqual(vol)) {
            DABasicModuleClass newcwc = DAWareTypeTreeBootstrap.CONTAINER_GIANT;
            if (newcwc.size.getVolume().isSmallerThan(volSto)
                     && newcwc.size.isSmaller(sto.getWareClass().size)) {
                cwc = newcwc;
            }
        }
        // fill n containers until full amount ist packed
        // max amount per container

        int x = (int) Math.floor(cwc.size.getVolume().doubleValueBase() / size.getVolume().doubleValueBase());
        int remaining = (int) a.doubleValueBase();
        DAArray<DAWaresContainer> v = new DAArray<DAWaresContainer>();
        while (remaining > 0) {
            int y = Math.min(x, remaining);
            final DAWaresContainer cont = new DAWaresContainer(ware, y, cwc);
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

    public DAValue<Pieces> getMaxAmount() {
        DAValue<Volume> volC = getWareClass().size.getVolume();
        DAValue<Volume> volW = getContentType().size.getVolume();
        int x = (int) (volC.doubleValueBase() / volW.doubleValueBase());
        return DAValue.create(x, NewUnits.PIECES);
    }

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
