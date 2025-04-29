package xyz.bitsquidd.bits.lib.command.params;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;
import xyz.bitsquidd.bits.lib.helper.EntityHelper;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayerArgument implements CommandArgument<Player> {
    public static final SinglePlayerArgument INSTANCE = new SinglePlayerArgument();

    @Override
    public Player parse(CommandContext context, int startIndex) throws ArgumentParseException {
        String arg = context.getArg(startIndex).toLowerCase();

        Player player = null;
        if (arg.startsWith("@")) {
            switch (arg) {
                case ("@s") -> player = context.sender instanceof Player ? (Player) context.sender : null;
                case ("@p") -> player = EntityHelper.getNearestEntity(context.getLocation(), Player.class, p -> !p.equals(context.sender));
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
    public boolean canParseArg(CommandContext context, int argIndex) {
        boolean isTabComplete = (context.getArgLength() == argIndex);
        String arg = context.getArg(argIndex);

        if (isTabComplete) {
            return true;
        }

        LogController.error(arg);

        return Bukkit.getPlayer(arg) != null;
    }

    //todo move to a general player class used for list players
    private boolean isObviouslyNotPlayerName(String input) {
        if (input.isEmpty()) {
            return true;
        }

        if (input.length() > 16) {
            return true;
        }

        boolean isNumeric = true;
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                isNumeric = false;
                break;
            }
        }

        if (isNumeric) {
            return true;
        }

        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandContext context, int startIndex) {
        List<String> completions = new ArrayList<>(Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(context.getLastArg()))
                .toList());

        completions.addAll(List.of("@s", "@p"));

        return completions;
    }

    @Override
    public String getTypeName() {
        return "player";
    }
}