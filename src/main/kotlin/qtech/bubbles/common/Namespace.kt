@file:Suppress("SpellCheckingInspection")

package qtech.bubbles.common

import qtech.bubbles.core.exceptions.IllegalCharacterException

open class Namespace(namespace: String) {
    open val namespace: String = namespace

    override fun toString(): String {
        return namespace
    }

    init {
        val firstAllowed = "abcdefghijklmnopqrstuvwxyz"
        val allowed = "abcdefghijklmnopqrstuvwxyz_0123456789"
        val lastAllowed = "abcdefghijklmnopqrstuvwxyz0123456789"
        val chars = namespace.toCharArray()

        // Check name containment.
        if (!firstAllowed.contains(String(charArrayOf(chars[0])))) {
            throw IllegalCharacterException("Namespace starts with invalid char: ")
        }
        if (!lastAllowed.contains(String(charArrayOf(chars[chars.size - 1])))) {
            throw IllegalCharacterException("Namespace ends with invalid char: ")
        }
        var i = 1
        while (i < chars.size - 1) {
            if (!allowed.contains(String(charArrayOf(chars[i])))) {
                throw IllegalCharacterException("Namespace contains invalid char: " + chars[i])
            }
            i++
            i++
        }
    }
}