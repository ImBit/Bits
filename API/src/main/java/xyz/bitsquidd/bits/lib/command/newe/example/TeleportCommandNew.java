package xyz.bitsquidd.bits.lib.command.newe.example;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Rotation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newe.BitsCommandAnnotation;
import xyz.bitsquidd.bits.lib.command.newe.BitsCommandNew;
import xyz.bitsquidd.bits.lib.command.newe.requires.PlayerSenderRequirement;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@BitsCommandAnnotation(name = "tp-example", aliases = {"tp-example-a1"}, description = "Teleport players to a location")
public class TeleportCommandNew extends BitsCommandNew {
    @Override
    protected @NotNull LiteralArgumentBuilder<CommandSourceStack> generateTree(@NotNull LiteralArgumentBuilder<CommandSourceStack> root) {
        return root
              .then(Commands.argument("entity", ArgumentTypes.entity())
                    .requires(PlayerSenderRequirement.INSTANCE)
                    .executes(TeleportCommandNew::teleportToEntity)
              )
              .then(Commands.argument("location", ArgumentTypes.finePosition())
                    .requires(PlayerSenderRequirement.INSTANCE)
                    .executes(TeleportCommandNew::teleportToLocation)
                    .then(Commands.argument("rotation", ArgumentTypes.rotation())
                          .requires(PlayerSenderRequirement.INSTANCE)
                          .executes(TeleportCommandNew::teleportToLocation)
                          .then(Commands.argument("world", ArgumentTypes.world())
                                .requires(PlayerSenderRequirement.INSTANCE)
                                .executes(TeleportCommandNew::teleportToLocation)
                          )
                    )
              )
              .then(Commands.argument("targets", ArgumentTypes.entities())
                    .then(Commands.argument("entity", ArgumentTypes.entity())
                          .executes(TeleportCommandNew::teleportEntitiesToEntity)
                    )
                    .then(Commands.argument("location", ArgumentTypes.finePosition())
                          .requires(PlayerSenderRequirement.INSTANCE)
                          .executes(TeleportCommandNew::teleportToLocation)
                          .then(Commands.argument("rotation", ArgumentTypes.rotation())
                                .requires(PlayerSenderRequirement.INSTANCE)
                                .executes(TeleportCommandNew::teleportToLocation)
                                .then(Commands.argument("world", ArgumentTypes.world())
                                      .requires(PlayerSenderRequirement.INSTANCE)
                                      .executes(TeleportCommandNew::teleportEntitiesToLocation)
                                )
                          )
                    )
              );

    }

    private static int teleportToLocationInternal(@NotNull Collection<Entity> entities, @NotNull Location location) {
        entities.forEach(entity -> {
            entity.teleport(location);
        });
        return Command.SINGLE_SUCCESS;
    }

    private static int teleportToEntity(@NotNull CommandContext<CommandSourceStack> ctx) {
        Location location = ctx.getArgument("entity", Entity.class).getLocation();
        Player player = (Player)ctx.getSource().getSender();
        teleportToLocationInternal(Collections.singleton(player), location);

        return Command.SINGLE_SUCCESS;
    }

    private static int teleportToLocation(@NotNull CommandContext<CommandSourceStack> ctx) {
        FinePosition finePosition = getArg(ctx, "location", FinePosition.class);
        Rotation rotation = getArgOrDefault(ctx, "rotation", Rotation.class, Rotation.rotation(0.0f, 0.0f));
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
        teleportToLocationInternal(Collections.singleton(player), location);

        return Command.SINGLE_SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private static int teleportEntitiesToEntity(@NotNull CommandContext<CommandSourceStack> ctx) {
        Location location = ctx.getArgument("entity", Entity.class).getLocation();
        List<Entity> entities = (List<Entity>)ctx.getArgument("entities", List.class);
        teleportToLocationInternal(entities, location);

        return Command.SINGLE_SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private static int teleportEntitiesToLocation(@NotNull CommandContext<CommandSourceStack> ctx) {
        FinePosition finePosition = getArg(ctx, "location", FinePosition.class);
        Rotation rotation = getArgOrDefault(ctx, "rotation", Rotation.class, Rotation.rotation(0.0f, 0.0f));
        World world = getArgOrDefault(ctx, "world", World.class, ctx.getSource().getLocation().getWorld());

        Location location = new Location(
              world,
              finePosition.x(),
              finePosition.y(),
              finePosition.z(),
              rotation.yaw(),
              rotation.pitch()
        );

        List<Entity> entities = (List<Entity>)ctx.getArgument("entities", List.class);

        teleportToLocationInternal(entities, location);

        return Command.SINGLE_SUCCESS;
    }

}
