package xyz.bitsquidd.bits.lib.command.argument.impl.vanilla;

import com.mojang.brigadier.arguments.DoubleArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.VanillaArgumentParser;

public class DoubleArgumentParser extends VanillaArgumentParser<Double> {
    public DoubleArgumentParser() {
        super(TypeSignature.of(Double.class), DoubleArgumentType.doubleArg());
    }

}