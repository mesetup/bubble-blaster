@file:Suppress("unused")

package com.qtech.bubbles.util

class CompareUtils {
    companion object {
        fun <T> isGreater(obj: Comparable<T>, than: T): Boolean {
            return obj > than
        }

        fun <T> isGreaterOrEqual(obj: Comparable<T>, than: T): Boolean {
            return obj >= than
        }

        fun <T> isEqual(obj: Comparable<T>, than: T): Boolean {
            return obj == than
        }

        fun <T> isLessEqual(obj: Comparable<T>, than: T): Boolean {
            return obj <= than
        }

        fun <T> isLess(obj: Comparable<T>, than: T): Boolean {
            return obj < than
        }
    }

    init {
        throw ExceptionUtils.utilityClass()
    }
}