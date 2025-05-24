package xyz.bitsquidd.bits.lib.logging;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.LogDecorator;

public abstract class BitsLogger {
    private final org.slf4j.Logger pluginLogger;

    public BitsLogger(JavaPlugin plugin) {
        this.pluginLogger = plugin.getSLF4JLogger();
    }

    public void log(LogType logType, String message) {
        String prefix = logType.prefix;
        String formattedMessage = "[" + prefix + "] " + message;

        switch (logType) {
            case SUCCESS, DEBUG -> pluginLogger.debug(formattedMessage);
            case INFO -> pluginLogger.info(formattedMessage);
            case WARNING -> pluginLogger.warn(formattedMessage);
            case ERROR, CRITICAL -> pluginLogger.error(formattedMessage);
        }

        Text.of(message, new LogDecorator(logType)).sendAll(logType.permission);
    }

    public void logException(Exception exception) {
        pluginLogger.error(exception.getMessage() + " : ");
        Thread.dumpStack();
    }
}
