/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.fabric.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.config.FabricBitsConfig;


public class FabricBitsCommandContext extends BitsCommandContext<CommandSourceStack> {
    public FabricBitsCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        super(brigadierContext);
    }

    @Override
    protected BitsCommandSourceContext<CommandSourceStack> createSourceContext(CommandContext<CommandSourceStack> brigadierContext) {
        return FabricBitsConfig.get().getCommandManager().createSourceContext(brigadierContext.getSource());
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandSourceStack getSender() {
        return source.getSender();
    }


}
