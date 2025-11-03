package xyz.bitsquidd.bits.lib.command.util;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.argument.BrigadierArgumentMapping;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

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

        String baseName = typeSignature.toRawType().getSimpleName().toLowerCase();

        this.parser = BitsArgumentRegistry.getInstance().getParser(typeSignature);
        this.heldArguments.addAll(BitsArgumentRegistry.getInstance().getArgumentTypeContainer(parser, baseName));
    }

    public List<ArgumentBuilder<CommandSourceStack, ?>> createBrigadierArguments() {
        List<ArgumentBuilder<CommandSourceStack, ?>> brigadierArguments = new ArrayList<>();

        boolean useArgSuggestions = heldArguments.size() > 1; // We use the arg suggestions only if there are multiple held arguments

        for (int i = 0; i < heldArguments.size(); i++) {
            BrigadierArgumentMapping arg = heldArguments.get(i);
            RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder = arg.toBrigadierArgument();

            if (useArgSuggestions) {
                TypeSignature<?> inputType = parser.getInputTypes().get(i).typeSignature();
                AbstractArgumentParser<?> inputParser = BitsArgumentRegistry.getInstance().getParser(inputType);

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
