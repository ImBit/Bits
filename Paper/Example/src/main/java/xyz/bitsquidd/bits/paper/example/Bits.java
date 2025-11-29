package xyz.bitsquidd.bits.paper.example;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.paper.example.command.CustomBitsCommandManager;

// TODO:
//  BitsConfig.DEBUGMODE
public class Bits extends JavaPlugin {
    private static Bits instance;

    private static CustomBitsCommandManager exampleCommandManager;

    public Bits() {
        instance = this;
    }

    public static Bits getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        BitsConfig.init(this); // We must initialise the BitsConfig with our plugin for Bits to work!

        exampleCommandManager = new CustomBitsCommandManager();
        exampleCommandManager.startup();

        getLogger().info("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        exampleCommandManager.shutdown();

        getLogger().info("Disabled " + getName());
    }
}
