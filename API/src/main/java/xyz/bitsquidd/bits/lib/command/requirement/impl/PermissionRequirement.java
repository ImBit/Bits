/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.lib.command.requirement.impl;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.lib.command.requirement.BitsCommandRequirement;
import xyz.bitsquidd.bits.lib.command.util.BitsCommandSourceContext;
import xyz.bitsquidd.bits.lib.permission.Permission;
import xyz.bitsquidd.bits.lib.sendable.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A command requirement that checks if the command sender has the specified permissions.
 * Note: Although can't be directly instantiated via annotation, still useful to have a constructor for manual use.
 */
public class PermissionRequirement extends BitsCommandRequirement {
    public final List<Permission> permissions = new ArrayList<>();

    protected PermissionRequirement(Collection<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    public static PermissionRequirement of(Collection<Permission> permissions) {
        return new PermissionRequirement(permissions);
    }

    public static PermissionRequirement of(Permission... permissions) {
        return new PermissionRequirement(List.of(permissions));
    }


    @Override
    public boolean test(BitsCommandSourceContext<?> ctx) {
        return permissions.stream().allMatch(permission -> permission.hasPermission(ctx.getSender()));
    }

    @Override
    public @Nullable Text getFailureMessage(BitsCommandSourceContext<?> ctx) {
        return Text.of(Component.text("You are lacking permissions to use this command."));
    }

}