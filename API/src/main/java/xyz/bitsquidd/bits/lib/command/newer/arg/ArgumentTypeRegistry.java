package xyz.bitsquidd.bits.lib.command.newer.arg;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.impl.*;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Type signature is the expected return type of the command.
 * ArgumentType is the native type of the argument.
 */
public class ArgumentTypeRegistry {
    private final Set<AbstractArgumentParser<?, ?>> parsers = new HashSet<>();

    public ArgumentTypeRegistry() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(new BooleanArgumentParser());
        register(new DoubleArgumentParser());
        register(new FloatArgumentParser());
        register(new IntegerArgumentParser());
        register(new LongArgumentParser());
        register(new PlayerArgumentParser());
        register(new PlayerCollectionArgumentParser());
        register(new StringArgumentParser());
        register(new WorldArgumentParser());
    }

    public <T> void register(@NotNull AbstractArgumentParser<?, ?> parser) {
        parsers.add(parser);
    }

    public @NotNull AbstractArgumentParser<?, ?> getParser(@NotNull Type type) {
        if (type instanceof Class<?> clazz) {
            if (clazz.isPrimitive()) {
                if (clazz == boolean.class) {
                    type = Boolean.class;
                } else if (clazz == byte.class) {
                    type = Byte.class;
                } else if (clazz == char.class) {
                    type = Character.class;
                } else if (clazz == short.class) {
                    type = Short.class;
                } else if (clazz == int.class) {
                    type = Integer.class;
                } else if (clazz == long.class) {
                    type = Long.class;
                } else if (clazz == float.class) {
                    type = Float.class;
                } else if (clazz == double.class) type = Double.class;
            }
        }

        for (AbstractArgumentParser<?, ?> parser : parsers) {
            if (parser.getTypeSignature().matches(type)) {
                return parser;
            }
        }
        throw new IllegalArgumentException("No parser found for type: " + type);
    }

    public ArgumentType<?> getArgumentType(@NotNull Type type) {
        return getParser(type).getArgumentType();
    }

}