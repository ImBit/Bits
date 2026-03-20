/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util.color;

import org.jetbrains.annotations.Range;

/**
 * A utility class for performing common color operations and transformations.
 *
 * @since 0.0.10
 */
public final class Colors {
    private Colors() {}

    /**
     * Softens or darkens a hexadecimal color code by a given ratio.
     *
     * @param color     the base RGB color as an integer
     * @param lightness the ratio to lighten (0.0 to 1.0) or darken (-1.0 to 0.0)
     *
     * @return the transformed RGB color integer
     *
     * @since 0.0.10
     */
    public static int lightenColour(int color, @Range(from = -1, to = 1) double lightness) {
        lightness = Math.clamp(lightness, -1, 1);

        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        if (lightness >= 0) {
            red = (int)(red + ((255 - red) * lightness));
            green = (int)(green + ((255 - green) * lightness));
            blue = (int)(blue + ((255 - blue) * lightness));
        } else {
            red = (int)(red * (1 + lightness));
            green = (int)(green * (1 + lightness));
            blue = (int)(blue * (1 + lightness));
        }

        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Returns the inverted value of the specified color.
     *
     * @param color the base RGB color as an integer
     *
     * @return the inverted RGB color integer
     *
     * @since 0.0.10
     */
    public static int invertColor(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;

        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Performs a linear interpolation between two colors.
     *
     * @param color1 the starting color
     * @param color2 the target color
     * @param ratio  the interpolation ratio (0.0 for color1, 1.0 for color2)
     *
     * @return the blended color integer
     *
     * @since 0.0.10
     */
    public static int blendColors(int color1, int color2, @Range(from = 0, to = 1) double ratio) {
        ratio = Math.clamp(ratio, 0, 1);

        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;

        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        int red = (int)((red1 * (1 - ratio)) + (red2 * ratio));
        int green = (int)((green1 * (1 - ratio)) + (green2 * ratio));
        int blue = (int)((blue1 * (1 - ratio)) + (blue2 * ratio));

        return (red << 16) | (green << 8) | blue;
    }

}
