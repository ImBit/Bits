/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import xyz.bitsquidd.bits.paper.gui.type.GuiType;

import java.util.List;
import java.util.function.Function;


/**
 * Static definition of a GUI.
 * These are used to build windows.
 */
public abstract class Gui {
    public static final int CENTER_OFFSET = 80;

    public final GuiType type = type();
    public final Component title = title();
    public final Function<Player, Component> background = this::tempCreateBackground;
    public final Flags flags = flags();


    /**
     * Open this menu for a player
     */
    public final Window addViewer(Player player) {
        Window window = new Window(this, player);
        define(window);
        window.open();
        return window;
    }

    /**
     * Apply this GUI's definition to the given window.
     */
    @ApiStatus.Internal
    protected abstract void define(Window window);

    /**
     * Define the core type of GUI this menu uses e.g. chest, brewing stand, book etc.
     */
    @ApiStatus.Internal
    protected abstract GuiType type();

    /**
     * Define the title of this menu
     */
    @ApiStatus.Internal
    protected abstract Component title();

    /**
     * Define the background of this menu
     */
    @ApiStatus.Internal
    @Deprecated // Use backgroundLayers
    protected List<Component> background() {
        return List.of();
    }

    protected List<BackgroundElement> backgroundLayers() {
        return List.of();
    }

    /**
     * Define any special flags for this menu
     */
    @ApiStatus.Internal
    protected Flags flags() {
        return Flags.flags();
    }


    /**
     * Additional component to layer upon the GUI.
     */
    public Component getAddedComponent(Player viewer) {
        return Component.empty();
    }


    /// GENERATORS
    // TODO remove this, migrate all to background()
    private Component tempCreateBackground(Player viewer) {
        List<BackgroundElement> layers = backgroundLayers();
        if (layers.isEmpty()) {
            return ZEROWIDTH(background());
        } else {
            return BackgroundBuilder.build(layers, type, viewer);
        }
    }

    public final Component generateBackground(Player viewer) {
        return Component.empty()
          .append(COFFSET(CENTER_OFFSET))
          .append(background.apply(viewer))
          .append(CENTER(VERTICAL_OFFSET(SMALLCOMP(title), -11)));
    }


    public static final class Flags {
        public final boolean async;

        public Flags(boolean async) {
            this.async = async;
        }

        public Flags async() {
            return new Flags(true);
        }

        public static Flags flags() {
            return new Flags(
              false
            );
        }

    }

}
