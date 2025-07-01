package xyz.bitsquidd.bits.lib.command.requirements;

import org.bukkit.permissions.Permission;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public class PermissionRequirement implements CommandRequirement {
    private final Permission permission;

    public PermissionRequirement(Permission permission) {
        this.permission = permission;
    }

    public PermissionRequirement(String permission) {
        this.permission = new Permission(permission);
    }

    @Override
    public boolean check(CommandContext context) {
        return context.getSender().hasPermission(permission);
    }

    @Override
    public String getFailMessage(CommandContext context) {
        return "You do not have permission to use this command.";
    }
}