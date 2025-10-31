package xyz.bitsquidd.bits.lib.command;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

import java.util.Collections;
import java.util.Set;

@NullMarked
public abstract class BitsCommand {

    protected void onRegister() {
        // Default implementation does nothing
        // Use this method to set up necessary states or permissions for roles dynamically.
    }

    @Command("usage")
    public void sendUsage(final BitsCommandContext ctx) {
        ctx.respond(Text.of(Component.text("Test from " + getClass().getSimpleName() + "!")));
    }

    public Set<BitsCommandRequirement> getAddedRequirements() {
        return Collections.emptySet();
    }

}