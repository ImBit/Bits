package xyz.bitsquidd.bits.lib.command.examples;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;
import xyz.bitsquidd.bits.lib.command.CommandArgumentInfo;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.CommandPath;
import xyz.bitsquidd.bits.lib.command.annotations.CommandNew;
import xyz.bitsquidd.bits.lib.command.params.LocationArgument;
import xyz.bitsquidd.bits.lib.command.params.MultiPlayerArgument;
import xyz.bitsquidd.bits.lib.command.params.SinglePlayerArgument;
import xyz.bitsquidd.bits.lib.command.requirements.PermissionRequirement;
import xyz.bitsquidd.bits.lib.command.requirements.PlayerRequirement;

import java.util.Collection;
import java.util.List;

@CommandNew(name = "tpnew", aliases = {"tp"}, description = "Teleport players to a location", permission = "minecraft.command.teleport")
public class TeleportCommand extends AbstractCommand {

    @Override
    public void initialisePaths() {
        addPath(new CommandPath(
                "teleportToPlayer",
                "Teleport to a player",
                List.of(PlayerRequirement.INSTANCE),
                List.of(new CommandArgumentInfo("target", SinglePlayerArgument.INSTANCE)),
                this::teleportSelfToPlayer
        ));
        addPath(new CommandPath(
                "teleportToLocation",
                "Teleport to a Location",
                List.of(PlayerRequirement.INSTANCE),
                List.of(new CommandArgumentInfo("targetLocation", LocationArgument.INSTANCE)),
                this::teleportSelfToLocation
        ));
        addPath(new CommandPath(
                "teleportPlayerToLocation",
                "Teleport a player to a location",
                List.of(new PermissionRequirement("minecraft.command.teleport.others")),
                List.of(new CommandArgumentInfo("target", MultiPlayerArgument.INSTANCE), new CommandArgumentInfo("targetLocation", LocationArgument.INSTANCE)),
                this::teleportPlayerToLocation
        ));
        addPath(new CommandPath(
                "teleportPlayerToPlayer",
                "Teleport a player to a player",
                List.of(new PermissionRequirement("minecraft.command.teleport.others")),
                List.of(new CommandArgumentInfo("target", MultiPlayerArgument.INSTANCE), new CommandArgumentInfo("targetPlayer", SinglePlayerArgument.INSTANCE)),
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
        Collection<Player> targets = context.get("target");
        Location location = context.get("targetLocation");

        targets.forEach(p -> p.teleport(location));
        sender.sendMessage("&aTeleported to " + location);
    }

    private void teleportPlayerToPlayer(CommandContext context) {
        Player sender = (Player) context.sender;
        Collection<Player> targets = context.get("target");
        Player targetPlayer = context.get("targetPlayer");

        targets.forEach(p -> p.teleport(targetPlayer));
        sender.sendMessage("&aTeleported to " + targetPlayer);
    }
}
