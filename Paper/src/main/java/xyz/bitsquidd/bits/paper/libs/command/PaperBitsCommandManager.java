package xyz.bitsquidd.bits.paper.libs.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.paper.PaperBitsConfig;

public abstract class PaperBitsCommandManager extends BitsCommandManager<CommandSourceStack> {

    @Override
    public BitsArgumentRegistry<CommandSourceStack> initialiseArgumentRegistry() {
        return new PaperBitsArgumentRegistry();
    }

    @Override
    protected BitsRequirementRegistry<CommandSourceStack> initialiseRequirementRegistry() {
        return new PaperBitsRequirementRegistry();
    }

    @Override
    protected void executeCommand(boolean isAsync, Runnable commandExecution) {
        if (isAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(((PaperBitsConfig)PaperBitsConfig.get()).getPlugin(), commandExecution);
        } else {
            Bukkit.getScheduler().runTask(((PaperBitsConfig)PaperBitsConfig.get()).getPlugin(), commandExecution);
        }
    }

    @Override
    protected void enableAllCommands() {
        CommandDispatcher<CommandSourceStack> dispatcher = MinecraftServer.getServer().getCommands().getDispatcher();

        getAllCommands().forEach(this::registerCommand);
        getRegisteredCommands()
              .forEach(bitsCommand -> {
                  brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                        .forEach(node -> {
                            dispatcher.getRoot().removeCommand(node.getName());
                            dispatcher.getRoot().addChild(node);
                        });
                  bitsCommand.onRegister();
              });
    }

    @Override
    public BitsCommandContext<CommandSourceStack> createContext(CommandContext<CommandSourceStack> brigadierContext) {
        return new PaperBitsCommandContext(brigadierContext);
    }

    @Override
    public BitsCommandSourceContext<CommandSourceStack> createSourceContext(CommandSourceStack sourceStack) {
        return new PaperBitsCommandSourceContext(sourceStack);
    }

}
