package xyz.bitsquidd.bits.lib.command.requirement.impl;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Although can't be directly instantiated via annotation, still useful to have a constructor for manual use.
public class PermissionRequirement extends BitsCommandRequirement {
    public final List<String> permissions = new ArrayList<>();

    protected PermissionRequirement(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }

    public static PermissionRequirement of(Collection<String> permissions) {
        return new PermissionRequirement(permissions);
    }

    public static PermissionRequirement of(String... permissions) {
        return new PermissionRequirement(List.of(permissions));
    }


    @Override
    public boolean test(BitsCommandSourceContext ctx) {
        return permissions.stream().allMatch(permission -> ctx.getSender().hasPermission(permission));
    }

    @Override
    public @Nullable Text getFailureMessage(BitsCommandSourceContext ctx) {
        return Text.of(Component.text("You are lacking permissions to use this command."));
    }
}