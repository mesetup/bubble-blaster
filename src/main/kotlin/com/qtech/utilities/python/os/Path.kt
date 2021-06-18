@file:Suppress("unused")

package com.qtech.utilities.python.os

import java.nio.file.Paths
import java.util.*

object Path {
    private const val STRING_BUILDER_SIZE = 256
    // Performance testing notes (JDK 1.4, Jul03, scolebourne)
    // Whitespace:
    // Character.isWhitespace() is faster than WHITESPACE.indexOf()
    // where WHITESPACE is a string of all whitespace characters
    //
    // Character access:
    // String.charAt(n) versus toCharArray(), then array[n]
    // String.charAt(n) is about 15% worse for a 10K string
    // They are about equal for a length 50 string
    // String.charAt(n) is about 4 times better for a length 3 string
    // String.charAt(n) is best bet overall
    //
    // Append:
    // String.concat about twice as fast as StringBuffer.append
    // (not sure who tested this)
    /**
     * A String for a space character.
     *
     * @since 3.2
     */
    const val SPACE = " "

    /**
     * The empty String `""`.
     *
     * @since 2.0
     */
    const val EMPTY = ""

    /**
     * A String for linefeed LF ("\n").
     *
     * @see [JLF: Escape Sequences
     * for Character and String Literals](http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html.jls-3.10.6)
     *
     * @since 3.2
     */
    const val LF = "\n"

    /**
     * A String for carriage return CR ("\r").
     *
     * @see [JLF: Escape Sequences
     * for Character and String Literals](http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html.jls-3.10.6)
     *
     * @since 3.2
     */
    const val CR = "\r"

    /**
     * Represents a failed index search.
     *
     * @since 2.1
     */
    const val INDEX_NOT_FOUND = -1

    /**
     *
     * The maximum size to which the padding constant(s) can expand.
     */
    private const val PAD_LIMIT = 8192
    fun join(path: String, vararg paths: String): String {
        val strings: MutableList<String> = ArrayList<String>()
        strings.add(path)
        strings.addAll(paths.asList())
        return Paths.get(path, *paths).toString()
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: ByteArray?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
     * array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
     * the array
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: ByteArray?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        buf.append(array[startIndex])
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            buf.append(array[i])
        }
        return buf.toString()
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: CharArray?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
     * array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
     * the array
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: CharArray?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        buf.append(array[startIndex])
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            buf.append(array[i])
        }
        return buf.toString()
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: DoubleArray?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
     * array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
     * the array
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: DoubleArray?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        buf.append(array[startIndex])
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            buf.append(array[i])
        }
        return buf.toString()
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: FloatArray?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
     * array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
     * the array
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: FloatArray?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        buf.append(array[startIndex])
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            buf.append(array[i])
        }
        return buf.toString()
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: IntArray?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
     * array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
     * the array
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: IntArray?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        buf.append(array[startIndex])
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            buf.append(array[i])
        }
        return buf.toString()
    }

    /**
     *
     * Joins the elements of the provided `Iterable` into
     * a single String containing the provided elements.
     *
     *
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.
     *
     *
     * See the examples here: [.join].
     *
     * @param iterable  the `Iterable` providing the values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null iterator input
     * @since 2.3
     */
    fun join(iterable: Iterable<*>?, separator: Char): String? {
        return if (iterable == null) {
            null
        } else join(iterable.iterator(), separator)
    }

    /**
     *
     * Joins the elements of the provided `Iterable` into
     * a single String containing the provided elements.
     *
     *
     * No delimiter is added before or after the list.
     * A `null` separator is the same as an empty String ("").
     *
     *
     * See the examples here: [.join].
     *
     * @param iterable  the `Iterable` providing the values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, `null` if null iterator input
     * @since 2.3
     */
    fun join(iterable: Iterable<*>?, separator: String?): String? {
        return if (iterable == null) {
            null
        } else join(iterable.iterator(), separator)
    }

    /**
     *
     * Joins the elements of the provided `Iterator` into
     * a single String containing the provided elements.
     *
     *
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.
     *
     *
     * See the examples here: [.join].
     *
     * @param iterator  the `Iterator` of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null iterator input
     * @since 2.0
     */
    fun join(iterator: Iterator<*>?, separator: Char): String? {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null
        }
        if (!iterator.hasNext()) {
            return EMPTY
        }
        val first = iterator.next()
        if (!iterator.hasNext()) {
            return Objects.toString(first, EMPTY)
        }

        // two or more elements
        val buf = StringBuilder(STRING_BUILDER_SIZE) // Java default is 16, probably too small
        if (first != null) {
            buf.append(first)
        }
        while (iterator.hasNext()) {
            buf.append(separator)
            val obj = iterator.next()
            if (obj != null) {
                buf.append(obj)
            }
        }
        return buf.toString()
    }

    /**
     *
     * Joins the elements of the provided `Iterator` into
     * a single String containing the provided elements.
     *
     *
     * No delimiter is added before or after the list.
     * A `null` separator is the same as an empty String ("").
     *
     *
     * See the examples here: [.join].
     *
     * @param iterator  the `Iterator` of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, `null` if null iterator input
     */
    fun join(iterator: Iterator<*>?, separator: String?): String? {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null
        }
        if (!iterator.hasNext()) {
            return EMPTY
        }
        val first = iterator.next()
        if (!iterator.hasNext()) {
            return Objects.toString(first, "")
        }

        // two or more elements
        val buf = StringBuilder(STRING_BUILDER_SIZE) // Java default is 16, probably too small
        if (first != null) {
            buf.append(first)
        }
        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator)
            }
            val obj = iterator.next()
            if (obj != null) {
                buf.append(obj)
            }
        }
        return buf.toString()
    }

    /**
     *
     * Joins the elements of the provided `List` into a single String
     * containing the provided list of elements.
     *
     *
     * No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
    </pre> *
     *
     * @param list       the `List` of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from.  It is
     * an error to pass in a start index past the end of the list
     * @param endIndex   the index to stop joining from (exclusive). It is
     * an error to pass in an end index past the end of the list
     * @return the joined String, `null` if null list input
     * @since 3.8
     */
    fun join(list: List<*>?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (list == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val subList = list.subList(startIndex, endIndex)
        return join(subList.iterator(), separator)
    }

    /**
     *
     * Joins the elements of the provided `List` into a single String
     * containing the provided list of elements.
     *
     *
     * No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
    </pre> *
     *
     * @param list       the `List` of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from.  It is
     * an error to pass in a start index past the end of the list
     * @param endIndex   the index to stop joining from (exclusive). It is
     * an error to pass in an end index past the end of the list
     * @return the joined String, `null` if null list input
     * @since 3.8
     */
    fun join(list: List<*>?, separator: String?, startIndex: Int, endIndex: Int): String? {
        if (list == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val subList = list.subList(startIndex, endIndex)
        return join(subList.iterator(), separator)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: LongArray?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     *
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     *
     *
     *
     *
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
     * by empty strings.
     *
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
     * StringUtils.join([1, 2, 3], null) = "123"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
     * array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
     * the array
     * @return the joined String, `null` if null array input
     * @since 3.2
     */
    fun join(array: LongArray?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        buf.append(array[startIndex])
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            buf.append(array[i])
        }
        return buf.toString()
    }

    /**
     *
     * Joins the elements of the provided array into a single String
     * containing the provided list of elements.
     *
     *
     * No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, `null` if null array input
     * @since 2.0
     */
    fun join(array: Array<Any?>?, separator: Char): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     * Joins the elements of the provided array into a single String
     * containing the provided list of elements.
     *
     *
     * No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([Nothing], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from.  It is
     * an error to pass in a start index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is
     * an error to pass in an end index past the end of the array
     * @return the joined String, `null` if null array input
     * @since 2.0
     */
    fun join(array: Array<Any?>?, separator: Char, startIndex: Int, endIndex: Int): String? {
        if (array == null) {
            return null
        }
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        if (array[startIndex] != null) {
            buf.append(array[startIndex])
        }
        for (i in startIndex + 1 until endIndex) {
            buf.append(separator)
            if (array[i] != null) {
                buf.append(array[i])
            }
        }
        return buf.toString()
    }

    /**
     *
     * Joins the elements of the provided array into a single String
     * containing the provided list of elements.
     *
     *
     * No delimiter is added before or after the list.
     * A `null` separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([Nothing], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
    </pre> *
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, `null` if null array input
     */
    fun join(array: Array<Any?>?, separator: String?): String? {
        return if (array == null) {
            null
        } else join(array, separator, 0, array.size)
    }

    /**
     *
     * Joins the elements of the provided array into a single String
     * containing the provided list of elements.
     *
     *
     * No delimiter is added before or after the list.
     * A `null` separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.
     *
     * <pre>
     * StringUtils.join(null, *, *, *)                = null
     * StringUtils.join([], *, *, *)                  = ""
     * StringUtils.join([Nothing], *, *, *)              = ""
     * StringUtils.join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], "--", 1, 3)  = "b--c"
     * StringUtils.join(["a", "b", "c"], "--", 2, 3)  = "c"
     * StringUtils.join(["a", "b", "c"], "--", 2, 2)  = ""
     * StringUtils.join(["a", "b", "c"], null, 0, 3)  = "abc"
     * StringUtils.join(["a", "b", "c"], "", 0, 3)    = "abc"
     * StringUtils.join([null, "", "a"], ',', 0, 3)   = ",,a"
    </pre> *
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from.
     * @param endIndex   the index to stop joining from (exclusive).
     * @return the joined String, `null` if null array input; or the empty string
     * if `endIndex - startIndex <= 0`. The number of joined entries is given by
     * `endIndex - startIndex`
     * @throws ArrayIndexOutOfBoundsException ife<br></br>
     * `startIndex < 0` or <br></br>
     * `startIndex >= array.length()` or <br></br>
     * `endIndex < 0` or <br></br>
     * `endIndex > array.length()`
     */
    fun join(array: Array<Any?>?, separator: String?, startIndex: Int, endIndex: Int): String? {
        var sep = separator
        if (array == null) {
            return null
        }
        if (sep == null) {
            sep = EMPTY
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        val noOfItems = endIndex - startIndex
        if (noOfItems <= 0) {
            return EMPTY
        }
        val buf = StringBuilder(noOfItems)
        if (array[startIndex] != null) {
            buf.append(array[startIndex])
        }
        for (i in startIndex + 1 until endIndex) {
            buf.append(sep)
            if (array[i] != null) {
                buf.append(array[i])
            }
        }
        return buf.toString()
    }
}