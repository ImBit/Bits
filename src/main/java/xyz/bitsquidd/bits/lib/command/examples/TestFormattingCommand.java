package xyz.bitsquidd.bits.lib.command.examples;

import xyz.bitsquidd.bits.lib.command.*;
import xyz.bitsquidd.bits.lib.command.annotations.Command;
import xyz.bitsquidd.bits.lib.command.arguments.BooleanArgument;
import xyz.bitsquidd.bits.lib.logging.LogController;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.examples.CommandReturnDecorator;

import java.util.List;

@Command(name = "testformatting2", description = "Test formatting", permission = "minecraft.command.teleport")
public class TestFormattingCommand extends AbstractCommand {
    @Override
    public void initialisePaths() {
        addPath(new CommandPath(
                "testinOptionalPaths",
                "Debug command used to test optional paths",
                List.of(),
                List.of(
                        new CommandArgumentInfo<>("optional1", BooleanArgument.INSTANCE),
                        new CommandArgumentInfo<>("optional2", BooleanArgument.INSTANCE)
                ),
                this::executeCommand
        ));
    }

    @Override
    public boolean defaultExecute(CommandContext commandContext) {
        new Text("<b>Hel <i> lo </b> there </i> how <u> are <s>you </u> ?</s>", new CommandReturnDecorator(CommandReturnType.SUCCESS)).send(commandContext.getSender());
        return true;
    }

    private void executeCommand(CommandContext context) {
        LogController.info("1");

        new Text(context.get("optional1")+"   "+context.get("optional2")).send(context.getSender());
    }
}
