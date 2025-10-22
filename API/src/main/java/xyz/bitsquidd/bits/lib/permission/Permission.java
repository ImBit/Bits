package xyz.bitsquidd.bits.lib.permission;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Permission {
    private final String name;
    private final String description;


    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public static Permission of(String name) {
        return new Permission(name, "");
    }

    public static Permission of(String name, String description) {
        return new Permission(name, description);
    }


    public boolean hasPermission(CommandSender commandSender) {
        return name.isEmpty() || commandSender.hasPermission(name);
    }
}
