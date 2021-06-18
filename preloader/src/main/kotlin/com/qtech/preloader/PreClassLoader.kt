@file:Suppress("DEPRECATION")

package com.qtech.preloader

import it.unimi.dsi.fastutil.bytes.ByteArrays
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLClassLoader
import java.net.URLConnection
import java.security.CodeSigner
import java.security.CodeSource
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

class PreClassLoader(sources: Array<URL?>) : URLClassLoader(sources, null) {
    val sources: MutableList<URL?> = mutableListOf(*sources)
    private val parentClassLoader = javaClass.classLoader
    private val cachedClasses: MutableMap<String, Class<*>> = ConcurrentHashMap()
    private val invalidClassesCache: MutableSet<String> = HashSet(1000)
    private val internalPackages: MutableSet<String> = HashSet()
    private val dataCache: MutableMap<String, ByteArray> = ConcurrentHashMap(1000)
    private val invalidDataCache = Collections.newSetFromMap(ConcurrentHashMap<String, Boolean>())
    private val readBuffer = AtomicReference<ByteArray>()
    @Throws(ClassNotFoundException::class)
    public override fun findClass(name: String): Class<*> {
        if (invalidClassesCache.contains(name)) {
            throw ClassNotFoundException(name)
        }
        for (pkg in internalPackages) {
            if (name.startsWith(pkg)) {
                return parentClassLoader.loadClass(name)
            }
        }
        return if (cachedClasses.containsKey(name)) {
            cachedClasses[name]!!
        } else try {
            // Check for cached classes.
            if (cachedClasses.containsKey(name)) {
                return cachedClasses[name]!!
            }

            // Get the last dot in the name. Used to get the package name.
            val lastDot = name.lastIndexOf('.')

            // Get package name and filename.
            val packageName = if (lastDot == -1) "" else name.substring(0, lastDot)
            val dataLocation = name.replace('.', '/') + ".class"

            // Get url connections for the file got from the filename.
            val urlConnection = getResourceConnection(dataLocation)

            // Check for external.
            if (lastDot > 0 && !name.startsWith("com.qtech.bubbles.")) {
                val pkg = getPackage(packageName)
                if (pkg == null) { // Package is non-existent.
                    definePackage(packageName, null, null, null, null, null, null, null)
                } else if (pkg.isSealed) { // Note: Found online that this might help with some issues, not sure why.
                    PreGameLoader.LOGGER.warn(String.format("External url got a sealed path %s", requireNotNull(urlConnection).url))
                }
            }

            // Get class bytes.
            val classBytes = getClassBytes(name)

            // Get code source and define the class.
            val codeSource = if (urlConnection == null) null else CodeSource(urlConnection.url, null as Array<CodeSigner?>?)
            val clazz = defineClass(name, classBytes, 0, classBytes!!.size, codeSource)

            // Cache class.
            cachedClasses[name] = clazz

            // Return class.
            clazz
        } catch (e: Throwable) {
            invalidClassesCache.add(name)
            throw ClassNotFoundException(name, e)
        }
    }

    private fun getResourceConnection(name: String): URLConnection? {
        val resource = findResource(name)
        return if (resource != null) {
            try {
                resource.openConnection()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else null
    }

    public override fun addURL(url: URL) {
        super.addURL(url)
        sources.add(url)
    }

    private fun readComplete(stream: InputStream?): ByteArray {
        return try {
            var buffer = orCreateBuffer
            var read: Int
            var totalLength = 0
            while (stream!!.read(buffer, totalLength, buffer.size - totalLength).also { read = it } != -1) {
                totalLength += read

                // Extend the buffer
                if (totalLength >= buffer.size - 1) {
                    buffer = ByteArrays.grow(buffer, buffer.size + READ_BUFFER_SIZE)
                }
            }
            val result = ByteArray(totalLength)
            System.arraycopy(buffer, 0, result, 0, totalLength)
            result
        } catch (t: Throwable) {
            PreGameLoader.LOGGER.warn("Problem loading class, throwable follows:", t)
            ByteArray(0)
        }
    }

    private val orCreateBuffer: ByteArray
        get() {
            var buffer = readBuffer.get()
            if (buffer == null) {
                readBuffer.set(ByteArray(READ_BUFFER_SIZE))
                buffer = readBuffer.get()
            }
            return buffer
        }

    fun addInternalPackage(packageName: String) {
        internalPackages.add(packageName)
    }

    @Throws(IOException::class)
    fun getClassBytes(name: String): ByteArray? {
        if (invalidDataCache.contains(name)) {
            return null
        } else if (dataCache.containsKey(name)) {
            return dataCache[name]
        }
        var classStream: InputStream? = null
        return try {
            val resourcePath = name.replace('.', '/') + ".class"
            val classResource = findResource(resourcePath)

            // Check for if the class resource is null.
            if (classResource == null) {
                invalidDataCache.add(name)
                return null
            }
            classStream = classResource.openStream()

            // Read class stream.
            val data = readComplete(classStream)

            // Store class data in resource cache.
            dataCache[name] = data

            // Return data.
            data
        } finally {
            if (classStream != null) {
                closeIgnoringThrowables(classStream)
            }
        }
    }

    fun clearInvalidDataCache(entriesToClear: Set<String>?) {
        invalidDataCache.removeAll(entriesToClear!!)
    }

    @Suppress("unused")
    fun clearInvalidDataCache() {
        invalidDataCache.clear()
    }

    fun isInternalPackage(s: String): Boolean {
        var s1 = s
        if (!s1.endsWith(".")) {
            s1 = "$s1."
        }
        for (internalPackage in internalPackages) {
            var internalPackage1: String = internalPackage

            if (!internalPackage1.endsWith(".")) {
                internalPackage1 = "$internalPackage1."
            }
            if (internalPackage1.startsWith(s1)) {
                return true
            }
        }
        return false
    }

    companion object {
        const val READ_BUFFER_SIZE = 1 shl 12
        private fun closeIgnoringThrowables(closeable: Closeable) {
            try {
                closeable.close()
            } catch (ignored: IOException) {
            }
        }
    }

    init {

        // classloader exclusions
        addInternalPackage("java.")
        addInternalPackage("javax.")
        addInternalPackage("javafx.")
        addInternalPackage("javax.jmdns.")
        addInternalPackage("it.unimi.dsi.")
        addInternalPackage("com.jhlabs.")
        addInternalPackage("com.sun.")
        addInternalPackage("com.google.")
        addInternalPackage("org.fusesource.")
        addInternalPackage("org.lwjgl.")
        addInternalPackage("org.apache.")
        addInternalPackage("org.apache.batik.")
        addInternalPackage("org.apache.logging.")
        addInternalPackage("com.qtech.bubbles.")
        addInternalPackage("com.qtech.preloader.")
        addInternalPackage("com.qtech.dev.")
    }
}