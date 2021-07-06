package qtech.bubbles.common.versioning

abstract class AbstractVersion<T : AbstractVersion<T>?> : Comparable<T> {
    abstract val isStable: Boolean
    abstract override fun toString(): String
}