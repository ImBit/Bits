package xyz.bitsquidd.bits.lib.command;

import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.exception.ParamParseException;
import xyz.bitsquidd.bits.lib.command.parameters.ParamInfo;
import xyz.bitsquidd.bits.lib.command.requirement.CommandRequirement;

import java.util.ArrayList;
import java.util.List;

public class CommandPath {
    private final String name;
    private final List<ParamInfo<?>> params;
    private final List<CommandRequirement> requirements;
    private final CommandHandler handler;
    private final boolean hidden;

    private static final boolean DEBUG = true;

    public CommandPath(String name, List<ParamInfo<?>> params,
                       List<CommandRequirement> requirements,
                       CommandHandler handler, boolean hidden) {
        this.name = name;
        this.params = new ArrayList<>(params);
        this.requirements = new ArrayList<>(requirements);
        this.handler = handler;
        this.hidden = hidden;
    }

    public boolean matches(CommandContext context) {
        String[] args = context.getArgs();

        if (DEBUG) {
            System.out.println("\nMatching path: " + name);
            System.out.println("Path has " + params.size() + " params, args length: " + args.length);
            for (int i = 0; i < params.size(); i++) {
                ParamInfo<?> paramInfo = params.get(i);
                System.out.println("Param " + i + ": " + paramInfo.getName() +
                        " (type: " + paramInfo.getParam().getTypeName() + ")" +
                        " expects " + paramInfo.getParam().getExpectedArgCount() + " args");
            }
            System.out.println("Args: " + String.join(" ", args));
        }

        context.resetArgIndex();

        if (args.length == 0 && params.isEmpty()) {
            if (DEBUG) System.out.println("Match: No args, no params");
            return true;
        }

        if (args.length == 0) {
            if (DEBUG) System.out.println("No match: No args but params needed");
            return false;
        }

        try {
            int totalExpectedArgs = 0;
            for (ParamInfo<?> paramInfo : params) {
                totalExpectedArgs += paramInfo.getParam().getExpectedArgCount();
            }

            if (args.length != totalExpectedArgs) {
                if (DEBUG) {
                    System.out.println("No match: Expected " + totalExpectedArgs +
                            " args but got " + args.length);
                }
                return false;
            }

            for (ParamInfo<?> paramInfo : params) {
                if (context.getCurrentArgIndex() >= args.length) {
                    if (DEBUG) {
                        System.out.println("No match: Not enough args at index " +
                                context.getCurrentArgIndex());
                    }
                    return false;
                }

                int startIndex = context.getCurrentArgIndex();
                int expectedArgs = paramInfo.getParam().getExpectedArgCount();

                if (DEBUG) {
                    System.out.println("Parsing param: " + paramInfo.getName() +
                            " at index " + startIndex +
                            ", expects " + expectedArgs + " args");
                }

                if (startIndex + expectedArgs > args.length) {
                    if (DEBUG) {
                        System.out.println("No match: Not enough args for parameter " +
                                paramInfo.getName() + ". Need " + expectedArgs +
                                " but only have " + (args.length - startIndex));
                    }
                    return false;
                }

                Object value;
                try {
                    int beforeParse = context.getCurrentArgIndex();

                    value = paramInfo.parseParam(context);

                    int afterParse = context.getCurrentArgIndex();
                    if (afterParse != beforeParse + expectedArgs) {
                        if (DEBUG) {
                            System.out.println("WARNING: Arg index not advanced correctly! " +
                                    "Before: " + beforeParse + ", After: " + afterParse +
                                    ", Expected: " + (beforeParse + expectedArgs));
                            System.out.println("Manually advancing to expected position");
                        }
                        context.setArgIndex(beforeParse + expectedArgs);
                    }

                } catch (ParamParseException e) {
                    if (DEBUG) {
                        System.out.println("No match: Parse failed: " + e.getMessage());
                    }
                    return false;
                }

                if (value == null) {
                    if (DEBUG) {
                        System.out.println("No match: Param returned null");
                    }
                    return false;
                }

                context.setParam(paramInfo.getName(), value);

                if (DEBUG) {
                    System.out.println("Param parsed successfully, moved from index " +
                            startIndex + " to " + context.getCurrentArgIndex());
                }
            }

            boolean success = context.getCurrentArgIndex() == args.length;
            if (DEBUG) {
                if (success) {
                    System.out.println("Match successful: " + name);
                } else {
                    System.out.println("No match: Extra arguments, still at index " +
                            context.getCurrentArgIndex() + " of " + args.length);
                }
            }

            return success;

        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Exception during matching: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean execute(CommandContext context) {
        context.resetArgIndex();

        if (DEBUG) {
            System.out.println("\nExecuting path: " + name);
            System.out.println("Args: " + String.join(" ", context.getArgs()));
        }

        try {
            for (ParamInfo<?> paramInfo : params) {
                try {
                    int beforeParse = context.getCurrentArgIndex();
                    Object value = paramInfo.parseParam(context);
                    int afterParse = context.getCurrentArgIndex();

                    if (DEBUG) {
                        System.out.println("Parsed " + paramInfo.getName() + ", value: " + value +
                                ", index moved from " + beforeParse + " to " + afterParse);
                    }

                    context.setParam(paramInfo.getName(), value);
                } catch (ParamParseException e) {
                    context.sendMessage("&cError parsing " + paramInfo.getName() + ": " + e.getMessage());
                    return true;
                }
            }
        } catch (Exception e) {
            context.sendMessage("&cError preparing command: " + e.getMessage());
            return true;
        }

        for (CommandRequirement req : requirements) {
            if (!req.check(context)) {
                context.sendMessage(req.getFailMessage());
                return true;
            }
        }

        try {
            handler.handle(context);
        } catch (Exception e) {
            context.sendMessage("&cError executing command: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    public List<String> tabComplete(CommandContext context) {
        String[] args = context.getArgs();
        List<String> completions = new ArrayList<>();

        context.resetArgIndex();

        int targetIndex = args.length - 1;
        int paramIndex = 0;
        int argsProcessed = 0;

        try {
            while (paramIndex < params.size()) {
                ParamInfo<?> paramInfo = params.get(paramIndex);
                int expectedArgs = paramInfo.getParam().getExpectedArgCount();

                if (argsProcessed <= targetIndex && argsProcessed + expectedArgs > targetIndex) {
                    String current = args[targetIndex];

                    context.setArgIndex(argsProcessed);

                    return paramInfo.getParam().tabComplete(context, current);
                }

                if (argsProcessed + expectedArgs <= args.length - 1) {
                    try {
                        context.setArgIndex(argsProcessed);
                        Object value = paramInfo.parseParam(context);
                        context.setParam(paramInfo.getName(), value);

                        argsProcessed += expectedArgs;
                        context.setArgIndex(argsProcessed);
                    } catch (Exception e) {
                        break;
                    }
                } else {
                    break;
                }

                paramIndex++;
            }
        } catch (Exception e) {
            if (DEBUG) {
                LogController.error("Exception during tab completion: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return completions;
    }

    public String getUsage() {
        StringBuilder usage = new StringBuilder();
        if (name != null && !name.isEmpty()) {
            usage.append(name).append(" ");
        }

        for (ParamInfo<?> paramInfo : params) {
            String typeName = paramInfo.getParam().getTypeName();
            if (paramInfo.getParam().getExpectedArgCount() > 1) {
                usage.append("<").append(paramInfo.getName()).append(" (").append(typeName).append(")> ");
            } else {
                usage.append("<").append(paramInfo.getName()).append("> ");
            }
        }

        return usage.toString().trim();
    }

    public List<ParamInfo<?>> getParams() {
        return params;
    }

    public List<CommandRequirement> getRequirements() {
        return requirements;
    }

    public boolean isHidden() {
        return hidden;
    }
}