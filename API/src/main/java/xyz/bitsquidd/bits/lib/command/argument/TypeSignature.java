package xyz.bitsquidd.bits.lib.command.argument;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;


public class TypeSignature {
    private final Class<?> rawType;
    private final Type[] typeArguments;

    private TypeSignature(Class<?> rawType, Type[] typeArguments) {
        this.rawType = rawType;
        this.typeArguments = typeArguments != null ? typeArguments.clone() : new Type[0];
    }

    public static TypeSignature of(@NotNull Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Class<?> rawType = (Class<?>)parameterizedType.getRawType();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            return new TypeSignature(rawType, typeArgs);
        } else if (type instanceof Class<?> clazz) {
            return new TypeSignature(clazz, null);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public static TypeSignature of(@NotNull Class<?> clazz) {
        return new TypeSignature(clazz, null);
    }

    public static TypeSignature of(@NotNull Class<?> rawType, @NotNull Class<?>... typeArguments) {
        return new TypeSignature(rawType, typeArguments);
    }

    public Class<?> toRawType() {
        return rawType;
    }

    public boolean matches(@NotNull Type other) {
        TypeSignature otherSig = TypeSignature.of(other);
        if (!rawType.equals(otherSig.rawType)) {
            return false;
        }
        if (typeArguments.length != otherSig.typeArguments.length) {
            return false;
        }
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
        if (!(obj instanceof TypeSignature other)) return false;
        return Objects.equals(rawType, other.rawType) && Arrays.equals(typeArguments, other.typeArguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawType, Arrays.hashCode(typeArguments));
    }

    @Override
    public String toString() {
        if (typeArguments.length == 0) {
            return rawType.getSimpleName();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(rawType.getSimpleName()).append("<");
        for (int i = 0; i < typeArguments.length; i++) {
            if (i > 0) sb.append(", ");
            if (typeArguments[i] instanceof Class<?> clazz) {
                sb.append(clazz.getSimpleName());
            } else {
                sb.append(typeArguments[i].toString());
            }
        }
        sb.append(">");
        return sb.toString();
    }
}