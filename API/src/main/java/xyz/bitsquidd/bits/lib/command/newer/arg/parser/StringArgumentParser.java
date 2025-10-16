package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

public class StringArgumentParser implements ArgumentParser<String> {

    @Override
    public @NotNull String parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public @NotNull Class<String> getType() {
        return String.class;
    }

}