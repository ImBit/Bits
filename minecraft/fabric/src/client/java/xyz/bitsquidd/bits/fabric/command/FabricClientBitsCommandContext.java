/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.fabric.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.config.FabricClientBitsConfig;


public class FabricClientBitsCommandContext extends BitsCommandContext<FabricClientCommandSource> {
    public FabricClientBitsCommandContext(CommandContext<FabricClientCommandSource> brigadierContext) {
        super(brigadierContext);
    }

    @Override
    protected BitsCommandSourceContext<FabricClientCommandSource> createSourceContext(CommandContext<FabricClientCommandSource> brigadierContext) {
        return FabricClientBitsConfig.get().getCommandManager().createSourceContext(brigadierContext.getSource());
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandSourceStack getSender() {
        return source.getSender();
    }


}
