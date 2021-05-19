package com.qtech.bubbles.screen;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.data.GlobalSaveData;
import com.qtech.bubbles.event.bus.EventBus;

import java.awt.*;

public class GameOverScreen extends Screen {
    private final boolean isHighScore;
    private final Font titleFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 64);
    private final Font descriptionFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 14);
    private final Font scoreFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 32);
    private long score;
    private EventBus.Handler binding;

    public GameOverScreen(long score) {
        this.score = score;

        GlobalSaveData globalSaveData = GlobalSaveData.instance();
        if (!globalSaveData.isLoaded()) {
            if (!globalSaveData.isCreated()) {
                globalSaveData.create();
            }
            globalSaveData.load();
        }

        isHighScore = globalSaveData.getHighScore() < score;

        if (isHighScore) {
            globalSaveData.setHighScore(score, System.currentTimeMillis());
            globalSaveData.dump();
        }

        this.score = score;
    }

    @Override
    public void init() {
        bindEvents();
    }

    @Override
    public boolean onClose(Screen to) {
        unbindEvents();

        return super.onClose(to);
    }

    @Override
    public void render(QBubbles game, Graphics2D gg) {
        LinearGradientPaint gradient = new LinearGradientPaint(0, 0, 0, game.getHeight(), new float[]{0.0f, 0.4f}, new Color[]{new Color(0, 0, 224), new Color(0, 0, 128)});
        Paint old = gg.getPaint();
        gg.setPaint(gradient);
        gg.fillRect(0, 0, game.getWidth(), game.getHeight());
        gg.setPaint(old);
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {
        gg.setColor(Color.white);

        if (isHighScore) {
            GraphicsUtils.drawCenteredString(gg, "Congratulations!", new Rectangle(0, 120, game.getWidth(), 64), titleFont);
            GraphicsUtils.drawCenteredString(gg, "You beat your high-score!", new Rectangle(0, 184, game.getWidth(), 64), descriptionFont);
        }

        GraphicsUtils.drawCenteredString(gg, Long.toString(score), new Rectangle(0, 248, game.getWidth(), 64), scoreFont);
    }

    public boolean isHighScore() {
        return isHighScore;
    }
}