package xyz.bitsquidd.bits.lib.command.newe.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

// TODO test to see if it works, if it does, try implementing rotations.
public class LocationArgument implements CustomArgumentType<Location, FinePositionResolver> {

    private static final SimpleCommandExceptionType ERROR_INCOMPLETE_LOCATION = new SimpleCommandExceptionType(
          MessageComponentSerializer.message().serialize(Component.text("Location must include at least x, y, and z coordinates"))
    );

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_WORLD = new DynamicCommandExceptionType(worldName ->
          MessageComponentSerializer.message().serialize(Component.text("Unknown world: " + worldName))
    );

    @Override
    public Location parse(StringReader reader) throws CommandSyntaxException {
        int startPos = reader.getCursor();

        try {
            // Parse x, y, z
            double x = reader.readDouble();
            reader.expect(' ');
            double y = reader.readDouble();
            reader.expect(' ');
            double z = reader.readDouble();

            float yaw = 0.0f;
            float pitch = 0.0f;
            String worldName = null;

            // Check if there are more arguments
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip(); // Skip the space

                // Try to parse world name or yaw
                if (Character.isLetter(reader.peek()) || reader.peek() == '_') {
                    // This is likely a world name
                    int worldNameStart = reader.getCursor();
                    while (reader.canRead() && (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')) {
                        reader.skip();
                    }

                    worldName = reader.getString().substring(worldNameStart, reader.getCursor());

                    // Try to parse yaw and pitch if there are more arguments
                    if (reader.canRead() && reader.peek() == ' ') {
                        reader.skip(); // Skip the space
                        yaw = reader.readFloat();

                        if (reader.canRead() && reader.peek() == ' ') {
                            reader.skip(); // Skip the space
                            pitch = reader.readFloat();
                        }
                    }
                } else {
                    // This is likely yaw
                    yaw = reader.readFloat();

                    // Try to parse pitch
                    if (reader.canRead() && reader.peek() == ' ') {
                        reader.skip(); // Skip the space
                        pitch = reader.readFloat();

                        // Try to parse world name if there are more arguments
                        if (reader.canRead() && reader.peek() == ' ') {
                            reader.skip(); // Skip the space

                            int worldNameStart = reader.getCursor();
                            while (reader.canRead() && (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')) {
                                reader.skip();
                            }

                            worldName = reader.getString().substring(worldNameStart, reader.getCursor());
                        }
                    }
                }
            }

            // Get the world or use the default world
            World world = worldName != null ? Bukkit.getWorld(worldName) : Bukkit.getWorlds().get(0);
            if (world == null && worldName != null) {
                throw ERROR_UNKNOWN_WORLD.create(worldName);
            }

            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            // Reset the reader position and throw the exception
            reader.setCursor(startPos);
            throw ERROR_INCOMPLETE_LOCATION.create();
        }
    }

    @Override
    public <S> Location parse(StringReader reader, S source) throws CommandSyntaxException {
        // Store the starting position in case we need to rewind
        int startPos = reader.getCursor();

        try {
            // Parse x, y, z
            double x = reader.readDouble();
            reader.expect(' ');
            double y = reader.readDouble();
            reader.expect(' ');
            double z = reader.readDouble();

            float yaw = 0.0f;
            float pitch = 0.0f;
            String worldName = null;

            // Check if there are more arguments
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip(); // Skip the space

                // Try to parse world name or yaw
                if (Character.isLetter(reader.peek()) || reader.peek() == '_') {
                    // This is likely a world name
                    int worldNameStart = reader.getCursor();
                    while (reader.canRead() && (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')) {
                        reader.skip();
                    }

                    worldName = reader.getString().substring(worldNameStart, reader.getCursor());

                    // Try to parse yaw and pitch if there are more arguments
                    if (reader.canRead() && reader.peek() == ' ') {
                        reader.skip(); // Skip the space
                        yaw = reader.readFloat();

                        if (reader.canRead() && reader.peek() == ' ') {
                            reader.skip(); // Skip the space
                            pitch = reader.readFloat();
                        }
                    }
                } else {
                    // This is likely yaw
                    yaw = reader.readFloat();

                    // Try to parse pitch
                    if (reader.canRead() && reader.peek() == ' ') {
                        reader.skip(); // Skip the space
                        pitch = reader.readFloat();

                        // Try to parse world name if there are more arguments
                        if (reader.canRead() && reader.peek() == ' ') {
                            reader.skip(); // Skip the space

                            int worldNameStart = reader.getCursor();
                            while (reader.canRead() && (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')) {
                                reader.skip();
                            }

                            worldName = reader.getString().substring(worldNameStart, reader.getCursor());
                        }
                    }
                }
            }

            // Get the world based on context or use default
            World world;
            if (worldName != null) {
                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    throw ERROR_UNKNOWN_WORLD.create(worldName);
                }
            } else if (source instanceof CommandSourceStack sourceStack) {
                world = sourceStack.getLocation().getWorld();
            } else {
                world = Bukkit.getWorlds().get(0);
            }

            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            // Reset the reader position and throw the exception
            reader.setCursor(startPos);
            throw ERROR_INCOMPLETE_LOCATION.create();
        }
    }

    @Override
    public ArgumentType<FinePositionResolver> getNativeType() {
        return ArgumentTypes.finePosition();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining().toLowerCase();
        String[] parts = remaining.split(" ", -1);

        // Suggest based on how many parts are already entered
        switch (parts.length) {
            case 1:
                // Only X entered or partial X, suggest current coordinates
                if (context.getSource() instanceof CommandSourceStack sourceStack) {
                    Location loc = sourceStack.getLocation();
                    if (parts[0].isEmpty()) {
                        builder.suggest(String.format("%.1f %.1f %.1f", loc.getX(), loc.getY(), loc.getZ()));
                    }
                }
                break;
            case 4:
                // X Y Z entered, and possibly start of world name
                List<String> worldNames = Bukkit.getWorlds().stream()
                      .map(World::getName)
                      .filter(name -> name.toLowerCase().startsWith(parts[3].toLowerCase()))
                      .collect(Collectors.toList());

                for (String worldName : worldNames) {
                    builder.suggest(parts[0] + " " + parts[1] + " " + parts[2] + " " + worldName);
                }
                break;
        }

        return builder.buildFuture();
    }
}