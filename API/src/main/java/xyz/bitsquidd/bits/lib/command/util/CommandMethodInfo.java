package xyz.bitsquidd.bits.lib.command.util;

import xyz.bitsquidd.bits.lib.command.annotation.Async;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.impl.PermissionRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores information about a method in a command
 */
public class CommandMethodInfo<T> {
    private final Method method;

    private final boolean isAsync;
    private final boolean requiresContext;

    private final Command commandAnnotation;
    private final List<CommandParameterInfo> classParameters;
    private final List<CommandParameterInfo> methodParameters;

    public CommandMethodInfo(Method method, List<CommandParameterInfo> classParameters) {
        this.method = method;
        this.commandAnnotation = method.getAnnotation(Command.class);

        this.isAsync = method.isAnnotationPresent(Async.class);
        this.requiresContext = method.getParameterCount() > 0 && BitsCommandContext.class.isAssignableFrom(method.getParameters()[0].getType());

        // If the first parameter is a BitsCommandContext, we filter it, technically means we cant "parse" any BitsCommandContext args, this shouldn't be an issue...
        this.methodParameters = new ArrayList<>(
          Arrays.stream(method.getParameters())
            .filter(param -> !BitsCommandContext.class.isAssignableFrom(param.getType()))
            .map(CommandParameterInfo::new)
            .toList()
        );

        this.classParameters = new ArrayList<>(classParameters);
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

    public List<CommandParameterInfo> getClassParameters() {
        return classParameters;
    }

    public List<CommandParameterInfo> getMethodParameters() {
        return methodParameters;
    }

    public List<CommandParameterInfo> getAllParameters() {
        List<CommandParameterInfo> allParams = new ArrayList<>(classParameters);
        allParams.addAll(methodParameters);
        return allParams;
    }

    public String literalName() {
        return commandAnnotation.value();
    }

    //TODO merge with BitsCommandBuilder permission gathering?
    public List<BitsCommandRequirement> getRequirements(xyz.bitsquidd.bits.lib.permission.Permission corePermission) {
        List<BitsCommandRequirement> requirements = new ArrayList<>();

        // Gather permission strings and convert them to requirements.
        Permission permissionAnnotation = method.getAnnotation(Permission.class);
        if (permissionAnnotation != null) {
            requirements.addAll(Arrays.stream(permissionAnnotation.value())
              .map(appended -> PermissionRequirement.of(xyz.bitsquidd.bits.lib.permission.Permission.of(corePermission + "." + appended)))
              .toList());
        }

        // Gather requirement instances
        Requirement requirementAnnotation = method.getAnnotation(Requirement.class);
        if (requirementAnnotation != null) {
            requirements.addAll(Arrays.stream(requirementAnnotation.value())
              .map(clazz -> BitsConfig.get().getCommandManager().getRequirementRegistry().getRequirement(clazz))
              .toList());
        }

        return requirements;
    }

}
