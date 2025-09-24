package xyz.bitsquidd.bits.lib.command.newe.example;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Rotation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newe.BitsCommand;
import xyz.bitsquidd.bits.lib.command.newe.BitsCommandAnnotation;
import xyz.bitsquidd.bits.lib.command.newe.requirement.PlayerSenderRequirement;
import xyz.bitsquidd.bits.lib.command.newe.util.CommandArgUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@BitsCommandAnnotation(name = "tp-example", aliases = {"tp-example-a1"}, description = "Teleport players to a location")
public class TeleportCommandExample extends BitsCommand {
    @Override
    protected @NotNull LiteralArgumentBuilder<CommandSourceStack> generateTree(@NotNull LiteralArgumentBuilder<CommandSourceStack> root) {
        return root
              .then(Commands.argument("entity", ArgumentTypes.entity())
                    .requires(PlayerSenderRequirement.INSTANCE)
                    .executes(TeleportCommandExample::teleportToEntity)
              )
              .then(CommandArgUtils.locationArg(
                    "location", TeleportCommandExample::teleportToLocation
              ))
              .then(Commands.argument("targets", ArgumentTypes.entities())
                    .then(Commands.argument("entity", ArgumentTypes.entity())
                          .executes(TeleportCommandExample::teleportEntitiesToEntity)
                    )
                    .then(CommandArgUtils.locationArg(
                          "location", TeleportCommandExample::teleportToLocation
                    ))
              );
    }

    private static int teleportToLocationInternal(@NotNull Collection<Entity> entities, @NotNull Location location) {
        entities.forEach(entity -> {
            entity.teleport(location);
        });
        return Command.SINGLE_SUCCESS;
    }

    private static int teleportToEntity(@NotNull CommandContext<CommandSourceStack> ctx) {
        Location location = resolveArg(ctx, "entity", EntitySelectorArgumentResolver.class).getFirst().getLocation();
        Player player = (Player)ctx.getSource().getSender();

        return teleportToLocationInternal(Collections.singleton(player), location);
    }

    private static int teleportToLocation(@NotNull CommandContext<CommandSourceStack> ctx) {
        FinePosition finePosition = resolveArg(ctx, "location", FinePositionResolver.class);
        Rotation rotation = resolveArgOrDefault(ctx, "rotation", RotationResolver.class, Rotation.rotation(0.0f, 0.0f));
        World world = getArgOrDefault(ctx, "world", World.class, ctx.getSource().getLocation().getWorld());

        Location location = new Location(
              world,
              finePosition.x(),
              finePosition.y(),
              finePosition.z(),
              rotation.yaw(),
              rotation.pitch()
        );

        Player player = (Player)ctx.getSource().getSender();

        return teleportToLocationInternal(Collections.singleton(player), location);
    }

    private static int teleportEntitiesToEntity(@NotNull CommandContext<CommandSourceStack> ctx) {
        Location location = resolveArg(ctx, "entity", EntitySelectorArgumentResolver.class).getFirst().getLocation();
        List<Entity> entities = resolveArg(ctx, "targets", EntitySelectorArgumentResolver.class);

        return teleportToLocationInternal(entities, location);
    }

    private static int teleportEntitiesToLocation(@NotNull CommandContext<CommandSourceStack> ctx) {
        FinePosition finePosition = resolveArg(ctx, "location", FinePositionResolver.class);
        Rotation rotation = resolveArgOrDefault(ctx, "rotation", RotationResolver.class, Rotation.rotation(0.0f, 0.0f));
        World world = getArgOrDefault(ctx, "world", World.class, ctx.getSource().getLocation().getWorld());

        Location location = new Location(
              world,
              finePosition.x(),
              finePosition.y(),
              finePosition.z(),
              rotation.yaw(),
              rotation.pitch()
        );

        List<Entity> entities = resolveArg(ctx, "targets", EntitySelectorArgumentResolver.class);

        return teleportToLocationInternal(entities, location);
    }

}
