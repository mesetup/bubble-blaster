package qtech.bubbles.event.bus

import qtech.bubbles.event.Event
import qtech.bubbles.event.EventHandler
import qtech.bubbles.event.SubscribeEvent
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.BiPredicate
import java.util.function.Predicate
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubclassOf

abstract class EventBus {
    abstract fun <T : Event> post(event: T): Boolean

    inline fun <reified T : Event> addListener(noinline handler: (event: T) -> Unit) {
        this.`access$addListener`(T::class.java, handler)
    }

    protected abstract fun <T : Event> addListener(clazz: Class<T>, handler: (event: T) -> Unit)

    inline fun <reified T : Event> addListenerK(noinline handler: (event: T) -> Unit) {
        this.`access$addListenerK`(T::class, handler)
    }

    protected abstract fun <T : Event> addListenerK(clazz: KClass<T>, handler: (event: T) -> Unit)

    companion object {
        var classPredicate: Predicate<Method>
        var instancePredicate: Predicate<Method>
        var kClassPredicate: Predicate<KFunction<*>>
        var kInstancePredicate: BiPredicate<KFunction<*>, Any>
        private fun isSubscriber(method: Method): Boolean {
            return method.isAnnotationPresent(SubscribeEvent::class.java)
        }
        private fun isSubscriber(method: KFunction<*>): Boolean {
            for (annotation in method.annotations) {
                if (annotation is SubscribeEvent) {
                    return true
                }
            }
            return false
        }

        private fun isEventHandler(method: Method): Boolean {
            val parameterTypes = method.parameterTypes
            if (parameterTypes.size == 1) {
                val `class` = parameterTypes[0]
                return Event::class.java.isAssignableFrom(`class`)
            }
            return false
        }

        private fun isEventHandler(method: KFunction<*>): Boolean {
            val parameterTypes = method.parameters
            if (parameterTypes.size == 1) {
                val `class` = parameterTypes[0]
                return (`class`.type.classifier as KClass<*>).isSubclassOf(Event::class)
            }
            return false
        }

        private fun isEventHandler(method: KFunction<*>, o: Any): Boolean {
            val parameterTypes = method.parameters
            if (parameterTypes.size == 2) {
                val class1 = parameterTypes[0]
                if ((class1.type.classifier as KClass<*>).isInstance(o)) {
                    val `class` = parameterTypes[1]
                    return (`class`.type.classifier as KClass<*>).isSubclassOf(Event::class)
                }
                return false
            }
            return false
        }

        init {
            val isHandler: Predicate<Method> = Predicate { method: Method -> isEventHandler(method) }
            val isKHandler: Predicate<KFunction<*>> = Predicate { method: KFunction<*> -> isEventHandler(method) }
            val isKIHandler: BiPredicate<KFunction<*>, Any> = BiPredicate { method: KFunction<*>, o: Any -> isEventHandler(method, o) }
            val isSubscriber: Predicate<Method> = Predicate { method: Method -> isSubscriber(method) }
            val isKSubscriber: Predicate<KFunction<*>> = Predicate { method: KFunction<*> -> isSubscriber(method) }
            val isKISubscriber: BiPredicate<KFunction<*>, Any> = BiPredicate { method: KFunction<*>, _: Any -> isSubscriber(method) }
            classPredicate = isHandler.and(isSubscriber).and { method: Method -> Modifier.isStatic(method.modifiers) }
            instancePredicate = isHandler.and(isSubscriber).and { method: Method -> !Modifier.isStatic(method.modifiers) }
            kClassPredicate = isKHandler.and(isKSubscriber)
            kInstancePredicate = isKIHandler.and(isKISubscriber)
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

    @Suppress("unused", "FunctionName")
    @PublishedApi
    internal fun <T : Event> `access$addListener`(clazz: Class<T>, handler: (event: T) -> Unit) =
        addListener(clazz, handler)

    @Suppress("unused", "FunctionName")
    @PublishedApi
    internal fun <T : Event> `access$addListenerK`(clazz: KClass<T>, handler: (event: T) -> Unit) =
        addListenerK(clazz, handler)

    abstract fun register(`class`: Class<*>)
    abstract fun register(o: Any)
    abstract fun registerK(`class`: KClass<*>)
    abstract fun registerK(o: Any)
    abstract fun unregister(`class`: Class<*>)
    abstract fun unregister(o: Any)
    abstract fun unregisterK(`class`: KClass<*>)
    abstract fun unregisterK(o: Any)
    abstract fun unregister(event: Class<out Event>, `class`: Class<*>)
    abstract fun unregister(event: Class<out Event>, o: Any)
    abstract fun unregisterK(event: KClass<out Event>, `class`: KClass<*>)
    abstract fun unregisterK(event: KClass<out Event>, o: Any)
}