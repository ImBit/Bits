package xyz.bitsquidd.bits.lib.command.newe;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO:
//  Personalised Show usage
//  Automatic permission registration? - have a string for a base, then use the name of each command to auto permission?

public abstract class BitsCommandManager {
    private final @NotNull JavaPlugin plugin;

    protected BitsCommandManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

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
     * Registers all {@link BitsCommand}s.
     * <p>
     * Ensure this method is run on {@link JavaPlugin#onEnable()}.
     */
    public void enableAllCommands() {
        List<BitsCommand> bitCommands = getAllCommands();

        plugin.getLifecycleManager().registerEventHandler(
              LifecycleEvents.COMMANDS, commands -> {
                  bitCommands.forEach(bitCommand -> {
                      buildCommands(bitCommand).forEach(builtCommand -> {
                          commands.registrar().register(builtCommand);
                      });
                  });
              }
        );
    }

    protected @NotNull List<LiteralCommandNode<CommandSourceStack>> buildCommands(@NotNull BitsCommand command) {
        BitsCommandAnnotation annotation = command.getClass().getAnnotation(BitsCommandAnnotation.class);
        if (annotation == null) throw new IllegalStateException("Command class " + command.getClass().getName() + " is not annotated with @BitsCommandAnnotation");

        return command.build(annotation, List.of(commandBasePermission() + "." + annotation.name().replaceAll(" ", "_").toLowerCase()));
    }


}
