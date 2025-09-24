package xyz.bitsquidd.bits.lib.command.newe.core;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class BCommands {
    static LiteralArgumentBuilder<CommandSourceStack> literal(final String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    static <T> BRequiredArgumentBuilder<CommandSourceStack, T> argument(final String name, final ArgumentType<T> argumentType) {
        return BRequiredArgumentBuilder.argument(name, argumentType);
    }
}
