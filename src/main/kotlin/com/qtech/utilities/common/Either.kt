package com.qtech.utilities.common

open class Either<L, R> private constructor(left: L?, right: R?) {
    companion object {
        class Left<T>(var obj: T?) {
            val isPresent: Boolean
                get() = obj != null

            fun ifPresent(func: (T) -> Unit): Unit = if (isPresent) func.invoke(obj!!) else Unit
            fun orElse(other: T): T = if (isPresent) obj!! else other
            fun orElseGet(other: () -> T): T = if (isPresent) obj!! else other.invoke()

            fun <E : Throwable> orElseThrow(other: () -> E): T = if (isPresent) obj!! else throw other.invoke()
        }
        class Right<T>(var obj: T?) {
            val isPresent: Boolean
                get() = obj != null

            fun ifPresent(func: (T) -> Unit): Unit = if (isPresent) func.invoke(obj!!) else Unit
            fun orElse(other: T): T = if (isPresent) obj!! else other
            fun orElseGet(other: () -> T): T = if (isPresent) obj!! else other.invoke()

            fun <E : Throwable> orElseThrow(other: () -> E): T = if (isPresent) obj!! else throw other.invoke()
        }

        fun <L, R> left(left: L): Either<L, R> {
            return Either(left, null)
        }

        fun <L, R> right(right: R): Either<L, R> {
            return Either(null, right)
        }
    }

    var right = Right(right)
    var left = Left(left)

    fun ifLeft(func: (L) -> Unit): Either<L, R> {
        left.ifPresent(func)
        return this;
    }

    fun ifRight(func: (R) -> Unit): Either<L, R> {
        right.ifPresent(func)
        return this;
    }

    fun <T> mapLeft(func: (L?) -> T): Either<T, R> {
        val left1 = left.obj
        val right1 = right.obj

        if (left1 == null) {
            return Companion.right(right1!!)
        }
        return Companion.left(func.invoke(left1)!!)
    }
}