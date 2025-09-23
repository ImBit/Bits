package xyz.bitsquidd.bits.example.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.*;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.List;

public abstract class ExampleBitsCommand extends AbstractCommand {
    @Override
    protected void showUsage(@NotNull CommandContext commandContext) {
        Component usageComponent = Component.empty().appendNewline().appendNewline().appendNewline();

        usageComponent = usageComponent
              .append(Component.text("          ").decorate(TextDecoration.STRIKETHROUGH))
              .append(Component.text(" /" + name + " ").decorate(TextDecoration.BOLD))
              .append(Component.text("          ").decorate(TextDecoration.STRIKETHROUGH))
              .appendNewline();

        List<CommandPath> availablePaths = paths.stream()
              .filter(path -> path.hasPermissions(commandContext))
              .toList();

        if (availablePaths.isEmpty()) {
            Text.of(Component.text("You don't have permission to use this command."))
                  .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
                  .send(commandContext.getSender());
        } else {

            for (CommandPath path : availablePaths) {
                usageComponent = usageComponent.append(
                      Component.text("/" + name + " ", TextColor.color(0x11DDFF))
                );

                for (CommandArgumentInfo<?> arg : path.getParams()) {
                    String paramName = arg.param.getTypeName();
                    if (paramName.isEmpty()) {
                        paramName = "<" + arg.name + "> ";
                    } else {
                        paramName = "<" + arg.name + " : " + paramName + "> ";
                    }

                    usageComponent = usageComponent.append(
                          Component.text(paramName, TextColor.color(0x11DDFF)));
                }

                usageComponent = usageComponent
                      .appendNewline()
                      .append(Component.text("  ⏵ " + path.description, NamedTextColor.GRAY)
                            .appendNewline()
                      );
            }

            usageComponent = usageComponent.append(
                  Component.text("                              ").decorate(TextDecoration.STRIKETHROUGH));

            Text.of(usageComponent).send(commandContext.getSender());
        }
    }
}
