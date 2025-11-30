package xyz.bitsquidd.bits.lib.config;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import xyz.bitsquidd.bits.lib.command.BitsCommandManager;
import xyz.bitsquidd.bits.lib.permission.Permission;

import java.util.Locale;
import java.util.Objects;

public abstract class BitsConfig {
    private static @Nullable BitsConfig instance;

    protected final boolean developmentMode = false;
    protected final Logger logger;

    protected @Nullable BitsCommandManager commandManager;


    protected BitsConfig(Logger logger) {
        if (instance != null) throw new IllegalStateException("BitsConfig instance already exists!");
        instance = this;

        this.logger = logger;
    }

    public static BitsConfig get() {
        if (instance == null) throw new IllegalStateException("BitsConfig instance has not been created yet!");
        return instance;
    }

    protected void checkInitialized() {
        if (instance == null) throw new IllegalStateException("BitsConfig hasn't been initialised!");
    }


    public Logger logger() {
        return logger;
    }

    public boolean isDevelopment() {
        return developmentMode;
    }


    public void setCommandManager(BitsCommandManager manager) {
        checkInitialized();
        commandManager = manager;
    }

    public BitsCommandManager getCommandManager() {
        return Objects.requireNonNull(commandManager, "BitsCommandManager has not been set!");
    }


    public abstract boolean hasPermission(Audience audience, Permission permission);

    public abstract Locale getLocale(Audience audience);

    public abstract Audience getAll();

}
