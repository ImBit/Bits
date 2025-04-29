package xyz.bitsquidd.bits;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.examples.TeleportCommandNew;
import xyz.bitsquidd.bits.lib.command.registering.CommandManagerNew;

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
//        CommandManager.initialise(this);
//        CommandManager.register(new TeleportCommand());
//        CommandManager.register(new GameModeCommand());

        CommandManagerNew.initialise(this);
        CommandManagerNew.register(new TeleportCommandNew());

        LogController.success("Enabled " + getName());
    }

    @Override
    public void onDisable() {
        CommandManagerNew.unregisterAll();

        LogController.success("Disabled " + getName());
    }
}
