package xyz.bitsquidd.bits.paper.example.command;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.command.UnknownCommandEvent;

import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.ITextDecorator;

public class CustomBitsCommandListener implements Listener {
    private final ITextDecorator errorDecorator;
    private final Component unknownCommandMessage;

    public CustomBitsCommandListener(ITextDecorator errorDecorator, Component unknownCommandMessage) {
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