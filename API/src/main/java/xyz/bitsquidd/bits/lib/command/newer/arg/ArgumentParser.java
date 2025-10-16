package xyz.bitsquidd.bits.lib.command.newer.arg;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

public interface ArgumentParser<T> {
    @NotNull T parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException;

    @NotNull Class<T> getType();
}