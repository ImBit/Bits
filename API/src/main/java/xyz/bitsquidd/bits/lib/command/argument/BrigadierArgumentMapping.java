package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

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