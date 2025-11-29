package xyz.bitsquidd.bits.paper;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.Objects;

public class PaperBitsConfig extends BitsConfig {
    private final JavaPlugin plugin;

    public PaperBitsConfig(JavaPlugin plugin) {
        super(plugin.getSLF4JLogger());
        this.plugin = plugin;
    }


    public JavaPlugin getPlugin() {
        checkInitialized();
        return Objects.requireNonNull(plugin);
    }

}
