package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.arguments.SimpleOptionArgument;

public class CommandOptionInfo extends CommandArgumentInfo<String> {
    public CommandOptionInfo(@NotNull String name) {
        super(name.toUpperCase(), SimpleOptionArgument.of(name));
    }

    public static CommandOptionInfo of(@NotNull String name) {
        return new CommandOptionInfo(name);
    }
}
