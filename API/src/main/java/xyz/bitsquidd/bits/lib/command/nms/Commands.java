package xyz.bitsquidd.bits.lib.command.nms;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class Commands<T> {
    public LiteralArgumentBuilder<T> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public <W> RequiredArgumentBuilder<T, W> argument(String name, ArgumentType<W> type) {

    }

}
