/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command.argument.parser;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.exception.ExceptionBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrapper.type.TypeSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A parser which can be defined to convert a list of input command arguments into a specific object type.
 */
public abstract class AbstractArgumentParser<O> {
    private final TypeSignature<?> typeSignature; // The type signature this parser handles
    private final String argumentName;            // The name of the argument, used while displaying suggestions

    protected AbstractArgumentParser(TypeSignature<?> typeSignature, String argumentName) {
        this.typeSignature = typeSignature;
        this.argumentName = argumentName;
    }


    public abstract O parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandSyntaxException;

    /**
     * Returns a list of required objects the parser expects in.
     * In the case of most custom parsers this will be a String.
     * <p>
     * In the case a <a href="https://docs.papermc.io/paper/dev/command-api/basics/arguments-and-literals/#arguments">non-vanilla primitive</a> is passed in, we expect a parser to be present for this.<p>
     * For example: <ul>
     * <li> An Integer parser would expect a single int {@code List.of(Integer.class)} </li>
     * <li> A Location parser may expect three doubles and a World {@code List.of(Double.class, Double.class, Double.class, World.class)} </li>
     * </ul>
     */
    public List<InputTypeContainer> getInputTypes() {
        return List.of(new InputTypeContainer(TypeSignature.of(String.class), getArgumentName()));
    }


    //region Validation

    /**
     * Helper function to validate singleton inputs for basic argument parsers.
     */
    protected <I> I singletonInputValidation(List<Object> inputObjects, Class<I> expectedType) throws CommandSyntaxException {
        List<InputTypeContainer> inputTypes = getInputTypes();
        if (inputTypes.size() != 1) throw ExceptionBuilder.createCommandException("Expected exactly one input type, got " + inputTypes.size() + ".");
        if (inputTypes.getFirst().typeSignature().toRawType() != expectedType) {
            throw ExceptionBuilder.createCommandException("Expected input type signature to be " + expectedType.getSimpleName() + ", got " + inputTypes.getFirst().typeSignature().toRawType().getSimpleName() + ".");
        }

        if (inputObjects.size() != 1) throw ExceptionBuilder.createCommandException("Expected exactly one input object, got " + inputObjects.size() + ".");
        Object value = inputObjects.getFirst();

        if (!expectedType.isInstance(value)) {
            throw ExceptionBuilder.createCommandException("Expected input object of type " + expectedType.getSimpleName() + ", got " + value.getClass().getSimpleName() + ".");
        }

        return expectedType.cast(value);
    }

    /**
     * Helper function to validate multiple arguments for complex argument parsers.
     */
    protected List<Object> inputValidation(List<Object> inputObjects) throws CommandSyntaxException {
        List<InputTypeContainer> inputTypes = getInputTypes();
        int inputSize = inputTypes.size();

        if (inputObjects.size() != inputSize) {
            throw ExceptionBuilder.createCommandException("Expected exactly " + inputSize + " input object" + (inputSize > 1 ? "s" : "") + ", got " + inputObjects.size() + ".");
        }

        List<Object> returnList = new ArrayList<>();

        for (int i = 0; i < inputSize; i++) {
            InputTypeContainer expectedTypeContainer = inputTypes.get(i);
            Object value = inputObjects.get(i);
            Class<?> expectedType = expectedTypeContainer.typeSignature().toRawType();

            if (!expectedType.isInstance(value)) {
                throw ExceptionBuilder.createCommandException("Expected input object of type " + expectedType.getSimpleName() + ", got " + value.getClass().getSimpleName() + ".");
            }

            returnList.add(expectedType.cast(value));
        }

        return returnList;
    }
    //endregion


    public final <T> SuggestionProvider<T> getSuggestionProvider() {
        return (ctx, builder) -> {
            Supplier<List<String>> suggestionSupplier = getSuggestions();
            if (suggestionSupplier == null) return builder.buildFuture();

            List<String> suggestions = suggestionSupplier.get();
            String remaining = builder.getRemaining().toLowerCase();

            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().startsWith(remaining)) {
                    builder.suggest(suggestion);
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
