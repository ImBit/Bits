package xyz.bitsquidd.bits.example;

import org.bukkit.plugin.Plugin;
import xyz.bitsquidd.bits.lib.command.examples.ClearAllCommandsCommand;
import xyz.bitsquidd.bits.lib.command.examples.TeleportCommand;
import xyz.bitsquidd.bits.lib.command.examples.TestFormattingCommand;
import xyz.bitsquidd.bits.lib.command.registering.CommandManager;

public class ExampleCommandManager extends CommandManager {
    public ExampleCommandManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void registerCommands() {
        register(new TestFormattingCommand());
        register(new TeleportCommand());
        register(new ClearAllCommandsCommand());
    }
}