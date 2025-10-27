package xyz.bitsquidd.bits.lib.command.argument.parser;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@NullMarked
public abstract class AbstractArgumentParserNew<O> {
    private final TypeSignature<?> typeSignature; // The type signature this parser handles
    private final String argumentName;            // The name of the argument, used while displaying suggestions

    protected AbstractArgumentParserNew(TypeSignature<?> typeSignature, String argumentName) {
        this.typeSignature = typeSignature;
        this.argumentName = argumentName;
    }


    public abstract @NotNull O parse(List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException;

    /**
     * Returns a list of required objects the parser expects in.
     * In the case of most custom parsers this will be a String.
     * <p>
     * In the case a <a href="https://docs.papermc.io/paper/dev/command-api/basics/arguments-and-literals/#arguments">non-vanilla primitive</a> is passed in, we expect a parser to be present for this.     * <p>
     * For example: <ul>
     * <li> An Integer parser would expect a single int {@code List.of(Integer.class)} </li>
     * <li> A Location parser may expect three doubles and a World {@code List.of(Double.class, Double.class, Double.class, World.class)} </li>
     * </ul>
     */
    public List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(String.class), getArgumentName()));
    }

    /**
     * Helper function to validate singleton inputs for basic argument parsers.
     */
    protected <I> I singletonInputValidation(List<Object> inputObjects, Class<I> expectedType) {
        List<InputTypeContainer> inputTypes = getInputTypes();
        if (inputTypes.size() != 1) throw new CommandParseException("Expected exactly one input type, got " + inputTypes.size());
        if (inputTypes.getFirst().typeSignature().toRawType() != expectedType) {
            throw new CommandParseException("Expected input type signature to be " + expectedType.getSimpleName() + ", got " + inputTypes.getFirst().typeSignature().toRawType().getSimpleName());
        }

        if (inputObjects.size() != 1) throw new CommandParseException("Expected exactly one input object, got " + inputObjects.size());
        Object value = inputObjects.getFirst();

        if (!expectedType.isInstance(value)) {
            throw new CommandParseException("Expected input object of type " + expectedType.getSimpleName() + ", got " + value.getClass().getSimpleName());
        }

        return expectedType.cast(value);
    }

    /**
     * Helper function to validate multiple arguments for complex argument parsers..
     */
    protected List<Object> inputValidation(List<Object> inputObjects) {
        List<InputTypeContainer> inputTypes = getInputTypes();
        int inputSize = inputTypes.size();

        if (inputObjects.size() != inputSize) throw new CommandParseException("Expected exactly " + inputSize + " input object" + (inputSize > 1 ? "s" : "") + ", got " + inputObjects.size());

        List<Object> returnList = new ArrayList<>();

        for (int i = 0; i < inputSize; i++) {
            InputTypeContainer expectedTypeContainer = inputTypes.get(i);
            Object value = inputObjects.get(i);
            Class<?> expectedType = expectedTypeContainer.typeSignature().toRawType();

            if (!expectedType.isInstance(value)) {
                throw new CommandParseException("Expected input object of type " + expectedType.getSimpleName() + ", got " + value.getClass().getSimpleName());
            }

            returnList.add(expectedType.cast(value));
        }

        return returnList;
    }


    public final SuggestionProvider<CommandSourceStack> getSuggestionProvider() {
        return (ctx, builder) -> {
            BitsCommandContext bitsCtx = BitsConfig.getCommandManager().createContext(ctx);

            Supplier<List<String>> suggestionSupplier = getSuggestions();
            if (suggestionSupplier == null) return builder.buildFuture();
            
            List<String> suggestions = suggestionSupplier.get();
            String remaining = builder.getRemaining().toLowerCase();

            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().startsWith(remaining)) {
                    builder.suggest(suggestion);

                    BitsConfig.getPlugin().getLogger().info("Suggesting: " + suggestion);
                }
            }

            return builder.buildFuture();
        };
    }

    /**
     * Returns a list of suggestions for the argument at a given context state.
     */
    public @Nullable Supplier<List<String>> getSuggestions() {
        return null;
    }


    public TypeSignature<?> getTypeSignature() {
        return typeSignature;
    }

    public String getArgumentName() {
        return argumentName;
    }

}
