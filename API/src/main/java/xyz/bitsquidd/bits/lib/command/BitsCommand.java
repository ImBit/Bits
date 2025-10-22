package xyz.bitsquidd.bits.lib.command;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public abstract class BitsCommand {

    protected void onRegister() {
        // Default implementation does nothing
    }

    protected void onUnregister() {
        // Default implementation does nothing
    }

    @Command("usage")
    public void sendUsage(@NotNull final BitsCommandContext ctx) {
        ctx.respond(Text.of(Component.text("Test from " + getClass().getSimpleName() + "!")));
    }

}