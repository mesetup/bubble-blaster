package qtech.bubbles.util

import kotlin.jvm.kotlin
import kotlin.reflect.KClass

object KotlinUtils {
    @JvmStatic
    fun <T : Any> getKClass(cls: Class<T>): KClass<T> {
        return cls.kotlin
    }
    @JvmStatic
    fun <T : Any> getKClass(t: T): KClass<out T> {
        return t::class
    }
    @JvmStatic
    fun <T : Any> getClass(cls: KClass<T>): Class<T> {
        return cls.java
    }
    @JvmStatic
    fun <T : Any> getClass(t: T): Class<out T> {
        return t::class.java
    }
}