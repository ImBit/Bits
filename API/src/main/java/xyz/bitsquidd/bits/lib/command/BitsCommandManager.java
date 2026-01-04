package xyz.bitsquidd.bits.lib.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// TODO:
//  - Allow for @Range annotations for Numbers.

/**
 * Manages the registration and lifecycle of all {@link BitsCommand}s.
 */
public abstract class BitsCommandManager<T> {
    protected final BitsArgumentRegistry<T> argumentRegistry;
    protected final BitsRequirementRegistry<T> requirementRegistry;
    protected final BrigadierTreeGenerator<T> brigadierTreeGenerator;

    private final Set<BitsCommand> registeredCommands = new HashSet<>();
    private final Permission commandBasePermission = initialiseBasePermission();

    protected BitsCommandManager() {
        BitsConfig.get().setCommandManager(this);

        this.argumentRegistry = initialiseArgumentRegistry();
        this.requirementRegistry = initialiseRequirementRegistry();
        this.brigadierTreeGenerator = new BrigadierTreeGenerator<>(this);
    }

    protected abstract BitsArgumentRegistry<T> initialiseArgumentRegistry();

    public final BitsArgumentRegistry<T> getArgumentRegistry() {
        return argumentRegistry;
    }

    protected abstract BitsRequirementRegistry<T> initialiseRequirementRegistry();

    public final BitsRequirementRegistry<T> getRequirementRegistry() {
        return requirementRegistry;
    }

    /**
     * Creates a new {@link BitsCommandContext} for the given {@link CommandContext}.
     * This  can be overridden to provide custom context implementations i.e. format a command response.
     */
    public abstract BitsCommandContext<T> createContext(CommandContext<T> brigadierContext);

    public abstract BitsCommandSourceContext<T> createSourceContext(T sourceStack);

    /**
     * Gets all currently registered commands.
     */
    public final Set<BitsCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    protected final void registerCommand(BitsCommand command) {
        registeredCommands.add(command);
    }


    /**
     * Registers the command listener and enables all commands.
     * <p>
     * Ensure this method is run on onEnable() oe.
     */
    public void startup() {
        enableAllCommands();
    }

    /**
     * Unregisters the command listener.
     * <p>
     * Ensure this method is run on onDisable() oe.
     */
    public void shutdown() {

    }


    /**
     * Provides all {@link BitsCommand}s to be registered on startup of the manager.
     *
     * @return A list of all {@link BitsCommand}s to be registered.
     */
    protected abstract Collection<BitsCommand> getAllCommands();

    /**
     * Defines the base permission string for all commands.
     * <p>
     * For example, if returning the string: {@code bits.command}, all commands will have
     * permission {@code bits.command.[command_name]}.
     *
     * @return The base permission string for all commands.
     */
    protected Permission initialiseBasePermission() {
        return Permission.of("bits.command"); // The base prefix for all commands, can be overridden.
    }

    public final Permission getCommandBasePermission() {
        return commandBasePermission;
    }


    /**
     * Main command executor handler. This must be implemented for commands to be executed.
     *
     * @param isAsync          Whether the command is to be executed asynchronously.
     * @param commandExecution The command execution runnable.
     */
    protected abstract void executeCommand(boolean isAsync, Runnable commandExecution);


    public LiteralArgumentBuilder<T> createLiteral(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public <W> RequiredArgumentBuilder<T, W> createArgument(String name, ArgumentType<W> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }


    /**
     * Registers all {@link BitsCommand}s.
     */
    protected abstract void enableAllCommands();

}
