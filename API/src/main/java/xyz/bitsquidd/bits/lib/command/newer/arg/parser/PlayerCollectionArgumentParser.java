package xyz.bitsquidd.bits.lib.command.newer.arg.parser;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.arg.ArgumentParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerCollectionArgumentParser implements ArgumentParser<Collection<Player>> {
    private static final SimpleCommandExceptionType NO_PLAYERS_FOUND = new SimpleCommandExceptionType(new LiteralMessage("No players found matching the selector"));

    @Override
    public @NotNull Collection<Player> parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        String selector = reader.readString();

        if (selector.equals("@a")) {
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        } else if (selector.equals("@s")) {
            try {
                return List.of(context.requirePlayer());
            } catch (IllegalStateException e) {
                throw NO_PLAYERS_FOUND.create();
            }
        } else {
            Player player = Bukkit.getPlayer(selector);
            if (player == null) throw NO_PLAYERS_FOUND.create();
            return List.of(player);
        }
    }

    @Override
    public @NotNull Class<Collection<Player>> getType() {
        return (Class<Collection<Player>>)(Class<?>)Collection.class;
    }

}