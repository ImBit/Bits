package xyz.bitsquidd.bits.lib.command.newer.arg;

import com.mojang.brigadier.arguments.*;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.arg.parser.*;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Type signature is the expected return type of the command.
 * ArgumentType is the native type of the argument.
 */
public class ArgumentTypeRegistry {
    private final Map<TypeSignature, ArgumentType<?>> typeMap = new ConcurrentHashMap<>();
    private final Map<TypeSignature, ArgumentParser<?>> parserMap = new ConcurrentHashMap<>();

    public ArgumentTypeRegistry() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(TypeSignature.of(String.class), StringArgumentType.string(), new StringArgumentParser());
        register(TypeSignature.of(int.class), IntegerArgumentType.integer(), new IntegerArgumentParser());
        register(TypeSignature.of(Integer.class), IntegerArgumentType.integer(), new IntegerArgumentParser());
        register(TypeSignature.of(double.class), DoubleArgumentType.doubleArg(), new DoubleArgumentParser());
        register(TypeSignature.of(Double.class), DoubleArgumentType.doubleArg(), new DoubleArgumentParser());
        register(TypeSignature.of(float.class), FloatArgumentType.floatArg(), new FloatArgumentParser());
        register(TypeSignature.of(Float.class), FloatArgumentType.floatArg(), new FloatArgumentParser());
        register(TypeSignature.of(long.class), LongArgumentType.longArg(), new LongArgumentParser());
        register(TypeSignature.of(Long.class), LongArgumentType.longArg(), new LongArgumentParser());
        register(TypeSignature.of(boolean.class), BoolArgumentType.bool(), new BooleanArgumentParser());
        register(TypeSignature.of(Boolean.class), BoolArgumentType.bool(), new BooleanArgumentParser());

        register(TypeSignature.of(Player.class), ArgumentTypes.player(), new PlayerArgumentParser());
        register(TypeSignature.of(Collection.class, Player.class), ArgumentTypes.players(), new PlayerCollectionArgumentParser());
        register(TypeSignature.of(World.class), ArgumentTypes.world(), new WorldArgumentParser());
    }

    public <T> void register(@NotNull TypeSignature signature, @NotNull ArgumentType<?> argumentType, @NotNull ArgumentParser<T> parser) {
        typeMap.put(signature, argumentType);
        parserMap.put(signature, parser);
    }

    public @Nullable ArgumentType<?> getArgumentType(@NotNull Type type) {
        TypeSignature signature = TypeSignature.of(type);
        return typeMap.get(signature);
    }

}