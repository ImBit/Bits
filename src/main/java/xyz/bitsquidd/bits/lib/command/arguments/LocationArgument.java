package xyz.bitsquidd.bits.lib.command.arguments;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.ArrayList;
import java.util.List;

public class LocationArgument extends CommandArgument<Location> {
    public static final LocationArgument INSTANCE = new LocationArgument();

    @Override
    public String getTypeName() {
        return "location";
    }

    @Override
    public int getRequiredArgs() {
        return 3;
    }

    @Override
    public Location parse(CommandContext context, int startIndex) throws ArgumentParseException {
        try {
            Location base = context.getLocation();

            double x = parseCoordinate(context.getArgs()[startIndex], base != null ? base.getX() : 0);
            double y = parseCoordinate(context.getArgs()[startIndex + 1], base != null ? base.getY() : 0);
            double z = parseCoordinate(context.getArgs()[startIndex + 2], base != null ? base.getZ() : 0);

            return new Location(context.getWorld(), x, y, z);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Invalid coordinate format: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArgumentParseException("Not enough coordinates provided");
        }
    }

    @Override
    public boolean canParseArg(CommandContext context, int argIndex) {
        if (argIndex >= context.getArgsLength()) {
            return false;
        }

        try {
            parseCoordinate(context.getArgs()[argIndex], 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(CommandContext context, int startIndex) {
        int relativeArgIndex = context.getArgLength() - 1 - startIndex;

        return suggestCoordinate(context.getLastArg(), relativeArgIndex);
    }

    private double parseCoordinate(String input, double base) {
        if (input.startsWith("~")) {
            if (input.length() == 1) {
                return base;
            }
            return base + Double.parseDouble(input.substring(1));
        }
        return Double.parseDouble(input);
    }

    private List<String> suggestCoordinate(String prefix, double baseValue) {
        if (prefix.startsWith("~")) {
            return List.of(prefix);
        }

        List<String> suggestions = new ArrayList<>(List.of(prefix));

        if (baseValue <= 0) {
            suggestions.add("~ ~ ~");
        }
        if (baseValue <= 1) {
            suggestions.add("~ ~");
        }
        if (baseValue <= 2) {
            suggestions.add("~");
        }

        return suggestions;
    }
}