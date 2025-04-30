package xyz.bitsquidd.bits.lib.command.examples;

import xyz.bitsquidd.bits.lib.command.AbstractCommand;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.CommandReturnDecorator;

@Command(name = "testformatting", description = "Test formatting", permission = "minecraft.command.teleport")
public class TestFormattingCommand extends AbstractCommand {
    @Override
    public void initialisePaths() {

    }

    @Override
    public boolean defaultExecute(CommandContext commandContext) {
        new Text("<b>Hel <i> lo </b> there </i> how <u> are <s>you </u> ?</s>", new CommandReturnDecorator(CommandReturnType.SUCCESS)).send(commandContext.sender);
        return true;
    }
}
