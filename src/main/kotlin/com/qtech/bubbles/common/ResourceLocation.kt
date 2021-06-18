package com.qtech.bubbles.common

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.util.TextUtils
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import java.util.regex.Pattern

@Suppress("unused")
class ResourceLocation {
    val path: String
    val namespace: String?
    private var addonInstanceClass: Class<out AddonInstance?>? = null

    constructor(s: String) {
        // Create a Pattern object
        val r = Pattern.compile(pattern)

        // Now create matcher object.
        val m = r.matcher(s)
        if (m.find()) {
            val namespace = m.group(1)
            val path = m.group(2)
            this.namespace = namespace
            this.path = StringUtils.stripEnd(path, "/")
        } else {
            throw IllegalArgumentException("Could not recognize resource location from string \"" + TextUtils.getRepresentationString(s) + "\"")
        }
        addonInstanceClass = if (namespace != null) {
            val instance = BubbleBlaster.instance
            val addonManager = instance.addonManager
            val containerFromId = addonManager.getContainer(namespace)
            if (containerFromId != null) {
                val addonInstance: AddonObject<out AddonInstance> = containerFromId.obj
                addonInstance.addonClass
            } else {
                null
            }
        } else {
            null
        }
    }

    constructor(namespace: String?, path: String) {
        var pathSet: String = path
        pathSet = StringUtils.stripEnd(pathSet, "/")
        testNamespace(namespace)
        testPath(pathSet)
        this.path = pathSet
        this.namespace = namespace
        //        this.url = namespace == null ? null : "assets/" + namespace + "/" + path;
        addonInstanceClass = if (namespace != null) {
            val instance = BubbleBlaster.instance
            val addonManager = instance.addonManager
            val addonContainer = addonManager.getContainer(namespace)
            if (addonContainer != null) {
                val addonInstance: AddonObject<out AddonInstance> = addonContainer.obj
                addonInstance.addonClass
            } else {
                null
            }
        } else {
            null
        }
    }

    fun getResourceAsStream(type: DataType, objectPath: String, ext: String): InputStream? {
        if (type.path == null) {
            return null
        }
        return addonInstanceClass?.getResourceAsStream(type.path + "/" + namespace + "/" + objectPath + "/" + path + ext)
    }

    fun getResourceURL(type: DataType, objectPath: String, ext: String): URL? {
        if (type.path == null) {
            return null
        }
        return addonInstanceClass?.getResource(type.path + "/" + namespace + "/" + objectPath + "/" + path + ext)
    }

    @Throws(IOException::class)
    fun listDir(type: DataType, objectPath: String): Enumeration<URL>? {
        if (type.path == null) {
            return null
        }
        return addonInstanceClass?.classLoader?.getResources(type.path + "/" + namespace + "/" + objectPath + "/" + path)
    }

    fun withNamespace(namespace: String): ResourceLocation {
        require(this.namespace == null) { "Namespace already defined" }
        return ResourceLocation(namespace, path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ResourceLocation
        return path == that.path &&
                namespace == that.namespace
    }

    override fun hashCode(): Int {
        return Objects.hash(path, namespace)
    }

    override fun toString(): String {
        return if (namespace == null) {
            path
        } else "$namespace:$path"
    }

    fun toString(type: DataType): String {
        return if (namespace == null) {
            type.path + "@" + path
        } else type.path + "@" + namespace + ":" + path
    }

    companion object {
        private const val pattern = "([a-z_]*):([a-z_/]*)"
        private fun getNamespace(s: String): String {
            val split = s.split(Regex.fromLiteral(":"), 2).toTypedArray()
            return split[0]
        }

        private fun getPath(s: String): String {
            val split = s.split(Regex.fromLiteral(":"), 2).toTypedArray()
            return split[1]
        }

        @kotlin.jvm.JvmStatic
        fun fromString(s: String): ResourceLocation {
            // Create a Pattern object
            val r = Pattern.compile(pattern)

            // Now create matcher object.
            val m = r.matcher(s)
            return if (m.find()) {
                val namespace = m.group(1)
                var path = m.group(2)
                path = StringUtils.stripEnd(path, "/")
                ResourceLocation(namespace, path)
            } else {
                throw IllegalArgumentException("Could not recognize resource location from regex ($pattern)")
            }
        }

        fun testNamespace(`val`: String?) {
            if (`val` == null) {
                return
            }

            // String to be scanned to find the pattern.
            val pattern = "[a-z_]*"

            // Create a Pattern object
            val r = Pattern.compile(pattern)

            // Now create matcher object.
            val m = r.matcher(`val`)
            require(m.find()) { "Namespace has an invalid format ($pattern)" }
        }

        fun testPath(`val`: String) {
            // String to be scanned to find the pattern.
            val pattern = "[a-z_/]*"

            // Create a Pattern object
            val r = Pattern.compile(pattern)

            // Now create matcher object.
            val m = r.matcher(`val`)
            require(m.find()) { "Path has an invalid format ($pattern)" }
        }
    }
}