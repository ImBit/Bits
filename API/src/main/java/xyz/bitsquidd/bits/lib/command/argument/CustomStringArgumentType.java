package xyz.bitsquidd.bits.lib.command.argument;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;

public final class CustomStringArgumentType implements ArgumentType<String> {
    public CustomStringArgumentType() {}

    public static String getString(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    public Collection<String> getExamples() {
        return Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");
    }
}
