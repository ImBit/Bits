package xyz.bitsquidd.bits.velocity.lib.command;

import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.config.BitsConfig;

public class VelocityBitsCommandContext extends BitsCommandContext<CommandSource> {
    public VelocityBitsCommandContext(CommandContext<CommandSource> brigadierContext) {
        super(brigadierContext);
    }

    @Override
    protected BitsCommandSourceContext<CommandSource> createSourceContext(CommandContext<CommandSource> brigadierContext) {
        return ((VelocityBitsCommandManager)BitsConfig.get().getCommandManager()).createSourceContext(brigadierContext.getSource());
    }


}
