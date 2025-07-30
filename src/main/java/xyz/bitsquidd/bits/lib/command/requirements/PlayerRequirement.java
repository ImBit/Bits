package xyz.bitsquidd.bits.lib.command.requirements;

import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public class PlayerRequirement implements CommandRequirement {
    public static final PlayerRequirement INSTANCE = new PlayerRequirement();

    @Override
    public boolean check(CommandContext context) {
        return context.getSender() instanceof Player;
    }

    @Override
    public String getFailMessage(CommandContext context) {
        return "You must be a player to execute this command.";
    }
}