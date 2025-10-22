package xyz.bitsquidd.bits.example.command;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.example.command.impl.TeleportCommand;
import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.requirement.BitsRequirementRegistry;

import java.util.List;

public class ExampleBitsCommandManager extends BitsCommandManager {

    @Override
    public BitsArgumentRegistry getArgumentRegistry() {
        return new BitsArgumentRegistry();
    }

    @Override
    protected BitsRequirementRegistry getRequirementRegistry() {
        return new BitsRequirementRegistry();
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