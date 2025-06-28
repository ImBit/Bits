package xyz.bitsquidd.bits.lib.command.arguments.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContext;

import java.util.List;

public abstract class AbstractPlayerArgument<T> extends CommandArgument<T> {

    protected boolean isObviouslyNotPlayerName(String input) {
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

    protected List<String> getPlayerNameCompletions() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }

    @Override
    public boolean canParseArg(CommandContext context, int argIndex) {
        String arg = context.getArg(argIndex);
        if (arg.startsWith("@")) {
            return true; //getSupportedSelectors().contains(arg.toLowerCase())
        }
        
        return !isObviouslyNotPlayerName(arg);
    }

    protected abstract List<String> getSupportedSelectors();
}