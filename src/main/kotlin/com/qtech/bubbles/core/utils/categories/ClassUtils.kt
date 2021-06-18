package com.qtech.bubbles.core.utils.categories

object ClassUtils {
    val callerClassName: String?
        get() {
            val stElements = Thread.currentThread().stackTrace
            for (i in 1 until stElements.size) {
                val ste = stElements[i]
                if (ste.className != ClassUtils::class.java.name && ste.className.indexOf("java.lang.Thread") != 0) {
                    return stElements[i + 1].className
                }
            }
            return null
        }
    val callerClass: Class<*>?
        get() {
            val className = callerClassName ?: return null
            return try {
                Class.forName(className)
            } catch (exception: ClassNotFoundException) {
                exception.printStackTrace()
                null
            }
        }

    fun checkCallerClassEquals(clazz: Class<*>) {
        if (callerClass != clazz) throw IllegalCallerException("Called from illegal class, valid class: " + clazz.simpleName)
    }

    fun checkCallerClassExtends(clazz: Class<*>) {
        if (callerClass == null || callerClass!!.isAssignableFrom(clazz)) throw IllegalCallerException("Called from illegal class, valid (extendable) class: " + clazz.simpleName)
    }
}