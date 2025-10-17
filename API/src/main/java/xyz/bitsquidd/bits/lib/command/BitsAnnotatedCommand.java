package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.info.BitsCommandContext;

public abstract class BitsAnnotatedCommand {

    protected void onRegister() {
        // Default implementation does nothing
    }

    protected void onUnregister() {
        // Default implementation does nothing
    }

    protected void defaultExecution(@NotNull BitsCommandContext context) {
        context.sendMessage("Test from " + getClass().getSimpleName() + "!");
    }
}