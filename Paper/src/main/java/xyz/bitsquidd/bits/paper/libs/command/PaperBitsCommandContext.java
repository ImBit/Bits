package xyz.bitsquidd.bits.paper.libs.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;

public class PaperBitsCommandContext extends BitsCommandContext<CommandSourceStack> {
    public PaperBitsCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        super(brigadierContext);
    }

}
