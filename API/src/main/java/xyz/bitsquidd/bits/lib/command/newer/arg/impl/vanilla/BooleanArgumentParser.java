package xyz.bitsquidd.bits.lib.command.newer.arg.impl.vanilla;

import com.mojang.brigadier.arguments.BoolArgumentType;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.VanillaArgumentParser;

public class BooleanArgumentParser extends VanillaArgumentParser<Boolean> {
    public BooleanArgumentParser() {
        super(TypeSignature.of(Boolean.class), BoolArgumentType.bool());
    }

}