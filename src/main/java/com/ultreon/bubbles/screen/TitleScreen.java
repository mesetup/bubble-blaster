package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.text.translation.I18n;
import com.ultreon.bubbles.render.gui.TitleButton;
import com.ultreon.bubbles.util.Util;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.bus.AbstractEvents;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.ScreenManager;
import com.ultreon.hydro.util.GraphicsUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("FieldCanBeLocal")
public class TitleScreen extends Screen {
    private static TitleScreen INSTANCE;
    private final TitleButton startButton;
    private final TitleButton languageButton;
    private final TitleButton savesButton;
    private final TitleButton optionsButton;
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setLoadMessage);
    private String message;
    private int ticks;

    private void setLoadMessage(String s) {
        this.message = s;
    }

    private AbstractEvents.AbstractSubscription binding;

    public TitleScreen() {
        INSTANCE = this;

        startButton = add(new TitleButton.Builder().bounds(0, 220, 225, 48).text("Start Game").command(this::startGame).build());
        optionsButton = add(new TitleButton.Builder().bounds(BubbleBlaster.instance().getWidth() - 225, 220, 225, 48).text("Options").command(this::openOptions).build());
        savesButton = add(new TitleButton.Builder().bounds(0, 280, 200, 48).text("Select Save (WIP)").command(this::openSavesSelection).build());
        languageButton = add(new TitleButton.Builder().bounds(BubbleBlaster.instance().getWidth() - 200, 280, 200, 48).text("Language").command(this::openLanguageSettings).build());
    }

    private void openSavesSelection() {
        ScreenManager screenManager = Util.getSceneManager();
        screenManager.displayScreen(new SavesScreen(this));
    }

    public static TitleScreen instance() {
        return INSTANCE;
    }

    private void openLanguageSettings() {
        ScreenManager screenManager = Util.getSceneManager();
        screenManager.displayScreen(new LanguageScreen(this));
    }

    private void openOptions() {
        ScreenManager screenManager = Util.getSceneManager();
        screenManager.displayScreen(new OptionsScreen(this));
    }

    private void startGame() {
//        ScreenManager screenManager = Util.getSceneManager();
//        screenManager.displayScreen(new EnvLoadScreen(SavedGame.fromFile(new File(References.SAVES_DIR, "save")), GameTypes.CLASSIC_TYPE::get));
        BubbleBlaster.instance().loadGame();
    }

    @Override
    public void init() {
        message = "";

        BubbleBlaster.getEventBus().subscribe(this);
        startButton.make();
        savesButton.make();
        optionsButton.make();
        languageButton.make();
    }

    @Override
    public boolean onClose(Screen to) {
        BubbleBlaster.getEventBus().unsubscribe(this);
        startButton.destroy();
        savesButton.destroy();
        optionsButton.destroy();
        languageButton.destroy();
        return super.onClose(to);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public void renderGUI(Game game, Renderer gg) {
        this.renderGUI((BubbleBlaster) game, gg);
    }

    public void renderGUI(BubbleBlaster game, Renderer gg) {
        gg.color(new Color(128, 128, 128));
        gg.fill(BubbleBlaster.instance().getGameBounds());

        startButton.setText(I18n.translateToLocal("scene.bubbleblaster.title.start"));
        optionsButton.setText(I18n.translateToLocal("scene.bubbleblaster.title.options"));
        languageButton.setText(I18n.translateToLocal("scene.bubbleblaster.title.language"));

        startButton.setX(0);
        optionsButton.setX(BubbleBlaster.instance().getWidth() - 225);
        languageButton.setX(BubbleBlaster.instance().getWidth() - 200);
        savesButton.setX(0);

        gg.color(new Color(64, 64, 64));
        gg.rect(0, 0, BubbleBlaster.instance().getWidth(), 175);

        double shiftX = ((double) BubbleBlaster.instance().getWidth() * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);

        GradientPaint p = new GradientPaint((float) shiftX - BubbleBlaster.instance().getWidth(), 0f, new Color(0, 192, 255), (float) shiftX, 0f, new Color(0, 255, 192), true);
        gg.paint(p);
        gg.rect(0, 175, BubbleBlaster.instance().getWidth(), 3);

        gg.color(new Color(255, 255, 255));
        GraphicsUtils.drawCenteredString(gg, "Bubble Blaster", new Rectangle2D.Double(0, 0, BubbleBlaster.instance().getWidth(), 145), new Font(BubbleBlaster.instance().getGameFont().getFontName(), Font.PLAIN, 87));
        gg.color(new Color(255, 255, 255));
        GraphicsUtils.drawCenteredString(gg, message, new Rectangle2D.Double(0, 145, BubbleBlaster.instance().getWidth(), 30), new Font(BubbleBlaster.instance().getSansFontName(), Font.PLAIN, 24));

        this.startButton.render(gg);
        this.languageButton.render(gg);
        this.savesButton.render(gg);
        this.optionsButton.render(gg);
    }

    @Override
    public void render(Game game, Renderer gg) {
        this.render((BubbleBlaster) game, gg);
    }

    public void render(BubbleBlaster game, Renderer gg) {

    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void tick() {
        ticks = (ticks + 1) % (BubbleBlaster.TPS * 10);
    }
}
