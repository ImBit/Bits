package xyz.bitsquidd.bits.lib.command.requirement;

import org.bukkit.command.CommandSender;
import xyz.bitsquidd.bits.lib.command.CommandContext;

public class HasPermission implements CommandRequirement {
    private final String permission;
    private final String message;

    public HasPermission(String permission) {
        this(permission, "&cYou don't have permission to use this command.");
    }

    public HasPermission(String permission, String message) {
        this.permission = permission;
        this.message = message;
    }

    @Override
    public boolean check(CommandContext context) {
        CommandSender sender = context.getSender();
        return sender.hasPermission(permission);
    }

    @Override
    public String getFailMessage() {
        return message;
    }
}