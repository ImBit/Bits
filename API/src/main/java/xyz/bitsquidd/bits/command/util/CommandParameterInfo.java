/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.util;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.command.BitsCommandManager;
import xyz.bitsquidd.bits.command.argument.BrigadierArgumentMapping;
import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.BitsConfig;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about a parameter in a command method.
 */
@NullMarked
public class CommandParameterInfo {
    private final Parameter parameter;

    private final TypeSignature<?> typeSignature; // TypeSignature of the parameter

    private final AbstractArgumentParser<?> parser;

    private final List<BrigadierArgumentMapping> heldArguments = new ArrayList<>(); // Direct mapping from the custom args to brigadier ArgumentTypes


    public CommandParameterInfo(Parameter parameter) {
        this.parameter = parameter;
        this.typeSignature = TypeSignature.of(parameter.getParameterizedType());

        xyz.bitsquidd.bits.command.annotation.Parameter parameterAnnotation = parameter.getAnnotation(xyz.bitsquidd.bits.command.annotation.Parameter.class);
        String name;
        if (parameterAnnotation != null && !parameterAnnotation.value().isEmpty()) {
            name = parameterAnnotation.value();
        } else {
            String parameterName = parameter.getName();
            name = parameterName.contains("arg") ? typeSignature.toRawType().getSimpleName().toLowerCase() : parameterName;
        }

        this.parser = BitsConfig.get().getCommandManager().getArgumentRegistry().getParser(typeSignature);
        this.heldArguments.addAll(BitsConfig.get().getCommandManager().getArgumentRegistry().getArgumentTypeContainer(parser, name));
    }

    @SuppressWarnings("unchecked")
    public <T> List<ArgumentBuilder<T, ?>> createBrigadierArguments() {
        BitsCommandManager<T> commandManager = (BitsCommandManager<T>)BitsConfig.get().getCommandManager();
        List<ArgumentBuilder<T, ?>> brigadierArguments = new ArrayList<>();

        boolean useArgSuggestions = heldArguments.size() > 1; // We use the arg suggestions only if there are multiple held arguments

        for (int i = 0; i < heldArguments.size(); i++) {
            BrigadierArgumentMapping arg = heldArguments.get(i);
            RequiredArgumentBuilder<T, ?> argumentBuilder = arg.toBrigadierArgument(commandManager);

            if (useArgSuggestions) {
                TypeSignature<?> inputType = parser.getInputTypes().get(i).typeSignature();
                AbstractArgumentParser<?> inputParser = commandManager.getArgumentRegistry().getParser(inputType);

                // Use the input-specific parser's suggestions
                if (hasSuggestions(inputParser)) argumentBuilder.suggests(inputParser.getSuggestionProvider());
            } else {
                // Use the main parser's suggestions
                if (hasSuggestions(parser)) argumentBuilder.suggests(parser.getSuggestionProvider());
            }

            brigadierArguments.add(argumentBuilder);
        }
        return brigadierArguments;
    }

    private boolean hasSuggestions(AbstractArgumentParser<?> parser) {
        return parser.getSuggestions() != null;
    }

    public AbstractArgumentParser<?> getParser() {
        return parser;
    }

    public List<BrigadierArgumentMapping> getHeldArguments() {
        return heldArguments;
    }

    public TypeSignature<?> getTypeSignature() {
        return typeSignature;
    }

    @Override
    public String toString() {
        return "CommandParameterInfo{" +
          "typeSignature=" + typeSignature +
          ", parameter=" + parameter +
          '}';
    }

}
