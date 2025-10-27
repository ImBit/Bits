package xyz.bitsquidd.bits.lib.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;

import java.util.Objects;

public class BitsConfig {
    private static boolean initialized = false;
    private static boolean developmentMode = false;
    private static @Nullable JavaPlugin plugin;

    public static @NotNull String COMMAND_BASE_STRING = "bits.command"; // The base prefix for all commands, can be overridden.

    private static @Nullable BitsCommandManager commandManager;


    public static void init(@NotNull JavaPlugin pluginInstance) {
        if (initialized) throw new IllegalStateException("BitsConfig has already been initialised!");
        plugin = pluginInstance;

        initialized = true;
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


    public static @NotNull JavaPlugin getPlugin() {
        checkInitialized();
        return Objects.requireNonNull(plugin);
    }


    public static void setCommandManager(@NotNull BitsCommandManager manager) {
        checkInitialized();
        commandManager = manager;
    }

    public static @NotNull BitsCommandManager getCommandManager() {
        return Objects.requireNonNull(commandManager, "BitsCommandManager has not been set!");
    }

}
