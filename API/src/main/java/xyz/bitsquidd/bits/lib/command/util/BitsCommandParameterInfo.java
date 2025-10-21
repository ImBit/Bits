package xyz.bitsquidd.bits.lib.command.util;

import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Optional;
import xyz.bitsquidd.bits.lib.command.argument.ArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.AbstractArgumentParser;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class to encapsulate information about command parameters
 */
@NullMarked
public class BitsCommandParameterInfo {
    private final Type type;
    private final String name;
    private final boolean optional;

    public BitsCommandParameterInfo(Parameter parameter) {
        this.type = parameter.getParameterizedType();
        this.optional = parameter.isAnnotationPresent(Optional.class);

        this.name = getParameterName(parameter);
    }

    private String getParameterName(Parameter parameter) {
        // If the parameter name is synthetic (arg0, arg1), get the name from the argument parser
        // By default, Java does not retain parameter names at runtime unless compiled with the -parameters flag,
        // see: https://docs.gradle.org/current/userguide/java_plugin.html#example_registering_incremental_annotation_processors_dynamically
        if (parameter.getName().contains("arg")) {
            AbstractArgumentParser<?, ?> parser = ArgumentRegistry.getInstance().getParser(type);
            return parser.getArgumentName();
        } else {
            return parameter.getName();
        }

    }

    public Type getType() {
        return type;
    }

    public Class<?> getRawType() {
        if (type instanceof Class<?>) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<?>)((ParameterizedType)type).getRawType();
        }
        throw new IllegalStateException("Unknown type: " + type);
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }
}