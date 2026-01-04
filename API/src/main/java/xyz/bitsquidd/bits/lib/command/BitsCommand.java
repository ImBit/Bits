package xyz.bitsquidd.bits.lib.command;

import net.kyori.adventure.text.Component;

import xyz.bitsquidd.bits.lib.command.annotation.Command;
import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for all Bits commands.
 */
public abstract class BitsCommand {

    public void onRegister() {
        // Default implementation does nothing
        // Use this method to set up necessary states or permissions for roles dynamically.
    }

    @Command("usage")
    public void sendUsage(final BitsCommandContext<?> ctx) {
        ctx.respond(Text.of(Component.text("Test from " + getClass().getSimpleName() + "!")));
    }

    /**
     * Override to provide additional requirement instances needed to execute this command.
     */
    public Collection<BitsCommandRequirement> getAddedRequirements() {
        return new ArrayList<>();
    }

    /**
     * Override to provide alternate permission strings for this command.
     * Note: These will NOT be prefixed with the core permission string.
     */
    public Collection<String> getAlternatePermissionStrings() {
        return new ArrayList<>();
    }

}