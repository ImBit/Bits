package xyz.bitsquidd.bits.example.command;

import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.SpigotConfig;

import xyz.bitsquidd.bits.example.command.impl.TeleportCommand;
import xyz.bitsquidd.bits.example.text.decorator.impl.CommandDecorator;
import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.BitsCommandListener;
import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

import java.util.List;

public class CustomBitsCommandManager extends BitsCommandManager {

    @Override
    public @NotNull BitsArgumentRegistry getArgumentRegistry() {
        return new BitsArgumentRegistry();
    }

    @Override
    protected @NotNull BitsRequirementRegistry getRequirementRegistry() {
        return new BitsRequirementRegistry();
    }

    @Override
    public @NotNull BitsCommandContext createContext(@NotNull CommandContext<CommandSourceStack> brigadierContext) {
        return new CustomCommandContext(brigadierContext);
    }

    @Override
    protected @NotNull BitsCommandListener getListenerInternal() {
        return new BitsCommandListener(
              CommandDecorator.of(CommandReturnType.ERROR),
              Component.text(SpigotConfig.unknownCommandMessage)
        );
    }

    @Override
    protected @NotNull List<BitsCommand> getAllCommands() {
        return List.of(new TeleportCommand());
    }

    @Override
    protected @NotNull String commandBasePermission() {
        return "bits.command";
    }

}