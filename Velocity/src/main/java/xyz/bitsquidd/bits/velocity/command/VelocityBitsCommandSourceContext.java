/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.velocity.command;

import com.velocitypowered.api.command.CommandSource;

import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;

public class VelocityBitsCommandSourceContext extends BitsCommandSourceContext<CommandSource> {
    public VelocityBitsCommandSourceContext(CommandSource source) {
        super(source);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandSource getSender() {
        return source;
    }

}
