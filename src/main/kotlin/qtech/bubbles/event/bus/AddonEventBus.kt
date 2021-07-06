package qtech.bubbles.event.bus

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.mods.loader.ModContainer
import qtech.bubbles.common.mods.ModObject
import qtech.bubbles.common.mods.ModInstance
import qtech.bubbles.event.Event
import qtech.bubbles.event.ICancellable
import java.lang.UnsupportedOperationException
import kotlin.reflect.KClass

class AddonEventBus : EventBus() {
    override fun <T : Event> post(event: T): Boolean {
        BubbleBlaster.instance.modManager.containers.stream().map(ModContainer::obj).forEach { modInstanceObject: ModObject<out ModInstance>? ->
            BubbleBlaster.logger.info("Sending addon event to: " + modInstanceObject!!.modId)
            modInstanceObject.eventBus.post(event)
        }
        return event is ICancellable && (event as ICancellable).isCancelled
    }

    override fun <T : Event> addListener(clazz: Class<T>, handler: (event: T) -> Unit) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun <T : Event> addListenerK(clazz: KClass<T>, handler: (event: T) -> Unit) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun register(`class`: Class<*>) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun register(o: Any) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun registerK(`class`: KClass<*>) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun registerK(o: Any) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregister(`class`: Class<*>) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregister(o: Any) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregisterK(`class`: KClass<*>) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregisterK(o: Any) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregister(event: Class<out Event>, `class`: Class<*>) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregister(event: Class<out Event>, o: Any) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregisterK(event: KClass<out Event>, `class`: KClass<*>) {
        throw UnsupportedOperationException("Not available.")
    }

    override fun unregisterK(event: KClass<out Event>, o: Any) {
        throw UnsupportedOperationException("Not available.")
    }
}