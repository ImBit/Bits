/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.command.requirement;

import java.util.HashMap;
import java.util.Map;

public class BitsRequirementRegistry<T> {

    private final Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> requirementInstances = new HashMap<>();

    public BitsRequirementRegistry() {
        Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialRequirements = new HashMap<>(initialiseParsers());
        requirementInstances.putAll(initialRequirements);
    }

    protected Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialiseParsers() {
        return new HashMap<>();
    }

    public BitsCommandRequirement getRequirement(Class<? extends BitsCommandRequirement> requirementClass) {
        BitsCommandRequirement requirement = requirementInstances.get(requirementClass);
        if (requirement == null) throw new IllegalArgumentException("No requirement registered for class: " + requirementClass.getName());
        return requirement;
    }

}