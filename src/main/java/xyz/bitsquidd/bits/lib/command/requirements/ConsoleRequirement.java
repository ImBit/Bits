package xyz.bitsquidd.bits.lib.command.requirements;

import org.bukkit.command.ConsoleCommandSender;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public class ConsoleRequirement implements CommandRequirement {
    public static final ConsoleRequirement INSTANCE = new ConsoleRequirement();

    @Override
    public boolean check(CommandContext context) {
        return context.getSender() instanceof ConsoleCommandSender;
    }

    @Override
    public String getFailMessage(CommandContext context) {
        return "You must be a player to execute this command.";
    }
}