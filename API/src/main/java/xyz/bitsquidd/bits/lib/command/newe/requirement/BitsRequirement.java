package xyz.bitsquidd.bits.lib.command.newe.requirement;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.function.Predicate;

public abstract class BitsRequirement implements Predicate<CommandSourceStack> {

    protected boolean fail(@NotNull CommandSourceStack commandSourceStack, @NotNull Component failureMessage) {
        Text.of(failureMessage)
              .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
              .send(commandSourceStack.getSender());

        return false;
    }


}
