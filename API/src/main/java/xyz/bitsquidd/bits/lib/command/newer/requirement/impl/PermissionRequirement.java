package xyz.bitsquidd.bits.lib.command.newer.requirement.impl;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.newer.info.BitsCommandContext;
import xyz.bitsquidd.bits.lib.command.newer.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Although can't be directly instantiated via annotation, still useful to have a constructor for manual use.
public class PermissionRequirement extends BitsCommandRequirement {
    public final @NotNull List<String> permissions = new ArrayList<>();

    public PermissionRequirement(@NotNull Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }

    public PermissionRequirement(@NotNull String permission) {
        this.permissions.add(permission);
    }

    @Override
    public boolean test(@NotNull BitsCommandContext context) {
        return permissions.stream().allMatch(permission -> context.getSender().hasPermission(permission));
    }

    @Override
    public @Nullable Text getFailureMessage(@NotNull BitsCommandContext context) {
        return Text.of(Component.text("You are lacking permissions to use this command."));
    }
}