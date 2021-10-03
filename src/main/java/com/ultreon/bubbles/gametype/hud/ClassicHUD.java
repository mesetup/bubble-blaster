package com.ultreon.bubbles.gametype.hud;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.common.gametype.HUD;
import com.ultreon.bubbles.common.text.translation.I18n;
import com.ultreon.bubbles.effect.StatusEffectInstance;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.bubbles.gametype.ClassicType;
import com.ultreon.bubbles.util.helpers.MathHelper;
import com.ultreon.commons.util.TimeUtils;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.util.GraphicsUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Objects;

/**
 * Classic HUD
 * The HUD that was is older versions and editions of the game.
 *
 * @see HUD
 */
public class ClassicHUD extends HUD {
    private boolean gameOver;
    private long gameOverTime;
    private final Font gameOverFont = new Font(BubbleBlaster.instance().getGameFontName(), Font.BOLD, 50);
    private final Font infoTitleFont = new Font(BubbleBlaster.instance().getSansFontName(), Font.BOLD, 18);
    private final Font infoValueFont = new Font(BubbleBlaster.instance().getSansFontName(), Font.PLAIN, 14);
    private final Stroke healthLine = new BasicStroke(1);

    public ClassicHUD(AbstractGameType gameType) {
        super(gameType);
    }

    public void renderHUD(Renderer renderer) {
        BubbleBlaster game = BubbleBlaster.instance();
        LoadedGame loadedGame = game.getLoadedGame();
        if (loadedGame == null) return;

        AbstractGameType gameType = loadedGame.getGameType();
        PlayerEntity player = gameType.getPlayer();

        Rectangle2D topBar = new Rectangle2D.Double(0, 0, game.getWidth(), 70);
        Rectangle2D topShade = new Rectangle2D.Double(0, 71, game.getWidth(), 30);

        if (this.gameOver) {
            renderer.color(new Color(0, 0, 0, 128));
            renderer.rect(0, 0, game.getWidth(), game.getHeight());
        }

        // Top-bar.
        renderer.color(new Color(0x7f000000, true));
        renderer.fill(topBar);
        renderer.outline(topBar);

        // Health line.
        renderer.stroke(this.healthLine);
        renderer.color(new Color(0x7ffffff, true));
        renderer.line(0, 69, game.getWidth(), 69);

        // Old paint.
        Paint old = renderer.getPaint();

        // Shadow
        GradientPaint p = new GradientPaint(0f, 71f, new Color(0, 0, 0, 48), 0f, 100f, new Color(0, 0, 0, 0));
        renderer.paint(p);
        renderer.fill(topShade);
        renderer.outline(topShade);
        renderer.paint(old);

        // Assign colors for title and description.
        Color titleColor = new Color(255, 128, 0);
        Color descriptionColor = new Color(255, 255, 255);

        try {
            // EffectInstance image.
            Image effectImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/bubbleblaster/textures/gui/effect_banner.png")));

            int i = 0;
            for (StatusEffectInstance statusEffectInstance : Objects.requireNonNull(player).getActiveEffects()) {
                // GraphicsProcessor 2D
                Renderer graphics2D = renderer.create(320 + i * 196, 16, 192, 38);

                // Format duration to string.
                String time = TimeUtils.formatDuration(statusEffectInstance.getRemainingTime());

                // EffectInstance bar.
                graphics2D.image(effectImage, 0, 0);

                // EffectInstance icon.
                graphics2D.image(statusEffectInstance.getType().getIcon(32, 32, new Color(0, 191, 191)), 5, 3);
                graphics2D.color(new Color(255, 255, 255, 192));

                // Time. 0:00:00
                GraphicsUtils.drawLeftAnchoredString(graphics2D, time, new Point2D.Double(56, 2), 35, new Font(game.getFont().getFontName(), Font.BOLD, 16));
                graphics2D.dispose();

                // Next
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {

        }

        renderer.color(titleColor);
        if (!gameOver) {
            if (player != null) {
                // Score
                renderer.color(titleColor);
                GraphicsUtils.drawCenteredString(renderer, I18n.translateToLocal("info.bubbleblaster.score"), new Rectangle(10, 10, 80, 20), infoTitleFont);
                renderer.color(descriptionColor);
                GraphicsUtils.drawCenteredString(renderer, String.valueOf((int) player.getScore()), new Rectangle(10, 40, 80, 20), infoValueFont);

                // Level
                renderer.color(titleColor);
                GraphicsUtils.drawCenteredString(renderer, I18n.translateToLocal("info.bubbleblaster.level"), new Rectangle(100, 10, 80, 20), infoTitleFont);
                renderer.color(descriptionColor);
                GraphicsUtils.drawCenteredString(renderer, String.valueOf(player.getLevel()), new Rectangle(100, 40, 80, 20), infoValueFont);

                // Prepare for health display.
                int greenValue;
                int redValue;
                double playerMaxDamage = player.getMaxDamageValue() / 2;
                double playerDamage = player.getDamageValue();

                // Calculate colors based on damage and max damage.
                playerDamage = MathHelper.clamp(playerDamage, 0, player.getMaxDamageValue());
                if (player.getDamageValue() > player.getMaxDamageValue() / 2) {
                    redValue = (int) ((playerMaxDamage - ((playerDamage - playerMaxDamage))) * 255 / playerMaxDamage);
                    redValue = (int) MathHelper.clamp((double) redValue, 0, 255);
                    greenValue = 255;
                } else {
                    greenValue = (int) ((playerDamage) * 255 / playerMaxDamage);
                    greenValue = (int) MathHelper.clamp((double) greenValue, 0, 255);
                    redValue = 255;
                }

                // Render health bar.
                renderer.stroke(healthLine);
                renderer.color(new Color(redValue, greenValue, 0));
                renderer.line(0, 69, (int) (playerDamage / playerMaxDamage * game.getWidth()), 69);
            }
        }

        // Render FPS text.
        renderer.color(new Color(0, 165, 220, 127));
        GraphicsUtils.drawRightAnchoredString(renderer, ((Integer) game.getFps()).toString(), new Point2D.Double(game.getWidth() - 10, 10), 20, infoTitleFont);

        renderer.color(Color.white);

        // Game over message.
        if (gameOver) {
            if ((System.currentTimeMillis() - gameOverTime) % 1000 < 500) {
                renderer.color(new Color(255, 0, 0));
            } else {
                renderer.color(new Color(255, 128, 0));
            }

            GraphicsUtils.drawCenteredString(renderer, "GAME OVER", gameType.getGameBounds(), gameOverFont);
        }

        // Gradient.
        GradientPaint gp = new GradientPaint(0f, 0f, new Color(0, 0, 0, 0), 0f, 70f, new Color(0, 0, 0, 24));
        renderer.paint(gp);
        renderer.fill(topBar);
        renderer.outline(topBar);
        renderer.paint(old);
    }

    /**
     * Update Event
     * Used for updating values.
     *
     * @see TickEvent
     */
    @Override
    @SubscribeEvent
    public void tick() {

    }

    /**
     * Sets Game Over flag
     * Yes, as the title says: it sets the game over flag in the HUD.
     *
     * @see ClassicType#triggerGameOver()
     */
    public void setGameOver() {
        gameOver = true;
        gameOverTime = System.currentTimeMillis();
    }
}
