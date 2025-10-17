package xyz.bitsquidd.bits.lib.command.requirement;

import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public class ConsoleSenderRequirement implements CommandRequirement {
    public static final ConsoleSenderRequirement INSTANCE = new ConsoleSenderRequirement();

    @Override
    public boolean check(@NotNull CommandContext context) {
        return context.getSender() instanceof ConsoleCommandSender;
    }

    @Override
    public @NotNull String getFailMessage(@NotNull CommandContext context) {
        return "You must be the console to execute this command.";
    }
}