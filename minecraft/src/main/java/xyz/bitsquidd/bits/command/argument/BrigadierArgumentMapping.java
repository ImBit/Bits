/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import xyz.bitsquidd.bits.command.BitsCommandManager;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;

/**
 * A data container linking an expected return type with a built Brigadier argument type.
 * <p>
 * This record is primarily used when compiling custom {@link xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser}
 * instances into their base Brigadier representations for tree registration.
 *
 * @param argumentType  the underlying Brigadier argument type implementation
 * @param typeSignature the expected type signature
 * @param argumentName  the literal node label in the command sequence
 *
 * @since 0.0.10
 */
public record BrigadierArgumentMapping(
  ArgumentType<?> argumentType,
  TypeSignature<?> typeSignature,
  String argumentName
) {
    public <T> RequiredArgumentBuilder<T, ?> toBrigadierArgument(BitsCommandManager<T> manager) {
        return manager.createArgument(
          argumentName,
          argumentType
        );
    }

}