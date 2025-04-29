package xyz.bitsquidd.bits.lib.command;

import xyz.bitsquidd.bits.lib.command.params.CommandParamNew;

// Used to store params and their names in commands.
public class CommandParamInfo {
    public final String name;
    public final CommandParamNew<?> param;

    public CommandParamInfo(String name, CommandParamNew<?> param) {
        this.name = name;
        this.param = param;
    }
}
