package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.example.ExampleCommandManager;
import xyz.bitsquidd.bits.example.ExampleLogger;

// TODO:
//  BitsConfig.DEBUGMODE
public class Bits extends JavaPlugin {
    private static Bits instance;

    private static ExampleCommandManager exampleCommandManager;

    public Bits() {
        instance = this;
    }

    public static Bits getInstance() {return instance;}

    @Override
    public void onEnable() {
        new ExampleLogger(this);

        exampleCommandManager = new ExampleCommandManager(this);
        exampleCommandManager.registerCommands();

        ExampleLogger.success("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        exampleCommandManager.unregisterAll();

        ExampleLogger.success("Disabled " + getName());
    }
}
