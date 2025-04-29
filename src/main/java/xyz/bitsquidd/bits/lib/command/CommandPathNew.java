package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.List;

// Sub-command paths.
public class CommandPathNew {
    public final String name;
    public final String description;
    public final String permission;

    public final List<CommandArgumentInfo> params;
    public final CommandHandler handler;

    public CommandPathNew(String name, String description, String permission, List<CommandArgumentInfo> params, CommandHandler handler) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.params = params;
        this.handler = handler;
    }


    public boolean matchesPartial(CommandContext commandContext) {
        if (commandContext.args.length > getArgLength()) {
            LogController.warning("EXIT 1");
            return false;
        }

        for (int i = 0; i < commandContext.args.length; i++) {
            CommandArgumentInfo commandArgumentInfo = getCommandParamAtIndex(i);
            if (!commandContext.getArg(i).isEmpty() && !commandArgumentInfo.param.canParseArg(commandContext, i)) {
                LogController.warning("EXIT 2   " + commandArgumentInfo.name + " " + commandContext.getArg(i) + "  " + i);
                return false;
            }
        }

        return true;
    }
    public boolean matchesFully(CommandContext commandContext) {
        int argLength = commandContext.getArgLength();

        if (argLength != getArgLength()) {
            return false;
        }

        int argIndex = 0;
        for (CommandArgumentInfo commandArgumentInfo : params) {
            if (!commandArgumentInfo.param.canParseFull(commandContext, argIndex)) {
                return false;
            }
            argIndex += commandArgumentInfo.param.getRequiredArgs();
        }

        return true;
    }

    public int getArgLength() {
        int argLength = 0;
        for (CommandArgumentInfo commandArgumentInfo : params) {
            argLength += commandArgumentInfo.param.getRequiredArgs();
        }

        return argLength;
    }

    private @NotNull CommandArgumentInfo getCommandParamAtIndex(int index) {
        for (CommandArgumentInfo commandArgumentInfo : params) {
            if (index < commandArgumentInfo.param.getRequiredArgs()) {
                return commandArgumentInfo;
            }
            index -= commandArgumentInfo.param.getRequiredArgs();
        }

        //TODO error here
        return null;
    }
    private int getCommandParamIndex(CommandArgumentInfo commandArgumentInfo) {
        int argIndex = 0;
        for (CommandArgumentInfo param : params) {
            if (param.equals(commandArgumentInfo)) {
                return argIndex;
            }
            argIndex += param.param.getRequiredArgs();
        }
        //TODO error here
        return -1;
    }

    public void execute(CommandContext commandContext) {
        int argIndex = 0;

        try {
            for (CommandArgumentInfo commandArgumentInfo : params) {
                commandContext.set(commandArgumentInfo.name, commandArgumentInfo.param.parse(commandContext, argIndex));
                argIndex += commandArgumentInfo.param.getRequiredArgs();
            }
        } catch (ArgumentParseException e) {
            LogController.error("Command Parsing Exception: " + e.getMessage());
            return;
        }

        handler.execute(commandContext);
    }

    public List<String> tabComplete(@NotNull CommandContext commandContext) {
        CommandArgumentInfo commandArgumentInfo = getCommandParamAtIndex(commandContext.getArgLength()-1);
        return commandArgumentInfo.param.tabComplete(commandContext, getCommandParamIndex(commandArgumentInfo));
    }
}
