package xyz.bitsquidd.bits.paper.example.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.command.CommandReturnType;
import xyz.bitsquidd.bits.sendable.text.Text;
import xyz.bitsquidd.bits.paper.example.text.decorator.impl.CommandDecorator;
import xyz.bitsquidd.bits.paper.lib.command.PaperBitsCommandContext;

public class CustomCommandContext extends PaperBitsCommandContext {
    public CustomCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        super(brigadierContext);
    }

    public void respond(Text message, CommandReturnType type) {
        respond(message.decorate(CommandDecorator.of(type)));
    }

}
