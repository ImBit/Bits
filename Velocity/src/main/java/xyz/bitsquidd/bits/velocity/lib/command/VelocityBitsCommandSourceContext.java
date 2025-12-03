package xyz.bitsquidd.bits.velocity.lib.command;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;

public class VelocityBitsCommandSourceContext extends BitsCommandSourceContext<CommandSource> {
    public VelocityBitsCommandSourceContext(CommandSource source) {
        super(source);
    }

    @Override
    public Audience getSender() {
        return source;
    }

}
