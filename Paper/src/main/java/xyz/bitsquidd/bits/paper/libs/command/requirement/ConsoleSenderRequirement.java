package xyz.bitsquidd.bits.paper.libs.command.requirement;

import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

public class ConsoleSenderRequirement extends BitsCommandRequirement {
    public static final ConsoleSenderRequirement INSTANCE = new ConsoleSenderRequirement();

    protected ConsoleSenderRequirement() {}

    @Override
    public boolean test(BitsCommandSourceContext<?> ctx) {
        return ctx.getSender() instanceof ConsoleCommandSender;
    }

    @Override
    public @Nullable Text getFailureMessage(BitsCommandSourceContext<?> ctx) {
        return Text.of(Component.text("This command can only be executed by the console."));
    }
}