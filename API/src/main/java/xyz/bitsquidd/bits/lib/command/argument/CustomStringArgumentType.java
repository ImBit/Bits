package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

public final class CustomStringArgumentType implements CustomArgumentType<@NotNull String, @NotNull String> {
    public CustomStringArgumentType() {}

    public @NotNull String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

}
