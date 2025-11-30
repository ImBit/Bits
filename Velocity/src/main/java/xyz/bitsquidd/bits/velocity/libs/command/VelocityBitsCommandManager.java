package xyz.bitsquidd.bits.velocity.libs.command;

import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;
import xyz.bitsquidd.bits.velocity.VelocityBitsConfig;

public abstract class VelocityBitsCommandManager extends BitsCommandManager<CommandSource> {

    @Override
    protected void executeCommand(boolean isAsync, Runnable commandExecution) {
        // Note all Velocity tasks are async by default, so we don't need to differentiate
        ((VelocityBitsConfig)BitsConfig.get())
              .getServer()
              .getScheduler()
              .buildTask(((VelocityBitsConfig)BitsConfig.get()).getPlugin(), commandExecution)
              .schedule();
    }

    @Override
    protected void enableAllCommands() {
        CommandManager velocityCommandManager = ((VelocityBitsConfig)BitsConfig.get()).getServer().getCommandManager();

        getAllCommands().forEach(this::registerCommand);
        getRegisteredCommands()
              .forEach(bitsCommand -> {
                  brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                        .forEach(node -> {
                            velocityCommandManager.register(
                                  velocityCommandManager
                                        .metaBuilder(node.getName())
                                        .plugin(((VelocityBitsConfig)BitsConfig.get()).getPlugin())
                                        .build(),
                                  new BrigadierCommand(node)
                            );
                        });
                  bitsCommand.onRegister();
              });
    }

    @Override
    public BitsCommandContext<CommandSource> createContext(CommandContext<CommandSource> brigadierContext) {
        return new VelocityBitsCommandContext(brigadierContext);
    }

    @Override
    public BitsCommandSourceContext<CommandSource> createSourceContext(CommandSource sourceStack) {
        return new VelocityBitsCommandSourceContext(sourceStack);
    }
    
}
