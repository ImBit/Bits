package xyz.bitsquidd.bits.lib.command.newe.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

/**
 * Bits' custom way of returning exceptions to allow for better formatted error messages.
 * In the future we could consider parsing {@link  com.mojang.brigadier.exceptions.CommandSyntaxException}, as this is
 * what Brigadier uses, and would allow for far more integrated functionality.
 */
public class BitsCommandException extends CommandSyntaxException {
    public BitsCommandException(@NotNull String message) {
        super(new SimpleCommandExceptionType(getAdventureComponent(message)), getAdventureComponent(message));
    }

    private static AdventureComponent getAdventureComponent(@NotNull String message) {
        return new AdventureComponent(Text.of(Component.text(message))
              .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
              .getComponent(null)); //TODO pass sender in here?
    }

    //public abstract class AbstractCommandException extends Exception implements ComponentMessageThrowable {
    //    private final @NotNull String message;
    //
    //    public AbstractCommandException(@NotNull String message) {
    //        this.message = message;
    //    }
    //
    //    public final void sendTo(@NotNull CommandSender sender) {
    //        Text.of(Component.text(message))
    //              .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
    //              .send(sender);
    //    }
    //
    //}

}
