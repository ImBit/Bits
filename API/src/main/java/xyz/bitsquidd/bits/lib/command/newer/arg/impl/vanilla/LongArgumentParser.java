package xyz.bitsquidd.bits.lib.command.newer.arg.impl.vanilla;

import com.mojang.brigadier.arguments.LongArgumentType;

import xyz.bitsquidd.bits.lib.command.newer.arg.TypeSignature;
import xyz.bitsquidd.bits.lib.command.newer.arg.parser.VanillaArgumentParser;

public class LongArgumentParser extends VanillaArgumentParser<Long> {
    public LongArgumentParser() {
        super(TypeSignature.of(Long.class), LongArgumentType.longArg());
    }

}