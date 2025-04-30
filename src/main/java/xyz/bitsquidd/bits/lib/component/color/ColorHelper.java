package xyz.bitsquidd.bits.lib.component.color;

public class ColorHelper {

    public static int lightenColour(int color, float lightness) {
        lightness = Math.clamp(lightness, 0, 1);

        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = (int) (red + ((255 - red) * lightness));
        green = (int) (green + ((255 - green) * lightness));
        blue = (int) (blue + ((255 - blue) * lightness));

        return (red << 16) | (green << 8) | blue;
    }
}
