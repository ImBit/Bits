package xyz.bitsquidd.bits.lib.command.parameters;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exception.ParamParseException;

public class ParamInfo<T> {
    private final CommandParam<T> param;
    private final String name;
    private static final boolean DEBUG = true;

    public ParamInfo(CommandParam<T> param, String name) {
        this.param = param;
        this.name = name;
    }

    public CommandParam<T> getParam() {
        return param;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public T parseParam(CommandContext context) throws ParamParseException {
        if (DEBUG) {
            System.out.println("Starting to parse param: " + name +
                    " at index " + context.getCurrentArgIndex());
        }

        String arg = context.getCurrentArg();

        if (param.getExpectedArgCount() == 1) {
            T result = param.parse(context, arg);
            if (DEBUG) {
                System.out.println("Parsed single arg param: " + name +
                        ", result: " + result);
            }
            context.advanceArgIndex(1);
            return result;
        }
        else {
            if (DEBUG) {
                System.out.println("Parsing multi-arg param: " + name +
                        ", expected args: " + param.getExpectedArgCount());
            }
            return param.parse(context, arg);
        }
    }

    public int getExpectedArgCount() {
        return param.getExpectedArgCount();
    }
}