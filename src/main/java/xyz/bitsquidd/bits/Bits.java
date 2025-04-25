package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.core.LogController;

public class Bits extends JavaPlugin {
    private static Bits instance;

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
        LogController.success("Disabling " + getName());
    }
}
