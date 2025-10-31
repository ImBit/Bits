package xyz.bitsquidd.bits.example.command;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.example.text.decorator.impl.CommandDecorator;
import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

@NullMarked
public class CustomCommandContext extends BitsCommandContext {
    public CustomCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        super(brigadierContext);
    }

    public void respond(Text message, CommandReturnType type) {
        respond(message.decorate(CommandDecorator.of(type)));
    }

}
