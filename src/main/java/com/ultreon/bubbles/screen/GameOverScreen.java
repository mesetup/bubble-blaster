package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.data.GlobalSaveData;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.bus.AbstractEvents;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.util.GraphicsUtils;

import java.awt.*;

public class GameOverScreen extends Screen {
    private final boolean isHighScore;
    private final Font titleFont = new Font(BubbleBlaster.instance().getSansFontName(), Font.BOLD, 64);
    private final Font descriptionFont = new Font(BubbleBlaster.instance().getSansFontName(), Font.BOLD, 14);
    private final Font scoreFont = new Font(BubbleBlaster.instance().getSansFontName(), Font.BOLD, 32);
    private long score;
    private AbstractEvents.AbstractSubscription binding;

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
        make();
    }

    @Override
    public boolean onClose(Screen to) {
        destroy();

        return super.onClose(to);
    }

    @Override
    public void render(Game game, Renderer gg) {
        this.render((BubbleBlaster) game, gg);
    }

    public void render(BubbleBlaster game, Renderer gg) {
        LinearGradientPaint gradient = new LinearGradientPaint(0, 0, 0, game.getScaledHeight(), new float[]{0.0f, 0.4f}, new Color[]{new Color(0, 0, 224), new Color(0, 0, 128)});
        Paint old = gg.getPaint();
        gg.paint(gradient);
        gg.rect(0, 0, game.getScaledWidth(), game.getScaledHeight());
        gg.paint(old);
    }

    @Override
    public void renderGUI(Game game, Renderer gg) {
        this.renderGUI((BubbleBlaster) game, gg);
    }

    public void renderGUI(BubbleBlaster game, Renderer gg) {
        gg.color(Color.white);

        if (isHighScore) {
            GraphicsUtils.drawCenteredString(gg, "Congratulations!", new Rectangle(0, 120, game.getScaledWidth(), 64), titleFont);
            GraphicsUtils.drawCenteredString(gg, "You beat your high-score!", new Rectangle(0, 184, game.getScaledWidth(), 64), descriptionFont);
        }

        GraphicsUtils.drawCenteredString(gg, Long.toString(score), new Rectangle(0, 248, game.getScaledWidth(), 64), scoreFont);
    }

    public boolean isHighScore() {
        return isHighScore;
    }
}
