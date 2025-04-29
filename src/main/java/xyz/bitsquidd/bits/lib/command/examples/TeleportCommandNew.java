package xyz.bitsquidd.bits.lib.command.examples;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.CommandContextNew;
import xyz.bitsquidd.bits.lib.command.CommandParamInfo;
import xyz.bitsquidd.bits.lib.command.CommandPathNew;
import xyz.bitsquidd.bits.lib.command.annotations.CommandNew;
import xyz.bitsquidd.bits.lib.command.params.LocationParamNew;
import xyz.bitsquidd.bits.lib.command.params.PlayerParamNew;

import java.util.List;

//TODO:
// Add requirements to paths - e.g. player != null etc.
// Add permissions to paths - e.g. teleport self teleport others etc.
@CommandNew(name = "tpnew20", aliases = {"tp"}, description = "Teleport players to a location", permission = "minecraft.command.teleport")
public class TeleportCommandNew extends AbstractCommandNew {

    @Override
    public void initialisePaths() {
        addPath(new CommandPathNew(
                "teleportToPlayer",
                "Teleport to a player",
                List.of("minecraft.command.teleport"),
                List.of(new CommandParamInfo("target", PlayerParamNew.INSTANCE)),
                this::teleportSelfToPlayer
        ));
        addPath(new CommandPathNew(
                "teleportToLocation",
                "Teleport to a Location",
                List.of("minecraft.command.teleport"),
                List.of(new CommandParamInfo("targetLocation", LocationParamNew.INSTANCE)),
                this::teleportSelfToLocation
        ));
        addPath(new CommandPathNew(
                "teleportPlayerToLocation",
                "Teleport a player to a location",
                List.of("minecraft.command.teleport"),
                List.of(new CommandParamInfo("target", PlayerParamNew.INSTANCE), new CommandParamInfo("targetLocation", LocationParamNew.INSTANCE)),
                this::teleportPlayerToLocation
        ));
    }




    private void teleportSelfToPlayer(CommandContextNew context) {
        Player sender = (Player) context.sender;
        Player target = context.get("target");

        sender.teleport(target);
        sender.sendMessage("&aTeleported to " + target);
    }

    private void teleportSelfToLocation(CommandContextNew context) {
        Player sender = (Player) context.sender;
        Location target = context.get("targetLocation");

        sender.teleport(target);
        sender.sendMessage("&aTeleported to " + target);
    }

    private void teleportPlayerToLocation(CommandContextNew context) {
        Player sender = (Player) context.sender;
        Player target = context.get("target");
        Location location = context.get("targetLocation");

        target.teleport(location);
        sender.sendMessage("&aTeleported to " + location);
    }
}
