package xyz.bitsquidd.bits.lib.command.arg.impl.vanilla;

import com.mojang.brigadier.arguments.FloatArgumentType;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.arg.parser.VanillaArgumentParser;

public class FloatArgumentParser extends VanillaArgumentParser<Float> {
    public FloatArgumentParser() {
        super(TypeSignature.of(Float.class), FloatArgumentType.floatArg());
    }

}