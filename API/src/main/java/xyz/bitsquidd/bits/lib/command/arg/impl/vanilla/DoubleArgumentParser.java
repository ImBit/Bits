package xyz.bitsquidd.bits.lib.command.arg.impl.vanilla;

import com.mojang.brigadier.arguments.DoubleArgumentType;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.arg.parser.VanillaArgumentParser;

public class DoubleArgumentParser extends VanillaArgumentParser<Double> {
    public DoubleArgumentParser() {
        super(TypeSignature.of(Double.class), DoubleArgumentType.doubleArg());
    }

}