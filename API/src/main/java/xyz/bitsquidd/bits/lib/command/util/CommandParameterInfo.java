package xyz.bitsquidd.bits.lib.command.util;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Optional;
import xyz.bitsquidd.bits.lib.command.argument.ArgumentRegistryNew;
import xyz.bitsquidd.bits.lib.command.argument.BrigadierArgumentMapping;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

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
        this.typeSignature = TypeSignature.of(parameter.getType());
        this.isOptional = parameter.isAnnotationPresent(Optional.class);

        BitsConfig.getPlugin().getLogger().info("Registering parameter: " + parameter.getName() + " of type " + typeSignature);

        this.parser = ArgumentRegistryNew.getInstance().getParser(typeSignature);
        this.heldArguments.addAll(ArgumentRegistryNew.getInstance().getArgumentTypeContainer(parser));
    }

    public List<ArgumentBuilder<CommandSourceStack, ?>> createBrigadierArguments() {
        List<ArgumentBuilder<CommandSourceStack, ?>> brigadierArguments = new ArrayList<>();
        heldArguments.forEach(arg -> brigadierArguments.add(arg.toBrigadierArgument()));
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

}
