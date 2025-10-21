package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record BrigadierArgumentMapping(
      ArgumentType<?> argumentType,
      TypeSignature<?> typeSignature,
      String argumentName
) {
    public ArgumentBuilder<CommandSourceStack, ?> toBrigadierArgument() {
        return Commands.argument(
              argumentName,
              argumentType
        );
    }
}