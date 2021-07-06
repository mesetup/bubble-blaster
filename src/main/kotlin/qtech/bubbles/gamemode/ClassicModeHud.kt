package qtech.bubbles.gamemode

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.gametype.HUD
import qtech.bubbles.common.text.translation.I18n.translateToLocal
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawLeftAnchoredString
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawRightAnchoredString
import qtech.bubbles.core.utils.categories.TimeUtils.formatDuration
import qtech.bubbles.event.SubscribeEvent
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.util.helpers.MathHelper
import qtech.hydro.Game
import java.awt.*
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * <h1>Classic HUD</h1>
 * The HUD that was is older versions and editions of the game.
 *
 * @see HUD
 */
class ClassicModeHud(gameMode: AbstractGameMode?) : HUD(gameMode!!) {
    private var gameOver = false
    private var gameOverTime: Long = 0
    private val gameOverFont = Font("Chicle Regular", Font.BOLD, 50)
    private val infoTitleFont = Font("Helvetica", Font.BOLD, 18)
    private val infoValueFont = Font("Helvetica", Font.PLAIN, 14)
    fun renderHUD(gg: GraphicsProcessor?) {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
        val gameType = loadedGame.gameMode
        val player = gameType.player
        val topBar: Rectangle2D = Rectangle2D.Double(0.0, 0.0, BubbleBlaster.instance.width.toDouble(), 70.0)
        val topShade: Rectangle2D = Rectangle2D.Double(0.0, 71.0, BubbleBlaster.instance.width.toDouble(), 30.0)
        if (gameOver) {
            gg!!.color(Color(0, 0, 0, 128))
            gg.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
        }

        // Top-bar.
        gg!!.color(Color(96, 96, 96))
        gg.shapeF(topBar)
        gg.shape(topBar)
        val old = gg.paint()

        // Shadow
        val p = GradientPaint(0f, 71f, Color(0, 0, 0, 48), 0f, 100f, Color(0, 0, 0, 0))
        gg.paint(p)
        gg.shapeF(topShade)
        gg.shape(topShade)
        gg.paint(old)

        // Assign colors for title and description.
        val titleColor = Color(255, 128, 0)
        val descriptionColor = Color(255, 255, 255)
        try {
            // EffectInstance image.
            val effectImage: Image = ImageIO.read(Objects.requireNonNull(javaClass.getResourceAsStream("/assets/bubbleblaster/textures/gui/effect_banner.png")))
            for ((i, effectInstance) in Objects.requireNonNull(player)!!.activeEffects.withIndex()) {
                // GraphicsProcessor 2D
                val graphics2D = gg.create(320 + i * 196, 16, 192, 38)

                // Format duration to string.
                val time = formatDuration(effectInstance.remainingTime)

                // EffectInstance bar.
                graphics2D.img(effectImage, 0, 0)

                // EffectInstance icon.
                graphics2D.img(effectInstance.type.getIcon(32, 32, Color(0, 191, 191)), 5, 3)
                graphics2D.color(Color(255, 255, 255, 192))

                // Time. 0:00:00
                drawLeftAnchoredString(graphics2D, time, Point2D.Double(56.0, 2.0), 35.0, Font(BubbleBlaster.instance.font.fontName, Font.BOLD, 16))
                graphics2D.dispose()

                // Next
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (ignored: NullPointerException) {
        }


//            gg.drawImage(image, 320, 10, Game.instance());
//            if (GameSettings.instance().isTextAntialiasEnabled())
//                gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.color(titleColor)
        if (!gameOver) {
            if (player != null) {
                gg.color(titleColor)
                drawCenteredString(gg, translateToLocal("info.bubbleblaster.score")!!, Rectangle(10, 10, 80, 20), infoTitleFont)
                gg.color(descriptionColor)
                drawCenteredString(gg, player.score.toInt().toString(), Rectangle(10, 40, 80, 20), infoValueFont)
                gg.color(titleColor)
                drawCenteredString(gg, translateToLocal("info.bubbleblaster.level")!!, Rectangle(100, 10, 80, 20), infoTitleFont)
                gg.color(descriptionColor)
                drawCenteredString(gg, player.level.toString(), Rectangle(100, 40, 80, 20), infoValueFont)
                gg.color(titleColor)
                drawCenteredString(gg, translateToLocal("info.bubbleblaster.health")!!, Rectangle(210, 10, 80, 20), infoTitleFont)
                var greenValue: Int
                var redValue: Int
                val `val` = (player.maxDamageValue / 2).toDouble()
                var health = player.damageValue.toDouble()
                health = MathHelper.clamp(health, 0.0, player.maxDamageValue.toDouble())
                if (player.damageValue > player.maxDamageValue / 2) {
                    redValue = ((`val` - (health - `val`)) * 255 / `val`).toInt()
                    redValue = MathHelper.clamp(redValue.toDouble(), 0.0, 255.0).toInt()
                    greenValue = 255
                } else {
                    greenValue = (health * 255 / `val`).toInt()
                    greenValue = MathHelper.clamp(greenValue.toDouble(), 0.0, 255.0).toInt()
                    redValue = 255
                }
                gg.stroke(BasicStroke(1f))
                gg.color(Color(redValue, greenValue, 0).darker())
                gg.rectF(210, 40, 80, 20)
                gg.color(Color(redValue, greenValue, 0))
                gg.rectF(210, 40, (health / player.maxDamageValue * 80).toInt(), 20)
                gg.color(Color(redValue, greenValue, 0).brighter())
                gg.rect(210, 40, 80, 20)
            }
        }
        gg.color(Color(0, 165, 220))
        drawRightAnchoredString(gg, Game.fps.toString(), Point2D.Double((BubbleBlaster.instance.width - 10).toDouble(), 10.0), 20.0, infoTitleFont)
        gg.color(Color.white)

        // Game over message.
        if (gameOver) {
            if ((System.currentTimeMillis() - gameOverTime) % 1000 < 500) {
                gg.color(Color(255, 0, 0))
            } else {
                gg.color(Color(255, 128, 0))
            }
            drawCenteredString(gg, "GAME OVER", gameType.gameBounds!!, gameOverFont)
        }

        // Gradient.
        val gp = GradientPaint(0f, 0f, Color(0, 0, 0, 0), 0f, 70f, Color(0, 0, 0, 24))
        gg.paint(gp)
        gg.shapeF(topBar)
        gg.shape(topBar)
        gg.paint(old)

        // Disable text-antialiasing.
//            if (GameSettings.instance().isTextAntialiasEnabled())
//                gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    /**
     * <h1>Update Event</h1>
     * Used for updating values.
     *
     * @see TickEvent
     */
    @SubscribeEvent
    override fun tick() {
    }

    /**
     * <h1>Sets Game Over flag</h1>
     * Yes, as the title says: it sets the game over flag in the HUD.
     *
     * @see ClassicMode.triggerGameOver
     */
    fun setGameOver() {
        gameOver = true
        gameOverTime = System.currentTimeMillis()
    }
}