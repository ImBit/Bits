package xyz.bitsquidd.bits.lib.command.argument;

import xyz.bitsquidd.bits.lib.wrapper.type.TypeSignature;

public record InputTypeContainer(
  TypeSignature<?> typeSignature,
  String typeName
) {}