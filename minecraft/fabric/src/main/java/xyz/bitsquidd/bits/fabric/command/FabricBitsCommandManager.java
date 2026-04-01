package xyz.bitsquidd.bits.fabric.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.BitsCommand;
import xyz.bitsquidd.bits.command.BitsCommandManager;
import xyz.bitsquidd.bits.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.command.util.BitsCommandBuilder;
import xyz.bitsquidd.bits.fabric.command.provider.BitsCommandProvider;
import xyz.bitsquidd.bits.wrapper.collection.AddableSet;

public final class FabricBitsCommandManager extends BitsCommandManager<CommandSourceStack> {
    public static final String COMMAND_INSTANCE_ENTRYPOINT = "bits:command";

    @Override
    protected FabricBitsArgumentRegistry initialiseArgumentRegistry() {
        return new FabricBitsArgumentRegistry();
    }

    @Override
    protected BitsRequirementRegistry<CommandSourceStack> initialiseRequirementRegistry() {
        return new FabricBitsRequirementRegistry();
    }

    @Override
    public FabricBitsCommandContext createContext(CommandContext<CommandSourceStack> brigadierContext) {
        return new FabricBitsCommandContext(brigadierContext);
    }

    @Override
    public FabricBitsCommandSourceContext createSourceContext(CommandSourceStack sourceStack) {
        return new FabricBitsCommandSourceContext(sourceStack);
    }

    @Override
    protected void enableAllCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            getAllCommands().build().forEach(this::registerCommand);
            getRegisteredCommands().forEach(bitsCommand -> {
                brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                  .forEach(node -> dispatcher.register(node.createBuilder()));
                bitsCommand.onRegister();
            });
        });
    }

    /**
     * Fabric mods must use the {@code COMMAND_INSTANCE_ENTRYPOINT} entrypoint to provide commands.
     * This method retrieves all commands from that entrypoint.
     */
    @Override
    protected AddableSet<BitsCommand> getAllCommands() {
        return super.getAllCommands().addAll(FabricLoader.getInstance()
          .getEntrypoints(COMMAND_INSTANCE_ENTRYPOINT, BitsCommandProvider.class)
          .stream()
          .flatMap(provider -> provider.getCommands().build().stream())
          .toList()
        );
    }

}