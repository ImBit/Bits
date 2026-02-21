/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.lib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.paper.PaperBitsConfig;

public abstract class PaperBitsCommandManager extends BitsCommandManager<CommandSourceStack> {

    @Override
    protected PaperBitsArgumentRegistry initialiseArgumentRegistry() {
        return new PaperBitsArgumentRegistry();
    }

    @Override
    protected PaperBitsRequirementRegistry initialiseRequirementRegistry() {
        return new PaperBitsRequirementRegistry();
    }

    @Override
    public PaperBitsCommandContext createContext(CommandContext<CommandSourceStack> brigadierContext) {
        return new PaperBitsCommandContext(brigadierContext);
    }

    @Override
    public PaperBitsCommandSourceContext createSourceContext(CommandSourceStack sourceStack) {
        return new PaperBitsCommandSourceContext(sourceStack);
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

}
