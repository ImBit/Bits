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