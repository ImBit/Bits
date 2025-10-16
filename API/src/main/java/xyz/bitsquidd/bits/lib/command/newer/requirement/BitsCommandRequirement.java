package xyz.bitsquidd.bits.lib.command.newer.requirement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public abstract class BitsCommandRequirement {
    public abstract boolean test(@NotNull BitsCommandContext context);

    public @Nullable Text getFailureMessage(@NotNull BitsCommandContext context) {
        return null;
    }
}