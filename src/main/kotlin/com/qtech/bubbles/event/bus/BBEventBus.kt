@file:Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")

package com.qtech.bubbles.event.bus

import com.qtech.bubbles.event.Event
import com.qtech.bubbles.event.ICancellable
import org.apache.logging.log4j.LogManager
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.concurrent.CopyOnWriteArraySet
import java.util.function.Consumer
import java.util.function.Predicate

class BBEventBus : EventBus() {
    val eventToMethod: MutableMap<Class<out Event>, CopyOnWriteArraySet<Pair<Any?, Method>>> = HashMap()
    val methodToEvent: MutableMap<Pair<Any?, Method>, CopyOnWriteArraySet<Class<out Event>>> = HashMap()

    val eventToKFunction: MutableMap<Class<out Event>, CopyOnWriteArraySet<(Event) -> Unit>> = HashMap()
    val kFunctionToEvent: MutableMap<(Event) -> Unit, CopyOnWriteArraySet<Class<out Event>>> = HashMap()

    override fun <T : Event> post(event: T): Boolean {
        if (!eventToMethod.containsKey(event.javaClass)) {
            return false
        }
        val methods = eventToMethod[event.javaClass]!!
        for ((first, second) in methods) {
            try {
                second.invoke(first, event)
            } catch (e: InvocationTargetException) {
                LOGGER.error("Cannot invoke event handler error follows:")
                e.cause!!.printStackTrace()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        if (!eventToKFunction.containsKey(event.javaClass)) {
            return false
        }
        val functions = eventToKFunction[event.javaClass]!!
        for (second in functions) {
            try {
                second.invoke(event)
            } catch (t: Throwable) {
                LOGGER.error("Cannot invoke event handler error follows:")
                t.printStackTrace()
            }
        }
        return event is ICancellable && (event as ICancellable).isCancelled
    }

    override fun <T : Event> addListener(clazz: Class<T>, handler: (event: T) -> Unit) {
        addHandlers(clazz) { event: Event ->
            if (event.javaClass == clazz) handler.invoke(event as T)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun register(clazz: Class<*>) {
        loopDeclaredMethods(clazz) { method: Method ->
            // Get types and values.
            val parameterType = method.parameterTypes[0]
            addHandlers(parameterType as Class<out Event>, null, method)
        }
    }

    fun register(o: Any) {
        loopMethods(o) { method: Method ->
            // Get types and values.
            val event = method.parameterTypes[0] as Class<out Event>
            addHandlers(event, o, method)
        }
    }

    fun unregister(event: Class<out Event>, clazz: Class<*>) {
        loopDeclaredMethods(clazz) { method: Method ->
            // Get and check event.
            val evt = method.parameterTypes[0] as Class<out Event>
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, null, method)
                } catch (ignored: IllegalStateException) {
                }
            }
        }
    }

    fun unregister(event: Class<out Event>, o: Any) {
        loopMethods(o) { method: Method ->
            // Get types and values.
            val evt = method.parameterTypes[0] as Class<out Event>
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, o, method)
                } catch (ignored: IllegalStateException) {
                }
            }
        }
    }

    fun unregister(clazz: Class<*>) {
        loopDeclaredMethods(clazz) { method: Method ->
            // Get and check event.
            val evt = method.parameterTypes[0] as Class<out Event>

            // Remove handler.
            try {
                removeHandlers(evt, null, method)
            } catch (ignored: IllegalStateException) {
            }
        }
    }

    fun unregister(o: Any) {
        loopMethods(o) { method: Method ->
            // Get types and values.
            val evt = method.parameterTypes[0] as Class<out Event>

            // Remove handler.
            try {
                removeHandlers(evt, o, method)
            } catch (ignored: IllegalStateException) {
            }
        }
    }

    private fun loopDeclaredMethods(clazz: Class<*>, consumer: Consumer<Method>) {
        // Loop declared methods.
        loopMethods0(clazz.declaredMethods, classPredicate, consumer)
    }

    private fun loopMethods(o: Any, consumer: Consumer<Method>) {
        // Loop methods.
        loopMethods0(o.javaClass.methods, instancePredicate, consumer)
    }

    private fun loopMethods0(methods: Array<Method>, predicate: Predicate<Method>?, consumer: Consumer<Method>) {
        // Check all methods for event subscribers.
        for (method in methods) {
            // Check is instance method.
            if (instancePredicate!!.test(method)) {
                // Set accessible.
                method.isAccessible = true
                consumer.accept(method)
            }
        }
    }

    private fun removeHandlers(event: Class<out Event>, obj: Any?, method: Method) {
        val pair = Pair(obj, method)
        check(eventToMethod.containsKey(event)) { "Cannot unregister method for a non-registered event." }
        check(eventToMethod[event]!!.contains(pair)) { "Cannot unregister an unregistered method." }
        check(methodToEvent.containsKey(pair)) { "Cannot unregister an unregistered method." }
        check(methodToEvent[pair]!!.contains(event)) { "Cannot unregister method for a non-registered event." }
        methodToEvent[pair]!!.remove(event)
        eventToMethod[event]!!.remove(pair)
    }

    private fun removeAllEvents(obj: Any?, method: Method) {
        val pair = Pair(obj, method)
        check(methodToEvent.containsKey(pair)) { "Cannot unregister an unregistered method." }
        for (event in methodToEvent[pair]!!) {
            eventToMethod[event]!!.remove(pair)
        }
        methodToEvent.remove(pair)
    }

    private fun addHandlers(event: Class<out Event>, obj: Any?, method: Method) {
        val pair = Pair(obj, method)
        if (!eventToMethod.containsKey(event)) {
            eventToMethod[event] = CopyOnWriteArraySet()
        }
        if (!methodToEvent.containsKey(pair)) {
            methodToEvent[pair] = CopyOnWriteArraySet()
        }
        eventToMethod[event]!!.add(pair)
        methodToEvent[pair]!!.add(event)
    }

    private fun addHandlers(event: Class<out Event>, method: (Event) -> Unit) {
        if (!eventToKFunction.containsKey(event)) {
            eventToKFunction[event] = CopyOnWriteArraySet()
        }
        if (!kFunctionToEvent.containsKey(method)) {
            kFunctionToEvent[method] = CopyOnWriteArraySet()
        }
        eventToKFunction[event]!!.add(method)
        kFunctionToEvent[method]!!.add(event)
    }

    companion object {
        private val LOGGER = LogManager.getLogger("AddonEventBus")
    }
}