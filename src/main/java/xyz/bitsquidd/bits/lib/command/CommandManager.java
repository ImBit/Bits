package xyz.bitsquidd.bits.lib.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.parameters.CommandParam;
import xyz.bitsquidd.bits.lib.command.parameters.ParamInfo;
import xyz.bitsquidd.bits.lib.command.requirement.CommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.HasPermission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private static final Map<String, AbstractCommand> commands = new HashMap<>();
    private static Plugin plugin;
    private static CommandMap commandMap;
    
    public static void initialise(Plugin plugin) {
        CommandManager.plugin = plugin;
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to access command map: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void register(AbstractCommand command) {
        if (plugin == null) {
            throw new IllegalStateException("CommandManager has not been initialized. Call init() first.");
        }
        
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Command name cannot be empty");
        }

        command.configurePaths();

        commands.put(name.toLowerCase(), command);
        
        BukkitCommand bukkitCommand = new BukkitCommand(
            name,
            command.getDescription(),
            "/" + name,
            new ArrayList<>(List.of(command.getAliases())),
            command
        );
        
        commandMap.register(plugin.getName().toLowerCase(), bukkitCommand);
        plugin.getLogger().info("Registered command: " + name);
    }
    
    public static AbstractCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }
    
    public static void unregister(String name) {
        commands.remove(name.toLowerCase());
    }
    
    public static PathBuilder getPath(String commandName) {
        AbstractCommand command = getCommand(commandName);
        if (command == null) {
            throw new IllegalArgumentException("Command not found: " + commandName);
        }
        return new PathBuilder(command);
    }
    
    private static class BukkitCommand extends Command implements CommandExecutor, TabCompleter {
        private final AbstractCommand command;
        
        public BukkitCommand(String name, String description, String usageMessage, List<String> aliases, AbstractCommand command) {
            super(name, description, usageMessage, aliases);
            this.command = command;
            
            if (command.getPermission() != null && !command.getPermission().isEmpty()) {
                setPermission(command.getPermission());
            }
        }
        
        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            return command.execute(sender, args);
        }
        
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            return execute(sender, label, args);
        }
        
        @Override
        public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
            return command.tabComplete(sender, args);
        }
        
        @Override
        public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            return tabComplete(sender, alias, args);
        }
    }
    
    public static class PathBuilder {
        private final AbstractCommand command;
        private List<PathBranch> branches = new ArrayList<>();
        
        public PathBuilder(AbstractCommand command) {
            this.command = command;
        }
        
        public PathBuilder branch(java.util.function.Function<PathBranch, PathBranch> branchBuilder) {
            PathBranch branch = new PathBranch();
            branches.add(branchBuilder.apply(branch));
            return this;
        }
        
        public static class PathBranch {
            private final List<ParamInfo> params = new ArrayList<>();
            private final List<CommandRequirement> requirements = new ArrayList<>();
            private String permission;
            private CommandHandler handler;
            private boolean hidden = false;
            
            public PathBranch param(CommandParam<?> param, String name) {
                params.add(new ParamInfo(param, name));
                return this;
            }
            
            public PathBranch requires(CommandRequirement requirement) {
                requirements.add(requirement);
                return this;
            }
            
            public PathBranch permission(String permission) {
                this.permission = permission;
                requires(new HasPermission(permission));
                return this;
            }
            
            public PathBranch handler(CommandHandler handler) {
                this.handler = handler;
                return this;
            }
            
            public PathBranch hidden(boolean hidden) {
                this.hidden = hidden;
                return this;
            }
        }
    }
}