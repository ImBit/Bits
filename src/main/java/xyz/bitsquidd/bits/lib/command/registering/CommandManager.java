package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;

import java.lang.reflect.Field;
import java.util.*;

public class CommandManager {
    private static Plugin plugin;
    private static CommandMap commandMap;

    private static final Map<String, BukkitCommand> registeredCommands = new HashMap<>();
    private static final Set<Command> commandSet = new HashSet<>();

    public static void initialise(Plugin plugin) {
        CommandManager.plugin = plugin;
        initialiseCommandMap();
    }

    private static void initialiseCommandMap() {
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
        plugin.getLogger().info("Registered command: " + name);
    }

    public static void unregisterAll() {
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
    public static Set<Command> getCommands() {
        return Collections.unmodifiableSet(commandSet);
    }

    @Nullable
    public static BukkitCommand getCommand(String name) {
        return registeredCommands.get(name.toLowerCase(Locale.ROOT));
    }
}