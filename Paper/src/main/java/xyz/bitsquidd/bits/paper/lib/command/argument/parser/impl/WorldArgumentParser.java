package xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.World;

import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.ExceptionBuilder;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrapper.type.TypeSignature;

import java.util.List;
import java.util.function.Supplier;

public final class WorldArgumentParser extends AbstractArgumentParser<World> {

    public WorldArgumentParser() {
        super(TypeSignature.of(World.class), "World");
    }

    @Override
    public World parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandSyntaxException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        World world = Bukkit.getWorld(inputString);
        if (world == null) throw ExceptionBuilder.createCommandException("World not found: " + inputString + ".");
        return world;
    }

    @Override
    public Supplier<List<String>> getSuggestions() {
        return () -> Bukkit.getWorlds().stream().map(World::getName).toList();
    }

}
