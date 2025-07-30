package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.bitsquidd.bits.example.command.ExampleCommandManager;

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
        exampleCommandManager = new ExampleCommandManager(this);
        exampleCommandManager.registerCommands();

        getLogger().info("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        exampleCommandManager.unregisterAll();

        getLogger().info("Disabled " + getName());
    }
}
