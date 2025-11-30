package xyz.bitsquidd.bits.velocity.libs.command;

import com.velocitypowered.api.command.CommandSource;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;

public class VelocityBitsCommandSourceContext extends BitsCommandSourceContext<CommandSource> {
    public VelocityBitsCommandSourceContext(CommandSource source) {
        super(source);
    }

}
