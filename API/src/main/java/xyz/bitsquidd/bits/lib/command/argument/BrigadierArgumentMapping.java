package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.lib.command.nms.Commands;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

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