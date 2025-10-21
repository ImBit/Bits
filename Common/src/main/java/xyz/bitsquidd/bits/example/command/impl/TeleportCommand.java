package xyz.bitsquidd.bits.example.command.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Optional;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.requirement.impl.PlayerSenderRequirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;


/**
 * Teleport command demonstrating new annotation structure.
 * <p>
 * Commands created:
 * <ul>
 * <li>/teleport [player] [entity]                - Direct teleport to entity </li>
 * <li>/teleport [player] location [x] [y] [z] [world] - Teleport to coordinates</li>
 * <li>/teleport [player] spawn                   - Teleport to spawn</li>
 * <li>/teleport all [entity]                     - Teleport all to entity</li>
 * <li>/teleport all location [x] [y] [z] [world] - Teleport all to coordinates</li>
 * <li>/teleport all spawn                        - Teleport all to spawn</li>
 * </ul>
 */
@Command(value = "test-teleport", aliases = {"test-teleport-alias"}, description = "Teleport commands")
@Requirement(value = {PlayerSenderRequirement.class})
public class TeleportCommand extends BitsCommand {

    @Command()
    public void teleportTest(@NotNull BitsCommandContext context) {
        context.respond(Text.of("Test teleport command!"));
    }


    /**
     * Player-specific teleport commands.
     * Captures the target player and provides various teleport destinations.
     */
    @Command()
    public static class PlayerCommands extends BitsCommand {
        private final Player targetPlayer;

        public PlayerCommands(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }

        public void showPlayerHelp(@NotNull BitsCommandContext context) {
            context.respond(Text.of("=== Teleport " + targetPlayer.getName() + " ==="));
            context.respond(Text.of("Usage: /teleport " + targetPlayer.getName() + " <entity|location|spawn>"));
        }

        // /teleport <player> <entity>
        @Command()
        public void teleportToEntity(@NotNull BitsCommandContext context, @NotNull Player entity) {
            if (targetPlayer.equals(entity)) {
                context.respond(Text.of("Cannot teleport " + targetPlayer.getName() + " to themselves!"));
                return;
            }

            targetPlayer.teleport(entity.getLocation());
            context.respond(Text.of("Teleported " + targetPlayer.getName() + " to " + entity.getName()));
            targetPlayer.sendMessage("You have been teleported to " + entity.getName());
        }

        // /teleport <player> location <x> <y> <z> [world]
        @Command("location")
        public void teleportToLocation(
              @NotNull BitsCommandContext context,
              double x, double y, double z,
              @Optional World world
        ) {
            World targetWorld = world != null ? world : targetPlayer.getWorld();
            Location location = new Location(targetWorld, x, y, z);

            targetPlayer.teleport(location);
            context.respond(Text.of("Teleported " + targetPlayer.getName() + " to " + x + ", " + y + ", " + z));
            targetPlayer.sendMessage("You have been teleported to " + x + ", " + y + ", " + z);
        }

        // /teleport <player> spawn
        @Command("spawn")
        public void teleportToSpawn(@NotNull BitsCommandContext context) {
            Location spawnLocation = targetPlayer.getWorld().getSpawnLocation();

            targetPlayer.teleport(spawnLocation);
            context.respond(Text.of("Teleported " + targetPlayer.getName() + " to spawn"));
            targetPlayer.sendMessage("You have been teleported to spawn");
        }
    }

    /**
     * All-players teleport commands.
     */
    @Command("all")
    public static class AllPlayersCommands extends BitsCommand {

        public void showAllHelp(@NotNull BitsCommandContext context) {
            context.respond(Text.of("=== Teleport All Players ==="));
            context.respond(Text.of("/teleport all <entity> - Teleport all to entity"));
            context.respond(Text.of("/teleport all location <x> <y> <z> [world] - Teleport all to coordinates"));
            context.respond(Text.of("/teleport all spawn - Teleport all to spawn"));
        }

        // /teleport all <entity>
        @Command()
        public void teleportAllToEntity(@NotNull BitsCommandContext context, @NotNull Player entity) {
            Location targetLocation = entity.getLocation();

            int count = 0;
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                if (!player.equals(entity)) {
                    player.teleport(targetLocation);
                    player.sendMessage("You have been teleported to " + entity.getName());
                    count++;
                }
            }

            context.respond(Text.of("Teleported " + count + " players to " + entity.getName()));
        }

        // /teleport all location <x> <y> <z> [world]
        @Command("location")
        public void teleportAllToLocation(
              @NotNull BitsCommandContext context,
              double x, double y, double z,
              @Optional World world
        ) {
            World targetWorld = world != null ? world : context.requirePlayer().getWorld();
            Location location = new Location(targetWorld, x, y, z);

            int count = 0;
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                player.teleport(location);
                player.sendMessage("You have been teleported to " + x + ", " + y + ", " + z);
                count++;
            }

            context.respond(Text.of("Teleported " + count + " players to " + x + ", " + y + ", " + z));
        }

        // /teleport all spawn
        @Command("spawn")
        public void teleportAllToSpawn(@NotNull BitsCommandContext context) {
            Location spawnLocation = context.requirePlayer().getWorld().getSpawnLocation();

            int count = 0;
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                player.teleport(spawnLocation);
                player.sendMessage("You have been teleported to spawn");
                count++;
            }

            context.respond(Text.of("Teleported " + count + " players to spawn"));
        }
    }
}