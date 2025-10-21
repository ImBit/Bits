package xyz.bitsquidd.bits.lib.command.argument.old.impl.vanilla;

import com.mojang.brigadier.arguments.DoubleArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.VanillaArgumentParser;

public class DoubleArgumentParser extends VanillaArgumentParser<Double> {
    public DoubleArgumentParser() {
        super(TypeSignature.of(Double.class), DoubleArgumentType.doubleArg(), "Double");
    }

}