package xyz.bitsquidd.bits.lib.sendable.text;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.sendable.Sendable;

public class Text implements Sendable {
    protected @NotNull Component component;

    public Text(@NotNull Component component) {
        this.component = component;
    }

    public static Text of(Component component) {
        return new Text(component);
    }

    private Component get() {
        return this.component;
    }

    @Override
    public <T extends CommandSender> void send(@NotNull T target) {
        target.sendMessage(get());
    }
}
