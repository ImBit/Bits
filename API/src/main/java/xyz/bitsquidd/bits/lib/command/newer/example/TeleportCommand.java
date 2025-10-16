package xyz.bitsquidd.bits.lib.command.newer.example;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newer.BitsAnnotatedCommand;
import xyz.bitsquidd.bits.lib.command.newer.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.annotation.*;
import xyz.bitsquidd.bits.lib.command.newer.requirement.impl.PlayerRequirement;


/**
 * Teleport command demonstrating new annotation structure.
 * <p>
 * Commands created:
 * /teleport <player> <entity>           - Direct teleport to entity
 * /teleport <player> location <x> <y> <z> [world] - Teleport to coordinates
 * /teleport <player> spawn              - Teleport to spawn
 * /teleport all <entity>                - Teleport all to entity
 * /teleport all location <x> <y> <z> [world] - Teleport all to coordinates
 * /teleport all spawn                   - Teleport all to spawn
 */
@Command(value = "teleport", aliases = {"tp"}, description = "Teleport commands")
@Permission({"bits.command.teleport"})
@Requirement({PlayerRequirement.class})
public class TeleportCommand extends BitsAnnotatedCommand {

    /**
     * Player-specific teleport commands.
     * Captures the target player and provides various teleport destinations.
     */
    @Command()
    public static class PlayerCommands extends BitsAnnotatedCommand {
        private final Player targetPlayer;

        public PlayerCommands(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }

        @Default
        public void showPlayerHelp(@NotNull BitsCommandContext context) {
            context.sendMessage("=== Teleport " + targetPlayer.getName() + " ===");
            context.sendMessage("Usage: /teleport " + targetPlayer.getName() + " <entity|location|spawn>");
        }

        // /teleport <player> <entity>
        @Command()
        public void teleportToEntity(@NotNull BitsCommandContext context, @NotNull Player entity) {
            if (targetPlayer.equals(entity)) {
                context.sendMessage("Cannot teleport " + targetPlayer.getName() + " to themselves!");
                return;
            }

            targetPlayer.teleport(entity.getLocation());
            context.sendMessage("Teleported " + targetPlayer.getName() + " to " + entity.getName());
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
            context.sendMessage("Teleported " + targetPlayer.getName() + " to " + x + ", " + y + ", " + z);
            targetPlayer.sendMessage("You have been teleported to " + x + ", " + y + ", " + z);
        }

        // /teleport <player> spawn
        @Command("spawn")
        public void teleportToSpawn(@NotNull BitsCommandContext context) {
            Location spawnLocation = targetPlayer.getWorld().getSpawnLocation();

            targetPlayer.teleport(spawnLocation);
            context.sendMessage("Teleported " + targetPlayer.getName() + " to spawn");
            targetPlayer.sendMessage("You have been teleported to spawn");
        }
    }

    /**
     * All-players teleport commands.
     */
    @Command("all")
    @Permission({"bits.command.teleport.all"})
    public static class AllPlayersCommands extends BitsAnnotatedCommand {

        @Default
        public void showAllHelp(@NotNull BitsCommandContext context) {
            context.sendMessage("=== Teleport All Players ===");
            context.sendMessage("/teleport all <entity> - Teleport all to entity");
            context.sendMessage("/teleport all location <x> <y> <z> [world] - Teleport all to coordinates");
            context.sendMessage("/teleport all spawn - Teleport all to spawn");
        }

        // /teleport all <entity>
        @Command("")
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

            context.sendMessage("Teleported " + count + " players to " + entity.getName());
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

            context.sendMessage("Teleported " + count + " players to " + x + ", " + y + ", " + z);
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

            context.sendMessage("Teleported " + count + " players to spawn");
        }
    }
}