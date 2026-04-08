package com.derekpage.RAMTimingCalculator.logging;

/**
 * A crude scaled down version of the log4j logging framework, simplified for this small app. It adopts the basic API
 * of a log4j Logger, but condenses the logging mechanism down to a single shared static class without appenders and
 * the other complex configuration stuff. At some point this may be moved to java.util.logging.
 */
public class Logger {

    //the current level of logging enabled.  Defaults to INFO level.
    private static Logger.LogLevel currentLevel = null;
    static {
        currentLevel = LogLevel.INFO;
    }

    /// LOGGING METHODS ///

    public static void trace(Object m) {
        Logger._doLog(LogLevel.TRACE, m);
    }

    public static void debug(Object m) {
        Logger._doLog(LogLevel.DEBUG, m);
    }

    public static void error(Object m) {
        Logger._doLog(LogLevel.ERROR, m);
    }

    public static void info(Object m) {
        Logger._doLog(LogLevel.INFO, m);
    }

    public static void warn(Object m) {
        Logger._doLog(LogLevel.WARN, m);
    }

    public static void fatal(Object m) {
        Logger._doLog(LogLevel.FATAL, m);
    }

    /// Log level setting methods. ///

    public static synchronized void setLogLevelAll() {
        Logger.currentLevel = LogLevel.ALL;
    }

    public static synchronized void setLogLevelTrace() {
            Logger.currentLevel = LogLevel.TRACE;
    }

    public static synchronized void setLogLevelDebug() {
        Logger.currentLevel = LogLevel.DEBUG;
    }

    public static synchronized void setLogLevelError() {
        Logger.currentLevel = LogLevel.ERROR;
    }

    public static synchronized void setLogLevelInfo() {
            Logger.currentLevel = LogLevel.INFO;
    }

    public static synchronized void setLogLevelWarn() {
        Logger.currentLevel = LogLevel.WARN;
    }

    public static synchronized void setLogLevelFatal() {
        Logger.currentLevel = LogLevel.FATAL;
    }

    public static synchronized void setLogLevelOff() {
        Logger.currentLevel = LogLevel.OFF;
    }

    /// Log level enabled checking methods ///

    public static boolean isLogLevelTraceEnabled() {
        return currentLevel.getLevelNum() <= LogLevel.TRACE.getLevelNum();
    }

    public static boolean isLogLevelDebugEnabled() {
        return currentLevel.getLevelNum() <= LogLevel.DEBUG.getLevelNum();
    }

    public static boolean isLogLevelErrorEnabled() {
        return currentLevel.getLevelNum() <= LogLevel.ERROR.getLevelNum();
    }

    public static boolean isLogLevelInfoEnabled() {
        return currentLevel.getLevelNum() <= LogLevel.INFO.getLevelNum();
    }

    public static boolean isLogLevelWarnEnabled() {
        return currentLevel.getLevelNum() <= LogLevel.WARN.getLevelNum();
    }

    public static boolean isLogLevelFatalEnabled() {
        return currentLevel.getLevelNum() <= LogLevel.FATAL.getLevelNum();
    }

    /// Log level equality checking methods ///

    public static boolean isLogLevelAll() {
        return currentLevel.getLevelNum() == LogLevel.ALL.getLevelNum();
    }

    public static boolean isLogLevelTrace() {
        return currentLevel.getLevelNum() == LogLevel.TRACE.getLevelNum();
    }

    public static boolean isLogLevelDebug() {
        return currentLevel.getLevelNum() == LogLevel.DEBUG.getLevelNum();
    }

    public static boolean isLogLevelError() {
        return currentLevel.getLevelNum() == LogLevel.ERROR.getLevelNum();
    }

    public static boolean isLogLevelInfo() {
        return currentLevel.getLevelNum() == LogLevel.INFO.getLevelNum();
    }

    public static boolean isLogLevelWarn() {
        return currentLevel.getLevelNum() == LogLevel.WARN.getLevelNum();
    }

    public static boolean isLogLevelFatal() {
        return currentLevel.getLevelNum() == LogLevel.FATAL.getLevelNum();
    }

    public static boolean isLogLevelOff() {
        return currentLevel.getLevelNum() == LogLevel.OFF.getLevelNum();
    }

    /**
     * @param levelName name of the log level to set.
     *
     * Allows for progammatically setting the log level (used to allow setting it via command line).
     */
    public static synchronized void setLogLevelByName(String levelName) {
        if (levelName == null || levelName.isEmpty())
            throw new IllegalArgumentException("Parameter 'levelName' is required!");

        switch (levelName.toUpperCase()) {
            case "ALL":
                Logger.setLogLevelAll();
                break;
            case "TRACE":
                Logger.setLogLevelTrace();
                break;
            case "DEBUG":
                Logger.setLogLevelDebug();
                break;
            case "ERROR":
                Logger.setLogLevelError();
                break;
            case "INFO":
                Logger.setLogLevelInfo();
                break;
            case "WARN":
                Logger.setLogLevelWarn();
                break;
            case "FATAL":
                Logger.setLogLevelFatal();
                break;
            case "OFF":
                Logger.setLogLevelOff();
                break;

            //error on anything that's not a supported log level.
            default:
                throw new IllegalArgumentException("Parameter 'levelName' value '" + levelName + "' is not a valid logging level!");
        }
    }

    /**
     * @param level the log level prefix.
     * @param o the content to be logged.
     *
     * The core implementation of the logging functionality.
     */
    private static void _doLog(LogLevel level, Object o) {
        if (level == null)
            throw new IllegalArgumentException("Parameter 'level' is required!");

        //if intended log message's level is less than the current level, then discard this message.
        if (Logger.currentLevel.getLevelNum() > level.getLevelNum())
            return;

        //if null then just add an empty line.
        System.out.println("[" + level + "]: " + ((o == null) ? "" : o.toString()));
    }


    /**
     * Inner Enum defines the logging levels allowed.
     */
    private enum LogLevel {

        ALL(Integer.MIN_VALUE, "ALL"),
        TRACE (5000, "TRACE"),
        DEBUG(10000, "DEBUG"),
        INFO(20000, "INFO"),
        WARN(30000, "WARN"),
        ERROR(40000, "ERROR"),
        FATAL(50000, "FATAL"),
        OFF(Integer.MAX_VALUE, "OFF");

        //integer number for logging level settings.
        private int levelNum;

        //Display name of the logging level.
        private String levelName;

        LogLevel(int levelNum, String levelName) {
            this.levelNum = levelNum;
            this.levelName = levelName;
        }

        public int getLevelNum() {
            return levelNum;
        }

        public String getLevelName() {
            return levelName;
        }
    }
}
