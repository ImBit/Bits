package xyz.bitsquidd.bits.lib.command.argument.old.impl.vanilla;

import com.mojang.brigadier.arguments.LongArgumentType;

import xyz.bitsquidd.bits.lib.command.argument.TypeSignature;
import xyz.bitsquidd.bits.lib.command.argument.old.parser.VanillaArgumentParser;

public class LongArgumentParser extends VanillaArgumentParser<Long> {
    public LongArgumentParser() {
        super(TypeSignature.of(Long.class), LongArgumentType.longArg(), "Long");
    }

}