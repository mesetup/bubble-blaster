package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.common.text.translation.I18n;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.TickEvent;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.gui.OptionsButton;
import com.qsoftware.bubbles.gui.OptionsNumberInput;
import com.qsoftware.bubbles.settings.GameSettings;
import com.qsoftware.bubbles.util.Util;

import java.awt.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class OptionsScene extends Scene {
    private static OptionsScene INSTANCE;
    private final OptionsNumberInput maxBubblesOption;
    private final OptionsButton languageButton;
    private final OptionsButton cancelButton;
    private final OptionsButton saveButton;
    private Scene backScene;
    private EventBus.Handler binding;

    public OptionsScene(Scene backScene) {
        super();

        OptionsScene.INSTANCE = this;

        this.backScene = backScene;

        this.maxBubblesOption = new OptionsNumberInput(0, 0, 321, 48, GameSettings.instance().getMaxBubbles(), 400, 2000);
        this.languageButton = new OptionsButton.Builder().bounds(0, 0, 321, 48).command(this::showLanguages).build();
        this.cancelButton = new OptionsButton.Builder().bounds(0, 0, 321, 48).command(this::back).build();
        this.saveButton = new OptionsButton.Builder().bounds(0, 0, 321, 48).command(this::save).build();
    }

    public static OptionsScene instance() {
        return INSTANCE;
    }

    private void save() {
        int maxBubbles = maxBubblesOption.getValue();

        GameSettings settings = GameSettings.instance();
        settings.setMaxBubbles(maxBubbles);
        settings.save();
    }

    private void showLanguages() {
        Objects.requireNonNull(Util.getSceneManager()).displayScene(new LanguageScene(this));
    }

    private void back() {
        Objects.requireNonNull(Util.getSceneManager()).displayScene(backScene);
    }

    @Override
    public void showScene() {
        bindEvents();
    }

    @Override
    public boolean hideScene(Scene to) {
        unbindEvents();

        if (to == backScene) {
            backScene = null;
        }
        return super.hideScene(to);
    }

    @Override
    public void bindEvents() {
        super.bindEvents();
        QBubbles.getEventBus().register(this);

        maxBubblesOption.bindEvents();
        languageButton.bindEvents();
        cancelButton.bindEvents();
        saveButton.bindEvents();
    }

    @Override
    public void unbindEvents() {
        super.unbindEvents();
        QBubbles.getEventBus().unregister(this);

        maxBubblesOption.unbindEvents();
        languageButton.unbindEvents();
        cancelButton.unbindEvents();
        saveButton.unbindEvents();
    }

    @Override
    public void render(QBubbles game, Graphics2D gg) {
        maxBubblesOption.setX((int) QBubbles.getMiddleX() - 322);
        maxBubblesOption.setY((int) QBubbles.getMiddleY() + 101);
        maxBubblesOption.setWidth(321);

        languageButton.setX((int) QBubbles.getMiddleX() + 1);
        languageButton.setY((int) QBubbles.getMiddleY() + 101);
        languageButton.setWidth(321);

        cancelButton.setX((int) QBubbles.getMiddleX() - 322);
        cancelButton.setY((int) QBubbles.getMiddleY() + 151);
        cancelButton.setWidth(321);

        saveButton.setX((int) QBubbles.getMiddleX() + 1);
        saveButton.setY((int) QBubbles.getMiddleY() + 151);
        saveButton.setWidth(321);

//        if (evt.getPriority() == RenderEventPriority.BACKGROUND) {
//        }

//        if (evt.getPriority() == RenderEventPriority.FOREGROUND) {
//        }

        renderBackground(game, gg);
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {
        cancelButton.setText(I18n.translateToLocal("other.cancel"));
        cancelButton.paint(gg);

        languageButton.setText(I18n.translateToLocal("scene.qbubbles.options.language"));
        languageButton.paint(gg);

        maxBubblesOption.render(game, gg);

        saveButton.paint(gg);
        saveButton.setText(I18n.translateToLocal("other.save"));
    }

    public synchronized void renderBackground(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(96, 96, 96));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
    }

    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
    }
}
