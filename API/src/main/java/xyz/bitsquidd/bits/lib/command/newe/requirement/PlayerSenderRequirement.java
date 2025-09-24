package xyz.bitsquidd.bits.lib.command.newe.requirement;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerSenderRequirement extends BitsRequirement {
    public static final PlayerSenderRequirement INSTANCE = new PlayerSenderRequirement();

    @Override
    public boolean test(CommandSourceStack commandSourceStack) {
        CommandSender sender = commandSourceStack.getSender();

        if (!(sender instanceof Player)) {
            return fail(commandSourceStack, Component.text("This command can only be run by a player."));
        }

        return true;
    }
}
