@file:Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")

package qtech.bubbles.event.bus

import qtech.bubbles.event.Event
import qtech.bubbles.event.ICancellable
import org.apache.logging.log4j.LogManager
import qtech.bubbles.event.FilterEvent
import qtech.bubbles.event.SubscribeEvent
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.concurrent.CopyOnWriteArraySet
import java.util.function.BiPredicate
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

class BBEventBus : EventBus() {
    val eventToMethod: MutableMap<Class<out Event>, CopyOnWriteArraySet<Pair<Any?, Method>>> = HashMap()
    val methodToEvent: MutableMap<Pair<Any?, Method>, CopyOnWriteArraySet<Class<out Event>>> = HashMap()

    val eventToKFunction: MutableMap<Class<out Event>, CopyOnWriteArraySet<(Event) -> Unit>> = HashMap()
    val kFunctionToEvent: MutableMap<(Event) -> Unit, CopyOnWriteArraySet<Class<out Event>>> = HashMap()

    val kEventToKFunction: MutableMap<KClass<out Event>, CopyOnWriteArraySet<Pair<Any?, KFunction<*>>>> = HashMap()
    val kFunctionToKEvent: MutableMap<Pair<Any?, KFunction<*>>, CopyOnWriteArraySet<KClass<out Event>>> = HashMap()

    override fun <T : Event> post(event: T): Boolean {
//        if (event !is FilterEvent) {
//            println(eventToKFunction)
//            println(event)
//        }
        if (eventToKFunction.containsKey(event.javaClass)) {
            val functions = eventToKFunction[event.javaClass]!!
            for (second in functions) {
                try {
                    second.invoke(event)
                } catch (t: Throwable) {
                    LOGGER.error("Cannot invoke event handler error follows:")
                    t.printStackTrace()
                }
            }
        }
//        if (event !is FilterEvent) {
//            println(kEventToKFunction)
//            println(event)
//        }
        if (kEventToKFunction.containsKey(event::class)) {
            val kFunctions = kEventToKFunction[event::class]!!
            for ((first, second) in kFunctions) {
                try {
                    second.call(first, event)
                } catch (t: Throwable) {
                    LOGGER.error("Cannot invoke event handler error follows:")
                    t.printStackTrace()
                }
            }
        }
        if (eventToMethod.containsKey(event.javaClass)) {
            val methods = eventToMethod[event.javaClass]!!
            for ((first, second) in methods) {
                try {
                    second.invoke(first, event)
                } catch (t: Throwable) {
                    LOGGER.error("Cannot invoke event handler error follows:")
                    t.printStackTrace()
                }
            }
        }
        return event is ICancellable && (event as ICancellable).isCancelled
    }

    override fun <T : Event> addListener(clazz: Class<T>, handler: (event: T) -> Unit) {
        addHandlers(clazz) { event: Event ->
            if (event.javaClass == clazz) handler.invoke(event as T)
        }
    }

    override fun <T : Event> addListenerK(clazz: KClass<T>, handler: (event: T) -> Unit) {
        addListener(clazz.java, handler)
    }

    override fun register(`class`: Class<*>) {
        loopDeclaredMethods(`class`) { method: Method ->
            // Get types and values.
            val event = method.parameterTypes[0] as Class<out Event>
            if (Modifier.isPublic(method.modifiers)) {
                registerK(event)
            }

            addHandlers(event, null, method)
        }
    }

    override fun register(o: Any) {
        loopMethods(o) { method: Method ->
            // Get types and values.
            val event = method.parameterTypes[0] as Class<out Event>
            if (Modifier.isPublic(method.modifiers)) {
                registerK(o)
            }

            addHandlers(event, o, method)
        }
    }

    override fun registerK(`class`: KClass<*>) {

//        var a: Modification.EventBusSubscriber? = null
//        for (annotation in method.annotations) {
//            if (annotation is Modification.EventBusSubscriber) {
//                a = annotation;
//                break
//            }
//        }

        loopDeclaredFunctions(`class`) {
            // Get types and values.
            val eventParameter = it.parameters[0]
            val event = eventParameter.type.classifier as KClass<Event>
            if (it.isOpen) {

                var a: SubscribeEvent? = null
                for (annotation in it.annotations) {
                    if (annotation is SubscribeEvent) {
                        a = annotation
                        break
                    }
                }

                val params = it.parameters

                if (params.size != 1) {
                    throw IllegalArgumentException("Expected one parameter of class that extends " + Event::class.qualifiedName + " (got " + params.size + ")")
                }
                val classifier = params[0].type.classifier
                if (classifier is KClass<*>) {
                    if (!classifier.isSubclassOf(Event::class)) {
                        throw IllegalArgumentException("Expected parameter of class that extends " + Event::class.qualifiedName + " (got " + classifier.qualifiedName + ")")
                    }
                }

                if (a != null) {
//                    val get: EventBus = a.bus.bus().get()
                } else {
                    return@loopDeclaredFunctions
                }
            }

            addHandlers(event, it)
        }
    }

    override fun registerK(o: Any) {
        loopFunctions(o) {
            // Get types and values.
            val eventParameter = it.parameters[0]
            val event = eventParameter.type.classifier as KClass<Event>
            addHandlers(event, o, it)
        }
    }

    override fun unregister(`class`: Class<*>) {
        loopDeclaredMethods(`class`) {
            // Get and check event.
            val evt = it.parameterTypes[0] as Class<out Event>
            removeHandlers(evt, null, it)
        }
    }

    override fun unregister(o: Any) {
        loopMethods(o) {
            // Get and check event.
            val evt = it.parameterTypes[0] as Class<out Event>
            removeHandlers(evt, o, it)
        }
    }

    override fun unregisterK(`class`: KClass<*>) {
        loopDeclaredFunctions(`class`) {
            // Get event
            val evt = it.parameters[0].type.classifier as KClass<out Event>
            removeHandlersK(evt, null, it)
        }
    }

    override fun unregisterK(o: Any) {
        loopFunctions(o) {
            // Get event
            val evt = it.parameters[0].type.classifier as KClass<out Event>
            removeHandlersK(evt, o, it)
        }
    }

    override fun unregister(event: Class<out Event>, `class`: Class<*>) {
        loopDeclaredMethods(`class`) { method: Method ->
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

    override fun unregister(event: Class<out Event>, o: Any) {
        loopMethods(o) {
            // Get types and values.
            val evt = it.parameterTypes[0] as Class<out Event>
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, o, it)
                } catch (ignored: IllegalStateException) {
                }
            }
        }
    }

    override fun unregisterK(event: KClass<out Event>, `class`: KClass<*>) {
        loopDeclaredFunctions(`class`) {
            // Get and check event.
            val eventParameter = it.parameters[0]
            val evt = eventParameter.type.classifier as KClass<Event>
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlersK(event, null, it)
                } catch (ignored: IllegalStateException) {
                }
            }
        }
    }

    override fun unregisterK(event: KClass<out Event>, o: Any) {
        loopFunctions(o) {
            // Get types and values.
            val eventParameter = it.parameters[0]
            val evt = eventParameter.type.classifier as KClass<Event>
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlersK(event, o, it)
                } catch (ignored: IllegalStateException) {
                }
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

    private fun loopDeclaredFunctions(clazz: KClass<*>, consumer: Consumer<KFunction<*>>) {
        // Loop declared methods.
        loopFunctions0(clazz.declaredFunctions, kClassPredicate, consumer)
    }

    private fun loopFunctions(o: Any, consumer: Consumer<KFunction<*>>) {
        // Loop methods.
        loopFunctions0(o::class.functions, kInstancePredicate, consumer, o)
    }

    private fun loopFunctions0(methods: Collection<KFunction<*>>, predicate: BiPredicate<KFunction<*>, Any>, consumer: Consumer<KFunction<*>>, o: Any) {
        // Check all methods for event subscribers.
        for (method in methods) {
            // Check is instance method.
            if (predicate.test(method, o)) {
                // Set accessible.
                method.isAccessible = true
                consumer.accept(method)
            }
        }
    }

    private fun loopFunctions0(methods: Collection<KFunction<*>>, predicate: Predicate<KFunction<*>>, consumer: Consumer<KFunction<*>>) {
        // Check all methods for event subscribers.
        for (method in methods) {
            // Check is instance method.
            if (predicate.test(method)) {
                // Set accessible.
                method.isAccessible = true
                consumer.accept(method)
            }
        }
    }

    private fun loopMethods0(methods: Array<Method>, predicate: Predicate<Method>, consumer: Consumer<Method>) {
        // Check all methods for event subscribers.
        for (method in methods) {
            // Check is instance method.
            if (predicate.test(method)) {
                // Set accessible.
                method.isAccessible = true
                consumer.accept(method)
            }
        }
    }

    private fun removeHandlers(event: Class<out Event>, obj: Any?, method: Method) {
        val pair = Pair(obj, method)
        check(!(!eventToMethod.containsKey(event) || !methodToEvent[pair]!!.contains(event))) { "Cannot unregister method for a non-registered event." }
        check(!(!eventToMethod[event]!!.contains(pair) || !methodToEvent.containsKey(pair))) { "Cannot unregister an unregistered method." }
        methodToEvent[pair]!!.remove(event)
        eventToMethod[event]!!.remove(pair)
    }

    private fun removeHandlersK(event: KClass<out Event>, obj: Any?, method: KFunction<*>) {
        val pair = Pair(obj, method)
        check(!(!kEventToKFunction.containsKey(event) || !kFunctionToKEvent[pair]!!.contains(event))) { "Cannot unregister method for a non-registered event." }
        check(!(!kEventToKFunction[event]!!.contains(pair) || !kFunctionToKEvent.containsKey(pair))) { "Cannot unregister an unregistered method." }
        kFunctionToKEvent[pair]!!.remove(event)
        kEventToKFunction[event]!!.remove(pair)
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

    private fun addHandlers(event: KClass<out Event>, obj: Any?, method: KFunction<*>) {
        val pair = Pair(obj, method)
        if (!kEventToKFunction.containsKey(event)) {
            kEventToKFunction[event] = CopyOnWriteArraySet()
        }
        if (!kFunctionToKEvent.containsKey(pair)) {
            kFunctionToKEvent[pair] = CopyOnWriteArraySet()
        }
        kEventToKFunction[event]!!.add(pair)
        kFunctionToKEvent[pair]!!.add(event)
    }

    private fun addHandlers(event: KClass<out Event>, method: KFunction<*>) {
        val pair = Pair(null, method)
        if (!kEventToKFunction.containsKey(event)) {
            kEventToKFunction[event] = CopyOnWriteArraySet()
        }
        if (!kFunctionToKEvent.containsKey(pair)) {
            kFunctionToKEvent[pair] = CopyOnWriteArraySet()
        }
        kEventToKFunction[event]!!.add(pair)
        kFunctionToKEvent[pair]!!.add(event)
    }

    companion object {
        private val LOGGER = LogManager.getLogger("ModEventBus")
    }
}