package xyz.bitsquidd.bits.lib.command.examples;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.CommandArgumentInfo;
import xyz.bitsquidd.bits.lib.command.CommandPathNew;
import xyz.bitsquidd.bits.lib.command.annotations.CommandNew;
import xyz.bitsquidd.bits.lib.command.params.LocationArgument;
import xyz.bitsquidd.bits.lib.command.params.SinglePlayerArgument;

import java.util.List;

//TODO:
// Add requirements to paths - e.g. player != null etc.
// Add permissions to paths - e.g. teleport self teleport others etc.
@CommandNew(name = "tpnew29", aliases = {"tp"}, description = "Teleport players to a location", permission = "minecraft.command.teleport")
public class TeleportCommand extends AbstractCommandNew {

    @Override
    public void initialisePaths() {
        addPath(new CommandPathNew(
                "teleportToPlayer",
                "Teleport to a player",
                "minecraft.command.teleport",
                List.of(new CommandArgumentInfo("target", SinglePlayerArgument.INSTANCE)),
                this::teleportSelfToPlayer
        ));
        addPath(new CommandPathNew(
                "teleportToLocation",
                "Teleport to a Location",
                "minecraft.command.teleport",
                List.of(new CommandArgumentInfo("targetLocation", LocationArgument.INSTANCE)),
                this::teleportSelfToLocation
        ));
        addPath(new CommandPathNew(
                "teleportPlayerToLocation",
                "Teleport a player to a location",
                "minecraft.command.teleport",
                List.of(new CommandArgumentInfo("target", SinglePlayerArgument.INSTANCE), new CommandArgumentInfo("targetLocation", LocationArgument.INSTANCE)),
                this::teleportPlayerToLocation
        ));
        addPath(new CommandPathNew(
                "teleportPlayerToPlayer",
                "Teleport a player to a player",
                "minecraft.command.teleport",
                List.of(new CommandArgumentInfo("target", SinglePlayerArgument.INSTANCE), new CommandArgumentInfo("targetPlayer", SinglePlayerArgument.INSTANCE)),
                this::teleportPlayerToPlayer
        ));
    }




    private void teleportSelfToPlayer(CommandContext context) {
        Player sender = (Player) context.sender;
        Player target = context.get("target");

        sender.teleport(target);
        sender.sendMessage("&aTeleported to " + target);
    }

    private void teleportSelfToLocation(CommandContext context) {
        Player sender = (Player) context.sender;
        Location target = context.get("targetLocation");

        sender.teleport(target);
        sender.sendMessage("&aTeleported to " + target);
    }

    private void teleportPlayerToLocation(CommandContext context) {
        Player sender = (Player) context.sender;
        Player target = context.get("target");
        Location location = context.get("targetLocation");

        target.teleport(location);
        sender.sendMessage("&aTeleported to " + location);
    }

    private void teleportPlayerToPlayer(CommandContext context) {
        Player sender = (Player) context.sender;
        Player target = context.get("target");
        Player targetPlayer = context.get("targetPlayer");

        target.teleport(targetPlayer);
        sender.sendMessage("&aTeleported to " + targetPlayer);
    }
}
