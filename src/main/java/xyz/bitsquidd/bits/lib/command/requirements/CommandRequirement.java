package xyz.bitsquidd.bits.lib.command.requirements;

import xyz.bitsquidd.bits.lib.command.CommandContext;


public interface CommandRequirement {
    boolean check(CommandContext context);
    default String getFailMessage(CommandContext context) {
        return "You do not meet the required conditions to execute this command.";
    }
}