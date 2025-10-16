package xyz.bitsquidd.bits.lib.command.newer.arg.parser.impl;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;

import java.util.List;

public class PlayerArgumentParser extends AbstractArgumentParser<@NotNull String, @NotNull Player> {
    private static final SimpleCommandExceptionType PLAYER_NOT_FOUND = new SimpleCommandExceptionType(new LiteralMessage("Player not found"));

    // Note: Not dealing with brigadier's silly complex parsers yet.
    public PlayerArgumentParser() {
        super(TypeSignature.of(Player.class), StringArgumentType.string(), String.class, Player.class);
    }

    @Override
    public @NotNull Player parse(@NotNull String input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        Player player = Bukkit.getPlayer(input);

        if (player == null) throw PLAYER_NOT_FOUND.create();

        return player;
    }

    @Override
    protected @NotNull List<String> getSuggestions(@NotNull BitsCommandContext context) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
}