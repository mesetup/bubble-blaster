package qtech.bubbles.registry.`object`

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS) //@IndexAnnotated(storeJavadoc = true)
annotation class ObjectHolder(val addonId: String, val type: KClass<Class<*>> = Class::class)