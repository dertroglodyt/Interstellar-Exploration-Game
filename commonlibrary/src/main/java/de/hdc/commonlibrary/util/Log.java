/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hdc.commonlibrary.data.compound.DAResult;

/**
 *
 * @author martin
 */
public class Log {

    public static void result(DAResult<?> result) {
        switch (result.getResultType()) {
            case DEBUG: {
                if (getLog(DAResult.class).isLoggable(Level.FINEST)) {
                    getLog(DAResult.class).finest(result.toString());
                }
//                debug(Log.class, result.toString());
                break;
            }
            case OK:
            case INFO: {
                if (getLog(DAResult.class).isLoggable(Level.INFO)) {
                    getLog(DAResult.class).info(result.toString());
                }
//                info(Log.class, result.toString());
                break;
            }
            case FAILED:
            case WARNING: {
                if (getLog(DAResult.class).isLoggable(Level.WARNING)) {
                    getLog(DAResult.class).warning(result.toString());
                }
//                warn(Log.class, result.toString());
                break;
            }
//            case ERROR: {
//                if (getLog(DAResult.class).isErrorEnabled()) {
//                    getLog(DAResult.class).warn(result);
//                }
////                error(Log.class, result.toString());
//                break;
//            }
            case UNKNOWN:
            case FATAL: {
                if (getLog(DAResult.class).isLoggable(Level.SEVERE)) {
                    getLog(DAResult.class).severe(result.toString());
                }
//                fatal(Log.class, result.toString());
                break;
            }
            default: {
                if (getLog(DAResult.class).isLoggable(Level.WARNING)) {
                    getLog(DAResult.class).warning(result.toString());
                }
//                fatal(Log.class, result.toString());
            }
        }
    }

    public static void throwable(Class<?> clazz, Throwable t, String methodName) {
        getLog(clazz).severe("Throwable in " + clazz + "." + methodName + "! <" +t.toString() +  ">");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(bos));
        getLog(clazz).severe(bos.toString());
//        LogMF.error(Logger.getLogger(clazz), t, "Throwable in " + clazz + "." + methodName + "!", null);
    }

    public static void debug(Class<?> clazz, String message, Object... arguments) {
        if (getLog(clazz).isLoggable(Level.FINEST)) {
            getLog(clazz).finest(unpack(message, arguments));
        }
//        LogMF.debug(Logger.getLogger(clazz), message, arguments);
    }

    public static void info(Class<?> clazz, String message, Object... arguments) {
        if (getLog(clazz).isLoggable(Level.INFO)) {
            getLog(clazz).info(unpack(message, arguments));
        }
//        LogMF.info(Logger.getLogger(clazz), message, arguments);
    }

    public static void warn(Class<?> clazz, String message, Object... arguments) {
        if (getLog(clazz).isLoggable(Level.WARNING)) {
            getLog(clazz).warning(unpack(message, arguments));
        }
//        LogMF.warn(Logger.getLogger(clazz), message, arguments);
    }

//    public static void error(Class<?> clazz, String message, Object... arguments) {
//        if (getLog(clazz).isErrorEnabled()) {
//            getLog(clazz).error(unpack(message, arguments));
//        }
////        LogMF.error(Logger.getLogger(clazz), message, arguments);
//    }

    public static void fatal(Class<?> clazz, String message, Object... arguments) {
        if (getLog(clazz).isLoggable(Level.SEVERE)) {
            getLog(clazz).severe(unpack(message, arguments) + "\n");
        }
//        LogMF.fatal(Logger.getLogger(clazz), message, arguments);
    }

    private static ConcurrentHashMap<Class<?>, Logger> pool  = new ConcurrentHashMap<>(0);

    private Log() {
        // no instance
    }

    private static String unpack(String message, Object... arguments) {
        StringBuilder sb = new StringBuilder(message);
        int i = 0;
        for (Object parm : arguments) {
            int start = sb.indexOf("{" + i + "}");
            if (start >= 0) {
                sb.replace(start, start + ("{" + i + "}").length(), (parm != null)?parm.toString():"null");
            }
            i += 1;
        }
        return sb.toString();
    }

    private static Logger getLog(Class<?> clazz) {
        Logger log = pool.get(clazz);
        if (log == null) {
            log = Logger.getLogger(clazz.getName());
            pool.put(clazz, log);
        }
        return log;
    }
}
