package com.qtech.bubbles.event.bus

import com.qtech.bubbles.event.Event
import com.qtech.bubbles.event.EventHandler
import com.qtech.bubbles.event.SubscribeEvent
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Predicate

abstract class EventBus {
    abstract fun <T : Event> post(event: T): Boolean

    inline fun <reified T : Event> addListener(noinline handler: (event: T) -> Unit) {
        this.addListener(T::class.java, handler)
    }

    abstract fun <T : Event> addListener(clazz: Class<T>, handler: (event: T) -> Unit)

    companion object {
        var classPredicate: Predicate<Method>? = null
        var instancePredicate: Predicate<Method>? = null
        private fun isSubscriber(method: Method): Boolean {
            return method.isAnnotationPresent(SubscribeEvent::class.java)
        }

        private fun isEventHandler(method: Method): Boolean {
            val parameterTypes = method.parameterTypes
            if (parameterTypes.size == 1) {
                val clazz1 = parameterTypes[0]
                return Event::class.java.isAssignableFrom(clazz1)
            }
            return false
        }

        init {
            val isHandler: Predicate<Method> = Predicate { method: Method -> isEventHandler(method) }
            val isSubscriber: Predicate<Method> = Predicate { method: Method -> isSubscriber(method) }
            classPredicate = isHandler.and(isSubscriber).and { method: Method -> Modifier.isStatic(method.modifiers) }
            instancePredicate = isHandler.and(isSubscriber).and { method: Method -> !Modifier.isStatic(method.modifiers) }
        }
    }

    abstract class Handler {
        protected abstract fun onRemove()
        abstract fun <T : Event?> getHandlers(clazz: Class<T>?): Collection<EventHandler<T>>?
        abstract fun id(): Long
        fun unbind() {
            onRemove()
        }

        fun <T : Event> handle(event: T) {
            val handlers = getHandlers(event.javaClass as Class<T>?) ?: return
            for (handler in handlers) {
                handler.handle(event)
            }
        }
    }
}