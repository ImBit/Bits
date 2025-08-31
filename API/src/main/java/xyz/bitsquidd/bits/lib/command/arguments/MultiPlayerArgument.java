package xyz.bitsquidd.bits.lib.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.AbstractPlayerArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;
import xyz.bitsquidd.bits.lib.entity.EntityHelper;

import java.util.*;

public class MultiPlayerArgument extends AbstractPlayerArgument<Collection<Player>> {
    public static final MultiPlayerArgument INSTANCE = new MultiPlayerArgument();
    private static final List<String> SUPPORTED_SELECTORS = Arrays.asList("@a", "@s", "@p");

    @Override
    public Collection<Player> parse(CommandContext context, int startIndex) throws ArgumentParseException {
        String arg = context.getArg(startIndex).toLowerCase();

        if (arg.startsWith("@")) {
            switch (arg) {
                case "@a":
                    return new ArrayList<>(Bukkit.getOnlinePlayers());
                case "@s":
                    if (context.getSender() instanceof Player player) {
                        return Collections.singletonList(player);
                    }
                    throw new ArgumentParseException("Selector @s can only be used by players");
                case "@p":
                    Player nearest = EntityHelper.getNearestEntity(context.getLocation(), Player.class, p -> !p.equals(context.getSender()));

                    if (nearest != null) {
                        return Collections.singletonList(nearest);
                    }
                    throw new ArgumentParseException("No players found");
                default:
                    throw new ArgumentParseException("Unsupported player selector: " + arg);
            }
        } else {
            Player player = Bukkit.getPlayer(arg);
            if (player != null) {
                return Collections.singletonList(player);
            }
            throw new ArgumentParseException("Player not found: " + arg);
        }
    }

    @Override
    public @NotNull List<String> tabComplete(CommandContext context, int startIndex) {
        List<String> completions = new ArrayList<>(getPlayerNameCompletions());
        completions.addAll(SUPPORTED_SELECTORS);

        return completions;
    }

    @Override
    public String getTypeName() {
        return "Player(s)";
    }

    @Override
    protected List<String> getSupportedSelectors() {
        return SUPPORTED_SELECTORS;
    }
}