package xyz.bitsquidd.bits.lib.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private final CommandSender sender;
    private final String[] args;
    private final Map<String, Object> parsedParams = new HashMap<>();
    private int currentArgIndex = 0;

    public CommandContext(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String paramName) {
        return (T) parsedParams.get(paramName);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) parsedParams.get(String.valueOf(index));
    }

    public void setParam(String name, Object value) {
        parsedParams.put(name, value);
    }

    public Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
        }
        return null;
    }

    public void sendMessage(String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public int getCurrentArgIndex() {
        return currentArgIndex;
    }

    public void setArgIndex(int index) {
        this.currentArgIndex = index;
    }

    public void advanceArgIndex(int count) {
        this.currentArgIndex += count;
    }

    public void resetArgIndex() {
        this.currentArgIndex = 0;
    }

    public String getCurrentArg() {
        if (currentArgIndex < args.length) {
            return args[currentArgIndex];
        }
        return null;
    }

    public String getArgString() {
        if (args.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        return sb.toString().trim();
    }
}