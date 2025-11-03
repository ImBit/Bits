package xyz.bitsquidd.bits.lib.command.argument;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

@NullMarked
public record InputTypeContainer(
      TypeSignature<?> typeSignature,
      String typeName
) {}