package xyz.bitsquidd.bits.lib.command.util;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Optional;
import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.argument.BrigadierArgumentMapping;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;

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
    private final boolean isOptional; // Whether the parameter is optional

    private final AbstractArgumentParserNew<?> parser;

    private final List<BrigadierArgumentMapping> heldArguments = new ArrayList<>(); // Direct mapping from the custom args to brigadier ArgumentTypes


    public CommandParameterInfo(Parameter parameter) {
        this.parameter = parameter;
        this.typeSignature = TypeSignature.of(parameter.getParameterizedType());
        this.isOptional = parameter.isAnnotationPresent(Optional.class);

        this.parser = BitsArgumentRegistry.getInstance().getParser(typeSignature);
        this.heldArguments.addAll(BitsArgumentRegistry.getInstance().getArgumentTypeContainer(parser));
    }

    public List<RequiredArgumentBuilder<CommandSourceStack, ?>> createBrigadierArguments() {
        List<RequiredArgumentBuilder<CommandSourceStack, ?>> brigadierArguments = new ArrayList<>();

        heldArguments.forEach(arg -> {
            RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = arg.toBrigadierArgument();
            argumentBuilder.suggests(parser.getSuggestionProvider(argumentBuilder.getSuggestionsProvider()));
            brigadierArguments.add(argumentBuilder);
        });
        return brigadierArguments;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public AbstractArgumentParserNew<?> getParser() {
        return parser;
    }

    public List<BrigadierArgumentMapping> getHeldArguments() {
        return heldArguments;
    }

    @Override
    public String toString() {
        return "CommandParameterInfo{" +
              "typeSignature=" + typeSignature +
              ", parameter=" + parameter +
              '}';
    }
}
