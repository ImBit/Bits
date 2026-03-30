/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.command;

import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;

import xyz.bitsquidd.bits.command.BitsCommandManager;
import xyz.bitsquidd.bits.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.config.VelocityBitsConfig;

public abstract class VelocityBitsCommandManager extends BitsCommandManager<CommandSource> {

    @Override
    protected VelocityBitsArgumentRegistry initialiseArgumentRegistry() {
        return new VelocityBitsArgumentRegistry();
    }

    @Override
    protected BitsRequirementRegistry<CommandSource> initialiseRequirementRegistry() {
        return new VelocityBitsRequirementRegistry();
    }

    @Override
    public VelocityBitsCommandContext createContext(CommandContext<CommandSource> brigadierContext) {
        return new VelocityBitsCommandContext(brigadierContext);
    }

    @Override
    public VelocityBitsCommandSourceContext createSourceContext(CommandSource sourceStack) {
        return new VelocityBitsCommandSourceContext(sourceStack);
    }

    @Override
    protected void enableAllCommands() {
        CommandManager velocityCommandManager = VelocityBitsConfig.get().getServer().getCommandManager();

        getAllCommands().forEach(this::registerCommand);
        getRegisteredCommands()
          .forEach(bitsCommand -> {
              brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                .forEach(node -> {
                    velocityCommandManager.register(
                      velocityCommandManager
                        .metaBuilder(node.getName())
                        .plugin(VelocityBitsConfig.get().getPlugin())
                        .build(),
                      new BrigadierCommand(node)
                    );
                });
              bitsCommand.onRegister();
          });
    }

}
