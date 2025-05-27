package xyz.bitsquidd.bits.lib.sendable.text.decorator.formatters;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFormatter {
    protected final @NotNull String openTag;
    protected final @NotNull String closeTag;

    protected AbstractFormatter(@NotNull String tag) {
        this.openTag = "<" + tag + ">";
        this.closeTag = "</" + tag + ">";
    }

    public abstract @NotNull Component format(@NotNull Component input);

    public final @NotNull String getOpenTag() {
        return openTag;
    }

    public final @NotNull String getCloseTag() {
        return closeTag;
    }
}
