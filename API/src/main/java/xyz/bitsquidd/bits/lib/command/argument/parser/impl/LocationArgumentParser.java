package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.InputTypeContainer;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;

import java.util.List;

public final class LocationArgumentParser extends AbstractArgumentParser<Location> {

    public LocationArgumentParser() {
        super(TypeSignature.of(Location.class), "Location");
    }

    @Override
    public Location parse(List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException {
        List<Object> inputs = inputValidation(inputObjects);
        double x = (double)inputs.get(0);
        double y = (double)inputs.get(1);
        double z = (double)inputs.get(2);
        World world = (World)inputs.get(3);

        return new Location(world, x, y, z);
    }

    @Override
    public List<InputTypeContainer> getInputTypes() {
        return List.of(
              new InputTypeContainer(TypeSignature.of(Double.class), "x"),
              new InputTypeContainer(TypeSignature.of(Double.class), "y"),
              new InputTypeContainer(TypeSignature.of(Double.class), "z"),
              new InputTypeContainer(TypeSignature.of(World.class), "world")
        );
    }

}
