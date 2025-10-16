package xyz.bitsquidd.bits.lib.command.newer.info;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.annotation.Async;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Command;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Utility class to encapsulate information about command methods
 */
public class BitsCommandMethodInfo {
    private final Method method;

    private final Command commandAnnotation;
    private final List<BitsCommandParameterInfo> parameters;

    private final boolean isAsync;
    private final boolean requiresContext;

    private final List<String> permissions;
    private final List<Class<? extends BitsCommandRequirement>> requirements;

    public BitsCommandMethodInfo(@NotNull Method method) {
        this.method = method;
        this.commandAnnotation = method.getAnnotation(Command.class);
        this.isAsync = method.isAnnotationPresent(Async.class);

        Permission permissionAnnotation = method.getAnnotation(Permission.class);
        this.permissions = permissionAnnotation != null ? Arrays.asList(permissionAnnotation.value()) : new ArrayList<>();

        Requirement requirementAnnotation = method.getAnnotation(Requirement.class);
        this.requirements = requirementAnnotation != null ? Arrays.asList(requirementAnnotation.value()) : new ArrayList<>();

        // If the first parameter is a BitsCommandContext, skip it, we don't need to parse this.
        this.requiresContext = method.getParameterCount() > 0 && method.getParameters()[0].getType().equals(BitsCommandContext.class);

        this.parameters = parseParameters(method);
    }

    private List<BitsCommandParameterInfo> parseParameters(Method method) {
        List<BitsCommandParameterInfo> params = new ArrayList<>();
        Parameter[] methodParams = method.getParameters();

        for (int i = (requiresContext ? 1 : 0); i < methodParams.length; i++) {
            Parameter param = methodParams[i];
            params.add(new BitsCommandParameterInfo(param));
        }

        return params;
    }

    // Getters
    public @NotNull Method getMethod() {
        return method;
    }

    public @NotNull List<BitsCommandParameterInfo> getParameters() {
        return parameters;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public boolean requiresContext() {
        return requiresContext;
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