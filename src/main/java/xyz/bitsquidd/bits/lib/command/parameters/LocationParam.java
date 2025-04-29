package xyz.bitsquidd.bits.lib.command.parameters;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exception.ParamParseException;

import java.util.Arrays;
import java.util.List;

public class LocationParam implements CommandParam<Location> {
    public static final LocationParam INSTANCE = new LocationParam();

    @Override
    public Location parse(CommandContext context, String arg) throws ParamParseException {
        try {
            String[] args = context.getArgs();
            int currentIndex = context.getCurrentArgIndex();

            if (currentIndex + 2 >= args.length) {
                throw new ParamParseException("Not enough arguments for location coordinates (need x y z)");
            }

            String xArg = args[currentIndex];
            String yArg = args[currentIndex + 1];
            String zArg = args[currentIndex + 2];

            double x = parseCoordinateValue(xArg, context, 0);
            double y = parseCoordinateValue(yArg, context, 1);
            double z = parseCoordinateValue(zArg, context, 2);

            Player player = context.getPlayer();
            World world = player != null ? player.getWorld() : Bukkit.getWorlds().getFirst();

            return new Location(world, x, y, z);

        } catch (NumberFormatException e) {
            throw new ParamParseException("Invalid coordinates. Expected numbers for x y z");
        } catch (IndexOutOfBoundsException e) {
            throw new ParamParseException("Not enough arguments for location coordinates");
        } catch (Exception e) {
            throw new ParamParseException("Error parsing location: " + e.getMessage());
        }
    }

    private double parseCoordinateValue(String coord, CommandContext context, int coordIndex) throws ParamParseException {
        if (coord.startsWith("~")) {
            if (!(context.getSender() instanceof Player player)) {
                throw new ParamParseException("Only players can use relative coordinates");
            }

            Location playerLoc = player.getLocation();
            double currentCoord;

            switch (coordIndex) {
                case 0:
                    currentCoord = playerLoc.getX();
                    break;
                case 1:
                    currentCoord = playerLoc.getY();
                    break;
                case 2:
                    currentCoord = playerLoc.getZ();
                    break;
                default:
                    throw new ParamParseException("Invalid coordinate index: " + coordIndex);
            }

            if (coord.equals("~")) {
                return currentCoord;
            } else {
                try {
                    double offset = Double.parseDouble(coord.substring(1));
                    return currentCoord + offset;
                } catch (NumberFormatException e) {
                    throw new ParamParseException("Invalid relative coordinate: " + coord);
                }
            }
        } else {
            try {
                return Double.parseDouble(coord);
            } catch (NumberFormatException e) {
                throw new ParamParseException("Invalid coordinate: " + coord + ". Expected a number");
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandContext context, String current) {
        if (!(context.getSender() instanceof Player player)) {
            return List.of("0");
        }

        Location loc = player.getLocation();
        int argIndex = context.getCurrentArgIndex();
        String[] args = context.getArgs();
        int relativeArgPos = args.length - argIndex - 1;

        return switch (relativeArgPos % 3) {
            case 0 -> // (X)
                    Arrays.asList(
                            String.format("%.1f", loc.getX()),
                            "~"
                    );
            case 1 -> // (Y)
                    Arrays.asList(
                            String.format("%.1f", loc.getY()),
                            "~"
                    );
            case 2 -> // (Z)
                    Arrays.asList(
                            String.format("%.1f", loc.getZ()),
                            "~"
                    );
            default -> List.of("~", "0");
        };
    }

    @Override
    public String getTypeName() {
        return "location (x y z)";
    }

    @Override
    public int getExpectedArgCount() {
        return 3;
    }
}