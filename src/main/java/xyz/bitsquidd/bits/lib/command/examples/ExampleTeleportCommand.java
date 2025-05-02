package xyz.bitsquidd.bits.lib.command.examples;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.*;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.command.arguments.LocationArgument;
import xyz.bitsquidd.bits.lib.command.arguments.MultiPlayerArgument;
import xyz.bitsquidd.bits.lib.command.arguments.SinglePlayerArgument;
import xyz.bitsquidd.bits.lib.command.requirements.PermissionRequirement;
import xyz.bitsquidd.bits.lib.command.requirements.PlayerRequirement;
import xyz.bitsquidd.bits.lib.sendable.text.FormatHelper;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.CommandReturnDecorator;

import java.util.Collection;
import java.util.List;

@Command(name = "tp-example", aliases = {"tp-example-2"}, description = "Teleport players to a location", permission = "minecraft.command.teleport")
public class ExampleTeleportCommand extends AbstractCommand {

    @Override
    public void initialisePaths() {
        addPath(new CommandPath(
                "teleportToPlayer",
                "Teleport to a player",
                List.of(PlayerRequirement.INSTANCE),
                List.of(new CommandArgumentInfo<>("target", SinglePlayerArgument.INSTANCE)),
                this::teleportSelfToPlayer
        ));
        addPath(new CommandPath(
                "teleportToLocation",
                "Teleport to a Location",
                List.of(PlayerRequirement.INSTANCE),
                List.of(new CommandArgumentInfo<>("targetLocation", LocationArgument.INSTANCE)),
                this::teleportSelfToLocation
        ));
        addPath(new CommandPath(
                "teleportPlayerToLocation",
                "Teleport a player to a location",
                List.of(new PermissionRequirement("minecraft.command.teleport.others")),
                List.of(new CommandArgumentInfo<>("target", MultiPlayerArgument.INSTANCE), new CommandArgumentInfo<>("targetLocation", LocationArgument.INSTANCE)),
                this::teleportPlayerToLocation
        ));
        addPath(new CommandPath(
                "teleportPlayerToPlayer",
                "Teleport a player to a player",
                List.of(new PermissionRequirement("minecraft.command.teleport.others")),
                List.of(new CommandArgumentInfo<>("target", MultiPlayerArgument.INSTANCE), new CommandArgumentInfo<>("targetPlayer", SinglePlayerArgument.INSTANCE)),
                this::teleportPlayerToPlayer
        ));
    }





    private void teleportSelfToPlayer(CommandContext context) {
        Player sender = (Player) context.getSender();
        Player target = context.get("target");

        sender.teleport(target);
        Text.of(
                "Teleported to " + target,
                new CommandReturnDecorator(CommandReturnType.SUCCESS)
        ).send(sender);
    }

    private void teleportSelfToLocation(CommandContext context) {
        Player sender = (Player) context.getSender();
        Location targetLocation = context.get("targetLocation");

        sender.teleport(targetLocation);
        Text.of(
                "Teleported to " + FormatHelper.formatLocation(targetLocation, false),
                new CommandReturnDecorator(CommandReturnType.SUCCESS)
        ).send(sender);
    }

    private void teleportPlayerToLocation(CommandContext context) {
        Player sender = (Player) context.getSender();
        Collection<Player> targets = context.get("target");
        Location targetLocation = context.get("targetLocation");

        targets.forEach(p -> p.teleport(targetLocation));
        Text.of("Teleported "+FormatHelper.formatPlayers(targets)+" to " + FormatHelper.formatLocation(targetLocation, false),
                new CommandReturnDecorator(CommandReturnType.SUCCESS)
        ).send(sender);
    }

    private void teleportPlayerToPlayer(CommandContext context) {
        Player sender = (Player) context.getSender();
        Collection<Player> targets = context.get("target");
        Player targetPlayer = context.get("targetPlayer");

        targets.forEach(p -> p.teleport(targetPlayer));
        Text.of("Teleported "+FormatHelper.formatPlayers(targets)+" to " + targetPlayer.getName(),
                new CommandReturnDecorator(CommandReturnType.SUCCESS)
        ).send(sender);
    }
}
