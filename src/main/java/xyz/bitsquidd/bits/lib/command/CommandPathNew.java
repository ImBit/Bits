package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.exceptions.ParamParseException;

import java.util.List;

// Sub-command paths.
public class CommandPathNew {
    public final String name;
    public final String description;
    public final List<String> permissions;

    public final List<CommandParamInfo> params;
    public final CommandHandlerNew handler;

    public CommandPathNew(String name, String description, List<String> permissions, List<CommandParamInfo> params, CommandHandlerNew handler) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.params = params;
        this.handler = handler;
    }


    public boolean matchesPartial(CommandContextNew commandContextNew) {
        if (commandContextNew.args.length > getArgLength()) {
            LogController.warning("EXIT 1");
            return false;
        }

        for (int i = 0; i < commandContextNew.args.length; i++) {
            CommandParamInfo commandParamInfo = getCommandParamAtIndex(i);
            LogController.warning(commandParamInfo.name);
            if (!commandContextNew.getArg(i).isEmpty() && !commandParamInfo.param.canParseArg(commandContextNew, i)) {
                LogController.warning("EXIT 2");
                return false;
            }
        }

        return true;
    }
    public boolean matchesFully(CommandContextNew commandContextNew) {
        int argLength = commandContextNew.getArgLength();

        if (argLength != getArgLength()) {
            return false;
        }

        int argIndex = 0;
        for (CommandParamInfo commandParamInfo : params) {
            if (!commandParamInfo.param.canParseFull(commandContextNew, argIndex)) {
                return false;
            }
            argIndex += commandParamInfo.param.getRequiredArgs();
        }

        return true;
    }

    public int getArgLength() {
        int argLength = 0;
        for (CommandParamInfo commandParamInfo : params) {
            argLength += commandParamInfo.param.getRequiredArgs();
        }

        return argLength;
    }

    private @NotNull CommandParamInfo getCommandParamAtIndex(int index) {
        int argIndex = 0;
        for (CommandParamInfo commandParamInfo : params) {
            argIndex += commandParamInfo.param.getRequiredArgs();
            if (argIndex >= index) {
                return commandParamInfo;
            }
        }
        //TODO error here
        return null;
    }

    public void execute(CommandContextNew commandContextNew) {
        int argIndex = 0;

        try {
            for (CommandParamInfo commandParamInfo : params) {
                commandContextNew.set(commandParamInfo.name, commandParamInfo.param.parse(commandContextNew, argIndex));
                argIndex += commandParamInfo.param.getRequiredArgs();
            }
        } catch (ParamParseException e) {
            LogController.error("Command Parsing Exception: " + e.getMessage());
            return;
        }

        handler.execute(commandContextNew);
    }

    public List<String> tabComplete(@NotNull CommandContextNew commandContextNew) {
        CommandParamInfo commandParamInfo = getCommandParamAtIndex(commandContextNew.getArgLength());
        return commandParamInfo.param.tabComplete(commandContextNew);
    }
}
