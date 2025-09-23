package xyz.bitsquidd.bits.lib.command.newe.requires;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.bitsquidd.bits.lib.command.CommandReturnType;
import xyz.bitsquidd.bits.lib.sendable.text.Text;
import xyz.bitsquidd.bits.lib.sendable.text.decorator.impl.CommandReturnDecorator;

import java.util.function.Predicate;

public final class PlayerSenderRequirement implements Predicate<CommandSourceStack> {
    public static final PlayerSenderRequirement INSTANCE = new PlayerSenderRequirement();


    @Override
    public boolean test(CommandSourceStack commandSourceStack) {
        CommandSender sender = commandSourceStack.getSender();

        if (!(sender instanceof Player)) {
            Text.of(Component.text("Only players can use this command."))
                  .decorate(CommandReturnDecorator.of(CommandReturnType.ERROR))
                  .send(sender);
            return false;
        }

        return true;
    }
}
