package qtech.bubbles.gui.style

open class StateBundle<T> @JvmOverloads constructor(var hover: T, var normal: T, var pressed: T, protected var active: T? = null) {
}