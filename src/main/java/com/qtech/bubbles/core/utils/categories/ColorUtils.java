package com.qtech.bubbles.core.utils.categories;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.utilities.python.builtins.ValueError;

import java.awt.*;
import java.util.ArrayList;

public class ColorUtils {
    /**************************************************************************
     * Converts a color hex code (e.g. "#FFFFFF ) to a color instance.
     *
     * @param colorStr e.g. "#FFFFFF" or with alpha "#FFFFFF00"
     * @return a new Color instance based on the color hex code
     * @see Color
     */
    public static Color hex2Rgb(String colorStr) {
        BubbleBlaster.getLogger().debug("" + colorStr.length());

        if (colorStr.length() > 9) throw new ValueError("Too large color hex code, must be a length of 7 or 9, got: " + colorStr.length());
        if (colorStr.length() == 9) {
            testColorHex(colorStr);

            return new Color(
                    Integer.valueOf(colorStr.substring(1, 3), 16),
                    Integer.valueOf(colorStr.substring(3, 5), 16),
                    Integer.valueOf(colorStr.substring(5, 7), 16),
                    Integer.valueOf(colorStr.substring(7, 9), 16));
        }

        if (colorStr.length() == 8)
            throw new ValueError("Too large or small color hex code, must be a length of 7 or 9, got: " + colorStr.length());
        if (colorStr.length() < 7)
            throw new ValueError("Too small color hex code, must be a length of 7 or 9, got: " + colorStr.length());
        testColorHex(colorStr);

        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    private static void testColorHex(String colorStr) {
        if (colorStr.charAt(0) != '#') throw new ValueError("Invalid color hex code, must start with '#'");
        char[] chars = colorStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            //noinspection SpellCheckingInspection
            if (i > 0 && !"0123456789abcdefABCDEF".contains(String.valueOf(c)))
                throw new ValueError("Invalid color hex code, text after '#' must be hexadecimals");
        }
    }

    public static Color[] multiConvertHexToRgb(String... colorStrings) {
        ArrayList<Color> colors = new ArrayList<>();

        for (String colorStr : colorStrings) {
            colors.add(hex2Rgb(colorStr));
        }

        Color[] colorsOut = colors.toArray(new Color[]{});
        BubbleBlaster.getLogger().debug(colors.toString());

        return colorsOut;
    }

    /**
     * Parse a color string into a color array.<br>
     * <b>Note: <i>this doesn't add the ‘#’ prefixes, so use only hex digits for the colors.</i></b><br>
     * <br>
     * Same as <code>parseColorString(colorString, false)</code>.<br>
     *
     * @param colorString the color string, hex colors separated by a comma.
     * @return an array of colors parsed from the color string.
     */
    public static Color[] parseColorString(String colorString) {
        return parseColorString(colorString, false);
    }

    /**
     * Parse a color string into a color array.<br>
     *
     * @param colorString the color string, hex colors separated by a comma.
     * @param addPrefix add the ‘#’ for every item in the color string.
     * @return an array of colors parsed from the color string.
     */
    public static Color[] parseColorString(String colorString, boolean addPrefix) {
        String[] strings = colorString.split(",");
        if (addPrefix) {
            for (int i = 0; i < strings.length; i++) {
                strings[i] = "#" + strings[i];
            }
        }

        return multiConvertHexToRgb(strings);
    }
}
