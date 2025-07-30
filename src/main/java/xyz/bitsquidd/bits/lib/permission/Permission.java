package xyz.bitsquidd.bits.lib.permission;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Permission {
    private final @NotNull String name;
    private final @NotNull String description;


    public Permission(@NotNull String name) {
        this.name = name;
        this.description = "";
    }

    public static Permission of(@NotNull String name) {
        return new Permission(name);
    }

    public boolean hasPermission(CommandSender commandSender) {
        return name.isEmpty() || commandSender.hasPermission(name);
    }
}
