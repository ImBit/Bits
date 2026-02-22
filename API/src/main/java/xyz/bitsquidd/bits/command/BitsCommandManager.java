/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import xyz.bitsquidd.bits.BitsConfig;
import xyz.bitsquidd.bits.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lifecycle.manager.CoreManager;
import xyz.bitsquidd.bits.permission.Permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// TODO:
//  - Allow for @Range annotations for Numbers.

/**
 * Manages the registration and lifecycle of all {@link BitsCommand}s.
 */
public abstract class BitsCommandManager<T> implements CoreManager {
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

    /**
     * Registers the command listener and enables all commands.
     * <p>
     * Ensure this method is run on onEnable() oe.
     */
    @Override
    public void startup() {
        enableAllCommands();
    }


    //region Registries
    protected abstract BitsArgumentRegistry<T> initialiseArgumentRegistry();

    public final BitsArgumentRegistry<T> getArgumentRegistry() {
        return argumentRegistry;
    }

    protected abstract BitsRequirementRegistry<T> initialiseRequirementRegistry();

    public final BitsRequirementRegistry<T> getRequirementRegistry() {
        return requirementRegistry;
    }
    //endregion


    //region Context

    /**
     * Creates a new {@link BitsCommandContext} for the given {@link CommandContext}.
     * This can be overridden to provide custom context implementations i.e. format a command response.
     */
    public abstract BitsCommandContext<T> createContext(CommandContext<T> brigadierContext);

    public abstract BitsCommandSourceContext<T> createSourceContext(T sourceStack);
    //endregion


    //region Commands

    /**
     * Gets all currently registered commands.
     */
    public final Set<BitsCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    /**
     * Registers a command to be enabled on startup.
     */
    protected final void registerCommand(BitsCommand command) {
        registeredCommands.add(command);
    }
    //endregion


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
