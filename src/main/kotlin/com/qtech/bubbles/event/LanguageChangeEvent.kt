package com.qtech.bubbles.event

import java.util.*

class LanguageChangeEvent(private val from: Locale, val to: Locale) {
    fun getFrom(): Locale? {
        return from
    }
}