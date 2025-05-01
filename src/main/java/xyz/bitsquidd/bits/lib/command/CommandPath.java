package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.logging.LogController;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;
import xyz.bitsquidd.bits.lib.command.requirements.CommandRequirement;

import java.util.List;

// Sub-command paths.
// TODO: allow for nested command paths - i.e. Whitelist mode/limit/add CommandPath1/2/3
public class CommandPath {
    public final @NotNull String name;
    public final @NotNull String description;

    private final @NotNull List<CommandRequirement> requirements;
    private final @NotNull List<CommandArgumentInfo<?>> params;
    private final @NotNull CommandHandler handler;

    public CommandPath(@NotNull String name, @NotNull String description, @NotNull List<CommandRequirement> requirements, @NotNull List<CommandArgumentInfo<?>> params, @NotNull CommandHandler handler) {
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.params = params;
        this.handler = handler;
    }


    public boolean matchesPartial(CommandContext commandContext) {
        if (commandContext.getArgsLength() > getArgLength()) {
            return false;
        }

        for (int i = 0; i < commandContext.getArgsLength(); i++) {
            CommandArgumentInfo<?> commandArgumentInfo = getCommandParamAtIndex(i);
            if (!commandContext.getArg(i).isEmpty() && !commandArgumentInfo.param.canParseArg(commandContext, i)) {
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
        for (CommandArgumentInfo<?> commandArgumentInfo : params) {
            if (!commandArgumentInfo.param.canParseFull(commandContext, argIndex)) {
                return false;
            }
            argIndex += commandArgumentInfo.param.getRequiredArgs();
        }

        return true;
    }

    public int getArgLength() {
        int argLength = 0;
        for (CommandArgumentInfo<?> commandArgumentInfo : params) {
            argLength += commandArgumentInfo.param.getRequiredArgs();
        }

        return argLength;
    }

    private @NotNull CommandArgumentInfo<?> getCommandParamAtIndex(int index) {
        for (CommandArgumentInfo<?> commandArgumentInfo : params) {
            if (index < commandArgumentInfo.param.getRequiredArgs()) {
                return commandArgumentInfo;
            }
            index -= commandArgumentInfo.param.getRequiredArgs();
        }

        //TODO error here
        return null;
    }
    private int getCommandParamIndex(CommandArgumentInfo<?> commandArgumentInfo) {
        int argIndex = 0;
        for (CommandArgumentInfo<?> param : params) {
            if (param.equals(commandArgumentInfo)) {
                return argIndex;
            }
            argIndex += param.param.getRequiredArgs();
        }
        //TODO error here
        return -1;
    }

    public boolean execute(CommandContext commandContext) {
        int argIndex = 0;

        try {
            for (CommandArgumentInfo<?> commandArgumentInfo : params) {
                commandContext.set(commandArgumentInfo.name, commandArgumentInfo.param.parse(commandContext, argIndex));
                argIndex += commandArgumentInfo.param.getRequiredArgs();
            }
        } catch (ArgumentParseException e) {
            LogController.error("Command Parsing Exception: " + e.getMessage());
            return false;
        }

        handler.execute(commandContext);
        return true;
    }

    public List<String> tabComplete(@NotNull CommandContext commandContext) {
        CommandArgumentInfo<?> commandArgumentInfo = getCommandParamAtIndex(commandContext.getArgLength()-1);
        return commandArgumentInfo.param.tabComplete(commandContext, getCommandParamIndex(commandArgumentInfo));
    }

    public boolean hasPermissions(CommandContext commandContext) {
        for (CommandRequirement commandRequirement : requirements) {
            if (!commandRequirement.check(commandContext)) {
                return false;
            }
        }
        return true;
    }

    ///  Getters  ///
    public @NotNull List<CommandRequirement> getRequirements() {
        return requirements;
    }
    public @NotNull List<CommandArgumentInfo<?>> getParams() {
        return params;
    }
    public @NotNull CommandHandler getHandler() {
        return handler;
    }
}
