package xyz.bitsquidd.bits.lib.command.requirements;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;


public interface CommandRequirement {
    boolean check(@NotNull CommandContext context);

    default @NotNull String getFailMessage(@NotNull CommandContext context) {
        return "You do not meet the required conditions to execute this command.";
    }
}