package xyz.bitsquidd.bits.lib.command.newe;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.SpigotConfig;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.List;

// TODO:
//  Personalised Show usage
//  Automatic permission registration? - have a string for a base, then use the name of each command to auto permission?

public abstract class BitsCommandManager {
    protected final @NotNull JavaPlugin plugin;
    protected final @NotNull BitsCommandListener listener;

    protected BitsCommandManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.listener = getListenerInternal();

        BitsConfig.COMMAND_BASE_STRING = commandBasePermission();
    }

    /**
     * Provides all {@link BitsCommand}s to be registered on startup of the manager.
     *
     * @return A list of all {@link BitsCommand}s to be registered.
     */
    protected abstract @NotNull List<BitsCommand> getAllCommands();

    /**
     * Defines the base permission string for all commands.
     * <p>
     * For example: {@code bitsplugin.command},
     *
     * @return The base permission string for all commands.
     */
    protected abstract @NotNull String commandBasePermission();

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
     * Override this method if you want to use a custom listener.
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
     * Registers all {@link BitsCommand}s.
     */
    protected void enableAllCommands() {
        List<BitsCommand> bitsCommands = getAllCommands();

        plugin.getLifecycleManager().registerEventHandler(
              LifecycleEvents.COMMANDS, commands -> {
                  bitsCommands.forEach(bitsCommand -> {
                      bitsCommand.build().forEach(builtCommand -> {
                          commands.registrar().register(builtCommand);
                      });
                  });
              }
        );
    }

}
