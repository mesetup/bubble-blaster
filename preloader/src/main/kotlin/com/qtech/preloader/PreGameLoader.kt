@file:Suppress("DEPRECATION")

package com.qtech.preloader

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

/**
 * Pre game loader, currently only for QBubbles.
 *
 * @since 1.0.0
 */
class PreGameLoader private constructor() {
    private fun loadGame(args: Array<String>) {
        try {
            val loader: QBubblesLoader
            val loaderLocation = QBUBBLES_LOADER
            LOGGER.info(String.format("Using loader from location: %s", loaderLocation))

            // Add the internal package and get the loader class from the loader location..
            classLoader.addInternalPackage(loaderLocation.substring(0, loaderLocation.lastIndexOf('.')))
            loader = Class.forName(loaderLocation, true, classLoader).newInstance() as QBubblesLoader

            // Set arguments of loader.
            LOGGER.info(String.format("Current loader class %s", loader.javaClass.name))
            val loadTarget = Objects.requireNonNull(loader).loadingTarget
            val clazz = Class.forName(loadTarget, false, classLoader)
            val mainMethod = clazz.getMethod("main", Array<String>::class.java, PreClassLoader::class.java)
            LOGGER.info(String.format("Loading Bubble Blaster {%s}", loadTarget))
            mainMethod.invoke(null, args, classLoader)
        } catch (e: Exception) {
            LOGGER.fatal("Problem occurred when trying to load a game.", e)
            exitProcess(1)
        }
    }

    companion object {
        val LOGGER: Logger = LogManager.getLogger("PreLoader")
        private const val QBUBBLES_LOADER = "com.qtech.preloader.QBubblesLoader"
        @JvmStatic
        fun main(args: Array<String>) {
            PreGameLoader().loadGame(args)
        }

        lateinit var classLoader: PreClassLoader
    }

    init {
        var urls = arrayOfNulls<URL>(0)
        try {
            urls = arrayOf(PreGameLoader::class.java.protectionDomain.codeSource.location.toURI().toURL())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        classLoader = PreClassLoader(urls)
        Thread.currentThread().contextClassLoader = classLoader
    }
}