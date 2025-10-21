package xyz.bitsquidd.bits.lib.command.util.newe;

import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandParameterInfo;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Stores information about a method in a command
 */
public class CommandMethodInfo {
    private final Method method;

    private final Command commandAnnotation;
    private final List<BitsCommandParameterInfo> classParameters;
    private final List<BitsCommandParameterInfo> methodParameters;

}
