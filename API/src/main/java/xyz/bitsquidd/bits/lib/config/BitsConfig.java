package xyz.bitsquidd.bits.lib.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BitsConfig {
    private static boolean initialized = false;
    private static @Nullable JavaPlugin plugin;

    public static @NotNull String COMMAND_BASE_STRING = "bits.command"; // The base prefix for all commands, can be overridden.


    public static void init(@NotNull JavaPlugin pluginInstance) {
        if (initialized) throw new IllegalStateException("BitsConfig has already been initialised!");
        plugin = pluginInstance;

        initialized = true;
    }

    private static void checkInitialized() {
        if (!initialized) throw new IllegalStateException("BitsConfig hasn't been initialised!");
    }


    public static @NotNull JavaPlugin getPlugin() {
        checkInitialized();
        return Objects.requireNonNull(plugin);
    }

}
