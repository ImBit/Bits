package xyz.bitsquidd.bits.lib.command.info;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Async;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Utility class to encapsulate information about command methods
 */
@NullMarked
public class BitsCommandMethodInfo {
    private final Method method;

    private final Command commandAnnotation;
    private final List<BitsCommandParameterInfo> parameters;

    private final boolean isAsync;
    private final boolean requiresContext;

    private final List<String> permissions;
    private final List<Class<? extends BitsCommandRequirement>> requirements;

    public BitsCommandMethodInfo(Method method, List<Parameter> addedParameters) {
        this.method = method;
        this.commandAnnotation = method.getAnnotation(Command.class);
        this.isAsync = method.isAnnotationPresent(Async.class);

        Permission permissionAnnotation = method.getAnnotation(Permission.class);
        this.permissions = permissionAnnotation != null ? Arrays.asList(permissionAnnotation.value()) : new ArrayList<>();

        Requirement requirementAnnotation = method.getAnnotation(Requirement.class);
        this.requirements = requirementAnnotation != null ? Arrays.asList(requirementAnnotation.value()) : new ArrayList<>();

        // If the first parameter is a BitsCommandContext, skip it, we don't need to parse this.
        this.requiresContext = method.getParameterCount() > 0 && method.getParameters()[0].getType().equals(BitsCommandContext.class);

        this.parameters = parseParameters(method, addedParameters);
    }

    private List<BitsCommandParameterInfo> parseParameters(Method method, List<Parameter> addedParameters) {
        List<BitsCommandParameterInfo> params = new ArrayList<>();
        List<Parameter> methodParams = new ArrayList<>(Arrays.stream(method.getParameters()).toList());
        methodParams.addAll(addedParameters);

        for (int i = (requiresContext ? 1 : 0); i < methodParams.size(); i++) {
            Parameter param = methodParams.get(i);
            params.add(new BitsCommandParameterInfo(param));
        }

        return params;
    }

    // Getters
    public Method getMethod() {
        return method;
    }

    public List<BitsCommandParameterInfo> getParameters() {
        return parameters;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public boolean requiresContext() {
        return requiresContext;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<Class<? extends BitsCommandRequirement>> getRequirements() {
        return requirements;
    }

    public String getCommandName() {
        return commandAnnotation.value();
    }

    public String[] getAliases() {
        return commandAnnotation.aliases();
    }

}