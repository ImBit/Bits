package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandHandler {
    void execute(@NotNull CommandContext context);
}