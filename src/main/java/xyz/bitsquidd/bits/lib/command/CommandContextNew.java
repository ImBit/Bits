package xyz.bitsquidd.bits.lib.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandContextNew {
    public final @NotNull CommandSender sender;
    public final @NotNull String[] args;

    private final @NotNull Map<String, Object> params = new HashMap<>();

    public CommandContextNew(@NotNull CommandSender sender, @NotNull String[] args) {
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
    public World getWorld() {
        if (sender instanceof Player player) {
            return player.getWorld();
        } else {
            return Bukkit.getWorld("world");
        }
    }
}
