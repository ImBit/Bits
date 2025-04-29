package xyz.bitsquidd.bits.lib.command.requirements;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public interface CommandRequirement {
    boolean check(CommandContext context, String[] args);
    default String getFailMessage(CommandContext context, String[] args) {
        return "You do not meet the required conditions to execute this command.";
    }
}