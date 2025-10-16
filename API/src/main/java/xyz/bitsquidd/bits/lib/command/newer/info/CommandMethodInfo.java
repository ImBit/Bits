package xyz.bitsquidd.bits.lib.command.newer.info;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.annotation.*;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Utility class to encapsulate information about command methods
 */
public class CommandMethodInfo {
    private final Method method;
    private final Object instance;

    private final Command commandAnnotation;
    private final List<ParameterInfo> parameters;

    private final boolean isAsync;

    private final List<String> permissions;
    private final List<Class<? extends BitsCommandRequirement>> requirements;

    public CommandMethodInfo(@NotNull Method method, @Nullable Object instance) {
        this.method = method;
        this.instance = instance;
        this.commandAnnotation = method.getAnnotation(Command.class);
        this.isAsync = method.isAnnotationPresent(Async.class);

        Permission permissionAnnotation = method.getAnnotation(Permission.class);
        this.permissions = permissionAnnotation != null ? Arrays.asList(permissionAnnotation.value()) : new ArrayList<>();

        Requirement requirementAnnotation = method.getAnnotation(Requirement.class);
        this.requirements = requirementAnnotation != null ? Arrays.asList(requirementAnnotation.value()) : new ArrayList<>();

        this.parameters = parseParameters(method);
    }

    private List<ParameterInfo> parseParameters(Method method) {
        List<ParameterInfo> params = new ArrayList<>();
        Parameter[] methodParams = method.getParameters();

        int startIndex = 0;
        // If the first parameter is a BitsCommandContext, skip it, we dont need to parse this.
        if (methodParams.length > 0 && methodParams[0].getType().equals(BitsCommandContext.class)) startIndex = 1;

        for (int i = startIndex; i < methodParams.length; i++) {
            Parameter param = methodParams[i];
            boolean isOptional = param.isAnnotationPresent(Optional.class);
            params.add(new ParameterInfo(param.getParameterizedType(), param.getName(), isOptional));
        }

        return params;
    }

    // Getters
    public @NotNull Method getMethod() {
        return method;
    }

    public @Nullable Object getInstance() {
        return instance;
    }

    public @Nullable Command getCommandAnnotation() {
        return commandAnnotation;
    }

    public @NotNull List<ParameterInfo> getParameters() {
        return parameters;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public @NotNull List<String> getPermissions() {
        return permissions;
    }

    public @NotNull List<Class<? extends BitsCommandRequirement>> getRequirements() {
        return requirements;
    }

    public @NotNull String getCommandName() {
        return commandAnnotation != null ? commandAnnotation.value() : "";
    }

    public @NotNull String[] getAliases() {
        return commandAnnotation != null ? commandAnnotation.aliases() : new String[0];
    }

}