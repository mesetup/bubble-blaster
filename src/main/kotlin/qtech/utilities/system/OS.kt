package qtech.utilities.system

import java.util.*

class OS constructor(val name: String = System.getProperty("os.name"), val version: String = System.getProperty("os.version")) {

    @Deprecated("")
    constructor(name: String) : this(name, System.getProperty("os.version"))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val os = other as OS
        return name == os.name && version == os.version
    }

    fun equalsIgnoreVersion(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val os = other as OS
        return name == os.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name, version)
    }
}