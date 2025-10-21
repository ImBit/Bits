package xyz.bitsquidd.bits.lib.command.argument.parser;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

@NullMarked
public abstract class AbstractArgumentParserNew<O> {
    private final TypeSignature typeSignature; // The type signature this parser handles
    private final Class<O> outputClass;        // The class the parser outputs
    private final String argumentName;         // The name of the argument, used while displaying suggestions

    protected AbstractArgumentParserNew(TypeSignature typeSignature, Class<O> outputClass, String argumentName) {
        this.typeSignature = typeSignature;
        this.outputClass = outputClass;
        this.argumentName = argumentName;
    }


    public abstract @NotNull O parse(List<Object> inputObjects) throws CommandParseException;

    /**
     * Returns a list of required objects the parser expects in.
     * In the case of most custom parsers this will be a String.
     * <p>
     * In the case a {@link <a href="https://docs.papermc.io/paper/dev/command-api/basics/arguments-and-literals/#arguments">non-vanilla primitive</a>} is passed in, we expect a parser to be present for this.
     * Therefore, unless a primitive, you should not expect to receive the type defined by O.
     * <p>
     * For example: <ul>
     * <li> An Integer parser would expect a single int {@code List.of(Integer.class)} </li>
     * <li> A Location parser may expect three doubles and a World {@code List.of(Double.class, Double.class, Double.class, World.class)} </li>
     * </ul>
     */
    public abstract List<TypeSignature> getInputTypes();

    /**
     * Helper function to validate singleton inputs for basic argument parsers.
     */
    protected <I> I singletonInputValidation(List<Object> inputObjects, Class<I> expectedInputClass) {
        if (inputObjects.size() != 1) throw new CommandParseException("Expected exactly one input object, got " + inputObjects.size());
        Object value = inputObjects.getFirst();

        if (!expectedInputClass.isInstance(value)) {
            throw new CommandParseException("Expected input object of type " + expectedInputClass.getSimpleName() + ", got " + value.getClass().getSimpleName());
        }

        return expectedInputClass.cast(value);
    }

    /**
     * Returns a list of suggestions for the argument at a given context state.
     */
    public List<String> getSuggestions(BitsCommandContext ctx) {
        return List.of();
    }


    public TypeSignature getTypeSignature() {
        return typeSignature;
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public String getArgumentName() {
        return argumentName;
    }

}
