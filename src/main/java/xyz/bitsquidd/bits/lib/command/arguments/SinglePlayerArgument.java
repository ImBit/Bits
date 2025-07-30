package xyz.bitsquidd.bits.lib.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.AbstractPlayerArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;
import xyz.bitsquidd.bits.lib.entity.EntityHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: have a way to disable @ args. not all commands should have this - e.g. /msg
public class SinglePlayerArgument extends AbstractPlayerArgument<Player> {
    public static final SinglePlayerArgument INSTANCE = new SinglePlayerArgument();
    private static final List<String> SUPPORTED_SELECTORS = Arrays.asList("@s", "@p");

    @Override
    public Player parse(CommandContext context, int startIndex) throws ArgumentParseException {
        String arg = context.getArg(startIndex).toLowerCase();

        Player player = null;
        if (arg.startsWith("@")) {
            switch (arg) {
                case "@s" -> player = context.getSender() instanceof Player ? (Player)context.getSender() : null;
                case "@p" -> player = EntityHelper.getNearestEntity(context.getLocation(), Player.class, p -> !p.equals(context.getSender()));
                default -> throw new ArgumentParseException("Unsupported player selector: " + arg);
            }
        } else {
            player = Bukkit.getPlayer(arg);
        }

        if (player == null) {
            throw new ArgumentParseException("Player not found: " + arg);
        }
        return player;
    }

    @Override
    public @NotNull List<String> tabComplete(CommandContext context, int startIndex) {
        List<String> completions = new ArrayList<>(getPlayerNameCompletions());
        completions.addAll(SUPPORTED_SELECTORS);

        return completions;
    }

    @Override
    public String getTypeName() {
        return "Player";
    }

    @Override
    protected List<String> getSupportedSelectors() {
        return SUPPORTED_SELECTORS;
    }
}