package xyz.bitsquidd.bits.example.command;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.example.command.impl.TeleportCommand;
import xyz.bitsquidd.bits.lib.command.BitsCommand;
import xyz.bitsquidd.bits.lib.command.BitsCommandManager;

import java.util.List;

public class ExampleBitsCommandManager extends BitsCommandManager {

    @Override
    protected @NotNull List<BitsCommand> getAllCommands() {
        return List.of(new TeleportCommand());
    }

    @Override
    protected @NotNull String commandBasePermission() {
        return "bits.command";
    }

}