package com.qtech.bubbles.gametype.hud;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.LoadedGame;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.gametype.HUD;
import com.qtech.bubbles.common.text.translation.I18n;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.core.utils.categories.TimeUtils;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.event.TickEvent;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.gametype.ClassicType;
import com.qtech.bubbles.util.helpers.MathHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Objects;

/**
 * <h1>Classic HUD</h1>
 * The HUD that was is older versions and editions of the game.
 *
 * @see HUD
 */
public class ClassicHUD extends HUD {
    private boolean gameOver;
    private long gameOverTime;
    private final Font gameOverFont = new Font("Chicle Regular", Font.BOLD, 50);
    private final Font infoTitleFont = new Font("Helvetica", Font.BOLD, 18);
    private final Font infoValueFont = new Font("Helvetica", Font.PLAIN, 14);

    public ClassicHUD(AbstractGameType gameType) {
        super(gameType);
    }

    public void renderHUD(Graphics2D gg) {
        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();
        if (loadedGame == null) return;

        AbstractGameType gameType = loadedGame.getGameType();
        PlayerEntity player = gameType.getPlayer();

        Rectangle2D topBar = new Rectangle2D.Double(0, 0, BubbleBlaster.getInstance().getWidth(), 70);
        Rectangle2D topShade = new Rectangle2D.Double(0, 71, BubbleBlaster.getInstance().getWidth(), 30);

        if (gameOver) {
            gg.setColor(new Color(0, 0, 0, 128));
            gg.fillRect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
        }

        // Top-bar.
        gg.setColor(new Color(96, 96, 96));
        gg.fill(topBar);
        gg.draw(topBar);

        Paint old = gg.getPaint();

        // Shadow
        GradientPaint p = new GradientPaint(0f, 71f, new Color(0, 0, 0, 48), 0f, 100f, new Color(0, 0, 0, 0));
        gg.setPaint(p);
        gg.fill(topShade);
        gg.draw(topShade);
        gg.setPaint(old);

        // Assign colors for title and description.
        Color titleColor = new Color(255, 128, 0);
        Color descriptionColor = new Color(255, 255, 255);

        try {
            // EffectInstance image.
            Image effectImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/bubbleblaster/textures/gui/effect_banner.png")));

            int i = 0;
            for (StatusEffectInstance statusEffectInstance : Objects.requireNonNull(player).getActiveEffects()) {
                // Graphics 2D
                Graphics2D graphics2D = (Graphics2D) gg.create(320 + i * 196, 16, 192, 38);

                // Format duration to string.
                String time = TimeUtils.formatDuration(statusEffectInstance.getRemainingTime());

                // EffectInstance bar.
                graphics2D.drawImage(effectImage, 0, 0, BubbleBlaster.getInstance());

                // EffectInstance icon.
                graphics2D.drawImage(statusEffectInstance.getType().getIcon(32, 32, new Color(0, 191, 191)), 5, 3, BubbleBlaster.getInstance());
                graphics2D.setColor(new Color(255, 255, 255, 192));

                // Time. 0:00:00
                GraphicsUtils.drawLeftAnchoredString(graphics2D, time, new Point2D.Double(56, 2), 35, new Font(BubbleBlaster.getInstance().getFont().getFontName(), Font.BOLD, 16));
                graphics2D.dispose();

                // Next
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {

        }


//            gg.drawImage(image, 320, 10, Game.instance());
//            if (GameSettings.instance().isTextAntialiasEnabled())
//                gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setColor(titleColor);
        if (!gameOver) {
            if (player != null) {
                gg.setColor(titleColor);
                GraphicsUtils.drawCenteredString(gg, I18n.translateToLocal("info.qbubbles.score"), new Rectangle(10, 10, 80, 20), infoTitleFont);
                gg.setColor(descriptionColor);
                GraphicsUtils.drawCenteredString(gg, String.valueOf((int) player.getScore()), new Rectangle(10, 40, 80, 20), infoValueFont);

                gg.setColor(titleColor);
                GraphicsUtils.drawCenteredString(gg, I18n.translateToLocal("info.qbubbles.level"), new Rectangle(100, 10, 80, 20), infoTitleFont);
                gg.setColor(descriptionColor);
                GraphicsUtils.drawCenteredString(gg, String.valueOf(player.getLevel()), new Rectangle(100, 40, 80, 20), infoValueFont);

                gg.setColor(titleColor);
                GraphicsUtils.drawCenteredString(gg, I18n.translateToLocal("info.qbubbles.health"), new Rectangle(210, 10, 80, 20), infoTitleFont);
                int greenValue;
                int redValue;
                double val = player.getMaxDamageValue() / 2;
                double HEALTH = player.getDamageValue();


                HEALTH = MathHelper.clamp(HEALTH, 0, player.getMaxDamageValue());
                if (player.getDamageValue() > player.getMaxDamageValue() / 2) {
                    redValue = (int) ((val - ((HEALTH - val))) * 255 / val);
                    redValue = (int) MathHelper.clamp((double) redValue, 0, 255);
                    greenValue = 255;
                } else {
                    greenValue = (int) ((HEALTH) * 255 / val);
                    greenValue = (int) MathHelper.clamp((double) greenValue, 0, 255);
                    redValue = 255;
                }

                gg.setStroke(new BasicStroke(1f));
                gg.setColor(new Color(redValue, greenValue, 0).darker());
                gg.fillRect(210, 40, 80, 20);
                gg.setColor(new Color(redValue, greenValue, 0));
                gg.fillRect(210, 40, (int) (HEALTH / player.getMaxDamageValue() * 80), 20);
                gg.setColor(new Color(redValue, greenValue, 0).brighter());
                gg.drawRect(210, 40, 80, 20);
            }
        }

        gg.setColor(new Color(0, 165, 220));
        GraphicsUtils.drawRightAnchoredString(gg, ((Integer) BubbleBlaster.getFps()).toString(), new Point2D.Double(BubbleBlaster.getInstance().getWidth() - 10, 10), 20, infoTitleFont);

        gg.setColor(Color.white);

        // Game over message.
        if (gameOver) {
            if ((System.currentTimeMillis() - gameOverTime) % 1000 < 500) {
                gg.setColor(new Color(255, 0, 0));
            } else {
                gg.setColor(new Color(255, 128, 0));
            }

            GraphicsUtils.drawCenteredString(gg, "GAME OVER", gameType.getGameBounds(), gameOverFont);
        }

        // Gradient.
        GradientPaint gp = new GradientPaint(0f, 0f, new Color(0, 0, 0, 0), 0f, 70f, new Color(0, 0, 0, 24));
        gg.setPaint(gp);
        gg.fill(topBar);
        gg.draw(topBar);
        gg.setPaint(old);

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
    @Override
    @SubscribeEvent
    public void tick() {

    }

    /**
     * <h1>Sets Game Over flag</h1>
     * Yes, as the title says: it sets the game over flag in the HUD.
     *
     * @see ClassicType#triggerGameOver()
     */
    public void setGameOver() {
        gameOver = true;
        gameOverTime = System.currentTimeMillis();
    }
}
