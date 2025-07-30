package xyz.bitsquidd.bits.lib.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private final @NotNull CommandSender sender;
    private final @NotNull String[] args;

    private final @NotNull Map<String, Object> params = new HashMap<>();

    public CommandContext(@NotNull CommandSender sender, @NotNull String[] args) {
        this.sender = sender;
        this.args = args;
    }


    // Vanilla Arg handling - these are the arguments passed to the command
    public @NotNull String getArg(int index) {
        if (index < 0 || index >= args.length) {
            return "";
        }
        return args[index];
    }

    public @NotNull String getLastArg() {
        if (args.length == 0) {
            return "";
        }
        return args[args.length - 1];
    }

    public int getArgLength() {
        return args.length;
    }

    public @NotNull CommandSender getSender() {
        return sender;
    }

    public @NotNull String[] getArgs() {
        return args.clone();
    }

    public int getArgsLength() {
        return args.length;
    }


    // Argument handling - these are the parameters parsed from the command
    public void set(String name, Object value) {
        params.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> T get(String name) {
        if (params.get(name) == null) {
            throw new IllegalArgumentException("Parameter " + name + " not found");
        } else {
            return (T)params.get(name);
        }
    }

    public @NotNull <T> T getOrDefault(String name, T defaultValue) {
        if (params.get(name) == null) {
            return defaultValue;
        } else {
            return (T)params.get(name);
        }
    }


    // Helpers for parsers:
    public boolean isEmpty() {
        return args.length == 0 || args[0].isEmpty();
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public @NotNull Location getLocation() {
        if (isPlayer()) {
            return ((Player) sender).getLocation();
        } else {
            return new Location(getWorld(), 0, 0, 0);
        }
    }

    public @NotNull World getWorld() {
        if (isPlayer()) {
            return ((Player) sender).getWorld();
        } else {
            World world = Bukkit.getWorld("world");
            if (world == null) {
                throw new IllegalArgumentException("Default world: `world` not found");
            } else {
                return world;
            }
        }
    }
}
