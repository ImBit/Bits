package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

public class WorldArgumentParser implements ArgumentParser<World> {
    private static final SimpleCommandExceptionType WORLD_NOT_FOUND = new SimpleCommandExceptionType(new LiteralMessage("World not found"));

    @Override
    public @NotNull World parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        String worldName = reader.readString();
        World world = Bukkit.getWorld(worldName);

        if (world == null) throw WORLD_NOT_FOUND.create();

        return world;
    }

    @Override
    public @NotNull Class<World> getType() {
        return World.class;
    }

}