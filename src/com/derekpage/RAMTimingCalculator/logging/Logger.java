package com.derekpage.RAMTimingCalculator.logging;

/**
 * A crude scaled down version of the log4j logging framework, simplified for this small app.  At some point this should
 * probably be moved to java.util.logging.
 */
public class Logger {

    //the current level of logging enabled.  Defaults to INFO level.
    private static Logger.LogLevel currentLevel = null;
    static {
        currentLevel = LogLevel.INFO;
    }

    /// LOGGING METHODS ///

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

    public static void setLogLevelDebug() {
        synchronized (Logger.class) {
            Logger.currentLevel = LogLevel.DEBUG;
        }
    }

    public static void setLogLevelError() {
        synchronized (Logger.class) {
            Logger.currentLevel = LogLevel.ERROR;
        }
    }

    public static void setLogLevelInfo() {
        synchronized (Logger.class) {
            Logger.currentLevel = LogLevel.INFO;
        }
    }

    public static void setLogLevelWarn() {
        synchronized (Logger.class) {
            Logger.currentLevel = LogLevel.WARN;
        }
    }

    public static void setLogLevelFatal() {
        synchronized (Logger.class) {
            Logger.currentLevel = LogLevel.FATAL;
        }
    }

    /**
     * @param levelName name of the log level to set.
     *
     * Allows for progammatically setting the log level (used to allow setting it via command line).
     */
    public static void setLogLevelByName(String levelName) {
        if (levelName == null || levelName.isEmpty())
            throw new IllegalArgumentException("Parameter 'levelName' is required!");

        synchronized (Logger.class) {
            switch (levelName.toUpperCase()) {
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

                //error on anything that's not a supported log level.
                default:
                    throw new IllegalArgumentException("Parameter 'levelName' value '" + levelName + "' is not a valid logging level!");
            }
        }
    }

    /// Log level checking methods ///

    public static boolean isLogLevelDebug() {
        return currentLevel.getLevelNum() <= LogLevel.DEBUG.getLevelNum();
    }

    public static boolean isLogLevelError() {
        return currentLevel.getLevelNum() <= LogLevel.ERROR.getLevelNum();
    }

    public static boolean isLogLevelInfo() {
        return currentLevel.getLevelNum() <= LogLevel.INFO.getLevelNum();
    }

    public static boolean isLogLevelWarn() {
        return currentLevel.getLevelNum() <= LogLevel.WARN.getLevelNum();
    }

    public static boolean isLogLevelFatal() {
        return currentLevel.getLevelNum() <= LogLevel.FATAL.getLevelNum();
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

        //if null then just add a empty line.
        System.out.println("[" + level + "]: " + ((o == null) ? "" : o.toString()));
    }


    /**
     * Enum defines the logging levels allowed.
     */
    private enum LogLevel {
        DEBUG(10000, "DEBUG"),
        INFO(20000, "INFO"),
        WARN(30000, "WARN"),
        ERROR(40000, "ERROR"),
        FATAL(50000, "FATAL");

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
