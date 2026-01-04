package xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl.primitive;

import net.minecraft.commands.arguments.selector.EntitySelector;

import xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive.PrimitiveArgumentParser;

/**
 * Argument parser for EntitySelector values.
 */
public final class EntitySelectorArgumentParser extends PrimitiveArgumentParser<EntitySelector> {
    public EntitySelectorArgumentParser() {
        super(EntitySelector.class, "EntitySelector");
    }

}
