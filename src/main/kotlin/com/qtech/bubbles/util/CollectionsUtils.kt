package com.qtech.bubbles.util

object CollectionsUtils {
    fun <T : Comparable<T>?> max(coll: Collection<T>, def: T): T {
        val i = coll.iterator()
        if (!i.hasNext()) {
            return def
        }
        var candidate = i.next()
        while (i.hasNext()) {
            val next = i.next()
            if (next!! > candidate) candidate = next
        }
        return candidate
    }
}