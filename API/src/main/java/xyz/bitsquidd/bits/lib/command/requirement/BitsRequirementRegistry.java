package xyz.bitsquidd.bits.lib.command.requirement;

import xyz.bitsquidd.bits.lib.command.requirement.impl.ConsoleSenderRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.impl.PlayerSenderRequirement;

import java.util.HashMap;
import java.util.Map;

public class BitsRequirementRegistry<T> {

    private final Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> requirementInstances = new HashMap<>();

    public BitsRequirementRegistry() {
        Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialRequirements = new HashMap<>(initialiseDefaultParsers());
        initialRequirements.putAll(initialiseParsers());
        requirementInstances.putAll(initialRequirements);
    }

    private Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialiseDefaultParsers() {
        return Map.ofEntries(
              Map.entry(PlayerSenderRequirement.class, PlayerSenderRequirement.INSTANCE),
              Map.entry(ConsoleSenderRequirement.class, ConsoleSenderRequirement.INSTANCE)
        );
    }

    protected Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialiseParsers() {
        return Map.of();
    }


    public BitsCommandRequirement getRequirement(Class<? extends BitsCommandRequirement> requirementClass) {
        BitsCommandRequirement requirement = requirementInstances.get(requirementClass);
        if (requirement == null) throw new IllegalArgumentException("No requirement registered for class: " + requirementClass.getName());
        return requirement;
    }


}