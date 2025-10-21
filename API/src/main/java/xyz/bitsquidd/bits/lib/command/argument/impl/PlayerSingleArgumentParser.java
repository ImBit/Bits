package xyz.bitsquidd.bits.lib.command.argument.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParserNew;
import xyz.bitsquidd.bits.lib.command.exception.CommandParseException;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public class PlayerSingleArgumentParser extends AbstractArgumentParserNew<@NotNull Player> {

    protected PlayerSingleArgumentParser() {
        super(TypeSignature.of(Player.class), "Player");
    }

    @Override
    public @NotNull Player parse(@NotNull List<Object> inputObjects, BitsCommandContext ctx) throws CommandParseException {
        String inputString = singletonInputValidation(inputObjects, String.class);

        Player player = Bukkit.getPlayer(inputString);
        if (player == null) throw new CommandParseException("Player not found: " + inputString);
        return player;
    }

    @Override
    public @NotNull List<TypeSignature<?>> getInputTypes() {
        return List.of(TypeSignature.of(String.class));
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull BitsCommandContext ctx) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

}
