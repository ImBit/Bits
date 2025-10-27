package xyz.bitsquidd.bits.lib.command.argument.parser.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public final class PlayerSingleArgumentParser extends AbstractArgumentParserNew<@NotNull Player> {

    public PlayerSingleArgumentParser() {
        super(TypeSignature.of(Player.class), "Player");
    }

    @Override
    public @NotNull Player parse(@NotNull List<Object> inputObjects, @NotNull BitsCommandContext ctx) throws CommandParseException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        Player player = Bukkit.getPlayer(inputString);
        if (player == null) throw new CommandParseException("Player not found: " + inputString);
        return player;
    }

    @Override
    public @NotNull List<String> getSuggestions() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

}
