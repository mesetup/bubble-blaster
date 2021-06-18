package com.qtech.bubbles.common.crash

import com.qtech.bubbles.core.utils.categories.StringUtils
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.AbstractMap.SimpleEntry

open class CrashCategory @JvmOverloads constructor(val details: String?, t: Throwable? = null) {
    val entries: MutableList<SimpleEntry<String, String>> = ArrayList()
    open var throwable: Throwable? = null
        protected set

    fun add(key: String, value: Any?) {
        require(!key.contains(":")) { "Key cannot contain a colon" }
        require(key.length <= 32) { "Key cannot be longer than 32 characters." }
        entries.add(SimpleEntry(key, value?.toString() ?: "null@0"))
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(details).append(": \r\n")
        println(entries)
        if (entries.size > 0) {
            val simpleEntries = ArrayList(entries)
            for (i in 0 until simpleEntries.size - 1) {
                val (key, value) = simpleEntries[i]
                sb.append("   ")
                sb.append(key)
                sb.append(": ")
                sb.append(value)
                sb.append(System.lineSeparator())
            }
            val (key, value) = simpleEntries[simpleEntries.size - 1]
            sb.append("   ")
            sb.append(key)
            sb.append(": ")
            sb.append(value)
            sb.append(System.lineSeparator())
        }
        val stringWriter = StringWriter()
        val writer = PrintWriter(stringWriter)
        throwable?.printStackTrace(writer)
        writer.flush()
        val buffer = stringWriter.buffer
        try {
            stringWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val s = buffer.toString()
        val strings = StringUtils.splitIntoLines(s)
        val join = "   " + org.apache.commons.lang3.StringUtils.join(strings, System.lineSeparator() + "   ")
        sb.append(join)
        sb.append(System.lineSeparator())
        return sb.toString()
    }

    init {
        throwable = t
    }
}