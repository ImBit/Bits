package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;
import java.util.function.Supplier;

public final class WorldArgumentParser extends AbstractArgumentParser<@NotNull World> {

    public WorldArgumentParser() {
        super(TypeSignature.of(World.class), "World");
    }

    @Override
    public @NotNull World parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        World world = Bukkit.getWorld(inputString);
        if (world == null) throw new CommandParseException("World not found: " + inputString);
        return world;
    }

    @Override
    public @NotNull Supplier<List<String>> getSuggestions() {
        return () -> Bukkit.getWorlds().stream().map(World::getName).toList();
    }

}
