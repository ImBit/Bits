package xyz.bitsquidd.bits.example;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.lib.logging.BitsLogger;
import xyz.bitsquidd.bits.lib.logging.LogType;

public class ExampleLogger extends BitsLogger {
    private static ExampleLogger INSTANCE;

    public ExampleLogger(JavaPlugin plugin) {
        super(plugin);
        INSTANCE = this;
    }

    public static void success(String message) {
        INSTANCE.log(LogType.SUCCESS, message);
    }

    public static void debug(String message) {
        INSTANCE.log(LogType.DEBUG, message);
    }

    public static void info(String message) {
        INSTANCE.log(LogType.INFO, message);
    }

    public static void warning(String message) {
        INSTANCE.log(LogType.WARNING, message);
    }

    public static void error(String message) {
        INSTANCE.log(LogType.ERROR, message);
    }

    public static void critical(String message) {
        INSTANCE.log(LogType.CRITICAL, message);
    }

    public static void exception(Exception exception) {
        INSTANCE.logException(exception);
    }
}