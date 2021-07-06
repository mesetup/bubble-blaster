package qtech.bubbles.event

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class SubscribeEvent(val ignoreCancelled: Boolean = false, val priority: EventPriority = EventPriority.NORMAL)