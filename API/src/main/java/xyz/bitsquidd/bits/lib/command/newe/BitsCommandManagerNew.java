package xyz.bitsquidd.bits.lib.command.newe;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BitsCommandManagerNew {
    private final @NotNull JavaPlugin plugin;

    protected BitsCommandManagerNew(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    abstract @NotNull List<BitsCommandNew> getAllCommands();

    /**
     * Registers all {@link BitsCommandNew}s.
     * <p>
     * Ensure this method is run on {@link JavaPlugin#onEnable()}.
     */
    public void enableAllCommands() {
        List<BitsCommandNew> bitCommands = getAllCommands();

        plugin.getLifecycleManager().registerEventHandler(
              LifecycleEvents.COMMANDS, commands -> {
                  bitCommands.forEach(bitCommand -> {
                      buildCommand(bitCommand).forEach(builtCommand -> {
                          commands.registrar().register(builtCommand);
                      });
                  });
              }
        );
    }

    protected @NotNull List<LiteralCommandNode<CommandSourceStack>> buildCommand(@NotNull BitsCommandNew command) {
        BitsCommandAnnotation annotation = command.getClass().getAnnotation(BitsCommandAnnotation.class);
        if (annotation == null) throw new IllegalStateException("Command class " + command.getClass().getName() + " is not annotated with @BitsCommandAnnotation");

        return command.build(annotation);
    }


}
