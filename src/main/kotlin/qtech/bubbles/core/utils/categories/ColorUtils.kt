package qtech.bubbles.core.utils.categories

import qtech.bubbles.BubbleBlaster
import qtech.utilities.python.builtins.ValueError
import java.awt.Color

object ColorUtils {
    /**************************************************************************
     * Converts a color hex code (e.g. "#FFFFFF ) to a color instance.
     *
     * @param colorStr e.g. "#FFFFFF" or with alpha "#FFFFFF00"
     * @return a new Color instance based on the color hex code
     * @see Color
     */
    @JvmStatic
    fun hex2Rgb(colorStr: String): Color {
//        BubbleBlaster.logger.debug("" + colorStr.length)
        if (colorStr.length > 9) throw ValueError("Too large color hex code, must be a length of 7 or 9, got: " + colorStr.length)
        if (colorStr.length == 9) {
            if (colorStr[0] != '#') throw ValueError("Invalid color hex code, must start with '#'")
            val chars = colorStr.toCharArray()
            for (i in chars.indices) {
                val c = chars[i]
                if (i > 0 && !"0123456789abcdefABCDEF".contains(String(charArrayOf(c)))) throw ValueError("Invalid color hex code, text after '#' must be hexadecimals")
            }
            return Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16),
                Integer.valueOf(colorStr.substring(7, 9), 16)
            )
        }
        if (colorStr.length == 8) throw ValueError("Too large or small color hex code, must be a length of 7 or 9, got: " + colorStr.length)
        if (colorStr.length < 7) throw ValueError("Too small color hex code, must be a length of 7 or 9, got: " + colorStr.length)
        if (colorStr[0] != '#') throw ValueError("Invalid color hex code, must start with '#'")
        val chars = colorStr.toCharArray()
        for (i in chars.indices) {
            val c = chars[i]
            if (i > 0 && !"0123456789abcdefABCDEF".contains(String(charArrayOf(c)))) throw ValueError("Invalid color hex code, text after '#' must be hexadecimals")
        }
        return Color(
            Integer.valueOf(colorStr.substring(1, 3), 16),
            Integer.valueOf(colorStr.substring(3, 5), 16),
            Integer.valueOf(colorStr.substring(5, 7), 16)
        )
    }

    fun multiConvertHexToRgb(vararg colorStrings: String): Array<Color> {
        val colors = ArrayList<Color>()
        for (colorStr in colorStrings) {
            colors.add(hex2Rgb(colorStr))
        }
        val colorsOut = colors.toArray(arrayOf<Color>())
//        BubbleBlaster.logger.debug(colors.toString())
        return colorsOut
    }
    /**
     * Parse a color string into a color array.<br></br>
     *
     * @param colorString the color string, hex colors separated by a comma.
     * @param addPrefix   add the ‘#’ for every item in the color string.
     * @return an array of colors parsed from the color string.
     */
    @JvmStatic
    @JvmOverloads
    fun parseColorString(colorString: String, addPrefix: Boolean = false): Array<Color> {
        val strings = colorString.split(",").toTypedArray()
        if (addPrefix) {
            for (i in strings.indices) {
                strings[i] = "#" + strings[i]
            }
        }
        return multiConvertHexToRgb(*strings)
    }
}