package xyz.bitsquidd.bits.lib.command.util;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Async;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores information about a method in a command
 */
@NullMarked
public class CommandMethodInfo {
    private final Method method;

    private final boolean isAsync;
    private final boolean requiresContext;

    private final List<String> permissions;
    private final List<Class<? extends BitsCommandRequirement>> requirements;

    private final Command commandAnnotation;
    private final List<CommandParameterInfo> classParameters;
    private final List<CommandParameterInfo> methodParameters;

    public CommandMethodInfo(Method method, List<CommandParameterInfo> classParameters) {
        this.method = method;
        this.commandAnnotation = method.getAnnotation(Command.class);

        this.isAsync = method.isAnnotationPresent(Async.class);
        this.requiresContext = method.getParameterCount() > 0 && method.getParameters()[0].getType().equals(BitsCommandContext.class);

        Permission permissionAnnotation = method.getAnnotation(Permission.class);
        this.permissions = permissionAnnotation != null ? Arrays.asList(permissionAnnotation.value()) : new ArrayList<>();

        Requirement requirementAnnotation = method.getAnnotation(Requirement.class);
        this.requirements = requirementAnnotation != null ? Arrays.asList(requirementAnnotation.value()) : new ArrayList<>();

        // If the first parameter is a BitsCommandContext, skip it, we don't need to parse this.

        this.methodParameters = parseParameters(Arrays.stream(method.getParameters()).map(CommandParameterInfo::new).toList(), this.requiresContext);
        this.classParameters = parseParameters(classParameters, false);
    }

    private List<CommandParameterInfo> parseParameters(List<CommandParameterInfo> parameters, boolean paramsRequireContext) {
        List<CommandParameterInfo> params = new ArrayList<>();

        for (int i = (paramsRequireContext ? 1 : 0); i < parameters.size(); i++) {
            CommandParameterInfo param = parameters.get(i);
            params.add(param);
        }

        return params;
    }


    public Method getMethod() {
        return method;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public boolean requiresContext() {
        return requiresContext;
    }


    public List<CommandParameterInfo> getMethodParameters() {
        return methodParameters;
    }

    public List<CommandParameterInfo> getAllParameters() {
        List<CommandParameterInfo> allParams = new ArrayList<>(classParameters);
        allParams.addAll(methodParameters);
        return allParams;
    }

}
