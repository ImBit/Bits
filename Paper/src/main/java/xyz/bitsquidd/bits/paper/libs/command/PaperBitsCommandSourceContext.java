package xyz.bitsquidd.bits.paper.libs.command;

import net.minecraft.commands.CommandSourceStack;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;

public class PaperBitsCommandSourceContext extends BitsCommandSourceContext<CommandSourceStack> {
    public PaperBitsCommandSourceContext(CommandSourceStack source) {
        super(source);
    }


}
