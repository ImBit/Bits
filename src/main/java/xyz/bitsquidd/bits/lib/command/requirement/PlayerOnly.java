package xyz.bitsquidd.bits.lib.command.requirement;

import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContext;

public class PlayerOnly implements CommandRequirement {
    public static final PlayerOnly INSTANCE = new PlayerOnly();

    @Override
    public boolean check(CommandContext context) {
        return context.getSender() instanceof Player;
    }

    @Override
    public String getFailMessage() {
        return "&cThis command can only be executed by players.";
    }
}