package xyz.bitsquidd.bits.lib.logging;

import xyz.bitsquidd.bits.lib.component.color.ColorStore;
import xyz.bitsquidd.bits.lib.permission.Permission;

public enum LogType {
    DEBUG("BIT-DEBUG", Permission.of("bits.log.debug"), ColorStore.DEBUG.color, "#"),
    SUCCESS("BIT-SUCCESS", Permission.of("bits.log.success"), ColorStore.SUCCESS.color, "#"),
    INFO("BIT-INFO", Permission.of("bits.log.info"), ColorStore.INFO.color, "#"),
    WARNING("BIT-WARNING", Permission.of("bits.log.warning"), ColorStore.WARNING.color, "#"),
    ERROR("BIT-ERROR", Permission.of("bits.log.error"), ColorStore.ERROR.color, "#"),
    CRITICAL("BIT-CRITICAL", Permission.of("bits.log.critical"), ColorStore.CRITICAL.color, "#"),;

    public final String prefix;
    public final Permission permission;

    public final int color;
    public final String icon;

    LogType(String prefix, Permission permission, int color, String icon) {
        this.prefix = prefix;
        this.permission = permission;
        this.color = color;
        this.icon = icon;
    }
}