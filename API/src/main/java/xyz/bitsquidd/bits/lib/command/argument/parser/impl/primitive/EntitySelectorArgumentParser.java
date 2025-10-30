package xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive;

import net.minecraft.commands.arguments.selector.EntitySelector;
import org.jetbrains.annotations.NotNull;

public final class EntitySelectorArgumentParser extends PrimitiveArgumentParser<@NotNull EntitySelector> {
    public EntitySelectorArgumentParser() {
        super(EntitySelector.class, "EntitySelector");
    }

}
