/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import xyz.bitsquidd.bits.lifecycle.builder.Buildable;
import xyz.bitsquidd.bits.log.Logger;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * A builder for player skulls with custom player head textures.
 */
public final class SkullBuilder implements Buildable<ItemStack> {
    private static final Material SKULL_MATERIAL = Material.PLAYER_HEAD;
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private final ItemStack item;
    private final SkullMeta meta;

    public SkullBuilder() {
        this.item = new ItemStack(SKULL_MATERIAL);
        this.meta = (SkullMeta)item.getItemMeta();
        validate();
    }

    /**
     * Passes in an existing skull ItemStack to modify.
     * Note: the item will be mutated directly.
     */
    public SkullBuilder(ItemStack base) {
        if (base.getType() != SKULL_MATERIAL) throw new IllegalArgumentException("Base ItemStack must be a " + SKULL_MATERIAL);
        this.item = base;
        this.meta = (SkullMeta)item.getItemMeta();
        validate();
    }

    private void validate() {
        if (this.item == null) throw new IllegalStateException("Failed to create SkullBuilder: ItemStack is null.");
        if (this.meta == null) throw new IllegalStateException("Failed to create SkullBuilder: ItemMeta is null.");
    }


    @Override
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }


    //region Creators
    public static SkullBuilder fromBase64(final String base64) {
        return new SkullBuilder().textureFromBase64(base64);
    }

    public static SkullBuilder fromUrl(final URL url) {
        return new SkullBuilder().textureFromUrl(url);
    }

    public static SkullBuilder fromUuid(final UUID uuid) {
        return new SkullBuilder().owner(uuid);
    }

    public static SkullBuilder fromPlayer(final OfflinePlayer player) {
        return new SkullBuilder().owner(player);
    }
    //endregion

    //region Builder Methods
    public SkullBuilder owner(final OfflinePlayer player) {
        meta.setPlayerProfile(player.getPlayerProfile());
        return this;
    }

    public SkullBuilder owner(final UUID uuid) {
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        return this;
    }

    public SkullBuilder noteBlockSound(final NamespacedKey noteBlockSound) {
        meta.setNoteBlockSound(noteBlockSound);
        return this;
    }

    public SkullBuilder textureFromBase64(final String base64) {
        try {
            return textureFromUrl(URI.create(extractUrlFromBase64(base64)).toURL());
        } catch (Exception e) {
            Logger.exception("Failed to set skull texture from base64", e);
        }
        return this;
    }

    public SkullBuilder textureFromUrl(final URL url) {
        PlayerProfile profile = createProfileWithTexture(url);
        meta.setPlayerProfile(profile);
        return this;
    }

    public SkullBuilder profile(final PlayerProfile profile) {
        meta.setPlayerProfile(profile);
        return this;
    }
    //endregion


    //region Private Utilities
    private static PlayerProfile createProfileWithTexture(final URL url) {
        PlayerProfile profile = Bukkit.getServer().createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(url);
        profile.setTextures(textures);
        return profile;
    }


    private static String extractUrlFromBase64(final String base64) {
        String decoded = new String(BASE64_DECODER.decode(base64), StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(decoded).getAsJsonObject();
        return json.getAsJsonObject("textures")
          .getAsJsonObject("SKIN")
          .get("url")
          .getAsString();
    }
    //endregion

}