/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.argument.parser.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.command.util.BitsCommandContext;
import xyz.bitsquidd.bits.wrapper.Link;
import xyz.bitsquidd.bits.wrapper.type.TypeSignature;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;


//TODO add better validation for this, enum for a type? WWW. HTTPS:// etc.?

/**
 * Argument parser for {@link Link} values.
 */
public final class LinkArgumentParser extends AbstractArgumentParser<Link> {
    public LinkArgumentParser() {
        super(TypeSignature.of(Link.class), "URL");
    }

    @Override
    public Link parse(List<Object> inputObjects, BitsCommandContext<?> ctx) throws CommandSyntaxException {
        String inputString = singletonInputValidation(inputObjects, String.class);
        return Link.of(inputString);
    }

    @Override
    public Supplier<List<String>> getSuggestions() {
        return () -> Stream.of("https://", "http://", "www.").toList();
    }

}
