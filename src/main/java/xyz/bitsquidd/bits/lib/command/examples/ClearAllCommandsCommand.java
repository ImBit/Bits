package xyz.bitsquidd.bits.lib.command.examples;

import org.bukkit.Bukkit;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.annotations.CommandNew;

@CommandNew(name = "clearallcommands", description = "Teleport players to a location", permission = "minecraft.command.teleport")
public class ClearAllCommandsCommand extends AbstractCommand {

    @Override
    public void initialisePaths() {

    }

    @Override
    public boolean defaultExecute(CommandContext commandContext) {
        Bukkit.getCommandMap().clearCommands();
        return true;
    }
}
