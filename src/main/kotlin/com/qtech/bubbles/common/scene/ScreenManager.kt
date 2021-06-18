package com.qtech.bubbles.common.scene

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.screen.LoadScreen
import com.qtech.bubbles.util.Util
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.Contract
import javax.annotation.ParametersAreNonnullByDefault

/**
 * Scene manager, used for change between scenes.
 *
 * @see Screen
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
class ScreenManager private constructor(private val startScreen: Screen) {
    var currentScreen: Screen?
        private set
    var isInitialized = false
        private set

    /**
     * Display a new scene.
     *
     * @param scene the scene to display
     * @return if changing the scene was successful.
     */
    fun displayScreen(scene: Screen?): Boolean {
        return displayScreen(scene, false)
    }

    /**
     * Display a new scene.
     *
     * @param screen the scene to display
     * @return if changing the scene was successful.
     */
    fun displayScreen(screen: Screen?, force: Boolean): Boolean {
        if (currentScreen != null) {
            LOGGER.debug("Hiding " + currentScreen!!.javaClass.simpleName)
            if (currentScreen!!.onClose(screen) || force) {
                currentScreen = screen
                if (screen != null) {
                    Util.setCursor(screen.defaultCursor)
                } else {
                    Util.setCursor(BubbleBlaster.instance.defaultCursor)
                }
                if (currentScreen != null) {
                    LOGGER.debug("Showing " + currentScreen!!.javaClass.simpleName)
                    currentScreen!!.init()
                } else {
                    LOGGER.debug("Showing <<NO-SCENE>>")
                }
                return true
            }
            LOGGER.debug("Hiding " + currentScreen!!.javaClass.simpleName + " canceled.")
        } else {
            LOGGER.debug("Hiding <<NO-SCENE>>")
            currentScreen = screen
            if (screen != null) {
                Util.setCursor(screen.defaultCursor)
            } else {
                Util.setCursor(BubbleBlaster.instance.defaultCursor)
            }
            if (currentScreen != null) {
                LOGGER.debug("Showing " + currentScreen!!.javaClass.simpleName)
                currentScreen!!.init()
            } else {
                LOGGER.debug("Showing <<NO-SCENE>>")
            }
            return true
        }
        return false
    }

    /**
     * DEPRECATED
     *
     * @return the current scene resource location.
     */
    @get:Deprecated("since 1.0.???", ReplaceWith("null"))
    @get:Contract("->null")
    @Deprecated("since 1.0.???", ReplaceWith("null"))
    val currentSceneKey: ResourceLocation?
        get() = null

    @Synchronized
    fun start() {
        check(!this.isInitialized) { "SceneManager already initialized." }
        this.isInitialized = true
        currentScreen = startScreen
        LOGGER.debug("Showing " + currentScreen!!.javaClass.simpleName)
        Util.setCursor(startScreen.defaultCursor)
        startScreen.init()
    }

    companion object {
        @JvmStatic
        val instance = ScreenManager(LoadScreen())
        private val LOGGER = LogManager.getLogger("QB:Screen:Manager")
    }

    init {
        currentScreen = startScreen
    }
}