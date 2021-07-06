package qtech.hydro.crash

import java.util.AbstractMap.SimpleEntry

open class CrashCategory constructor(val details: String?) {
    val entries: MutableList<SimpleEntry<String, String>> = ArrayList()
    fun add(key: String, value: Any?) {
        require(!key.contains(":")) { "Key cannot contain a colon" }
        require(key.length <= 32) { "Key cannot be longer than 32 characters." }
        entries.add(SimpleEntry(key, value?.toString() ?: "null@0"))
    }

    fun add(key: String, value: () -> Any?) {
        try {
            require(!key.contains(":")) { "Key cannot contain a colon" }
            require(key.length <= 32) { "Key cannot be longer than 32 characters." }
            entries.add(SimpleEntry(key, value.invoke()!!.toString()))
        } catch (t: Throwable) {
            entries.add(SimpleEntry(key, "/!\\ {" + t::class.simpleName + "} /!\\" ));
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("*** ").append(details).append(" *** \n")
        println(entries)
        if (entries.size > 0) {
            val simpleEntries = ArrayList(entries)
            for (i in 0 until simpleEntries.size - 1) {
                val (key, value) = simpleEntries[i]
                sb.append("  ")
                sb.append(key)
                sb.append(": ")
                sb.append(value)
                sb.append("\n")
            }
            val (key, value) = simpleEntries[simpleEntries.size - 1]
            sb.append("  ")
            sb.append(key)
            sb.append(": ")
            sb.append(value)
            sb.append("\n")
        }
        sb.append("\n")
        return sb.toString()
    }
}