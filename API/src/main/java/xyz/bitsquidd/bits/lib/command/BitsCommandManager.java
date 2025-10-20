package xyz.bitsquidd.bits.lib.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.spigotmc.SpigotConfig;

import xyz.bitsquidd.bits.lib.command.argument.ArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.RequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.Collection;

// TODO:
//  - Add ability for Restrictions.

/**
 * Manages the registration and lifecycle of all {@link BitsCommand}s.
 */
@NullMarked
public abstract class BitsCommandManager {
    protected final JavaPlugin plugin = BitsConfig.getPlugin();
    protected final BitsCommandListener listener;

    private final ArgumentRegistry argumentRegistry;
    private final RequirementRegistry requirementRegistry;
    private final BrigadierTreeGenerator brigadierTreeGenerator;


    protected BitsCommandManager() {
        BitsConfig.setCommandManager(this);

        this.listener = getListenerInternal();

        BitsConfig.COMMAND_BASE_STRING = commandBasePermission();

        this.argumentRegistry = new ArgumentRegistry();
        this.requirementRegistry = new RequirementRegistry();
        this.brigadierTreeGenerator = new BrigadierTreeGenerator();
    }

    /**
     * Registers the command listener and enables all commands.
     * <p>
     * Ensure this method is run on {@link JavaPlugin#onEnable()} oe.
     */
    public void startup() {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        enableAllCommands();
    }

    /**
     * Unregisters the command listener.
     * <p>
     * Ensure this method is run on {@link JavaPlugin#onDisable()} oe.
     */
    public void shutdown() {
        HandlerList.unregisterAll(listener);
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
    protected BitsCommandListener getListenerInternal() {
        return new BitsCommandListener(
              CommandReturnDecorator.of(CommandReturnType.ERROR),
              Component.text(SpigotConfig.unknownCommandMessage)
        );
    }

    /**
     * Creates a new {@link BitsCommandContext} for the given {@link CommandSourceStack}.
     * This can be overridden to provide custom context implementations i.e. format a command response.
     */
    public BitsCommandContext createContext(CommandSourceStack sourceStack) {
        return new BitsCommandContext(sourceStack);
    }


    /**
     * Registers all {@link BitsCommand}s.
     */
    private void enableAllCommands() {
        Collection<BitsCommand> bitsCommands = getAllCommands();

        plugin.getLifecycleManager().registerEventHandler(
              LifecycleEvents.COMMANDS, commands -> {
                  bitsCommands
                        .forEach(bitsCommand -> {
                            brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand.getClass()))
                                  .forEach(node -> commands.registrar().register(node));
                        });
              }
        );
    }

}
