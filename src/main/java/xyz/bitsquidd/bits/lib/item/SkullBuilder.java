package xyz.bitsquidd.bits.lib.item;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullBuilder {
    private static final Material SKULL_MATERIAL = Material.PLAYER_HEAD;

    private final ItemStack item;

    public SkullBuilder() {
        this.item = new ItemStack(SKULL_MATERIAL);
    }

    public SkullBuilder(ItemStack base) {
        this.item = base.clone();
    }

    public SkullBuilder owner(OfflinePlayer player) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setPlayerProfile(player.getPlayerProfile());
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return item.clone();
    }
}