package xyz.bitsquidd.bits.lib.logging;

import org.bukkit.Bukkit;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.LogDecorator;

import java.util.logging.Logger;

public class LogController {
    private static final Logger bukkitLogger = Bukkit.getLogger(); //Bits.getInstance().getLogger()

    public static void log(LogType logType, String message) {
        String prefix = logType.prefix;
        String formattedMessage = "[" + prefix + "] " + message;

        switch (logType) {
            case SUCCESS, DEBUG -> bukkitLogger.fine(formattedMessage);
            case INFO -> bukkitLogger.info(formattedMessage);
            case WARNING -> bukkitLogger.warning(formattedMessage);
            case ERROR, CRITICAL -> bukkitLogger.severe(formattedMessage);
        }

        Text.of(message, new LogDecorator(logType)).sendAll(logType.permission);
    }


    public static void success(String message) {
        log(LogType.SUCCESS, message);
    }

    public static void debug(String message) {
        log(LogType.DEBUG, message);
    }

    public static void info(String message) {
        log(LogType.INFO, message);
    }

    public static void warning(String message) {
        log(LogType.WARNING, message);
    }

    public static void error(String message) {
        log(LogType.ERROR, message);
    }

    public static void critical(String message) {
        log(LogType.CRITICAL, message);
    }

    public static void exception(Exception exception) {
        bukkitLogger.severe(exception.getMessage() + ":");
        Thread.dumpStack();
    }

}
