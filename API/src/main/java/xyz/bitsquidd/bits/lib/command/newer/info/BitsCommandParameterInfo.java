package xyz.bitsquidd.bits.lib.command.newer.info;

import xyz.bitsquidd.bits.lib.command.newer.annotation.Optional;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class to encapsulate information about command parameters
 */
public class BitsCommandParameterInfo {
    private final Type type;
    private final String name;
    private final boolean optional;

    public BitsCommandParameterInfo(Parameter parameter) {
        this.type = parameter.getParameterizedType();
        this.name = parameter.getName();
        this.optional = parameter.isAnnotationPresent(Optional.class);
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