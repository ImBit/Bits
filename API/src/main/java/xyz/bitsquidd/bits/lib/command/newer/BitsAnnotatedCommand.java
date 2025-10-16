package xyz.bitsquidd.bits.lib.command.newer;

import org.jetbrains.annotations.NotNull;

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