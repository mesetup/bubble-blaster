package qtech.utilities.system

object Path {
    val seperator: Char
        get() = System.getProperty("path.separator")[0]
}