/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.fabric.command.provider;

import xyz.bitsquidd.bits.command.BitsCommand;
import xyz.bitsquidd.bits.command.argument.parser.AbstractArgumentParser;
import xyz.bitsquidd.bits.wrapper.collection.AddableSet;

public interface BitsCommandProvider {
    default AddableSet<BitsCommand> getCommands() {
        return AddableSet.empty();
    }

    default AddableSet<AbstractArgumentParser<?>> getArguments() {
        return AddableSet.empty();
    }

}