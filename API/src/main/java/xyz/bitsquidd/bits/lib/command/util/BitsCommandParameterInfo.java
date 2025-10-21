package xyz.bitsquidd.bits.lib.command.util;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Optional;
import xyz.bitsquidd.bits.lib.command.argument.ArgumentRegistryNew;
import xyz.bitsquidd.bits.lib.command.argument.ArgumentTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;

import java.lang.reflect.Parameter;

/**
 * Utility class to encapsulate information about command parameters
 */
@NullMarked
public class BitsCommandParameterInfo {
    private final Parameter parameter;
    private final TypeSignature<?> typeSignature;
    private final boolean optional;
    private final ArgumentTypeContainer argumentTypeContainer; // Contains information about the argument


    public BitsCommandParameterInfo(Parameter parameter) {
        this.parameter = parameter;
        this.typeSignature = TypeSignature.of(parameter.getType());
        this.optional = parameter.isAnnotationPresent(Optional.class);

        argumentTypeContainer = ArgumentRegistryNew.getInstance().getArgumentTypeContainer(typeSignature, getParameterName());
    }

    private String getParameterName() {
        // If the parameter name is synthetic (arg0, arg1), get the name from the argument parser
        // By default, Java does not retain parameter names at runtime unless compiled with the -parameters flag,
        // see: https://docs.gradle.org/current/userguide/java_plugin.html#example_registering_incremental_annotation_processors_dynamically
        if (parameter.getName().contains("arg")) {
            AbstractArgumentParserNew<?> parser = ArgumentRegistryNew.getInstance().getParser(typeSignature);
            return parser.getArgumentName();
        } else {
            return parameter.getName();
        }
    }

    public ArgumentTypeContainer getArgumentTypeContainer() {
        return argumentTypeContainer;
    }


    public TypeSignature<?> getTypeSignature() {
        return typeSignature;
    }

    public boolean isOptional() {
        return optional;
    }
}