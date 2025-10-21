package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;

import java.util.List;

@NullMarked
public sealed abstract class PrimitiveArgumentParserNew<O> extends AbstractArgumentParserNew<O>
      permits BooleanArgumentParser, DoubleArgumentParser, FloatArgumentParser, IntegerArgumentParser, LongArgumentParser, StringArgumentParser {

    PrimitiveArgumentParserNew(TypeSignature typeSignature, Class<O> outputClass, String argumentName) {
        super(TypeSignature.of(outputClass), outputClass, argumentName);
    }

    @Override
    public final @NotNull O parse(List<Object> inputObjects) throws CommandParseException {
        return singletonInputValidation(inputObjects, getOutputClass());
    }

    @Override
    public final List<TypeSignature> getInputTypes() {
        return List.of(TypeSignature.of(getOutputClass()));
    }

}
