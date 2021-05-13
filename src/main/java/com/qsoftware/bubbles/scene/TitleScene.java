package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.InfoTransporter;
import com.qsoftware.bubbles.common.References;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.common.scene.SceneManager;
import com.qsoftware.bubbles.common.text.translation.I18n;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.core.utils.categories.GraphicsUtils;
import com.qsoftware.bubbles.event.RenderEventPriority;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.gametype.ClassicType;
import com.qsoftware.bubbles.gui.TitleButton;
import com.qsoftware.bubbles.init.GameTypeInit;
import com.qsoftware.bubbles.util.Util;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;

@SuppressWarnings("FieldCanBeLocal")
public class TitleScene extends Scene {
    private static TitleScene INSTANCE;
    private final TitleButton startButton;
    private final ClassicType classicType = GameTypeInit.CLASSIC_TYPE.get();
    private final TitleButton languageButton;
    private final TitleButton savesButton;
    private final TitleButton optionsButton;
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setLoadMessage);
    private String message;

    private void setLoadMessage(String s) {
        this.message = s;
    }

    private EventBus.Handler binding;

    public TitleScene() {
        INSTANCE = this;

        startButton = new TitleButton.Builder().bounds(0, 220, 225, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Start Game").command(this::startGame).build();
        optionsButton = new TitleButton.Builder().bounds(QBubbles.getInstance().getWidth() - 225, 220, 225, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Options").command(this::openOptions).build();
        savesButton = new TitleButton.Builder().bounds(0, 280, 200, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Select Save (WIP)").command(this::openSavesSelection).build();
        languageButton = new TitleButton.Builder().bounds(QBubbles.getInstance().getWidth() - 200, 280, 200, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Language").command(this::openLanguageSettings).build();
    }

    private void openSavesSelection() {
        SceneManager sceneManager = Util.getSceneManager();
        sceneManager.displayScene(new SavesScene(this));
    }

    public static TitleScene instance() {
        return INSTANCE;
    }

    private void openLanguageSettings() {
        SceneManager sceneManager = Util.getSceneManager();
        sceneManager.displayScene(new LanguageScene(this));
    }

    private void openOptions() {
        SceneManager sceneManager = Util.getSceneManager();
        sceneManager.displayScene(new OptionsScene(this));
    }

    private void startGame() {
        SceneManager sceneManager = Util.getSceneManager();
        sceneManager.displayScene(new EnvLoadScene(SavedGame.fromFile(new File(References.SAVES_DIR, "save")), GameTypeInit.CLASSIC_TYPE::get));
    }

    @Override
    public void showScene() {
        message = "";

        bindEvents();
    }

    @Override
    public boolean hideScene(Scene to) {
        unbindEvents();
        return super.hideScene(to);
    }

    @Override
    public void bindEvents() {
        super.bindEvents();

        QBubbles.getEventBus().register(this);
        startButton.bindEvents();
        savesButton.bindEvents();
        optionsButton.bindEvents();
        languageButton.bindEvents();
    }

    @Override
    public void unbindEvents() {
        super.unbindEvents();

        QBubbles.getEventBus().unregister(this);
        startButton.unbindEvents();
        savesButton.unbindEvents();
        optionsButton.unbindEvents();
        languageButton.unbindEvents();
    }

    @Override
    public boolean eventsAreActive() {
        return super.eventsAreActive();
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {
        startButton.setText(I18n.translateToLocal("scene.qbubbles.title.start"));
        optionsButton.setText(I18n.translateToLocal("scene.qbubbles.title.options"));
        languageButton.setText(I18n.translateToLocal("scene.qbubbles.title.language"));

        startButton.setX(0);
        optionsButton.setX(QBubbles.getInstance().getWidth() - 225);
        languageButton.setX(QBubbles.getInstance().getWidth() - 200);
        savesButton.setX(0);

        gg.setColor(new Color(64, 64, 64));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), 175);

        gg.setColor(new Color(255, 255, 255));
        GraphicsUtils.drawCenteredString(gg, "Q-Bubbles", new Rectangle2D.Double(0, 0, QBubbles.getInstance().getWidth(), 145), new Font(QBubbles.getInstance().getGameFont().getFontName(), Font.PLAIN, 87));
        gg.setColor(new Color(255, 255, 255));
        GraphicsUtils.drawCenteredString(gg, message, new Rectangle2D.Double(0, 145, QBubbles.getInstance().getWidth(), 30), new Font(QBubbles.getInstance().getSansFontName(), Font.PLAIN, 24));

        this.startButton.paint(gg);
        this.languageButton.paint(gg);
        this.savesButton.paint(gg);
        this.optionsButton.paint(gg);
    }

    @Override
    public void render(QBubbles game, Graphics2D gg) {
//        if (this.classicType.isInitialized()) {
//            this.classicType.render(gg);
//        }
//        gg.setBackground(Color.WHITE);
//        gg.clearRect(0, 0, game.getWindow().getWidth(), game.getWindow().getHeight());
//        gg.setColor(Color.WHITE);
//        gg.fillRect(0, 0, game.getWindow().getWidth(), game.getWindow().getHeight());
//
//        Shape shape1 = Background.getShape(0, game.getWindow().getHeight() / 2 + 50, 100, game.getWindow().getHeight());
//        gg.setColor(new Color(128, 128, 128));
//        gg.fill(shape1);
//
//        Shape shape2 = Background.getShape(150, game.getWindow().getHeight() / 2 + 150, game.getWindow().getWidth() - 300, game.getWindow().getHeight());
//        gg.setColor(new Color(192, 192, 192));
//        gg.fill(shape2);
//
//        Shape shape3 = Background.getShape(game.getWindow().getWidth() - 150, game.getWindow().getHeight() / 2 + 150, 100, game.getWindow().getHeight());
//        gg.setColor(new Color(128, 128, 128));
//        gg.fill(shape3);
    }

    @Override
    public void tick() {
//        this.classicType.tick();
    }
}
