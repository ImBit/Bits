package xyz.bitsquidd.bits.lib.command.argument.old.parser;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

public class VanillaArgumentParser<T> extends AbstractArgumentParser<@NotNull T, @NotNull T> {

    @SuppressWarnings("unchecked")
    public VanillaArgumentParser(TypeSignature typeSignature, ArgumentType<T> argumentType, String argumentName) {
        super(typeSignature, argumentType, (Class<T>)typeSignature.toRawType(), (Class<T>)typeSignature.toRawType(), argumentName);
    }

    @Override
    public @NotNull T parse(@NotNull T input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return input;
    }
}
