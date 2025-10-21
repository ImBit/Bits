package xyz.bitsquidd.bits.lib.command.argument;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record InputTypeContainer(
      TypeSignature<?> typeSignature,
      String typeName
) {}