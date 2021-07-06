package qtech.bubbles.common

open class InfoTransporter(private val onLog: (String) -> Unit) {
    fun log(text: String) {
        onLog.invoke(text)
    }
}