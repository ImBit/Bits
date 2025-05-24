package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;

import java.util.*;

public abstract class CommandManager {
    private final Plugin plugin;

    private CommandMap commandMap;
    private final Map<String, BitsCommand> registeredCommands = new HashMap<>();
    private final Set<Command> commandSet = new HashSet<>();

    protected CommandManager(Plugin plugin) {
        this.plugin = plugin;

        initialiseCommandMap();
    }

    private void initialiseCommandMap() {
        try {
            commandMap = Bukkit.getCommandMap();
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to access command map: " + e.getMessage());
        }
    }

    public abstract void registerCommands();

    public void register(AbstractCommand command) {
        if (plugin == null || commandMap == null) {
            throw new IllegalStateException("The commandMap has not been correctly initialized.");
        }

        String name = command.name;
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Command name cannot be empty");
        }

        if (registeredCommands.containsKey(name.toLowerCase())) {
            Bukkit.getLogger().warning("Command with name '" + name + "' is already registered. Skipping registration.");
            return;
        }

        BitsCommand bitsCommand = new BitsCommand(
                name,
                command.description,
                "/" + name,
                new ArrayList<>(List.of(command.aliases)),
                command
        );

        registeredCommands.put(name.toLowerCase(Locale.ROOT), bitsCommand);
        commandSet.add(bitsCommand);
        commandMap.register(plugin.getName().toLowerCase(Locale.ROOT), bitsCommand);
        Bukkit.getLogger().info("Registered command: " + name);
    }

    public void unregisterAll() {
        CommandMap commandMap = Bukkit.getCommandMap();

        registeredCommands.keySet().forEach(s -> {
            Command command = commandMap.getCommand(s);
            if (command != null) {
                command.unregister(commandMap);
                Bukkit.getLogger().info("Unregistered command: " + s);
            }
        });

        registeredCommands.clear();
        commandSet.clear();

        Bukkit.reloadCommandAliases();
        Bukkit.getLogger().info("Unregistered all commands");
    }

    @NotNull
    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commandSet);
    }

    @Nullable
    public BitsCommand getCommand(String name) {
        return registeredCommands.get(name.toLowerCase(Locale.ROOT));
    }
}