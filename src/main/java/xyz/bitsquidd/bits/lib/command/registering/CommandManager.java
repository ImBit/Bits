package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.logging.LogController;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;

import java.lang.reflect.Field;
import java.util.*;

public abstract class CommandManager {
    private final Plugin plugin;

    private CommandMap commandMap;
    private final Map<String, BukkitCommand> registeredCommands = new HashMap<>();
    private final Set<Command> commandSet = new HashSet<>();

    protected CommandManager(Plugin plugin) {
        this.plugin = plugin;

        initialiseCommandMap();
        registerCommands();
    }

    private void initialiseCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            LogController.error("Failed to access command map: " + e.getMessage());
            LogController.exception(e);
        }
    }

    protected abstract void registerCommands();

    public void register(AbstractCommand command) {
        if (plugin == null || commandMap == null) {
            throw new IllegalStateException("CommandManager has not been initialized. Call initialise() first.");
        }

        String name = command.name;
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Command name cannot be empty");
        }

        if (registeredCommands.containsKey(name.toLowerCase())) {
            LogController.warning("Command with name '" + name + "' is already registered. Skipping registration.");
            return;
        }

        BukkitCommand bukkitCommand = new BukkitCommand(
                name,
                command.description,
                "/" + name,
                new ArrayList<>(List.of(command.aliases)),
                command
        );

        registeredCommands.put(name.toLowerCase(Locale.ROOT), bukkitCommand);
        commandSet.add(bukkitCommand);
        commandMap.register(plugin.getName().toLowerCase(Locale.ROOT), bukkitCommand);
        LogController.info("Registered command: " + name);
    }

    public void unregisterAll() {
        CommandMap commandMap = Bukkit.getCommandMap();

        registeredCommands.keySet().forEach(s -> {
            LogController.info("Unregistered command: " + s);
            commandMap.getCommand(s).unregister(commandMap);
        });


        registeredCommands.clear();
        commandSet.clear();
        LogController.info("Unregistered all commands");
    }

    @NotNull
    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commandSet);
    }

    @Nullable
    public BukkitCommand getCommand(String name) {
        return registeredCommands.get(name.toLowerCase(Locale.ROOT));
    }
}