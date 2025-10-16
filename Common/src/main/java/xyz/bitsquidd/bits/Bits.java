package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.example.command.ExampleCommandManager;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

// TODO:
//  BitsConfig.DEBUGMODE
public class Bits extends JavaPlugin {
    private static Bits instance;

    private static ExampleCommandManager exampleCommandManager;

    public Bits() {
        instance = this;
    }

    public static Bits getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        BitsConfig.init(this); // We must initialise the BitsConfig with our plugin for Bits to work!

        exampleCommandManager = new ExampleCommandManager();
        exampleCommandManager.startup();

        getLogger().info("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        exampleCommandManager.shutdown();

        getLogger().info("Disabled " + getName());
    }
}
