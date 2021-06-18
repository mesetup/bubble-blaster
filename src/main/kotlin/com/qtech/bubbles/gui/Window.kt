@file:Suppress("unused")

package com.qtech.bubbles.gui

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.crash.CrashCategory
import com.qtech.bubbles.common.crash.CrashReport
import org.jdesktop.swingx.JXFrame
import org.jdesktop.swingx.StackLayout
import java.awt.*
import java.awt.Window
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

/**
 * <h1>Window Class </h1>
 *
 *
 * This is just a <img src="https://www.iconfinder.com/icons/35640/download/png/16"></img>
 */
class Window(main: BubbleBlaster?, fullscreen: Boolean) : JXFrame() {
    private val fullscreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
    fun toggleFullscreen() {
        dispose()
        if (this.isUndecorated) {
            fullscreenDevice.fullScreenWindow = null
            this.isUndecorated = false
        } else {
            this.isUndecorated = true
            fullscreenDevice.fullScreenWindow = this
        }
        this.isVisible = true
        this.repaint()
    }

    val isFullscreen: Boolean
        get() = this.isUndecorated
    val frame: JXFrame
        get() = this
    val window: Window
        get() = this

    override fun setCursor(c: Cursor) {
        if (!BubbleBlaster.isHeadless()) {
            super.setCursor(c)
        }
    }

    companion object {
        private const val serialVersionUID = -240840600533728354L
    }

    init {
        try {
//            this.setPreferredSize(new Dimension(width, height));
//            this.setMaximumSize(new Dimension(width, height));
//            this.setMinimumSize(new Dimension(width, height));
            background = Color(64, 64, 64)
            this.extendedState = MAXIMIZED_BOTH
            val bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration.bounds
            if (fullscreen) {
                this.bounds = bounds
            } else {
//                this.bounds = Rectangle(bounds.centerX.toInt() - 800 / 2, bounds.centerY.toInt() - 450 / 2, 1920, 1080)
//                this.maximumSize = Dimension(1920, 1080)
//                this.minimumSize = Dimension(1920, 1080)
            }

//            this.setAlwaysOnTop(true);
//            if (fullscreen) {
                this.isUndecorated = true
//            }
            this.setVisible(true);
            defaultCloseOperation = EXIT_ON_CLOSE
            this.layout = StackLayout()
            this.contentPane.layout = StackLayout()
            this.isResizable = false
            maximizedBounds = getBounds()
            setLocationRelativeTo(null)
            if (main != null) {
                this.contentPane.add(main)
                setComponentZOrder(main, 1)
            }
            addWindowListener(object : WindowAdapter() {
                override fun windowClosing(we: WindowEvent) {
                    main?.shutdown()
                    exitProcess(0)
                }
            })
            enableInputMethods(true)
            this.isVisible = true
            val device = GraphicsEnvironment
                .getLocalGraphicsEnvironment().screenDevices[0]
            if (fullscreen) {
                device.fullScreenWindow = this
            }
            //            this.setIdle(true);
            this.repaint()
            this.requestFocus()
            check(!(getBounds().minX > getBounds().maxX || getBounds().minY > getBounds().maxY)) { "Window bounds is invalid: negative size" }
            check(!(getBounds().minX == getBounds().maxX || getBounds().minY == getBounds().maxY)) { "Window bounds is invalid: zero size" }
        } catch (t: Throwable) {
            val crashReport = CrashReport("Could not startup Bubble Blaster 2.", t)
            val category = CrashCategory("Startup failure")
            category.add("Window Size", this.size)
            category.add("Window Pos", this.location)
            category.add("Window Pos-Screen", this.locationOnScreen)
            category.add("Window State", state)
            category.add("Window Extended State", this.extendedState)
            crashReport.addCategory(category)
            throw crashReport.reportedException
        }
        try {
            main?.start(this)
        } catch (t: Throwable) {
            val crashReport = CrashReport("Could not startup Bubble Blaster 2.", t)
            val category = CrashCategory("Startup failure")
            category.add("Window Size", this.size)
            category.add("Window Pos", this.location)
            category.add("Window Pos-Screen", this.locationOnScreen)
            category.add("Window State", state)
            category.add("Window Extended State", this.extendedState)
            crashReport.addCategory(category)
            throw crashReport.reportedException
        }
    }
}