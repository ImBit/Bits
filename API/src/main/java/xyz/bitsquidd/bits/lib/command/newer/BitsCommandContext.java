package xyz.bitsquidd.bits.lib.command.newer;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BitsCommandContext {
    private final @NotNull CommandSourceStack sourceStack;
    private final @NotNull String[] args;
    private final @NotNull String commandName;

    public BitsCommandContext(@NotNull CommandSourceStack sourceStack, @NotNull String[] args, @NotNull String commandName) {
        this.sourceStack = sourceStack;
        this.args = args;
        this.commandName = commandName;
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

    public @NotNull String[] getArgs() {
        return args.clone();
    }

    public @NotNull String getCommandName() {
        return commandName;
    }

    public void sendMessage(@NotNull String message) {
        getSender().sendMessage(message);
    }

}