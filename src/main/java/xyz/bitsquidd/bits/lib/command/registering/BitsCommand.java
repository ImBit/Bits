package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;
import xyz.bitsquidd.bits.lib.command.CommandContext;

import java.util.List;

public class BitsCommand extends Command implements CommandExecutor, TabCompleter {
    private final @NotNull AbstractCommand command;

    public BitsCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases, @NotNull AbstractCommand command) {
        super(name, description, usageMessage, aliases);
        this.command = command;

        if (!command.commandPermission.isEmpty()) {
            setPermission(command.commandPermission);
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return command.vanillaExecute(new CommandContext(sender, args));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return execute(sender, label, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return command.tabComplete(new CommandContext(sender, args));
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tabComplete(sender, alias, args);
    }

    @Override
    public boolean canBeOverriden() {
        return true;
    }
}
