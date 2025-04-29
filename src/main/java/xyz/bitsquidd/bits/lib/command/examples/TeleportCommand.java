package xyz.bitsquidd.bits.lib.command.examples;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.command.parameters.LocationParam;
import xyz.bitsquidd.bits.lib.command.parameters.PlayerParam;
import xyz.bitsquidd.bits.lib.command.requirement.HasPermission;
import xyz.bitsquidd.bits.lib.command.requirement.PlayerOnly;

@Command(name = "tp2", aliases = {"teleport"}, description = "Teleport players and entities",
        permission = "minecraft.command.teleport")
public class TeleportCommand extends AbstractCommand {

    @Override
    public void configurePaths() {
        // /tp <location>
        path().name("selfToLocation")
                .param(LocationParam.INSTANCE, "location")
                .requires(PlayerOnly.INSTANCE)
                .handler(this::executeSelfToLocation)
                .register();

        // /tp <player>
        path().name("selfToPlayer")
                .param(PlayerParam.INSTANCE, "target")
                .requires(PlayerOnly.INSTANCE)
                .handler(this::executeSelfToPlayer)
                .register();

        // /tp <player> <location>
        path().name("playerToLocation")
                .param(PlayerParam.INSTANCE, "player")
                .param(LocationParam.INSTANCE, "location")
                .requires(new HasPermission("minecraft.command.teleport.others"))
                .handler(this::executePlayerToLocation)
                .register();

        // /tp <player> <player>
        path().name("playerToPlayer")
                .param(PlayerParam.INSTANCE, "player")
                .param(PlayerParam.INSTANCE, "target")
                .requires(new HasPermission("minecraft.command.teleport.others"))
                .handler(this::executePlayerToPlayer)
                .register();
    }

    private void executeSelfToLocation(CommandContext context) {
        Player sender = context.getPlayer();
        Location location = context.get("location");

        sender.teleport(location);
        context.sendMessage("&aTeleported to " + formatLocation(location));
    }

    private void executeSelfToPlayer(CommandContext context) {
        Player sender = context.getPlayer();
        Player target = context.get("target");

        sender.teleport(target);
        context.sendMessage("&aTeleported to " + target.getName());
    }

    private void executePlayerToLocation(CommandContext context) {
        Player player = context.get("player");
        Location location = context.get("location");

        player.teleport(location);
        context.sendMessage("&aTeleported " + player.getName() + " to " + formatLocation(location));
    }

    private void executePlayerToPlayer(CommandContext context) {
        Player player = context.get("player");
        Player target = context.get("target");

        player.teleport(target);
        context.sendMessage("&aTeleported " + player.getName() + " to " + target.getName());
    }

    private String formatLocation(Location location) {
        return String.format("%.2f, %.2f, %.2f", location.getX(), location.getY(), location.getZ());
    }
}