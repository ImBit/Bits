package xyz.bitsquidd.bits.lib.command.argument.impl.vanilla;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.VanillaArgumentParser;

public class IntegerArgumentParser extends VanillaArgumentParser<Integer> {
    public IntegerArgumentParser() {
        super(TypeSignature.of(Integer.class), IntegerArgumentType.integer(), "Integer");
    }

}