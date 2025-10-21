package xyz.bitsquidd.bits.lib.command.argument;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a type signature, including raw type and type arguments.
 * For example: <ul>
 * <li>A {@code List<Integer>} would be represented as: {@code TypeSignature.of(List.class, Integer.class)}
 * <li>A {@code Map<String, Float>} would be represented as: {@code TypeSignature.of(Map.class, String.class, Float.class)}
 * </ul>
 */
public class TypeSignature<T> {
    private final Class<T> rawType;
    private final Type[] typeArguments;

    private TypeSignature(Class<T> rawType, Type[] typeArguments) {
        this.rawType = rawType;
        this.typeArguments = typeArguments != null ? typeArguments.clone() : new Type[0];
    }

    public static TypeSignature<?> of(@NotNull Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Class<?> rawType = (Class<?>)parameterizedType.getRawType();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            return new TypeSignature<>(rawType, typeArgs);
        } else if (type instanceof Class<?> clazz) {
            return new TypeSignature<>(clazz, null);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public static <I> TypeSignature<I> of(@NotNull Class<I> clazz) {
        return new TypeSignature<>(clazz, null);
    }

    public static TypeSignature<?> of(@NotNull Class<?> rawType, @NotNull Class<?>... typeArguments) {
        return new TypeSignature<>(rawType, typeArguments);
    }

    public Class<T> toRawType() {
        return rawType;
    }

    public boolean matches(@NotNull Type other) {
        TypeSignature<?> otherSig = TypeSignature.of(other);
        if (!rawType.equals(otherSig.rawType)) return false;
        if (typeArguments.length != otherSig.typeArguments.length) return false;

        for (int i = 0; i < typeArguments.length; i++) {
            if (!typeArguments[i].equals(otherSig.typeArguments[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TypeSignature<?> other)) return false;
        return Objects.equals(rawType, other.rawType) && Arrays.equals(typeArguments, other.typeArguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawType, Arrays.hashCode(typeArguments));
    }

}