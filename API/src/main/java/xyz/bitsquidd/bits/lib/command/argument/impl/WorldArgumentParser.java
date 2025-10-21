package xyz.bitsquidd.bits.lib.command.argument.impl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public final class WorldArgumentParser extends AbstractArgumentParserNew<@NotNull World> {

    public WorldArgumentParser() {
        super(TypeSignature.of(World.class), "World");
    }

    @Override
    public @NotNull World parse(@NotNull List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        World world = Bukkit.getWorld(inputString);
        if (world == null) throw new CommandParseException("World not found: " + inputString);
        return world;
    }

}
