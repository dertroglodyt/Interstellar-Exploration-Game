/*
 * Inteface for atomar data types.
 */
package de.hdc.commonlibrary.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * All DataAtoms should be Immutables!
 * Impementing classes MUST also implement equals() and hashCode()!
 * If possible, use DataAtomImpl instead.
 *
 * Children MUST override equals!
 *
 * @Override
 *  public boolean equals(Object o) {
 *    if (! (o instanceof DataAtom)) {
 *      return false;
 *    }
 *    return (compareTo((DataAtom) o) == 0);
 *  }
 *
 * @author martin
 */
public interface IDataAtom extends Comparable<IDataAtom> {

    /**
     * JSON serialisation
     *
     * Add the version number of the class 's data to help distinguish changed read/write data formats.
     * It's value should only change if toStream/fromStream are changed.
     *
     * { version = 1, thisData = ??? }
     *
     * @param stream
     * @throws java.io.IOException
     */
    void toStream(final DataOutputStream stream) throws IOException;

    IDataAtom fromStream(DataInputStream stream) throws IOException;

    default int doCompare(final IDataAtom o) {
        return toString().compareTo(o.toString());
    }

    /**
     * Compares two objects of the same class for sorting purpose.
     *
     * @param o Object to be compared to this one.
     * @return Indicates sorting Order.
     */
    @Override
    default int compareTo(final IDataAtom o) {
        if (o == null) {
            return -1;
        }
//        Not an error because this is used i.e. for sorting a collection.
        if ((! (getClass().isInstance(o))) && (! (o.getClass().isInstance(this)))) {
            return (toString() + getClass().getName()).compareTo(o.toString() + getClass().getName());
        }
//            /* not this class nor a child class */
//            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//            StringBuffer sb = new StringBuffer();
//            for (int i=4; i < ste.length; i++) {
//                sb.append(((i > 4)?"  SRC: ":"") + ste[i] + ((i < ste.length-1)?"\n":""));
//            }
//            DVCErrorHandler.raiseError(DAResult.createWarning("Type mismatch! <" + o.getClass().getName() + "> " + sb.toString()
//                    , getClass().getName()+".compareTo"));
//            return -1;
//        }
//        if (o.getClass().isInstance(this)) {
        if (getClass().isInstance(o)) {
            // same class as this
            // "o.class instanceof this.class"
            return doCompare(o);
        }
        /* child may have other doCompare implementation */
        return -1 * o.doCompare(this);
    }

}
