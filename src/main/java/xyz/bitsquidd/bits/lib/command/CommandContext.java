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
    public final @NotNull CommandSender sender;
    public final @NotNull String[] args;

    private final @NotNull Map<String, Object> params = new HashMap<>();

    public CommandContext(@NotNull CommandSender sender, @NotNull String[] args) {
        this.sender = sender;
        this.args = args;
    }


    // Arg handling - these are the arguments passed to the command
    public @NotNull String getArg(int index) {
        if (index < 0 || index >= args.length) {
            // TODO error?
            return "ERROR_ARG";
        }
        return args[index];
    }
    public @NotNull String getLastArg() {
        if (args.length == 0) {
            // TODO error?
            return "ERROR_ARG";
        }
        return args[args.length - 1];
    }
    public int getArgLength() {
        return args.length;
    }

    // Parameter handling - these are the parameters parsed from the command
    public void set(String name, Object value) {
        params.put(name, value);
    }
    public @NotNull <T> T get(String name) {
        // TODO: error if doesnt exist
        return (T) params.get(name);
    }

    // Helpers for parsers:
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public @NotNull Location getLocation() {
        if (isPlayer()) {
            return ((Player)sender).getLocation();
        } else {
            return new Location(getWorld(), 0,0,0); //TODO error
        }
    }
    public @NotNull World getWorld() {
        if (isPlayer()) {
            return ((Player)sender).getWorld();
        } else {
            return Bukkit.getWorld("world"); //TODO error
        }
    }
}
