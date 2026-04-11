/*
 * This file is part of the BiomeBattle project.
 *
 * Copyright (c) 2023-2026 BitSquidd - All Rights Reserved
 *
 * Unauthorized copying, distribution, or disclosure of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential - for internal use only.
 */

package xyz.bitsquidd.bits.paper.gui;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import xyz.bitsquidd.bits.log.Logger;
import xyz.bitsquidd.bits.mc.sendable.text.Text;
import xyz.bitsquidd.bits.paper.gui.slot.GuiSlot;
import xyz.bitsquidd.bits.paper.inventory.InventoryContents;
import xyz.bitsquidd.bits.paper.util.bukkit.runnable.Runnables;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


// TODO:
//  - Currently items are generated async, we can do better.
//  - We should aim to generate the title async as well.
public final class Window implements InventoryHolder {
    private final Gui gui;
    private final Player viewer;
    private final GuiLayer layer;
    // TODO: private final WindowPropertyContainer savedProperties = new WindowPropertyContainer();

    private @Nullable BukkitTask ticker;
    private Inventory inventory;

    private final Map<GuiSlot, GuiItem> itemCache = new ConcurrentHashMap<>();
    private final Map<GuiSlot, OffsetComponent> componentCache = new ConcurrentHashMap<>();
    private final InventoryContents inventoryItems = InventoryContents.empty();

    public Window(Gui gui, Player viewer) {
        this.gui = gui;
        this.viewer = viewer;

        this.layer = GuiLayer.empty()
          .addLayer(GuiLayer.filled(GuiSlot.all(gui.type), GuiItem::empty));

        buildInv(buildTitle(Component.empty()), InventoryContents.empty());
    }

    public void open() {
        // First thing on open
        PaperManagers.gui().pushGuiInternal(viewer, gui);

        ticker = Runnables.timer(this::tick, 0L, 1L);
        renderAll();
    }

    private void tick() {
        Set<GuiSlot> updates = layer.fetchUpdates();
        if (!updates.isEmpty()) render(updates);
    }

    public void close() {
        ticker = Runnables.cleanup(ticker);
        if (viewer.getEntity().getOpenInventory().getTopInventory() == inventory) {
            viewer.getEntity().closeInventory();
        }

        PaperManagers.gui().popGuiInternal(viewer, gui);

        onClose();
    }

    public void onClose() {
        inventoryItems.apply(viewer);
        PaperManagers.gui().unregisterInventoryInternal(inventory);
    }

    public GuiLayer getLayer() {
        return layer;
    }


    public void renderAll() {
        render(GuiSlot.all(gui.type));
    }

    private void render(Set<GuiSlot> slots) {
        updateTitle(slots);
        updateContent(slots);
    }


    private void updateTitle(Set<GuiSlot> slots) {
        AtomicBoolean titleChanged = new AtomicBoolean(false);

        slots.parallelStream().forEach((slot) ->
          layer.getItem(slot).ifPresentOrElse(
            item -> {
                OffsetComponent component = item.component();

                if (component != null) {
                    OffsetComponent existing = componentCache.get(slot);
                    if (!component.equals(existing)) {
                        componentCache.put(slot, component);
                        titleChanged.set(true);
                    }
                } else {
                    if (componentCache.remove(slot) != null) {
                        titleChanged.set(true);
                    }
                }
            }, () -> {
                if (componentCache.remove(slot) != null) {
                    titleChanged.set(true);
                }
            }
          )
        );

        if (titleChanged.get()) rebuildInventory();
    }

    private void rebuildInventory() {
        if (inventory == null) throw new IllegalStateException("Inventory is not initialised yet.");

        InventoryContents invContents = InventoryContents.of(inventory);
        PaperManagers.gui().unregisterInventoryInternal(inventory);

        buildInv(buildTitle(buildAddedTitle()), invContents);
    }

    private void buildInv(Component title, InventoryContents contents) {
        inventory = gui.type.createInventory(title);
        contents.forEach(slotContent -> inventory.setItem(slotContent.slot, slotContent.item)); // Restore previous content.

        PaperManagers.gui().registerInventoryInternal(this, inventory);
        viewer.getEntity().openInventory(inventory);
    }

    private Component buildTitle(Component addedTitle) {
        return ComponentHelper.BLANK
          .append(ZEROWIDTH(gui.generateBackground(viewer)))
          .append(ZEROWIDTH(addedTitle))
          .append(ZEROWIDTH(gui.getAddedComponent(viewer)));
    }

    private Component buildAddedTitle() {
        Component addedTitle = Component.empty();

        for (Map.Entry<GuiSlot, OffsetComponent> entry : componentCache.entrySet()) {
            GuiSlot slot = entry.getKey();
            OffsetComponent component = entry.getValue();

            addedTitle = addedTitle.append(ZEROWIDTH(ALL_OFFSET(
              gui.type.getPosition(slot).add(component.offset()),
              ComponentHelper.getTranslatedComponent(viewer, component.component())
            )));
        }

        return addedTitle;
    }


    private void updateContent(Set<GuiSlot> slots) {
        slots.parallelStream().forEach((slot) ->
          layer.getItem(slot).ifPresentOrElse(
            item -> setItemInInventory(item, slot),
            () -> setItemInInventory(null, slot)
          )
        );
    }

    private void setItemInInventory(@Nullable GuiItem item, GuiSlot slot) {
        try {
            int slotIndex = slot.index();

            GuiItem replacedItem = itemCache.get(slot);
            if (replacedItem != null && replacedItem != item) {
                replacedItem.setOnUpdate(null);
                itemCache.remove(slot);
            }
            if (item != null) {
                // Schedule update in the main layer for this. Note: we could consider directly calling the update in the Window. This might be bad for caching.
                item.setOnUpdate(() -> layer.update(Collections.singleton(slot)));
                itemCache.put(slot, item);
            }

            FutureHelper.async(() -> postProcessItem(item)).thenCompose(itemStack -> FutureHelper.main(() -> {
                switch (slot.slotType()) {
                    case PLAYER -> {
                        if (!inventoryItems.hasItem(slotIndex)) {
                            inventoryItems.set(slotIndex, viewer.getInventory().getItem(slotIndex));
                        }
                        viewer.getInventory().setItem(slotIndex, itemStack);
                    }
                    case GUI -> {
                        getInventory().setItem(slotIndex, itemStack);
                    }
                }
            }));
        } catch (Exception e) {
            Logger.warn("Attempted to render invalid item in slot " + slot + ": " + e.getMessage());
        }
    }

    private @Nullable ItemStack postProcessItem(@Nullable GuiItem item) {
        if (item == null) return null;

        ItemStack itemStack = LoreHelper.refreshItemSimple(item.itemStack(), viewer);
        Items.Utility.immovable(itemStack);

        return itemStack;
    }


    ///  GETTERS
    ///
    @Override
    public Inventory getInventory() {
        if (inventory == null) throw new IllegalStateException("Inventory is not initialised yet.");
        return inventory;
    }

    public Gui gui() {
        return gui;
    }

    public BiomePlayer viewer() {
        return viewer;
    }


    ///  MEDIA
    ///
    public void playSound(final BBSound sound) {
        SoundEmitter.builder(viewer)
          .audience(viewer)
          .category(Sound.Source.UI)
          .build()
          .play(sound);
    }

    public void respond(final Text text) {
        text.send(viewer);
    }

    public void onClick(@Nullable ItemStack clickedItem, GuiSlot slot, ClickType clickType) {
        WindowClickEvent event = new WindowClickEvent(this, slot, clickType);
        boolean isClicked = layer.onClick(event);
        if (isClicked) event.setCancelled(true);
    }

}
