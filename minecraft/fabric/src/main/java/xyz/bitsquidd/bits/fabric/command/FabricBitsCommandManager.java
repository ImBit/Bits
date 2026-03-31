package xyz.bitsquidd.bits.fabric.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.BitsCommandManager;
import xyz.bitsquidd.bits.command.requirement.BitsRequirementRegistry;
import xyz.bitsquidd.bits.command.util.BitsCommandBuilder;

public class FabricBitsCommandManager extends BitsCommandManager<CommandSourceStack> {

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
            getAllCommands().forEach(this::registerCommand);
            getRegisteredCommands().forEach(bitsCommand -> {
                brigadierTreeGenerator.createNodes(new BitsCommandBuilder(bitsCommand))
                  .forEach(node -> dispatcher.register(node.createBuilder()));
                bitsCommand.onRegister();
            });
        });
    }

}