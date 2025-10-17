package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.impl.PlayerArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.PlayerCollectionArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.WorldArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.impl.vanilla.*;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Type signature is the expected return type of the command.
 * ArgumentType is the native type of the argument.
 */
@NullMarked
public class ArgumentRegistry {
    private static @Nullable ArgumentRegistry instance;

    private final Set<AbstractArgumentParser<?, ?>> parsers = new HashSet<>();

    public ArgumentRegistry() {
        if (instance != null) throw new IllegalStateException("ArgumentRegistry has already been initialized.");
        instance = this;

        List<AbstractArgumentParser<?, ?>> initialParsers = new ArrayList<>(initialiseDefaultParsers());
        initialParsers.addAll(initialiseParsers());
        parsers.addAll(initialParsers);
    }

    public static ArgumentRegistry getInstance() {
        if (instance == null) throw new IllegalStateException("ArgumentRegistry has not been initialized yet.");
        return instance;
    }

    private List<AbstractArgumentParser<?, ?>> initialiseDefaultParsers() {
        return List.of(
              new BooleanArgumentParser(),
              new DoubleArgumentParser(),
              new FloatArgumentParser(),
              new IntegerArgumentParser(),
              new LongArgumentParser(),
              new PlayerArgumentParser(),
              new PlayerCollectionArgumentParser(),
              new StringArgumentParser(),
              new WorldArgumentParser()
        );
    }

    protected List<AbstractArgumentParser<?, ?>> initialiseParsers() {
        return List.of();
    }


    public AbstractArgumentParser<?, ?> getParser(Type type) {
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
                } else if (clazz == double.class) {
                    type = Double.class;
                } else {
                    throw new IllegalArgumentException("No parser found for primitive type: " + clazz);
                }
            }
        }

        for (AbstractArgumentParser<?, ?> parser : parsers) {
            if (parser.getTypeSignature().matches(type)) {
                BitsConfig.getPlugin().getLogger().info("Found parser " + parser.getClass().getSimpleName() + " for type " + type + "  " + parser.getArgumentType());

                return parser;
            }
        }

        throw new IllegalArgumentException("No parser found for type: " + type);
    }


    public ArgumentType<?> getArgumentType(Type type) {
        return getParser(type).getArgumentType();
    }

}