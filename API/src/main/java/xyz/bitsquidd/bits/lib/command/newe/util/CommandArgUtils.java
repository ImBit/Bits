package xyz.bitsquidd.bits.lib.command.newe.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.newe.requires.PlayerSenderRequirement;

public final class CommandArgUtils {
    private CommandArgUtils() {}

    public static RequiredArgumentBuilder<CommandSourceStack, ?> locationArg(
          @NotNull String baseName,
          @NotNull Command<CommandSourceStack> exec
    ) {
        return locationArg(baseName, exec, exec, exec);
    }

    public static RequiredArgumentBuilder<CommandSourceStack, ?> locationArg(
          @NotNull String baseName,
          @NotNull Command<CommandSourceStack> locationExec,
          @NotNull Command<CommandSourceStack> rotationExec,
          @NotNull Command<CommandSourceStack> worldExec
    ) {
        RequiredArgumentBuilder<CommandSourceStack, ?> locationArg =
              Commands.argument(baseName, ArgumentTypes.finePosition())
                    .requires(PlayerSenderRequirement.INSTANCE)
                    .executes(locationExec);

        RequiredArgumentBuilder<CommandSourceStack, ?> rotationArg =
              Commands.argument("rotation", ArgumentTypes.rotation())
                    .requires(PlayerSenderRequirement.INSTANCE)
                    .executes(rotationExec);

        RequiredArgumentBuilder<CommandSourceStack, ?> worldArg =
              Commands.argument("world", ArgumentTypes.world())
                    .requires(PlayerSenderRequirement.INSTANCE)
                    .executes(worldExec);

        rotationArg.then(worldArg);
        locationArg.then(rotationArg);

        return locationArg;
    }
}