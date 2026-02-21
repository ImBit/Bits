/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.requirement;

import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.sendable.text.Text;

/**
 * A requirement that is checked before a command is executed, ensuring the sender has the specified permissions.
 */
public abstract class BitsCommandRequirement {
    public abstract boolean test(BitsCommandSourceContext<?> ctx);

    public @Nullable Text getFailureMessage(BitsCommandSourceContext<?> ctx) {
        return null;
    }

}