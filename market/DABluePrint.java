/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hdc.commonlibrary.market;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.measure.quantity.Duration;

import de.hdc.commonlibrary.data.SerialUIDPool;
import de.hdc.commonlibrary.data.types.atom.DALine;
import de.hdc.commonlibrary.data.types.atom.DAValue;

/**
 *
 * @author martin
 * @deprecated This class exists only for compatibility.
 */
@Deprecated
@SuppressWarnings("serial")
public class DABluePrint extends DAWare {

    private static final long serialVersionUID = SerialUIDPool.UID.DABluePrint.value();
//    private static final String DEF_ICON = "/datavault/common/space/icon/DABlueprint.gif";
//    static {
//        DVCBasicDataModel.register(DABluePrint.class, createImageIcon(DEF_ICON));
//    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        byte version = in.readByte();
        // Do something different here if old version demands it

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        /**
         * The version number of the class to help distinguish changed read/write data formats.
         * It should be set in every "writeExternal" of every class.
         * It's value should only change if write-/readExternal are changed.
         */
        int version = 1;
        out.writeInt(version);

    }

    @Deprecated
    public DABluePrint() {
        super();
        makeUnique(new DALine("Neue Blaupause"));
    }

//    @Override
//    public void resolveOther(DAModuleContainer aParentContainer) {
//        super.resolveOther(aParentContainer);
//        product = getWareClass().getInstance(aParentContainer);
////        product.resolveOther(aParentContainer);
//    }

    public IDAWare getProductClone() {
        return getWareClass().getInstance();
    }

    public DAValue<Duration> getProductionTime() {
        return ((DABluePrintClass) getWareClass()).getProductionTime();
    }

//    @Override
//    public DAWare clone() {
//        DVCErrorHandler.raiseError(DAResult.createWarning("Not supported yet.", "DAWare.clone"));
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

}
