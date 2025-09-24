package xyz.bitsquidd.bits.lib.command.newe.requirement;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class ConsoleSenderRequirement extends BitsRequirement {
    public static final ConsoleSenderRequirement INSTANCE = new ConsoleSenderRequirement();

    @Override
    public boolean test(CommandSourceStack commandSourceStack) {
        CommandSender sender = commandSourceStack.getSender();

        if (!(sender instanceof ConsoleCommandSender)) {
            return fail(commandSourceStack, Component.text("This command can only be run by the console."));
        }

        return true;
    }
}
