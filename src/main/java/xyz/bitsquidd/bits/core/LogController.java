package xyz.bitsquidd.bits.core;

import xyz.bitsquidd.bits.Bits;

import java.util.logging.Logger;

public class LogController {
    private static final Logger bukkitLogger = Bits.getInstance().getLogger();

    public enum LogType {
        SUCCESS("BIT-SUCCESS"),
        DEBUG("BIT-DEBUG"),
        INFO("BIT-INFO"),
        WARNING("BIT-WARNING"),
        ERROR("BIT-ERROR"),
        CRITICAL("BIT-CRITICAL"),;

        public final String prefix;

        LogType(String prefix) {
            this.prefix = prefix;
        }
    }

    public static void log(LogType logType, String message) {
        switch (logType) {
            case SUCCESS -> success(message);
            case DEBUG -> debug(message);
            case INFO -> info(message);
            case WARNING -> warning(message);
            case ERROR -> error(message);
            case CRITICAL -> critical(message);
        }
    }


    public static void success(String message) {
        bukkitLogger.info("[" + LogType.SUCCESS.prefix + "] "+ message);
    }

    public static void debug(String message) {

    }

    public static void info(String message) {
        bukkitLogger.info("[" + LogType.INFO.prefix + "] "+ message);
    }

    public static void warning(String message) {

    }

    public static void error(String message) {

    }

    public static void critical(String message) {

    }


}
