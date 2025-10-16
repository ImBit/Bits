package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

public class VanillaArgumentParser<T> extends AbstractArgumentParser<@NotNull T, @NotNull T> {

    public VanillaArgumentParser(TypeSignature typeSignature, ArgumentType<T> argumentType) {
        super(typeSignature, argumentType, (Class<T>)typeSignature.toRawType(), (Class<T>)typeSignature.toRawType());
    }

    @Override
    public @NotNull T parse(@NotNull T input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return input;
    }
}
