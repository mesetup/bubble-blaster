package qtech.bubbles.event

interface ICancellable {
    fun setCancelled()
    val isCancelled: Boolean
}