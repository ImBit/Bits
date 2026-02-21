/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.command;

import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.BitsConfig;

public class VelocityBitsCommandContext extends BitsCommandContext<CommandSource> {
    public VelocityBitsCommandContext(CommandContext<CommandSource> brigadierContext) {
        super(brigadierContext);
    }

    @Override
    protected BitsCommandSourceContext<CommandSource> createSourceContext(CommandContext<CommandSource> brigadierContext) {
        return ((VelocityBitsCommandManager)BitsConfig.get().getCommandManager()).createSourceContext(brigadierContext.getSource());
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandSource getSender() {
        return source.getSender();
    }


}
