package com.qtech.bubbles.scene;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.common.text.translation.I18n;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.TickEvent;
import com.qtech.bubbles.event.bus.EventBus;
import com.qtech.bubbles.gui.OptionsButton;
import com.qtech.bubbles.gui.OptionsNumberInput;
import com.qtech.bubbles.settings.GameSettings;
import com.qtech.bubbles.util.Util;

import java.awt.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class OptionsScreen extends Screen {
    private static OptionsScreen INSTANCE;
    private final OptionsNumberInput maxBubblesOption;
    private final OptionsButton languageButton;
    private final OptionsButton cancelButton;
    private final OptionsButton saveButton;
    private Screen backScene;
    private EventBus.Handler binding;

    public OptionsScreen(Screen backScene) {
        super();

        OptionsScreen.INSTANCE = this;

        this.backScene = backScene;

        this.maxBubblesOption = new OptionsNumberInput(0, 0, 321, 48, GameSettings.instance().getMaxBubbles(), 400, 2000);
        this.languageButton = new OptionsButton.Builder().bounds(0, 0, 321, 48).command(this::showLanguages).build();
        this.cancelButton = new OptionsButton.Builder().bounds(0, 0, 321, 48).command(this::back).build();
        this.saveButton = new OptionsButton.Builder().bounds(0, 0, 321, 48).command(this::save).build();
    }

    public static OptionsScreen instance() {
        return INSTANCE;
    }

    private void save() {
        int maxBubbles = maxBubblesOption.getValue();

        GameSettings settings = GameSettings.instance();
        settings.setMaxBubbles(maxBubbles);
        settings.save();
    }

    private void showLanguages() {
        Objects.requireNonNull(Util.getSceneManager()).displayScreen(new LanguageScreen(this));
    }

    private void back() {
        Objects.requireNonNull(Util.getSceneManager()).displayScreen(backScene);
    }

    @Override
    public void init() {
        QBubbles.getEventBus().register(this);

        maxBubblesOption.bindEvents();
        languageButton.bindEvents();
        cancelButton.bindEvents();
        saveButton.bindEvents();
    }

    @Override
    public boolean onClose(Screen to) {
        QBubbles.getEventBus().unregister(this);

        maxBubblesOption.unbindEvents();
        languageButton.unbindEvents();
        cancelButton.unbindEvents();
        saveButton.unbindEvents();

        if (to == backScene) {
            backScene = null;
        }
        return super.onClose(to);
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

        renderBackground(game, gg);
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {
        cancelButton.setText(I18n.translateToLocal("other.cancel"));
        cancelButton.paint(gg);

        languageButton.setText(I18n.translateToLocal("scene.qbubbles.options.language"));
        languageButton.paint(gg);

        maxBubblesOption.render(gg);

        saveButton.paint(gg);
        saveButton.setText(I18n.translateToLocal("other.save"));
    }

    public synchronized void renderBackground(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(96, 96, 96));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
    }

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
    }
}
