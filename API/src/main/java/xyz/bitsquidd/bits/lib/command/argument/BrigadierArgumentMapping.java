package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

@NullMarked
public record BrigadierArgumentMapping(
      ArgumentType<?> argumentType,
      TypeSignature<?> typeSignature,
      String argumentName
) {
    public RequiredArgumentBuilder<CommandSourceStack, ?> toBrigadierArgument() {
        return Commands.argument(
              argumentName,
              argumentType
        );
    }
}