/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command;

import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for all Bits commands.
 */
public abstract class BitsCommand {

    public void onRegister() {
        // Default implementation does nothing
        // Use this method to set up necessary states or permissions for roles dynamically.
    }

    /**
     * Override to provide additional requirement instances needed to execute this command.
     */
    public Collection<BitsCommandRequirement> getAddedRequirements() {
        return new ArrayList<>();
    }

    /**
     * Override to provide alternate permission strings for this command.
     * Note: These will NOT be prefixed with the core permission string.
     */
    public Collection<String> getAlternatePermissionStrings() {
        return new ArrayList<>();
    }

}