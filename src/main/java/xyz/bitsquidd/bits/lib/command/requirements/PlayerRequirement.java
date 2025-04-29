package xyz.bitsquidd.bits.lib.command.requirements;

import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContext;


public class PlayerRequirement implements CommandRequirement {

    @Override
    public boolean check(CommandContext context, String[] args) {
        return context.sender instanceof Player;
    }

    @Override
    public String getFailMessage(CommandContext context, String[] args) {
        return "You must be a player to execute this command.";
    }
}