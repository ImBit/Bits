package xyz.bitsquidd.bits.example.command.inst;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.example.command.ExampleBitsCommand;
import xyz.bitsquidd.bits.lib.command.CommandArgumentInfo;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.CommandPath;
import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.command.arguments.LocationArgument;
import xyz.bitsquidd.bits.lib.command.arguments.MultiPlayerArgument;
import xyz.bitsquidd.bits.lib.command.arguments.SinglePlayerArgument;
import xyz.bitsquidd.bits.lib.command.requirements.PermissionRequirement;
import xyz.bitsquidd.bits.lib.command.requirements.PlayerRequirement;
import xyz.bitsquidd.bits.lib.sendable.text.FormatHelper;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Command(name = "tp-example", aliases = {"tp-example-3"}, description = "Teleport players to a location", permission = "minecraft.command.teleport")
public class ExampleTeleportCommand extends ExampleBitsCommand {

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
              List.of(
                    new CommandArgumentInfo<>("target", MultiPlayerArgument.INSTANCE),
                    new CommandArgumentInfo<>("targetLocation", LocationArgument.INSTANCE)
              ),
              this::teleportPlayerToLocation
        ));
        addPath(new CommandPath(
              "teleportPlayerToPlayer",
              "Teleport a player to a player",
              List.of(new PermissionRequirement("minecraft.command.teleport.others")),
              List.of(
                    new CommandArgumentInfo<>("target", MultiPlayerArgument.INSTANCE),
                    new CommandArgumentInfo<>("targetPlayer", SinglePlayerArgument.INSTANCE)
              ),
              this::teleportPlayerToPlayer
        ));
    }


    private void teleportSelfToPlayer(CommandContext context) {
        Player sender = (Player)context.getSender();
        Player target = context.get("target");

        sender.teleportAsync(target.getLocation()).thenAccept((result) -> Text.of(Component.text("Teleported to <b>" + target + "</b>."))
              .decorate(CommandReturnDecorator.of(CommandReturnType.SUCCESS))
              .send(sender));
    }

    private void teleportSelfToLocation(CommandContext context) {
        Player sender = (Player)context.getSender();
        Location targetLocation = context.get("targetLocation");

        sender.teleportAsync(targetLocation).thenAccept((result) -> Text.of(Component.text("Teleported to <b>" + FormatHelper.formatLocation(targetLocation, false) + "</b>."))
              .decorate(CommandReturnDecorator.of(CommandReturnType.SUCCESS))
              .send(sender));
    }

    private void teleportPlayerToLocation(CommandContext context) {
        Player sender = (Player)context.getSender();
        Collection<Player> targets = context.get("target");
        Location targetLocation = context.get("targetLocation");

        targets.forEach(p -> p.teleportAsync(targetLocation)
              .thenAccept((result) -> Text.of(Component.text("Teleported <b>" + FormatHelper.formatPlayers(targets) + "</b> to <b>" + FormatHelper.formatLocation(
                          targetLocation,
                          false
                    ) + "</b>."))
                    .decorate(CommandReturnDecorator.of(CommandReturnType.SUCCESS))
                    .send(sender)));
    }

    private void teleportPlayerToPlayer(CommandContext context) {
        Player sender = (Player)context.getSender();
        Collection<Player> targets = context.get("target");
        Player targetPlayer = context.get("targetPlayer");
        Location targetLocation = targetPlayer.getLocation();

        Collection<CompletableFuture<Boolean>> futures = targets.stream()
              .map(p -> p.teleportAsync(targetLocation))
              .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
              .thenRun(() -> Text.of(Component.text("Teleported <b>" + FormatHelper.formatPlayers(targets) + "</b> to <b>" + targetPlayer.getName() + "</b>."))
                    .decorate(CommandReturnDecorator.of(CommandReturnType.SUCCESS))
                    .send(sender));
    }
}
