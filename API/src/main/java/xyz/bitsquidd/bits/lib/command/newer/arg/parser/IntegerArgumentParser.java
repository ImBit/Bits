package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;

public class IntegerArgumentParser implements ArgumentParser<Integer> {

    @Override
    public @NotNull Integer parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return reader.readInt();
    }

    @Override
    public @NotNull Class<Integer> getType() {
        return Integer.class;
    }

}