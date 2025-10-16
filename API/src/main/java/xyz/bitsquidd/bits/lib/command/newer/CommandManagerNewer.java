package xyz.bitsquidd.bits.lib.command.newer;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.SpigotConfig;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.newe.BitsCommandListener;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentTypeRegistry;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.HashMap;
import java.util.List;

public abstract class CommandManagerNewer {
    protected final @NotNull JavaPlugin plugin = BitsConfig.getPlugin();
    protected final @NotNull BitsCommandListener listener;

    private final ArgumentTypeRegistry argumentTypeRegistry = new ArgumentTypeRegistry();
    private final BrigadierTreeGenerator brigadierTreeGenerator = new BrigadierTreeGenerator(argumentTypeRegistry, new HashMap<>());


    protected CommandManagerNewer() {
        this.listener = getListenerInternal();

        BitsConfig.COMMAND_BASE_STRING = commandBasePermission();
    }

    /**
     * Registers the command listener and enables all commands.
     * <p>
     * Ensure this method is run on {@link JavaPlugin#onEnable()} or equivalent.
     */
    public void startup() {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        enableAllCommands();
    }

    /**
     * Unregisters the command listener.
     */
    public void shutdown() {
        HandlerList.unregisterAll(listener);
    }


    /**
     * Provides all {@link BitsAnnotatedCommand}s to be registered on startup of the manager.
     *
     * @return A list of all {@link BitsAnnotatedCommand}s to be registered.
     */
    protected abstract @NotNull List<BitsAnnotatedCommand> getAllCommands();

    /**
     * Defines the base permission string for all commands.
     * <p>
     * For example: {@code bitsplugin.command},
     *
     * @return The base permission string for all commands.
     */
    protected abstract @NotNull String commandBasePermission();


    /**
     * Gets a command listener, used for certain command functionality.
     * The default implementation returns a new instance of {@link BitsCommandListener}.
     * <p>
     * This default implementation provides basic formatting for unknown/error commands.
     */
    protected @NotNull BitsCommandListener getListenerInternal() {
        return new BitsCommandListener(
              CommandReturnDecorator.of(CommandReturnType.ERROR),
              Component.text(SpigotConfig.unknownCommandMessage)
        );
    }


    /**
     * Registers all {@link BitsAnnotatedCommand}s.
     */
    protected void enableAllCommands() {
        List<BitsAnnotatedCommand> bitsCommands = getAllCommands();

        plugin.getLifecycleManager().registerEventHandler(
              LifecycleEvents.COMMANDS, commands -> {
                  bitsCommands.forEach(bitsCommand -> {
                      commands.registrar().register(brigadierTreeGenerator.createNode(bitsCommand));
                  });
              }
        );
    }

}
