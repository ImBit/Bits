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

public class PlayerArgumentParser implements ArgumentParser<Player> {
    private static final SimpleCommandExceptionType PLAYER_NOT_FOUND = new SimpleCommandExceptionType(new LiteralMessage("Player not found"));

    @Override
    public @NotNull Player parse(@NotNull StringReader reader, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        String playerName = reader.readString();
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) throw PLAYER_NOT_FOUND.create();

        return player;
    }

    @Override
    public @NotNull Class<Player> getType() {
        return Player.class;
    }

}