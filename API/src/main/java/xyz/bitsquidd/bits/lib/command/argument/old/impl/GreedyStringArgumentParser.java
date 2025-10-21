package xyz.bitsquidd.bits.lib.command.argument.old.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.type.GreedyString;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public class GreedyStringArgumentParser extends AbstractArgumentParser<@NotNull String, @NotNull GreedyString> {
    public GreedyStringArgumentParser() {
        super(TypeSignature.of(GreedyString.class), StringArgumentType.greedyString(), String.class, GreedyString.class, "Greedy String");
    }

    @Override
    public @NotNull GreedyString parse(@NotNull String input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        return new GreedyString(input);
    }

    @Override
    protected @NotNull List<String> getSuggestions(@NotNull BitsCommandContext context) {
        return Bukkit.getWorlds().stream().map(World::getName).toList();
    }

}