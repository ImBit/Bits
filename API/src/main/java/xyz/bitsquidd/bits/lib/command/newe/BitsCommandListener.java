package xyz.bitsquidd.bits.lib.command.newe;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.command.UnknownCommandEvent;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;

public class BitsCommandListener implements Listener {
    private final @NotNull ITextDecorator errorDecorator;
    private final @NotNull Component unknownCommandMessage;

    public BitsCommandListener(@NotNull ITextDecorator errorDecorator, @NotNull Component unknownCommandMessage) {
        this.errorDecorator = errorDecorator;
        this.unknownCommandMessage = unknownCommandMessage;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUnknownCommand(UnknownCommandEvent unknownCommandEvent) {
        Component message = unknownCommandEvent.message();

        unknownCommandEvent.message(Text.of(message == null ? unknownCommandMessage : message)
              .decorate(errorDecorator)
              .getComponent(unknownCommandEvent.getSender())
        );
    }
}
