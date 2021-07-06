package qtech.bubbles.screen

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.command.CommandConstructor
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawLeftAnchoredString
import qtech.bubbles.event.KeyboardEvent
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.event.type.KeyEventType
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.util.Util
import qtech.bubbles.util.helpers.MathHelper
import org.apache.tools.ant.types.Commandline
import qtech.hydro.Game
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.geom.Point2D
import java.util.*

class CommandScreen : Screen() {
    private val defaultFont = Font(Util.game.pixelFontName, Font.PLAIN, 32)
    private var currentText = "/"
    private var cursorIndex = 1
    override fun init() {
        BubbleBlaster.eventBus.register(this)
    }

    override fun onClose(to: Screen?): Boolean {
        BubbleBlaster.eventBus.unregister(this)
        currentText = "/"
        cursorIndex = 1
        return super.onClose(to)
    }

    @SubscribeEvent
    fun onKeyboard(evt: KeyboardEvent) {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
        if (evt.type === KeyEventType.PRESS || evt.type === KeyEventType.HOLD) {
            if (evt.parentEvent.keyCode == KeyEvent.VK_BACK_SPACE) {
                if (currentText.isEmpty()) {
                    return
                }
                val leftText = currentText.substring(0, cursorIndex - 1)
                val rightText = currentText.substring(cursorIndex)
                currentText = leftText + rightText
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, currentText.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_LEFT) {
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, currentText.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_RIGHT) {
                cursorIndex = MathHelper.clamp(cursorIndex + 1, 0, currentText.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_ENTER) {
                if (currentText[0] != '/') {
                    Objects.requireNonNull(loadedGame.gameMode.player)!!.sendMessage("Not a command, start with a ‘/’ for a command.")
                    BubbleBlaster.instance.displayScene(null)
                    return
                }
                val parsed: List<String> = listOf<String>(*Commandline.translateCommandline(currentText.substring(1)))
                val args: Array<String> = parsed.subList(1, parsed.size).toTypedArray()
                if (!CommandConstructor.execute(parsed[0], loadedGame.gameMode.player!!, args)) {
                    Objects.requireNonNull(loadedGame.gameMode.player)!!.sendMessage("Command ‘" + parsed[0] + "’ is non-existent.")
                    BubbleBlaster.instance.displayScene(null)
                    return
                }
                BubbleBlaster.instance.displayScene(null)
                return
            }
            if (evt.type === KeyEventType.PRESS && evt.parentEvent.keyCode == KeyEvent.VK_ESCAPE) {
                BubbleBlaster.instance.displayScene(null)
                return
            }
            var c = evt.parentEvent.keyChar
            if (evt.parentEvent.keyCode == KeyEvent.VK_DEAD_ACUTE) {
                c = '\''
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_QUOTEDBL) {
                c = '"'
            }
            println(evt.parentEvent.paramString())
            println(evt.parentEvent.paramString())
            println(evt.parentEvent.keyChar)
            println(KeyEvent.getExtendedKeyCodeForChar('"'.code))
            println(KeyEvent.getKeyText(evt.parentEvent.keyCode))
            println(KeyEvent.getKeyText(evt.parentEvent.extendedKeyCode))
            if (c.code.toShort() >= 32) {
//                currentText += c;
                val leftText = currentText.substring(0, cursorIndex)
                val rightText = currentText.substring(cursorIndex)
                currentText = leftText + c + rightText
                cursorIndex++
            }
        }
    }

    override fun render(game: Game, gp: GraphicsProcessor) {
        if (!BubbleBlaster.instance.isGameLoaded) {
            return
        }
        gp.color(Color(0, 0, 0, 64))
        gp.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
        gp.color(Color(0, 0, 0, 128))
        gp.rectF(0, BubbleBlaster.instance.height - 32, BubbleBlaster.instance.width, 32)
        gp.color(Color(255, 255, 255, 255))
        drawLeftAnchoredString(gp, currentText, Point2D.Double(2.0, (BubbleBlaster.instance.height - 28).toDouble()), 28.0, defaultFont)
        val fontMetrics = gp.fontMetrics(defaultFont)
        val cursorX: Int
        gp.color(Color(0, 144, 192, 255))
        if (cursorIndex >= currentText.length) {
            cursorX = if (currentText.isNotEmpty()) {
                fontMetrics.stringWidth(currentText.substring(0, cursorIndex)) + 2
            } else {
                0
            }
            gp.line(cursorX, BubbleBlaster.instance.height - 30, cursorX, BubbleBlaster.instance.height - 2)
            gp.line(cursorX + 1, BubbleBlaster.instance.height - 30, cursorX + 1, BubbleBlaster.instance.height - 2)
        } else {
            cursorX = if (currentText.isNotEmpty()) {
                fontMetrics.stringWidth(currentText.substring(0, cursorIndex))
            } else {
                0
            }
            val width = fontMetrics.charWidth(currentText[cursorIndex])
            gp.line(cursorX, BubbleBlaster.instance.height - 2, cursorX + width, BubbleBlaster.instance.height - 2)
            gp.line(cursorX, BubbleBlaster.instance.height - 1, cursorX + width, BubbleBlaster.instance.height - 1)
        }
    }
}