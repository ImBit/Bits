package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ArgumentTypeHolder(
      ArgumentType<?> argumentType,
      TypeSignature<?> typeSignature,
      String argumentName
) {}