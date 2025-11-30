package xyz.bitsquidd.bits.paper.example.command.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.annotation.Requirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.helper.Formatter;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.paper.libs.command.requirement.PlayerSenderRequirement;


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
    public void teleportTest(BitsCommandContext ctx) {
        ctx.respond(Text.of("Test teleport command!"));
    }


    /**
     * Player-specific teleport commands.
     */
    @Command()
    public static class PlayerCommands extends BitsCommand {
        private final Player targetPlayer;

        public PlayerCommands(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }

        @Requirement(PlayerSenderRequirement.class)
        @Command()
        public void teleportToEntity(BitsCommandContext ctx) {
            Player senderPlayer = ctx.requirePlayer();

            if (targetPlayer.equals(senderPlayer)) {
                ctx.respond(Text.of("Cannot teleport " + targetPlayer.getName() + " to themselves!"));
                return;
            }

            senderPlayer.teleport(targetPlayer.getLocation());
            ctx.respond(Text.of("Teleported " + senderPlayer.getName() + " to " + targetPlayer.getName()));
        }

        // /teleport <player> <entity>
        @Command()
        public void teleportToEntity(BitsCommandContext ctx, Player entity) {
            if (targetPlayer.equals(entity)) {
                ctx.respond(Text.of("Cannot teleport " + targetPlayer.getName() + " to themselves!"));
                return;
            }

            targetPlayer.teleport(entity.getLocation());
            ctx.respond(Text.of("Teleported " + targetPlayer.getName() + " to " + entity.getName()));
        }

        // /teleport <player> <x> <y> <z> [world]
        @Command()
        public void teleportToLocation(BitsCommandContext ctx, Location location) {
            targetPlayer.teleport(location);
            ctx.respond(Text.of("Teleported " + targetPlayer.getName() + " to " + Formatter.format(location)));
            targetPlayer.sendMessage("You have been teleported to " + Formatter.format(location));
        }

        //teleport <player> spawn
        @Command("spawn")
        public void teleportToSpawn(BitsCommandContext ctx) {
            Location spawnLocation = targetPlayer.getWorld().getSpawnLocation();

            targetPlayer.teleport(spawnLocation);
            ctx.respond(Text.of("Teleported " + targetPlayer.getName() + " to spawn"));
        }
    }

    /**
     * All-players teleport commands.
     */
    @Command("all")
    public static class AllPlayersCommands extends BitsCommand {
        // /teleport all <entity>
        @Command()
        public void teleportAllToEntity(BitsCommandContext ctx, Player entity) {
            Location targetLocation = entity.getLocation();

            int count = 0;
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                if (!player.equals(entity)) {
                    player.teleport(targetLocation);
                    player.sendMessage("You have been teleported to " + entity.getName());
                    count++;
                }
            }

            ctx.respond(Text.of("Teleported " + count + " players to " + entity.getName()));
        }

        // /teleport all <x> <y> <z> [world]
        @Command()
        public void teleportAllToLocation(BitsCommandContext ctx, Location location) {
            int count = 0;
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                player.teleport(location);
                player.sendMessage("You have been teleported to " + Formatter.format(location));
                count++;
            }

            ctx.respond(Text.of("Teleported " + count + " players to " + Formatter.format(location)));
        }

        //teleport all spawn
        @Command("spawn")
        public void teleportAllToSpawn(BitsCommandContext ctx) {
            Location spawnLocation = ctx.requirePlayer().getWorld().getSpawnLocation();

            int count = 0;
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                player.teleport(spawnLocation);
                player.sendMessage("You have been teleported to spawn");
                count++;
            }

            ctx.respond(Text.of("Teleported " + count + " players to spawn"));
        }
    }

}