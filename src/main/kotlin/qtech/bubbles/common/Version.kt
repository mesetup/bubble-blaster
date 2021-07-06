package qtech.bubbles.common

import java.io.Serializable
import java.util.*

class Version(val major: Int, val minor: Int, val build: Int, val type: VersionType, val release: Int) : Serializable {
    override fun toString(): String {
        return major.toString() + "." + minor + "." + build + "-" + type.text + release
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val version = other as Version
        return major == version.major && minor == version.minor && release == version.release && type == version.type
    }

    override fun hashCode(): Int {
        return Objects.hash(major, minor, type, release)
    }
}