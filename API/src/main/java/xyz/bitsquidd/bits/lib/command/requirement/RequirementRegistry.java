package xyz.bitsquidd.bits.lib.command.requirement;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.requirement.impl.ConsoleSenderRequirement;
import xyz.bitsquidd.bits.lib.command.requirement.impl.PlayerSenderRequirement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public class RequirementRegistry {
    private static @Nullable RequirementRegistry instance;

    private final Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> requirementInstances = new HashMap<>();

    public RequirementRegistry() {
        if (instance != null) throw new IllegalStateException("RequirementRegistry has already been initialized.");
        instance = this;

        Map<Class<? extends BitsCommandRequirement>, BitsCommandRequirement> initialRequirements = new ArrayList<>(initialiseDefaultParsers());
        initialRequirements.putAll(initialiseParsers());
        requirementInstances.putAll(initialRequirements);
    }

    public static RequirementRegistry getInstance() {
        if (instance == null) throw new IllegalStateException("RequirementRegistry has not been initialized yet.");
        return instance;
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