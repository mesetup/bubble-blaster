package com.qtech.bubbles.event

import com.qtech.bubbles.common.annotation.Cancelable

open class Event {
    val isCancelable: Boolean
        get() = javaClass.isAnnotationPresent(Cancelable::class.java)
}