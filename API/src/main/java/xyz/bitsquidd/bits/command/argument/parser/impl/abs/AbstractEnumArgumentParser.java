/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.argument.parser.impl.abs;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.command.exception.ExceptionBuilder;
import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Argument parser for enum types.
 */
@NullMarked
public abstract class AbstractEnumArgumentParser<T extends Enum<T>> extends AbstractArgumentParser<T> {
    private final Class<T> enumClass;

    public AbstractEnumArgumentParser(Class<T> enumClass) {
        super(TypeSignature.of(Enum.class), enumClass.getName());
        this.enumClass = enumClass;
        if (!enumClass.isEnum()) throw new IllegalArgumentException("Provided class " + enumClass.getName() + " is not an enum!");
    }

    @Override
    public T parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandSyntaxException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        T enumValue;
        try {
            enumValue = Enum.valueOf(enumClass, inputString);
        } catch (IllegalArgumentException e) {
            throw ExceptionBuilder.createCommandException(inputString + " is not a valid " + enumClass.getSimpleName() + ".");
        }

        return enumValue;
    }

    @Override
    public @Nullable Supplier<List<String>> getSuggestions() {
        return () -> enumClass.isEnum() ? Stream.of(enumClass.getEnumConstants()).map(Enum::name).toList() : List.of();
    }

}