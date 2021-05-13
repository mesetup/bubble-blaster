package com.qsoftware.bubbles.core.utils.categories;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.utilities.python.builtins.ValueError;

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
        QBubbles.getLogger().debug("" + colorStr.length());

        if (colorStr.length() > 9) throw new ValueError("Too large color hex code, must be a length of 7 or 9");
        if (colorStr.length() == 9) {
            if (colorStr.charAt(0) != '#') throw new ValueError("Invalid color hex code, must start with '#'");
            char[] chars = colorStr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (i > 0 && !"0123456789abcdefABCDEF".contains(new String(new char[]{c})))
                    throw new ValueError("Invalid color hex code, text after '#' must be hexadecimals");
            }

            return new Color(
                    Integer.valueOf(colorStr.substring(1, 3), 16),
                    Integer.valueOf(colorStr.substring(3, 5), 16),
                    Integer.valueOf(colorStr.substring(5, 7), 16),
                    Integer.valueOf(colorStr.substring(7, 9), 16));
        }

        if (colorStr.length() == 8)
            throw new ValueError("Too large or small color hex code, must be a length of 7 or 9");
        if (colorStr.length() < 7) throw new ValueError("Too small color hex code, must be a length of 7 or 9");
        if (colorStr.charAt(0) != '#') throw new ValueError("Invalid color hex code, must start with '#'");
        char[] chars = colorStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i > 0 && !"0123456789abcdefABCDEF".contains(new String(new char[]{c})))
                throw new ValueError("Invalid color hex code, text after '#' must be hexadecimals");
        }

        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static Color[] multiConvertHexToRgb(String... colorStrings) {
        ArrayList<Color> colors = new ArrayList<>();

        for (String colorStr : colorStrings) {
            colors.add(hex2Rgb(colorStr));
        }

        Color[] colorsOut = colors.toArray(new Color[]{});
        QBubbles.getLogger().debug(colors.toString());

        return colorsOut;
    }

    public static Color[] parseColorString(String colorString) {
        return parseColorString(colorString, false);
    }

    public static Color[] parseColorString(String colorString, boolean doPrefix) {
        String[] strings = colorString.split(",");
        if (doPrefix) {
            for (int i = 0; i < strings.length; i++) {
                strings[i] = "#" + strings[i];
            }
        }

        return multiConvertHexToRgb(strings);
    }
}
