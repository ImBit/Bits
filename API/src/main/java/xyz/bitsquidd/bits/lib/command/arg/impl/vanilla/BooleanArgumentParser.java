package xyz.bitsquidd.bits.lib.command.arg.impl.vanilla;

import com.mojang.brigadier.arguments.BoolArgumentType;

import xyz.bitsquidd.bits.lib.command.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.arg.parser.VanillaArgumentParser;

public class BooleanArgumentParser extends VanillaArgumentParser<Boolean> {
    public BooleanArgumentParser() {
        super(TypeSignature.of(Boolean.class), BoolArgumentType.bool());
    }

}