package xyz.bitsquidd.bits.lib.command.argument.old.impl;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerCollectionArgumentParser extends AbstractArgumentParser<@NotNull String, @NotNull Collection<Player>> {
    private static final SimpleCommandExceptionType NO_PLAYERS_FOUND = new SimpleCommandExceptionType(new LiteralMessage("No players found matching the selector"));

    public PlayerCollectionArgumentParser() {
        super(TypeSignature.of(Collection.class, Player.class), StringArgumentType.string(), String.class, (Class<Collection<Player>>)(Class<?>)Collection.class, "Player(s)");
    }


    @Override
    public @NotNull Collection<Player> parse(@NotNull String input, @NotNull BitsCommandContext context) throws CommandSyntaxException {
        if (input.equals("@a")) {
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        } else if (input.equals("@s")) {
            try {
                return List.of(context.requirePlayer());
            } catch (IllegalStateException e) {
                throw NO_PLAYERS_FOUND.create();
            }
        } else {
            Player player = Bukkit.getPlayer(input);
            if (player == null) throw NO_PLAYERS_FOUND.create();
            return List.of(player);
        }
    }

    @Override
    protected List<String> getSuggestions(@NotNull BitsCommandContext context) {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("@a");
        try {
            context.requirePlayer();
            suggestions.add("@s");
        } catch (IllegalStateException ignored) {}
        suggestions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        return suggestions;
    }
}