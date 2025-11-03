package xyz.bitsquidd.bits.lib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.Collection;

// TODO:
//  - Add ability for Restrictions.
//  - Allow for @Range annotations for Numbers.
//  - Allow for multi arg arguments - i.e. Location which defines the amount of variables it creates.

/**
 * Manages the registration and lifecycle of all {@link BitsCommand}s.
 */
@NullMarked
public abstract class BitsCommandManager {
    protected final BitsCommandListener listener;

    protected final BitsArgumentRegistry argumentRegistry;
    protected final BitsRequirementRegistry requirementRegistry;
    protected final BrigadierTreeGenerator brigadierTreeGenerator;


    protected BitsCommandManager() {
        BitsConfig.setCommandManager(this);

        this.listener = getListenerInternal();

        BitsConfig.COMMAND_BASE_STRING = commandBasePermission();

        this.argumentRegistry = getArgumentRegistry();
        this.requirementRegistry = getRequirementRegistry();
        this.brigadierTreeGenerator = new BrigadierTreeGenerator();
    }

    protected abstract BitsArgumentRegistry getArgumentRegistry();

    protected abstract BitsRequirementRegistry getRequirementRegistry();


    /**
     * Registers the command listener and enables all commands.
     * <p>
     * Ensure this method is run on onEnable() oe.
     */
    public void startup() {
        // A Bukkit plugin would require the following.
        //   Bukkit.getPluginManager().registerEvents(listener, plugin);
        enableAllCommands();
    }

    /**
     * Unregisters the command listener.
     * <p>
     * Ensure this method is run on onDisable() oe.
     */
    public void shutdown() {
        // A Bukkit plugin would require the following.
        //   HandlerList.unregisterAll(listener);
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
    protected abstract String commandBasePermission();


    /**
     * Returns the default command listener.
     * We hook into this to provide custom formatting for unknown/error commands.
     * The default implementation returns a new instance of {@link BitsCommandListener}.
     */
    protected abstract BitsCommandListener getListenerInternal();


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

        getAllCommands()
              .forEach(bitsCommand -> {
                  brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                        .forEach(node -> dispatcher.getRoot().addChild(node));
                  bitsCommand.onRegister();
              });
    }

}
