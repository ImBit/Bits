package xyz.bitsquidd.bits.lib.command.newer;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.annotation.Command;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Default;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Permission;
import xyz.bitsquidd.bits.lib.command.newer.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentTypeRegistry;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


public class BrigadierTreeGenerator {
    private final ArgumentTypeRegistry argumentRegistry;
    private final Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> requirementInstances;

    public BrigadierTreeGenerator(@NotNull ArgumentTypeRegistry argumentRegistry) {
        this.argumentRegistry = argumentRegistry;
        this.requirementInstances = new HashMap<>();
    }

    public @NotNull List<LiteralCommandNode<CommandSourceStack>> generateTree(@NotNull Class<? extends BitsAnnotatedCommand> commandClass) {
        Command rootCommand = commandClass.getAnnotation(Command.class);
        if (rootCommand == null) throw new IllegalArgumentException("Command class must be annotated with @Command");

        List<LiteralCommandNode<CommandSourceStack>> nodes = new ArrayList<>();

        List<String> commandNames = new ArrayList<>();
        commandNames.add(rootCommand.value());
        commandNames.addAll(Arrays.asList(rootCommand.aliases()));

        for (String commandName : commandNames) {
            LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(commandName);

            addPermissionsAndRequirements(builder, commandClass, commandName);
            processCommandClass(builder, commandClass, new ArrayList<>());

            nodes.add(builder.build());
        }

        return nodes;
    }

    /**
     * Processes a command class and adds its methods and nested classes to the builder.
     */
    private void processCommandClass(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull Class<? extends BitsAnnotatedCommand> commandClass,
          @NotNull List<String> inheritedPermissions
    ) {
        List<String> currentPermissions = new ArrayList<>(inheritedPermissions);
        Permission permissionAnnotation = commandClass.getAnnotation(Permission.class);
        if (permissionAnnotation != null) currentPermissions.addAll(Arrays.asList(permissionAnnotation.value()));

        Method defaultMethod = findDefaultMethod(commandClass);
        if (defaultMethod != null) {
            CommandMethodInfo methodInfo = new CommandMethodInfo(defaultMethod, null);
            builder.executes(createCommandExecution(methodInfo, currentPermissions, new ArrayList<>()));
        } else {
            //todo use the default method here
        }

        for (Method method : commandClass.getDeclaredMethods()) {
            Command methodCommand = method.getAnnotation(Command.class);
            if (methodCommand != null && !method.isAnnotationPresent(Default.class)) {
                processCommandMethod(builder, method, currentPermissions, new ArrayList<>());
            }
        }

        for (Class<?> nestedClass : commandClass.getDeclaredClasses()) {
            if (BitsAnnotatedCommand.class.isAssignableFrom(nestedClass) && nestedClass.isAnnotationPresent(Command.class)) {
                processNestedCommandClass(builder, (Class<? extends BitsAnnotatedCommand>)nestedClass, currentPermissions, new ArrayList<>());
            }
        }
    }


    private void processCommandMethod(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull Method method,
          @NotNull List<String> inheritedPermissions,
          @NotNull List<Object> constructorArgs
    ) {
        CommandMethodInfo methodInfo = new CommandMethodInfo(method, null);
        String commandName = methodInfo.getCommandName();

        if (commandName.isEmpty()) {
            addArgumentsToBuilder(builder, methodInfo, inheritedPermissions, constructorArgs);
        } else {
            LiteralArgumentBuilder<CommandSourceStack> literalBuilder = Commands.literal(commandName);
            addPermissionsAndRequirements(literalBuilder, method);

            for (String alias : methodInfo.getAliases()) {
                LiteralArgumentBuilder<CommandSourceStack> aliasBuilder = Commands.literal(alias);
                addPermissionsAndRequirements(aliasBuilder, method);
                addArgumentsToBuilder(aliasBuilder, methodInfo, inheritedPermissions, constructorArgs);
                builder.then(aliasBuilder);
            }

            addArgumentsToBuilder(literalBuilder, methodInfo, inheritedPermissions, constructorArgs);
            builder.then(literalBuilder);
        }
    }


    private void processNestedCommandClass(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull Class<? extends BitsAnnotatedCommand> nestedClass,
          @NotNull List<String> inheritedPermissions,
          @NotNull List<Object> constructorArgs
    ) {
        Command nestedCommand = nestedClass.getAnnotation(Command.class);
        String commandName = nestedCommand.value();

        if (commandName.isEmpty()) {
            Constructor<?>[] constructors = nestedClass.getDeclaredConstructors();
            if (constructors.length > 0) {
                Constructor<?> constructor = constructors[0]; // Use first constructor
                addConstructorArgumentsToBuilder(builder, constructor, nestedClass, inheritedPermissions, constructorArgs);
            }
        } else {
            LiteralArgumentBuilder<CommandSourceStack> nestedBuilder = Commands.literal(commandName);
            addPermissionsAndRequirements(nestedBuilder, nestedClass);

            processCommandClass(nestedBuilder, nestedClass, inheritedPermissions);

            builder.then(nestedBuilder);
        }
    }

    private void addConstructorArgumentsToBuilder(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull Constructor<?> constructor,
          @NotNull Class<? extends BitsAnnotatedCommand> nestedClass,
          @NotNull List<String> inheritedPermissions,
          @NotNull List<Object> constructorArgs
    ) {
        Parameter[] parameters = constructor.getParameters();

        if (parameters.length == 0) {
            processCommandClass(builder, nestedClass, inheritedPermissions);
            return;
        }

        Parameter param = parameters[0];
        ArgumentType<?> argumentType = argumentRegistry.getArgumentType(param.getParameterizedType());

        if (argumentType == null) throw new IllegalArgumentException("No argument type registered for: " + param.getParameterizedType());

        RequiredArgumentBuilder<CommandSourceStack, ?> argBuilder = Commands.argument(param.getName(), argumentType);

        if (parameters.length == 1) {
            processCommandClass(argBuilder, nestedClass, inheritedPermissions);
        } else {
            // More parameters to process - recursively handle remaining parameters
            List<Object> newConstructorArgs = new ArrayList<>(constructorArgs);
            // TODO: Add logic to handle multiple constructor parameters
        }

        builder.then(argBuilder);
    }


    private void addArgumentsToBuilder(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull CommandMethodInfo methodInfo,
          @NotNull List<String> inheritedPermissions,
          @NotNull List<Object> constructorArgs
    ) {
        List<CommandMethodInfo.ParameterInfo> parameters = methodInfo.getParameters();

        if (parameters.isEmpty()) {
            builder.executes(createCommandExecution(methodInfo, inheritedPermissions, constructorArgs));
            return;
        }

        addParameterToBuilder(builder, methodInfo, parameters, 0, inheritedPermissions, constructorArgs);
    }


    private void addParameterToBuilder(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull CommandMethodInfo methodInfo,
          @NotNull List<CommandMethodInfo.ParameterInfo> parameters,
          int parameterIndex,
          @NotNull List<String> inheritedPermissions,
          @NotNull List<Object> constructorArgs
    ) {
        if (parameterIndex >= parameters.size()) {
            builder.executes(createCommandExecution(methodInfo, inheritedPermissions, constructorArgs));
            return;
        }

        CommandMethodInfo.ParameterInfo param = parameters.get(parameterIndex);
        ArgumentType<?> argumentType = argumentRegistry.getArgumentType(param.getType());

        if (argumentType == null) throw new IllegalArgumentException("No argument type registered for: " + param.getType());
        RequiredArgumentBuilder<CommandSourceStack, ?> argBuilder = Commands.argument(param.getName(), argumentType);
        if (param.isOptional()) argBuilder.executes(createCommandExecution(methodInfo, inheritedPermissions, constructorArgs));

        addParameterToBuilder(argBuilder, methodInfo, parameters, parameterIndex + 1, inheritedPermissions, constructorArgs);

        builder.then(argBuilder);
    }

    private com.mojang.brigadier.Command<CommandSourceStack> createCommandExecution(
          @NotNull CommandMethodInfo methodInfo,
          @NotNull List<String> inheritedPermissions,
          @NotNull List<Object> constructorArgs
    ) {

        return context -> {
            // TODO: Implement command execution logic
            // 1. Create BitsCommandContext
            // 2. Parse arguments from context
            // 3. Create instance if needed (with constructor args)
            // 4. Invoke method with parsed arguments
            // 5. Handle async execution if needed

            context.getSource().getSender().sendMessage("Command executed: " + methodInfo.getMethod().getName());
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        };
    }


    ///  UTILITY
    private @Nullable Method findDefaultMethod(@NotNull Class<? extends BitsAnnotatedCommand> commandClass) {
        for (Method method : commandClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Default.class)) return method;
        }
        return null;
    }

    private void addPermissionsAndRequirements(@NotNull LiteralArgumentBuilder<CommandSourceStack> builder, @NotNull Class<?> commandClass, String baseCommandName) {
        // Get all permissions required by this class
        Permission permissionAnnotation = commandClass.getAnnotation(Permission.class);
        List<String> permissions = new ArrayList<>(List.of(getDefaultPermissionString(baseCommandName)));
        if (permissionAnnotation != null) {
            permissions.addAll(Arrays.asList(permissionAnnotation.value()));

            builder.requires(ctx -> permissions.stream()
                  .allMatch(perm -> ctx.getSender().hasPermission(perm)));
        }

        // Get all requirements required by this class
        Requirement requirementAnnotation = commandClass.getAnnotation(Requirement.class);
        if (requirementAnnotation != null) {
            builder.requires(ctx -> Arrays.stream(requirementAnnotation.value())
                  .map(this::getRequirementInstance)
                  .allMatch(requirement -> requirement.test(new BitsCommandContext(ctx, new String[0], "")))
            );
        }
    }

    public @NotNull String getDefaultPermissionString(@NotNull String commandName) {
        return BitsConfig.COMMAND_BASE_STRING + "." + commandName.replaceAll(" ", "_").toLowerCase();
    }

    private void addPermissionsAndRequirements(
          @NotNull LiteralArgumentBuilder<CommandSourceStack> builder,
          @NotNull Method method
    ) {
        // TODO
        // Similar to class version but for methods
        // Implementation similar to above but for method annotations
    }

    private @NotNull BitsCommandRequirement getRequirementInstance(@NotNull Class<? extends BitsCommandRequirement> requirementClass) {
        return requirementInstances.computeIfAbsent(
              requirementClass, clazz -> {
                  try {
                      return clazz.getDeclaredConstructor().newInstance();
                  } catch (Exception e) {
                      throw new RuntimeException("Failed to create requirement instance: " + clazz.getName(), e);
                  }
              }
        );
    }
}