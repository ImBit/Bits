package xyz.bitsquidd.bits.lib.command.requirement;

import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public abstract class BitsCommandRequirement {
    public abstract boolean test(BitsCommandSourceContext<?> ctx);

    public @Nullable Text getFailureMessage(BitsCommandSourceContext<?> ctx) {
        return null;
    }

}