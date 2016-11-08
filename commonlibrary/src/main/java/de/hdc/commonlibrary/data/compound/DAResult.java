/*
 *  Created by DerTroglodyt on 2016-11-08 13:49
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

/*
 * DAResult.java
 *
 * Created on 6. Mai 2005, 19:34
 */

package de.hdc.commonlibrary.data.compound;

import android.support.annotation.NonNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hdc.commonlibrary.data.IDataAtom;
import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DataAtom;
import de.hdc.commonlibrary.util.Log;

/**
 * Used as return value of a method.
 * Used by ErrorHandler.handleError to show a message, warning, etc.
 * @param <T> Returned Data.
 * @author Martin
 */
@SuppressWarnings("serial")
public class DAResult<T extends IDataAtom> extends DataAtom {

    /** Type of error. "OK" i.e. NO ERROR occured! */
    public static enum ResultType {
        UNKNOWN,
        DEBUG,
        INFO,
        WARNING,
        FATAL,
        OK,
        FAILED,
        ;
    }

    @Deprecated
    public DAResult() {
        super();
        fDate = null;
        fMessage = null;
        resultType = null;
        fSource = null;
        result = null;
        fThrown = null;
    }

    public DAResult(DAText aMessage, ResultType aType, T result, DAText aSource, DADateTime date) {
        super();
        if ((aMessage == null) || (aType == null) || (aSource == null)) {
            Log.result(createWarning("Input paramter(s) is NULL!", "Result()"));
        }
        fDate = date;
        fMessage = aMessage;
        resultType = aType;
        fSource = aSource;
        this.result = result;
        fThrown = null;
//        // get stack trace only for errors to speed up things
//        if ((type == ResultType.INFO) || (type == ResultType.OK)) {
//            source = new DALine("Source", "UNKNOWN source");
//        } else {
//            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//            if (ste.length >= 4) {
//                StringBuffer sb = new StringBuffer();
//                for (int i=4; i < ste.length; i++) {
//                    sb.append(((i > 4)?"  SRC: ":"") + ste[i] + ((i < ste.length-1)?"\n":""));
//                }
//                source = new DALine("Source", sb.toString());
//            } else {
//                source = new DALine("Source", "UNKNOWN source");
//            }
//        }
    }

    public DAResult(DAText aMessage, ResultType aType, T result, DAText aSource) {
        this(aMessage, aType, result, aSource, DADateTime.now());
    }

    public DAResult(String message, ResultType type, String aSource) {
        this(DAText.create(message), type, null, DAText.create(aSource));
    }

    public DAResult(String message, ResultType type, T result, String source) {
        this(DAText.create(message), type, result, DAText.create(source));
    }

    public DAResult(String message, ResultType type, T result, String source, DADateTime date) {
        this(DAText.create(message), type, result, DAText.create(source), date);
    }

    public DAResult(Throwable thrown, ResultType type, T result, String source) {
        super();
        if ((thrown == null) || (type == null) || (source == null)) {
            Log.result(createWarning("Input paramter(s) is NULL!", "Result()"));
        }
        fDate = DADateTime.now();
        fMessage = DAText.create(String.valueOf(thrown));
        resultType = type;
        fSource = DAText.create(source);
        this.result = result;
        fThrown = thrown;
    }

    public static <R extends IDataAtom> DAResult<R> create(@NonNull DAResult<R> r) {
        return new DAResult<>(DAText.create(r.getMessage()),
                r.resultType, r.result, DAText.create(r.getSource()));
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createDebug(String message, String aSource) {
        return new DAResult<>(message, ResultType.DEBUG, null, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createInfo(String message, String aSource) {
        return new DAResult<>(message, ResultType.INFO, null, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createOK(String message, String aSource) {
        return new DAResult<>(message, ResultType.OK, null, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createOK(String message, String aSource, R object) {
        return new DAResult<>(message, ResultType.OK, object, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createFailed(String message, String aSource) {
        return new DAResult<>(message, ResultType.FAILED, null, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createWarning(String message, String aSource) {
        return new DAResult<>(message, ResultType.WARNING, null, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createWarning(Exception t, String aSource) {
        return new DAResult<>(t, ResultType.FATAL, null, aSource);
    }

    /**
     * Convenience factory method.
     */
    public static <R extends IDataAtom> DAResult<R> createFatal(String message, String aSource) {
        return new DAResult<>(message, ResultType.FATAL, null, aSource);
    }

    /**
     * Convenience factory method.
     * @deprecated Throwables should not get caught. Ever.
     */
    @Deprecated
    public static <R extends IDataAtom> DAResult<R> createFatal(Throwable t, String aSource) {
        return new DAResult<>(t, ResultType.FATAL, null, aSource);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(fDate.toString());
        sb.append(" ").append(resultType);
        sb.append("  MSG:").append(fMessage);
        if ((fSource != null) && (!fSource.toString().isEmpty())) {
            sb.append("  SRC: ").append(fSource);
        }
        if (fThrown != null) {
            sb.append("\n");
            StackTraceElement[] ste = fThrown.getStackTrace();
            for (StackTraceElement ste1: ste) {
                sb.append("   at ").append(ste1).append("\n");
            }
        }
        return sb.toString();
    }

//    @Override
//    public DAResult<T> clone() {
//        DAResult<T> r = new DAResult<T>(fMessage, resultType, result, fSource);
//        r.fDate.setDate(this.fDate.toParseString(), null);
//        return r;
//    }

    public String getDate() {
        return fDate.toString();
    }

    public String getSource() {
        return fSource.toString();
    }

    public String getMessage() {
        return fMessage.toString();
    }

    public ResultType getResultType() {
        return resultType;
    }

    public T getResult() {
        return result;
    }

    public boolean isDebug() {
        return (resultType == ResultType.DEBUG);
    }

    public boolean isInfo() {
        return (resultType == ResultType.INFO);
    }

    public boolean isOK() {
        return ((resultType == ResultType.OK) || (resultType == ResultType.INFO) || (resultType == ResultType.DEBUG));
    }

    public boolean isFatal() {
        return (resultType == ResultType.FATAL);
    }

    public boolean isFailed() {
        return (resultType == ResultType.FAILED);
    }

    public boolean isWarning() {
        return (resultType == ResultType.WARNING) || (resultType == ResultType.FAILED);
    }

    public boolean isUnknown() {
        return (resultType == ResultType.UNKNOWN);
    }

    public void log() {
        Log.result(this);
    }

    @Override
    public void toStream(@NonNull DataOutputStream stream) throws IOException {
        stream.writeByte(VERSION);
        stream.writeUTF(resultType.toString());
        stream.writeUTF(result.toString());
        if (fDate != null) {
            fDate.toStream(stream);
        } else {
            stream.writeUTF("NULL");
        }
        if (fSource != null) {
            fSource.toStream(stream);
        } else {
            stream.writeUTF("NULL");
        }
        if (fMessage != null) {
            fMessage.toStream(stream);
        } else {
            stream.writeUTF("NULL");
        }
    }

    @Override
    public DAResult<T> fromStream(DataInputStream stream) throws IOException {
        final byte v = stream.readByte();
        if (v < 1) {
            throw new IllegalArgumentException("Invalid version number " + v);
        }
        // todo
        throw new RuntimeException("Not implemented");
    }

    private static final byte VERSION = 1;

    private final ResultType resultType;
    private final T result;
    private final Throwable fThrown;
    private final DADateTime fDate;
    private final DAText fSource;
    private final DAText fMessage;

}
