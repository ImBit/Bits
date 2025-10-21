package xyz.bitsquidd.bits.lib.command.requirement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public abstract class BitsCommandRequirement {
    public abstract boolean test(@NotNull BitsCommandContext ctx);

    public @Nullable Text getFailureMessage(@NotNull BitsCommandContext ctx) {
        return null;
    }
}