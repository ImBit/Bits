package xyz.bitsquidd.bits.lib.command;

import org.bukkit.command.CommandSender;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.command.exception.ParamParseException;
import xyz.bitsquidd.bits.lib.command.parameters.CommandParam;
import xyz.bitsquidd.bits.lib.command.parameters.ParamInfo;
import xyz.bitsquidd.bits.lib.command.requirement.CommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.HasPermission;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    private String name;
    private String[] aliases = new String[0];
    private String description = "";
    private String permission = "";
    private boolean playerOnly = false;
    private boolean hidden = false;
    private String parent = "";

    private final List<CommandPath> commandPaths = new ArrayList<>();

    public AbstractCommand() {
        Command cmdAnnotation = getClass().getAnnotation(Command.class);
        if (cmdAnnotation != null) {
            this.name = cmdAnnotation.name();
            this.aliases = cmdAnnotation.aliases();
            this.description = cmdAnnotation.description();
            this.permission = cmdAnnotation.permission();
            this.playerOnly = cmdAnnotation.playerOnly();
            this.hidden = cmdAnnotation.hidden();
            this.parent = cmdAnnotation.parent();
        }
    }

    public abstract void configurePaths();

    protected CommandPathBuilder path() {
        return new CommandPathBuilder(this);
    }

    void addPath(CommandPath path) {
        commandPaths.add(path);
    }

    public boolean execute(CommandSender sender, String[] args) {
        CommandContext context = new CommandContext(sender, args);

        for (CommandPath path : commandPaths) {
            if (path.matches(context)) {
                return path.execute(context);
            }
        }

        List<String> errorMessages = new ArrayList<>();

        for (CommandPath path : commandPaths) {
            context.resetArgIndex();
            try {
                for (ParamInfo<?> paramInfo : path.getParams()) {
                    CommandParam<?> param = paramInfo.getParam();
                    String paramName = paramInfo.getName();

                    try {
                        if (context.getCurrentArgIndex() >= args.length) {
                            errorMessages.add("&c/" + name + " " + path.getUsage() + " - Missing argument: " + paramName);
                            break;
                        }

                        param.parse(context, context.getCurrentArg());
                        context.advanceArgIndex(1);
                    } catch (ParamParseException e) {
                        errorMessages.add("&c/" + name + " " + path.getUsage() + " - " + e.getMessage());
                        break;
                    }
                }

                if (context.getCurrentArgIndex() < args.length) {
                    errorMessages.add("&c/" + name + " " + path.getUsage() + " - Too many arguments");
                }
            } catch (Exception e) {
                errorMessages.add("&c/" + name + " " + path.getUsage() + " - Error: " + e.getMessage());
            }
        }

        sendUsage(context);

        if (!errorMessages.isEmpty()) {
            context.sendMessage("&cDetailed errors:");
            for (String error : errorMessages) {
                context.sendMessage(error);
            }
        }

        return true;
    }

    protected void sendUsage(CommandContext context) {
        context.sendMessage("&cUsage:");
        for (CommandPath path : commandPaths) {
            if (!path.isHidden()) {
                context.sendMessage("&e/" + name + " " + path.getUsage());
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        CommandContext context = new CommandContext(sender, args);
        List<String> suggestions = new ArrayList<>();

        List<CommandPath> matchingPaths = findMatchingPathsForTabCompletion(context);

        for (CommandPath path : matchingPaths) {
            suggestions.addAll(path.tabComplete(context));
        }

        return suggestions;
    }

    private List<CommandPath> findMatchingPathsForTabCompletion(CommandContext context) {
        List<CommandPath> matchingPaths = new ArrayList<>();
        String[] args = context.getArgs();

        for (CommandPath path : commandPaths) {
            List<ParamInfo<?>> params = path.getParams();
            if (params.size() < args.length - 1) {
                continue;
            }

            boolean isMatch = true;
            context.resetArgIndex();

            for (int i = 0; i < args.length - 1 && i < params.size(); i++) {
                ParamInfo<?> paramInfo = params.get(i);
                CommandParam<?> param = paramInfo.getParam();

                try {
                    param.parse(context, context.getCurrentArg());
                    context.advanceArgIndex(1);
                } catch (ParamParseException e) {
                    isMatch = false;
                    break;
                }
            }

            if (isMatch) {
                for (CommandRequirement requirement : path.getRequirements()) {
                    if (!requirement.check(context)) {
                        isMatch = false;
                        break;
                    }
                }
            }

            if (isMatch) {
                matchingPaths.add(path);
            }
        }

        return matchingPaths;
    }

    public String getName() {
        return name;
    }

    protected AbstractCommand name(String name) {
        this.name = name;
        return this;
    }

    public String[] getAliases() {
        return aliases;
    }

    protected AbstractCommand aliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public String getDescription() {
        return description;
    }

    protected AbstractCommand description(String description) {
        this.description = description;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    protected AbstractCommand permission(String permission) {
        this.permission = permission;
        return this;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    protected AbstractCommand playerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    protected AbstractCommand hidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public String getParent() {
        return parent;
    }

    protected AbstractCommand parent(String parent) {
        this.parent = parent;
        return this;
    }

    public static class CommandPathBuilder {
        private final AbstractCommand command;
        private final List<ParamInfo<?>> params = new ArrayList<>();
        private final List<CommandRequirement> requirements = new ArrayList<>();
        private CommandHandler handler;
        private String name;
        private boolean hidden = false;

        public CommandPathBuilder(AbstractCommand command) {
            this.command = command;
        }

        public CommandPathBuilder name(String name) {
            this.name = name;
            return this;
        }

        public <T> CommandPathBuilder param(CommandParam<T> param, String name) {
            params.add(new ParamInfo<>(param, name));
            return this;
        }

        public CommandPathBuilder requires(CommandRequirement requirement) {
            requirements.add(requirement);
            return this;
        }

        public CommandPathBuilder permission(String permission) {
            requires(new HasPermission(permission));
            command.permission(permission);
            return this;
        }

        public CommandPathBuilder handler(CommandHandler handler) {
            this.handler = handler;
            return this;
        }

        public CommandPathBuilder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public void register() {
            if (handler == null) {
                throw new IllegalStateException("Command path must have a handler");
            }

            CommandPath path = new CommandPath(name, params, requirements, handler, hidden);
            command.addPath(path);
        }
    }
}