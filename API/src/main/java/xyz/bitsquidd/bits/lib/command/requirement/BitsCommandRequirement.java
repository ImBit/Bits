package xyz.bitsquidd.bits.lib.command.requirement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public abstract class BitsCommandRequirement {
    public abstract boolean test(@NotNull BitsCommandSourceContext ctx);

    public @Nullable Text getFailureMessage(@NotNull BitsCommandSourceContext ctx) {
        return null;
    }
}