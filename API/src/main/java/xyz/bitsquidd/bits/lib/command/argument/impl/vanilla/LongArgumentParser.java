package xyz.bitsquidd.bits.lib.command.argument.impl.vanilla;

import com.mojang.brigadier.arguments.LongArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.parser.VanillaArgumentParser;

public class LongArgumentParser extends VanillaArgumentParser<Long> {
    public LongArgumentParser() {
        super(TypeSignature.of(Long.class), LongArgumentType.longArg(), "Long");
    }

}