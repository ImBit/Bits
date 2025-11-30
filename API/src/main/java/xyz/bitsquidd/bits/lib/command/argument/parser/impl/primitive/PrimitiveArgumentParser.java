package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;

public sealed abstract class PrimitiveArgumentParser<O> extends AbstractArgumentParser<O>
      permits BooleanArgumentParser, DoubleArgumentParser, FloatArgumentParser, IntegerArgumentParser, LongArgumentParser, StringArgumentParser, EntitySelectorArgumentParser {

    PrimitiveArgumentParser(Class<O> outputClass, String argumentName) {
        super(TypeSignature.of(outputClass), argumentName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final O parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandParseException {
        return (O)singletonInputValidation(inputObjects, getTypeSignature().toRawType());
    }

    @Override
    public final List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(getTypeSignature(), getArgumentName()));
    }

}
