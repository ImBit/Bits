package xyz.bitsquidd.bits.paper.example.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.paper.example.text.decorator.impl.CommandDecorator;

@NullMarked
public class CustomCommandContext extends BitsCommandContext {
    public CustomCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        super(brigadierContext);
    }

    public void respond(Text message, CommandReturnType type) {
        respond(message.decorate(CommandDecorator.of(type)));
    }

}
