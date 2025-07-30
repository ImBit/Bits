package xyz.bitsquidd.bits.example.command;

import org.bukkit.plugin.Plugin;

import xyz.bitsquidd.bits.example.command.inst.ExampleTeleportCommand;
import xyz.bitsquidd.bits.lib.command.registering.CommandManager;

public class ExampleCommandManager extends CommandManager {
    public ExampleCommandManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void registerCommands() {
        register(new ExampleTeleportCommand());
    }
}