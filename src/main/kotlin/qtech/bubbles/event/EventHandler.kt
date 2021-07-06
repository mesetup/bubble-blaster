package qtech.bubbles.event

abstract class EventHandler<T : Event?> {
    abstract fun handle(e: T)
    abstract val priority: EventPriority?
    abstract val annotation: SubscribeEvent?
    abstract val type: Class<out Event?>?
}