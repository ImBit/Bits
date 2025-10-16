package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;

public class LongArgumentParser implements ArgumentParser<Long> {

    @Override
    public @NotNull Long parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return reader.readLong();
    }

    @Override
    public @NotNull Class<Long> getType() {
        return Long.class;
    }

}