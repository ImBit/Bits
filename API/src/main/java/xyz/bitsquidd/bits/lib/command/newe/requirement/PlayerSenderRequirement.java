package xyz.bitsquidd.bits.lib.command.newe.requirement;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerSenderRequirement extends BitsRequirement {
    public static final PlayerSenderRequirement INSTANCE = new PlayerSenderRequirement();

    @Override
    public boolean test(CommandSourceStack commandSourceStack) {
        CommandSender sender = commandSourceStack.getSender();

        if (!(sender instanceof Player)) {
            return false; // fail(commandSourceStack, Component.text("This command can only be run by a player."))
        }

        return true;
    }
}
