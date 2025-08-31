package xyz.bitsquidd.bits.lib.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public final class SkullBuilder {
    private static final Material SKULL_MATERIAL = Material.PLAYER_HEAD;

    private final ItemStack item;
    private SkullMeta meta;

    public SkullBuilder() {
        this.item = new ItemStack(SKULL_MATERIAL);
        this.meta = (SkullMeta)item.getItemMeta();
        if (this.meta == null) {
            throw new IllegalStateException("Failed to retrieve SkullMeta from ItemStack");
        }
    }

    public SkullBuilder(@NotNull ItemStack base) {
        if (base.getType() != SKULL_MATERIAL) {
            throw new IllegalArgumentException("Base ItemStack must be a " + SKULL_MATERIAL.toString());
        }
        this.item = base.clone();
        this.meta = (SkullMeta)item.getItemMeta();
        if (this.meta == null) {
            throw new IllegalStateException("Failed to retrieve SkullMeta from base ItemStack");
        }
    }

    public @NotNull ItemStack build() {
        item.setItemMeta(meta);
        return item.clone();
    }

    public SkullBuilder owner(@NotNull OfflinePlayer player) {
        meta.setPlayerProfile(player.getPlayerProfile());
        return this;
    }

    public SkullBuilder owner(@NotNull UUID uuid) {
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        return this;
    }

    public SkullBuilder texture(@NotNull String base64) {
        try {
            PlayerProfile profile = createProfileWithTexture(extractUrlFromBase64(base64));
            meta.setPlayerProfile(profile);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to set skull texture from base64: " + e.getMessage());
        }
        return this;
    }

    public SkullBuilder textureFromUrl(@NotNull String url) {
        try {
            PlayerProfile profile = createProfileWithTexture(url);
            meta.setPlayerProfile(profile);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to set skull texture from URL: " + url + " - " + e.getMessage());
        }
        return this;
    }

    public SkullBuilder profile(@NotNull PlayerProfile profile) {
        meta.setPlayerProfile(profile);
        return this;
    }

    public static SkullBuilder fromBase64(@NotNull String base64) {
        return new SkullBuilder().texture(base64);
    }

    public static SkullBuilder fromUrl(@NotNull String url) {
        return new SkullBuilder().textureFromUrl(url);
    }

    public static SkullBuilder fromUuid(@NotNull UUID uuid) {
        return new SkullBuilder().owner(uuid);
    }

    public static SkullBuilder fromPlayer(@NotNull OfflinePlayer player) {
        return new SkullBuilder().owner(player);
    }

    private PlayerProfile createProfileWithTexture(@NotNull String url) throws MalformedURLException {
        PlayerProfile profile = Bukkit.getServer().createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(new URL(url));
        profile.setTextures(textures);
        return profile;
    }

    private String extractUrlFromBase64(@NotNull String base64) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            String decodedString = new String(decodedBytes);

            int urlStartIndex = decodedString.indexOf("\"url\":\"");
            if (urlStartIndex == -1) {
                throw new IllegalArgumentException("No URL found in base64 texture data");
            }
            urlStartIndex += 7;

            int urlEndIndex = decodedString.indexOf("\"", urlStartIndex);
            if (urlEndIndex == -1) {
                throw new IllegalArgumentException("Malformed URL in base64 texture data");
            }

            return decodedString.substring(urlStartIndex, urlEndIndex);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 string: " + e.getMessage(), e);
        }
    }
}