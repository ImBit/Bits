package xyz.bitsquidd.bits.lib.command;

import xyz.bitsquidd.bits.lib.command.params.CommandArgument;

// Used to store params and their names in commands.
public class CommandArgumentInfo {
    public final String name;
    public final CommandArgument<?> param;

    public CommandArgumentInfo(String name, CommandArgument<?> param) {
        this.name = name;
        this.param = param;
    }
}
