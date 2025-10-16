package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

public class BooleanArgumentParser implements ArgumentParser<Boolean> {

    @Override
    public @NotNull Boolean parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return reader.readBoolean();
    }

    @Override
    public @NotNull Class<Boolean> getType() {
        return Boolean.class;
    }

}