package xyz.bitsquidd.bits.lib.command.argument.impl.primitive;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

@NullMarked
public sealed abstract class PrimitiveArgumentParserNew<O> extends AbstractArgumentParserNew<O>
      permits BooleanArgumentParser, DoubleArgumentParser, FloatArgumentParser, IntegerArgumentParser, LongArgumentParser, StringArgumentParser, GreedyStringArgumentParser {

    PrimitiveArgumentParserNew(Class<O> outputClass, String argumentName) {
        super(TypeSignature.of(outputClass), argumentName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final @NotNull O parse(List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException {
        return (O)singletonInputValidation(inputObjects, getTypeSignature().toRawType());
    }

    @Override
    public final List<TypeSignature<?>> getInputTypes() {
        return List.of(getTypeSignature());
    }

}
