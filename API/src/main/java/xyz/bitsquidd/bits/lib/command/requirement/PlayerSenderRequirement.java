package xyz.bitsquidd.bits.lib.command.requirement;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public class PlayerSenderRequirement implements CommandRequirement {
    public static final PlayerSenderRequirement INSTANCE = new PlayerSenderRequirement();

    @Override
    public boolean check(@NotNull CommandContext context) {
        return context.getSender() instanceof Player;
    }

    @Override
    public @NotNull String getFailMessage(@NotNull CommandContext context) {
        return "You must be a player to execute this command.";
    }
}