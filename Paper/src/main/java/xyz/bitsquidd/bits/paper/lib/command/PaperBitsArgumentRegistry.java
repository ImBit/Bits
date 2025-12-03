package xyz.bitsquidd.bits.paper.lib.command;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.argument.BitsArgumentRegistry;
import xyz.bitsquidd.bits.lib.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.lib.command.argument.parser.impl.primitive.PrimitiveArgumentParser;
import xyz.bitsquidd.bits.lib.wrappers.TypeSignature;
import xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl.*;
import xyz.bitsquidd.bits.paper.lib.command.argument.parser.impl.primitive.EntitySelectorArgumentParser;

import java.util.ArrayList;
import java.util.List;

public class PaperBitsArgumentRegistry extends BitsArgumentRegistry<CommandSourceStack> {

    @Override
    protected @Nullable ArgumentType<?> toArgumentType(TypeSignature<?> inputType) {
        ArgumentType<?> superArgumentType = super.toArgumentType(inputType);
        Class<?> clazz = inputType.toRawType();

        if (superArgumentType == null) {
            if (clazz == EntitySelector.class) {
                // Note net.minecraft.world.entity.EntitySelector and net.minecraft.commands.arguments.selector.EntitySelector are different things.
                // Our parsers expect a result in net.minecraft.commands.arguments.selector.EntitySelector.
                return EntityArgument.entities(); // TODO, in the future, we could consider refining this to be single/multiple/entity/player selectors. For now the parser should filter this.
            }
        }

        return null;
    }

    @Override
    protected List<PrimitiveArgumentParser<?>> initialisePrimitiveParsers() {
        List<PrimitiveArgumentParser<?>> parsers = new ArrayList<>(super.initialisePrimitiveParsers());
        parsers.add(new EntitySelectorArgumentParser());
        return parsers;
    }

    @Override
    protected List<AbstractArgumentParser<?>> initialiseParsers() {
        List<AbstractArgumentParser<?>> parsers = new ArrayList<>(super.initialiseParsers());
        parsers.addAll(List.of(
              new WorldArgumentParser(),
              new LocationArgumentParser(),
              new BlockPosArgumentParser(),
              new PlayerCollectionArgumentParser(),
              new PlayerSingleArgumentParser()
        ));

        return parsers;
    }

}
