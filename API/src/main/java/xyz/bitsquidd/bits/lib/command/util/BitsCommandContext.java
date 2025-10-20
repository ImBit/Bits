package xyz.bitsquidd.bits.lib.command.util;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.sendable.text.Text;

/**
 * Utility class to encapsulate command context
 */
@NullMarked
public class BitsCommandContext {
    private final CommandSourceStack sourceStack;

    public BitsCommandContext(CommandSourceStack sourceStack) {
        this.sourceStack = sourceStack;
    }

    public CommandSender getSender() {
        return sourceStack.getSender();
    }

    public Player requirePlayer() {
        if (!(getSender() instanceof Player player)) throw new IllegalStateException("Command sender must be a player");
        return player;
    }


    public CommandSourceStack getSourceStack() {
        return sourceStack;
    }

    public void respond(Text message) {
        message.send(getSender());
    }

}