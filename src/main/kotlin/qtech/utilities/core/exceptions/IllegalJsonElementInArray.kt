package qtech.utilities.core.exceptions

import com.google.gson.JsonElement

class IllegalJsonElementInArray : Throwable {
    private val index: Int
    val element: JsonElement?

    constructor(index: Int, element: JsonElement?) {
        this.index = index
        this.element = element
    }

    constructor(message: String?, index: Int, element: JsonElement?) : super(message) {
        this.index = index
        this.element = element
    }

    constructor(message: String?, cause: Throwable?, index: Int, element: JsonElement?) : super(message, cause) {
        this.index = index
        this.element = element
    }

    constructor(cause: Throwable?, index: Int, element: JsonElement?) : super(cause) {
        this.index = index
        this.element = element
    }

    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean, index: Int, element: JsonElement?) : super(message, cause, enableSuppression, writableStackTrace) {
        this.index = index
        this.element = element
    }
}