package xyz.bitsquidd.bits.lib.command.argument.impl.vanilla;

import com.mojang.brigadier.arguments.FloatArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.VanillaArgumentParser;

public class FloatArgumentParser extends VanillaArgumentParser<Float> {
    public FloatArgumentParser() {
        super(TypeSignature.of(Float.class), FloatArgumentType.floatArg(), "Float");
    }

}