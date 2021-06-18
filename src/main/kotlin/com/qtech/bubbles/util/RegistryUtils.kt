package com.qtech.bubbles.util

import com.qtech.bubbles.registry.`object`.ObjectHolder

object RegistryUtils {
    fun getObjectHolder(clazz: Class<*>): ObjectHolder? {
        return clazz.getDeclaredAnnotation(ObjectHolder::class.java)
    }
}