package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

public class FloatArgumentParser implements ArgumentParser<Float> {

    @Override
    public @NotNull Float parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return reader.readFloat();
    }

    @Override
    public @NotNull Class<Float> getType() {
        return Float.class;
    }

}