package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;

public class DoubleArgumentParser implements ArgumentParser<Double> {

    @Override
    public @NotNull Double parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return reader.readDouble();
    }

    @Override
    public @NotNull Class<Double> getType() {
        return Double.class;
    }

}