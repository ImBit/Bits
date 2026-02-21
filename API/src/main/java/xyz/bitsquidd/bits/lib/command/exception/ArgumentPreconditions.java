/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.Nullable;

public final class ArgumentPreconditions {
    private ArgumentPreconditions() {}

    public static void checkNotEmpty(@Nullable String string) throws CommandSyntaxException {
        if (string == null || string.isEmpty()) {
            throw ExceptionBuilder.createCommandException("Input string cannot be empty.");
        }
    }


    public static void checkNotNull(@Nullable Object obj) throws CommandSyntaxException {
        if (obj == null) {
            throw ExceptionBuilder.createCommandException("Input cannot be null.");
        }
    }

}
