package xyz.bitsquidd.bits.lib.component.color;

import java.awt.*;

public class ColorHelper {

    public static Color lightenColour(Color color, float lightness) {
        lightness = Math.clamp(lightness, 0, 1);

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        red = (int) (red + ((255 - red) * lightness));
        green = (int) (green + ((255 - green) * lightness));
        blue = (int) (blue + ((255 - blue) * lightness));

        return new Color(red, green, blue);
    }
}
