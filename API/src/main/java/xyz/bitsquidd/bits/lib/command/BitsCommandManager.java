package xyz.bitsquidd.bits.lib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// TODO:
//  - Add ability for Restrictions.
//  - Allow for @Range annotations for Numbers.
//  - Allow for multi arg arguments - i.e. Location which defines the amount of variables it creates.

/**
 * Manages the registration and lifecycle of all {@link BitsCommand}s.
 */
public abstract class BitsCommandManager {
    protected final BitsArgumentRegistry argumentRegistry;
    protected final BitsRequirementRegistry requirementRegistry;
    protected final BrigadierTreeGenerator brigadierTreeGenerator;

    private final Set<BitsCommand> registeredCommands = new HashSet<>();
    private final String commandBasePermission = commandBasePermission();

    protected BitsCommandManager() {
        BitsConfig.get().setCommandManager(this);

        this.argumentRegistry = getArgumentRegistry();
        this.requirementRegistry = getRequirementRegistry();
        this.brigadierTreeGenerator = new BrigadierTreeGenerator();
    }

    protected abstract BitsArgumentRegistry getArgumentRegistry();

    protected abstract BitsRequirementRegistry getRequirementRegistry();

    /**
     * Gets all currently registered commands.
     */
    public final Set<BitsCommand> getRegisteredCommands() {
        return registeredCommands;
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
    protected String commandBasePermission() {
        return "bits.command"; // The base prefix for all commands, can be overridden.
    }


    /**
     * Main command executor handler. This must be implemented for commands to be executed.
     *
     * @param isAsync          Whether the command is to be executed asynchronously.
     * @param commandExecution The command execution runnable.
     */
    protected abstract void executeCommand(boolean isAsync, Runnable commandExecution);


    /**
     * Creates a new {@link BitsCommandContext} for the given {@link CommandContext}.
     * This  can be overridden to provide custom context implementations i.e. format a command response.
     */
    public BitsCommandContext createContext(CommandContext<CommandSourceStack> brigadierContext) {
        return new BitsCommandContext(brigadierContext);
    }

    public BitsCommandSourceContext createSourceContext(CommandSourceStack sourceStack) {
        return new BitsCommandSourceContext(sourceStack);
    }


    /**
     * Registers all {@link BitsCommand}s.
     */
    protected final void enableAllCommands() {
        CommandDispatcher<CommandSourceStack> dispatcher = MinecraftServer.getServer().getCommands().getDispatcher();

        registeredCommands.addAll(getAllCommands());

        registeredCommands
              .forEach(bitsCommand -> {
                  brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                        .forEach(node -> {
                            dispatcher.getRoot().removeCommand(node.getName());
                            dispatcher.getRoot().addChild(node);
                        });
                  bitsCommand.onRegister();
              });
    }

}
