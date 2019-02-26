package com.sv.common.util;

import android.util.Log;


/**
 * 自定义 Log 工具
 * 可以根据配置文件配置（app.PRINT_LOG）是否打印
 */
public final class Logger {
    private static boolean printLog = true;
    private Logger() {}

    /**
     * Send a {@link Log#VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.v(tag, msg);
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.v(tag, msg, tr);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.d(tag, msg);
    }

    /**
     * Send a {@link Log#DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.d(tag, msg, tr);
    }

    /**
     * Send an {@link Log#INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.i(tag, msg);
    }

    /**
     * Send a {@link Log#INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.i(tag,msg, tr);
    }

    /**
     * Send a {@link Log#WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.w(tag, msg);
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.w(tag, msg, tr);
    }

    /*
     * Send a {@link android.util.Log#WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.w(tag,tr);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.e(tag, msg);
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        if(!cantPrintLog()){
            return -1;
        }
        return Log.e(tag, msg, tr);
    }

    public static boolean isPrintLog() {
        return printLog;
    }

    public static void setPrintLog(boolean printLog) {
        Logger.printLog = printLog;
    }

    private static boolean cantPrintLog() {
        return Logger.printLog;
    }
}
