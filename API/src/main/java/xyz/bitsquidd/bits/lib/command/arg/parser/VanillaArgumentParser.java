package xyz.bitsquidd.bits.lib.command.arg.parser;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.info.BitsCommandContext;

public class VanillaArgumentParser<T> extends AbstractArgumentParser<@NotNull T, @NotNull T> {

    public VanillaArgumentParser(TypeSignature typeSignature, ArgumentType<T> argumentType) {
        super(typeSignature, argumentType, (Class<T>)typeSignature.toRawType(), (Class<T>)typeSignature.toRawType());
    }

    @Override
    public @NotNull T parse(@NotNull T input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return input;
    }
}
