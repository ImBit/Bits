/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.fabric.command;

import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;

public class FabricBitsCommandSourceContext extends BitsCommandSourceContext<CommandSourceStack> {
    public FabricBitsCommandSourceContext(CommandSourceStack source) {
        super(source);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandSourceStack getSender() {
        return source;
    }

}
