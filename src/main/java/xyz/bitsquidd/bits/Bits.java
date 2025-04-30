package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.example.ExampleCommandManager;
import xyz.bitsquidd.bits.lib.logging.LogController;

// TODO:
//  BitsConfig.DEBUGMODE
public class Bits extends JavaPlugin {
    private static Bits instance;

    private final ExampleCommandManager exampleCommandManager = new ExampleCommandManager(this);

    public Bits() {
        instance = this;
    }

    public static Bits getInstance() {return instance;}

    @Override
    public void onEnable() {
        LogController.success("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        exampleCommandManager.unregisterAll();

        LogController.success("Disabled " + getName());
    }
}
