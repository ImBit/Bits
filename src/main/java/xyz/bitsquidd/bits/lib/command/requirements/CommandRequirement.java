package xyz.bitsquidd.bits.lib.command.requirements;

import xyz.bitsquidd.bits.lib.command.CommandContextNew;


public interface CommandRequirement {
    boolean check(CommandContextNew context, String[] args);
    default String getFailMessage(CommandContextNew context, String[] args) {
        return "You do not meet the required conditions to execute this command.";
    }
}