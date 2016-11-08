/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author martin
 */
public class NiceLogFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        Date d = new Date(record.getMillis());
        StringBuilder sb;
        synchronized (UTCDateFormat) {
            sb = new StringBuilder(UTCDateFormat.format(d));
        }
        sb.append(" ").append(getLevel(record.getLevel()));
        String n = record.getLoggerName();
//        n = n.substring(n.lastIndexOf('.') + 1);
        sb.append(" ").append(n);
        sb.append(" ").append(record.getMessage());
        if (record.getLevel() == Level.SEVERE) {
            if (record.getThrown() != null) {
                sb.append("\n").append(record.getThrown().getMessage()).append("\n");
                StackTraceElement[] ste = record.getThrown().getStackTrace();
                for (StackTraceElement ste1: ste) {
                    sb.append("   at ").append(ste1).append("\n");
                }
            }
        } else {
            sb.append("\n");
        }
        return sb.toString();
    }

    private transient static final SimpleDateFormat UTCDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

    private String getLevel(Level level) {
        if (level == Level.ALL) {
            return "ALL";
        }
        if (level == Level.CONFIG) {
            return "DEBUG";
        }
        if (level == Level.FINE) {
            return "DEBUG";
        }
        if (level == Level.FINER) {
            return "DEBUG";
        }
        if (level == Level.FINEST) {
            return "DEBUG";
        }
        if (level == Level.INFO) {
            return "INFO ";
        }
        if (level == Level.OFF) {
            return "OFF  ";
        }
        if (level == Level.SEVERE) {
            return "FATAL";
        }
        if (level == Level.WARNING) {
            return "WARN ";
        }
        return "ERROR";
    }

}
