package com.qtech.bubbles.common.versioning

import java.util.regex.Pattern

class QVersion : AbstractVersion<QVersion?> {
    val version: Int
    val subversion: Int
    val type: TYPE
    val release: Int

    /**
     * @param s the version to parse.
     * @throws IllegalArgumentException when an invalid version has given.
     */
    constructor(s: String) {
        // String to be scanned to find the pattern.
        val pattern = "([0-9]*)\\.([0-9]*)-(alpha|beta|pre|release)([0-9]*)" // 1.0-alpha4 // 5.4-release-7

        // Create a Pattern object
        val r = Pattern.compile(pattern)

        // Now create matcher object.
        val m = r.matcher(s)
        if (m.find()) {
            version = m.group(1).toInt()
            subversion = m.group(2).toInt()
            type = when (m.group(3)) {
                "alpha" -> TYPE.ALPHA
                "beta" -> TYPE.BETA
                "pre" -> TYPE.PRE
                "release" -> TYPE.RELEASE
                else -> throw InternalError("Regex has invalid output.")
            }
            release = m.group(4).toInt()
        } else {
            throw IllegalArgumentException("Invalid version,")
        }
    }

    constructor(version: Int, subversion: Int, type: TYPE, release: Int) {
        this.version = version
        this.subversion = subversion
        this.type = type
        this.release = release
    }

    override val isStable: Boolean
        get() = type == TYPE.RELEASE

    override fun toString(): String {
        return String.format("%d.%d-%s%d", version, subversion, type.name.lowercase(), release)
    }

    override operator fun compareTo(other: QVersion?): Int {
        val cmp: Int? = other?.let { version.compareTo(it.version) }
        return if (cmp == 0) {
            val cmp1 = subversion.compareTo(other.subversion)
            if (cmp1 == 0) {
                val cmp2 = type.compareTo(other.type)
                if (cmp2 == 0) {
                    release.compareTo(other.release)
                } else {
                    cmp2
                }
            } else {
                cmp1
            }
        } else {
            if (cmp == null) {
                throw NullPointerException("Local variable 'cmp' is null")
            }
            cmp
        }
    }

    enum class TYPE {
        ALPHA, BETA, PRE, RELEASE
    }

    companion object {
        val EMPTY = QVersion(0, 0, TYPE.BETA, 0)
    }
}