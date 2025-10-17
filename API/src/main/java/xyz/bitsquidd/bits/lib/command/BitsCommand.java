package xyz.bitsquidd.bits.lib.command;

import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

public abstract class BitsCommand {

    protected void onRegister() {
        // Default implementation does nothing
    }

    protected void onUnregister() {
        // Default implementation does nothing
    }

    @Command("usage")
    public void sendUsage(@NotNull final BitsCommandContext ctx) {
        ctx.sendMessage("Test from " + getClass().getSimpleName() + "!");
    }

}