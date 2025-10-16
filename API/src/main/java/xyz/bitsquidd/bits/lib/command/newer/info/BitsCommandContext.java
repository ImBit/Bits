package xyz.bitsquidd.bits.lib.command.newer.info;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Utility class to encapsulate command context
 */
@NullMarked
public class BitsCommandContext {
    private final CommandContext<CommandSourceStack> ctx;
    private final CommandSourceStack sourceStack;

    public BitsCommandContext(CommandContext<CommandSourceStack> ctx) {
        this.ctx = ctx;
        this.sourceStack = ctx.getSource();
    }

    public @NotNull CommandSender getSender() {
        return sourceStack.getSender();
    }

    public @NotNull Player requirePlayer() {
        if (!(getSender() instanceof Player player)) throw new IllegalStateException("Command sender must be a player");
        return player;
    }


    public @NotNull CommandSourceStack getSourceStack() {
        return sourceStack;
    }

    public void sendMessage(@NotNull String message) {
        getSender().sendMessage(message);
    }

}