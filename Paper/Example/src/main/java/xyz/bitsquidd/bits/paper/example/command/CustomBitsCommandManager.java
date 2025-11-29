package xyz.bitsquidd.bits.paper.example.command;

import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.SpigotConfig;

import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.paper.PaperBitsConfig;
import xyz.bitsquidd.bits.paper.example.command.impl.TeleportCommand;
import xyz.bitsquidd.bits.paper.example.text.decorator.impl.CommandDecorator;

import java.util.List;

public class CustomBitsCommandManager extends BitsCommandManager {

    private final CustomBitsCommandListener listener = new CustomBitsCommandListener(
          CommandDecorator.of(CommandReturnType.ERROR),
          Component.text(SpigotConfig.unknownCommandMessage)
    );

    @Override
    public void startup() {
        super.startup();
        Bukkit.getPluginManager().registerEvents(listener, ((PaperBitsConfig)PaperBitsConfig.get()).getPlugin());
    }

    @Override
    public void shutdown() {
        super.shutdown();
        HandlerList.unregisterAll(listener);
    }

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
    protected void executeCommand(boolean isAsync, @NotNull Runnable commandExecution) {
        if (isAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(((PaperBitsConfig)PaperBitsConfig.get()).getPlugin(), commandExecution);
        } else {
            Bukkit.getScheduler().runTask(((PaperBitsConfig)PaperBitsConfig.get()).getPlugin(), commandExecution);
        }
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