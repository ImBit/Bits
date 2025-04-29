package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.examples.TeleportCommand;
import xyz.bitsquidd.bits.lib.command.registering.CommandManager;

// TODO:
//  BitsConfig.DEBUGMODE
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

        LogController.success("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        CommandManager.unregisterAll();

        LogController.success("Disabled " + getName());
    }
}
