package xyz.bitsquidd.bits.lib.command.arg.impl;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.arg.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.info.BitsCommandContext;

import java.util.List;

public class WorldArgumentParser extends AbstractArgumentParser<@NotNull String, @NotNull World> {
    private static final SimpleCommandExceptionType WORLD_NOT_FOUND = new SimpleCommandExceptionType(new LiteralMessage("World not found"));

    public WorldArgumentParser() {
        super(TypeSignature.of(World.class), StringArgumentType.string(), String.class, World.class);
    }

    @Override
    public @NotNull World parse(@NotNull String input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        World world = Bukkit.getWorld(input);

        if (world == null) throw WORLD_NOT_FOUND.create();

        return world;
    }

    @Override
    protected @NotNull List<String> getSuggestions(@NotNull BitsCommandContext context) {
        return Bukkit.getWorlds().stream().map(World::getName).toList();
    }

}