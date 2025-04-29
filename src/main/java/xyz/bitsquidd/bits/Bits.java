package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandConfig;
import xyz.bitsquidd.bits.lib.command.CommandManager;
import xyz.bitsquidd.bits.lib.command.examples.TeleportCommand;

public class Bits extends JavaPlugin {
    private static Bits instance;

    public Bits() {
        instance = this;
    }

    public static Bits getInstance() {return instance;}

    @Override
    public void onEnable() {
        CommandManager.initialise(this);
        CommandManager.register(new TeleportCommand());
        CommandConfig.setDebugMode(true);

        LogController.success("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        LogController.success("Disabling " + getName());
    }
}
