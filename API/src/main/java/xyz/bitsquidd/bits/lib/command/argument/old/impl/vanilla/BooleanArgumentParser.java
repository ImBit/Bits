package xyz.bitsquidd.bits.lib.command.argument.old.impl.vanilla;

import com.mojang.brigadier.arguments.BoolArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.VanillaArgumentParser;

public class BooleanArgumentParser extends VanillaArgumentParser<Boolean> {
    public BooleanArgumentParser() {
        super(TypeSignature.of(Boolean.class), BoolArgumentType.bool(), "Boolean");
    }

}