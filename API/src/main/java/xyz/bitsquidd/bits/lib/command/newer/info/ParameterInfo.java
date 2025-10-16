package xyz.bitsquidd.bits.lib.command.newer.info;

import java.lang.reflect.Type;

/**
 * Utility class to encapsulate information about command parameters
 */
public class ParameterInfo {
    private final Type type;
    private final String name;
    private final boolean optional;

    public ParameterInfo(Type type, String name, boolean optional) {
        this.type = type;
        this.name = name;
        this.optional = optional;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }
}