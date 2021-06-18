@file:Suppress("unused")

package com.qtech.bubbles.core.utils.categories

import com.qtech.bubbles.common.maps.SequencedHashMap
import org.apache.commons.lang3.StringUtils
import java.awt.Font
import java.awt.FontMetrics
import java.awt.font.TextAttribute
import java.text.AttributedString

/**
 * Globally available utility classes, mostly for string manipulation.
 *
 * @author Jim Menard: [jimm@io.com](mailto:jimm@io.com), Quinten Jungblut: [q.jungblut@gmail.com](mailto:q.jungblut@gmail.com)
 */
object StringUtils {
    fun count(s: String, c: Char): Int {
        var count = 0
        for (element in s) {
            if (element == c) {
                count++
            }
        }
        return count
    }

    /**
     * Returns an array of strings, one for each line in the string after it has
     * been wrapped to fit lines of <var>maxWidth</var>. Lines end with any of
     * cr, lf, or cr lf. A line ending at the end of the string will not output a
     * further, empty string.
     *
     *
     * This code assumes <var>str</var> is not `null`.
     *
     * @param str      the string to split
     * @param fm       needed for string width calculations
     * @param maxWidth the max line width, in points
     * @return a non-empty list of strings
     */
    @JvmStatic
    fun wrap(str: String, fm: FontMetrics, maxWidth: Int): List<String> {
        val lines = splitIntoLines(str)
        if (lines.isEmpty()) return lines
        val strings = ArrayList<String>()
        for (line in lines) wrapLineInto(line, strings, fm, maxWidth)
        return strings
    }

    /**
     * Given a line of text and font metrics information, wrap the line and add
     * the new line(s) to <var>list</var>.
     *
     * @param line     a line of text
     * @param list     an output list of strings
     * @param fm       font metrics
     * @param maxWidth maximum width of the line(s)
     */
    fun wrapLineInto(line: String, list: MutableList<String>, fm: FontMetrics, maxWidth: Int) {
        var line1 = line
        var len = line1.length
        var width = 0
        while (len > 0 && fm.stringWidth(line1).also { width = it } > maxWidth) {
            // Guess where to split the line. Look for the next space before
            // or after the guess.
            val guess = len * maxWidth / width
            var before = line1.substring(0, guess).trim { it <= ' ' }
            width = fm.stringWidth(before)
            var pos: Int
            if (width > maxWidth) // Too long
                pos = findBreakBefore(line1, guess) else { // Too short or possibly just right
                pos = findBreakAfter(line1, guess)
                if (pos != -1) { // Make sure this doesn't make us too long
                    before = line1.substring(0, pos).trim { it <= ' ' }
                    if (fm.stringWidth(before) > maxWidth) pos = findBreakBefore(line1, guess)
                }
            }
            if (pos == -1) pos = guess // Split in the middle of the word
            list.add(line1.substring(0, pos).trim { it <= ' ' })
            line1 = line1.substring(pos).trim { it <= ' ' }
            len = line1.length
        }
        if (len > 0) list.add(line1)
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or before <var>start</var>. Returns -1 if no such character is
     * found.
     *
     * @param line  a string
     * @param start where to star looking
     */
    fun findBreakBefore(line: String, start: Int): Int {
        for (i in start downTo 0) {
            val c = line[i]
            if (Character.isWhitespace(c) || c == '-') return i
        }
        return -1
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or after <var>start</var>. Returns -1 if no such character is
     * found.
     *
     * @param line  a string
     * @param start where to star looking
     */
    fun findBreakAfter(line: String, start: Int): Int {
        val len = line.length
        for (i in start until len) {
            val c = line[i]
            if (Character.isWhitespace(c) || c == '-') return i
        }
        return -1
    }

    /**
     * Returns an array of strings, one for each line in the string. Lines end
     * with any of cr, lf, or cr lf. A line ending at the end of the string will
     * not output a further, empty string.
     *
     *
     * This code assumes <var>str</var> is not `null`.
     *
     * @param str the string to split
     * @return a non-empty list of strings
     */
    @JvmStatic
    fun splitIntoLines(str: String): List<String> {
        val strings = ArrayList<String>()
        val len = str.length
        if (len == 0) {
            strings.add("")
            return strings
        }
        var lineStart = 0
        val chars = str.toCharArray()
        var i = 0
        while (i < chars.size) {
            when (chars[i]) {
                '\r' -> {
                    var newlineLength = 1
                    if (i + 1 < len && chars[i + 1] == '\n') newlineLength = 2
                    strings.add(str.substring(lineStart, i))
                    lineStart = i + newlineLength
                    if (newlineLength == 2) // skip \n next time through loop
                        i++
                }
                '\n' -> {
                    strings.add(str.substring(lineStart, i))
                    lineStart = i + 1
                }
            }
            ++i
        }
        if (lineStart < len) strings.add(str.substring(lineStart))
        return strings
    }

    @JvmStatic
    fun createFallbackString(text: String, mainFont: Font?): AttributedString {
        val result = AttributedString(text)
        val textLength = text.length
        if (textLength == 0) {
            return AttributedString(text)
        }
        result.addAttribute(TextAttribute.FONT, mainFont, 0, textLength)
        return result

//        boolean fallback = false;
//        Integer fallbackBegin = null;
//        for (int i = 0; i < text.length(); i++) {
//            boolean curFallback = !mainFont.canDisplay(text.charAt(i));
//            if (curFallback != fallback) {
//                fallback = curFallback;
//                if (fallback) {
//                    fallbackBegin = i;
//                } else {
//                    result.addAttribute(TextAttribute.FONT, fallbackFont, fallbackBegin, i);
//                }
//            }
//        }
//        return result;
    }

    fun join(toArray: Array<Any?>?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: ByteArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: CharArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: ShortArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: IntArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: LongArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: FloatArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: DoubleArray?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: Iterator<*>?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: Iterable<*>?, c: Char): String {
        return StringUtils.join(toArray, c)
    }

    fun join(toArray: List<*>?, c: Char, startIndex: Int, endIndex: Int): String {
        return StringUtils.join(toArray, c, startIndex, endIndex)
    }

    fun join(toArray: SequencedHashMap<*, *>, c: Char, startIndex: Int, endIndex: Int): String {
        return StringUtils.join(toArray.sequence(), c, startIndex, endIndex)
    }
}