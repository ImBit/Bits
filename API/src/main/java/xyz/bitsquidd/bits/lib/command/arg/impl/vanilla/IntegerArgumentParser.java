package xyz.bitsquidd.bits.lib.command.arg.impl.vanilla;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.arg.parser.VanillaArgumentParser;

public class IntegerArgumentParser extends VanillaArgumentParser<Integer> {
    public IntegerArgumentParser() {
        super(TypeSignature.of(Integer.class), IntegerArgumentType.integer());
    }

}