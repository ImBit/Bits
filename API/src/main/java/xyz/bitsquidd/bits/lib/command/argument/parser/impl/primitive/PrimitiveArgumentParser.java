package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;

/**
 * A parser for primitive argument types.
 * A primitive type directly corresponds to a Brigadier primitive type.
 */
public abstract class PrimitiveArgumentParser<O> extends AbstractArgumentParser<O> {

    protected PrimitiveArgumentParser(Class<O> outputClass, String argumentName) {
        super(TypeSignature.of(outputClass), argumentName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final O parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandSyntaxException {
        return (O)singletonInputValidation(inputObjects, getTypeSignature().toRawType());
    }

    @Override
    public final List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(getTypeSignature(), getArgumentName()));
    }

}
