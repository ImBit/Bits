package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import xyz.bitsquidd.bits.lib.command.examples.AbstractCommandNew;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManagerNew {
    private static Plugin plugin;
    private static CommandMap commandMap;

    private static final Map<String, AbstractCommandNew> commands = new HashMap<>();

    public static void initialise(Plugin plugin) {
        CommandManagerNew.plugin = plugin;
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to access command map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void register(AbstractCommandNew command) {
        if (plugin == null) {
            throw new IllegalStateException("CommandManager has not been initialized. Call init() first.");
        }

        String name = command.name;
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Command name cannot be empty");
        }

        commands.put(name.toLowerCase(), command);

        BukkitCommand bukkitCommand = new BukkitCommand(
                name,
                command.description,
                "/" + name,
                new ArrayList<>(List.of(command.aliases)),
                command
        );

        commandMap.register(plugin.getName().toLowerCase(), bukkitCommand);
        plugin.getLogger().info("Registered command: " + name);
    }

    public static void unregisterAll() {
        for (String name : commands.keySet()) {
            try {
                commandMap.getCommand(name).unregister(commandMap);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to unregister command: " + name + " - " + e.getMessage());
            }
        }
        commands.clear();
    }

}
