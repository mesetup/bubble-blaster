package com.qtech.bubbles.screen;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.InfoTransporter;
import com.qtech.bubbles.common.scene.ScreenManager;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.common.text.translation.I18n;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.event.RenderEventPriority;
import com.qtech.bubbles.event.bus.EventBus;
import com.qtech.bubbles.gametype.ClassicType;
import com.qtech.bubbles.gui.TitleButton;
import com.qtech.bubbles.init.GameTypes;
import com.qtech.bubbles.util.Util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("FieldCanBeLocal")
public class TitleScreen extends Screen {
    private static TitleScreen INSTANCE;
    private final TitleButton startButton;
    private final ClassicType classicType = GameTypes.CLASSIC_TYPE.get();
    private final TitleButton languageButton;
    private final TitleButton savesButton;
    private final TitleButton optionsButton;
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setLoadMessage);
    private String message;
    private int ticks;

    private void setLoadMessage(String s) {
        this.message = s;
    }

    private EventBus.Handler binding;

    public TitleScreen() {
        INSTANCE = this;

        startButton = new TitleButton.Builder().bounds(0, 220, 225, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Start Game").command(this::startGame).build();
        optionsButton = new TitleButton.Builder().bounds(BubbleBlaster.getInstance().getWidth() - 225, 220, 225, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Options").command(this::openOptions).build();
        savesButton = new TitleButton.Builder().bounds(0, 280, 200, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Select Save (WIP)").command(this::openSavesSelection).build();
        languageButton = new TitleButton.Builder().bounds(BubbleBlaster.getInstance().getWidth() - 200, 280, 200, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Language").command(this::openLanguageSettings).build();
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
        BubbleBlaster.getInstance().loadGame();
    }

    @Override
    public void init() {
        message = "";

        BubbleBlaster.getEventBus().register(this);
        startButton.bindEvents();
        savesButton.bindEvents();
        optionsButton.bindEvents();
        languageButton.bindEvents();
    }

    @Override
    public boolean onClose(Screen to) {
        BubbleBlaster.getEventBus().unregister(this);
        startButton.unbindEvents();
        savesButton.unbindEvents();
        optionsButton.unbindEvents();
        languageButton.unbindEvents();
        return super.onClose(to);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public boolean eventsAreActive() {
        return super.eventsAreActive();
    }

    @Override
    public void renderGUI(BubbleBlaster game, GraphicsProcessor gg) {
        gg.setColor(new Color(128, 128, 128));
        gg.fill(BubbleBlaster.getInstance().getBounds());

        startButton.setText(I18n.translateToLocal("scene.bubbleblaster.title.start"));
        optionsButton.setText(I18n.translateToLocal("scene.bubbleblaster.title.options"));
        languageButton.setText(I18n.translateToLocal("scene.bubbleblaster.title.language"));

        startButton.setX(0);
        optionsButton.setX(BubbleBlaster.getInstance().getWidth() - 225);
        languageButton.setX(BubbleBlaster.getInstance().getWidth() - 200);
        savesButton.setX(0);

        gg.setColor(new Color(64, 64, 64));
        gg.fillRect(0, 0, BubbleBlaster.getInstance().getWidth(), 175);

        double shiftX = ((double) BubbleBlaster.getInstance().getWidth() * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);

        GradientPaint p = new GradientPaint((float)shiftX - BubbleBlaster.getInstance().getWidth(), 0f, new Color(0, 192, 255), (float) shiftX, 0f, new Color(0, 255, 192), true);
        gg.setPaint(p);
        gg.fillRect(0, 175, BubbleBlaster.getInstance().getWidth(), 3);

        gg.setColor(new Color(255, 255, 255));
        GraphicsUtils.drawCenteredString(gg, "Bubble Blaster 2", new Rectangle2D.Double(0, 0, BubbleBlaster.getInstance().getWidth(), 145), new Font(BubbleBlaster.getInstance().getGameFont().getFontName(), Font.PLAIN, 87));
        gg.setColor(new Color(255, 255, 255));
        GraphicsUtils.drawCenteredString(gg, message, new Rectangle2D.Double(0, 145, BubbleBlaster.getInstance().getWidth(), 30), new Font(BubbleBlaster.getInstance().getSansFontName(), Font.PLAIN, 24));

        this.startButton.paint(gg);
        this.languageButton.paint(gg);
        this.savesButton.paint(gg);
        this.optionsButton.paint(gg);
    }

    @Override
    public void render(BubbleBlaster game, GraphicsProcessor gg) {

    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void tick() {
        ticks = (ticks + 1) % (BubbleBlaster.TPS * 10);
    }
}
