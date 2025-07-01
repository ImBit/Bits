package xyz.bitsquidd.bits.lib.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.arguments.interfaces.CommandArgument;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.List;
import java.util.stream.Collectors;

public class WorldArgument extends CommandArgument<World> {
    public static final WorldArgument INSTANCE = new WorldArgument();

    @Override
    public @NotNull String getTypeName() {
        return "World";
    }

    @Override
    public World parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.getArgsLength()) {
            throw new ArgumentParseException("No world argument provided");
        }

        try {
            return Bukkit.getWorld(context.getArgs()[startIndex]);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("Cannot parse '" + context.getArgs()[startIndex] + "' as a world.");
        }
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        String input = context.getArg(argIndex);

        return Bukkit.getWorlds().stream().anyMatch(world -> world.getName().equalsIgnoreCase(input));
    }

    @Override
    public @NotNull List<String> tabComplete(CommandContext context, int startIndex) {
        return Bukkit.getWorlds().stream()
                     .map(World::getName)
                     .collect(Collectors.toList());
    }
}