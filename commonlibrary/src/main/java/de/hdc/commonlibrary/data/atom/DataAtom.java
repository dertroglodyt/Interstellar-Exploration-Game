/*
 * DVCDataObject.java
 *
 * Created on 6. Mai 2005, 02:25
 */
package de.hdc.commonlibrary.data.atom;

import de.hdc.commonlibrary.data.IDataAtom;

/**
 * !!!All DataAtoms should be Immutables!!!
 * Ancestor of every data (a bit like java.lang.object) which is displayable and serializable.
 *
 * Children MUST implement:
 * public static <T extends Quantity> DAValue<T> fromStream(DataInputStream stream) throws IOException {
 *     final byte v = stream.readByte();
 *       if (v < 1) {
 *         throw new IllegalArgumentException("Invalid version number " + v);
 *     }
 * }
 *
 * @author Martin
 */
public abstract class DataAtom implements IDataAtom, Comparable<IDataAtom> {

    /**
     * Converts content to text representation.
     * @return The textual representation.
     */
    @Override
    public String toString() {
        // Object.toString() uses hashCode which uses getLongString() wich would create a circle...
        // toString() + "@" + Integer.toHexString(hashCode());
        return getClass().getName();
    }

    @Override
    public int doCompare(IDataAtom o) {
        return toString().compareTo(o.toString());
    }

    /**
     * Compares two objects of the same class for sorting purpose.
     *
     * @param o Object to be compared to this one.
     * @return Indicates sorting Order.
     */
    @Override
    public final int compareTo(IDataAtom o) {
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

    @Override
    public final boolean equals(Object obj) {
        if (! (obj instanceof IDataAtom)) {
            return false;
        }
        return (compareTo((IDataAtom) obj) == 0);
    }

    @Override
    public int hashCode() {
        //return 89 * 7 + (toParseString("") != null ? toParseString("").hashCode() : 0);
        return -1;
    }

}
