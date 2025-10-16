package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

@NullMarked
public abstract class AbstractArgumentParser<I, O> {
    private final TypeSignature typeSignature;  // The signature methods expect to receive
    private final ArgumentType<I> argumentType; // The brigadier type used for clientside highlighting.
    private final Class<I> inputClass;
    private final Class<O> outputClass;

    public AbstractArgumentParser(TypeSignature typeSignature, ArgumentType<I> argumentType, Class<I> inputClass, Class<O> outputClass) {
        this.typeSignature = typeSignature;
        this.argumentType = argumentType;

        this.inputClass = inputClass;
        this.outputClass = outputClass;
    }

    public abstract @NotNull O parse(@NotNull I input, BitsCommandContext context) throws CommandSyntaxException;

    public TypeSignature getTypeSignature() {
        return typeSignature;
    }

    public ArgumentType<I> getArgumentType() {
        return argumentType;
    }

    public Class<I> getInputClass() {
        return inputClass;
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

}
