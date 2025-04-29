package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;

import java.util.List;

public class BukkitCommand extends Command implements CommandExecutor, TabCompleter {
        private final AbstractCommand command;
        
        public BukkitCommand(String name, String description, String usageMessage, List<String> aliases, AbstractCommand command) {
            super(name, description, usageMessage, aliases);
            this.command = command;
            
            if (command.commandPermission != null && !command.commandPermission.isEmpty()) {
                setPermission(command.commandPermission);
            }
        }
        
        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            return command.execute(new CommandContext(sender, args));
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
    }
