package xyz.bitsquidd.bits.lib.command.requirement;

import xyz.bitsquidd.bits.lib.command.CommandContext;

public interface CommandRequirement {
    boolean check(CommandContext context);
    String getFailMessage();
}