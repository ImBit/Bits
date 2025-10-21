package xyz.bitsquidd.bits.lib.command.argument.parser;

import com.mojang.brigadier.arguments.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ArgumentTypeRegistry {

    public static ArgumentType<?> getArgumentType(Class<?> clazz) {
        if (clazz == Integer.class || clazz == int.class) {
            return IntegerArgumentType.integer();
        } else if (clazz == Double.class || clazz == double.class) {
            return DoubleArgumentType.doubleArg();
        } else if (clazz == Float.class || clazz == float.class) {
            return FloatArgumentType.floatArg();
        } else if (clazz == Long.class || clazz == long.class) {
            return LongArgumentType.longArg();
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return BoolArgumentType.bool();
        } else {
            return StringArgumentType.word();
        }
    }
}
