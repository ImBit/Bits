package xyz.bitsquidd.bits.lib.permission;

import net.kyori.adventure.audience.Audience;

import xyz.bitsquidd.bits.lib.config.BitsConfig;

/**
 * Object representation of a permission.
 */
public final class Permission {
    private final String value;
    private final String description;


    private Permission(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Permission that = (Permission)obj;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public Permission append(String suffix) {
        return Permission.of(value + suffix);
    }

    public static Permission all() {
        return new Permission("", "Grants all permissions");
    }

    public static Permission of(String name) {
        return new Permission(name, "");
    }

    public static Permission of(String name, String description) {
        return new Permission(name, description);
    }


    public boolean hasPermission(Audience audience) {
        return value.isEmpty() || BitsConfig.get().hasPermission(audience, this);
    }

    public void register() {
        BitsConfig.get().registerPermission(this);
    }

}
