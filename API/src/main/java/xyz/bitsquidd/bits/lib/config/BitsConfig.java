package xyz.bitsquidd.bits.lib.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;

import java.util.Objects;

@NullMarked
public final class BitsConfig {
    private static boolean initialized = false;
    private static boolean developmentMode = false;
    private static @Nullable JavaPlugin plugin;
    private static final Logger logger = LoggerFactory.getLogger("BitsLogging");

    public static String COMMAND_BASE_STRING = "bits.command"; // The base prefix for all commands, can be overridden.

    private static @Nullable BitsCommandManager commandManager;


    public static void init(JavaPlugin pluginInstance) {
        if (initialized) throw new IllegalStateException("BitsConfig has already been initialised!");
        plugin = pluginInstance;

        initialized = true;
    }

    public static Logger getLogger() {
        return logger;
    }


    public static void enableDeveloperMode() {
        developmentMode = true;
    }

    public static boolean isDevelopmentMode() {
        return developmentMode;
    }


    private static void checkInitialized() {
        if (!initialized) throw new IllegalStateException("BitsConfig hasn't been initialised!");
    }


    public static JavaPlugin getPlugin() {
        checkInitialized();
        return Objects.requireNonNull(plugin);
    }


    public static void setCommandManager(BitsCommandManager manager) {
        checkInitialized();
        commandManager = manager;
    }

    public static BitsCommandManager getCommandManager() {
        return Objects.requireNonNull(commandManager, "BitsCommandManager has not been set!");
    }

}
