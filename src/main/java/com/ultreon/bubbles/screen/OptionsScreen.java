package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.text.translation.I18n;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event._common.SubscribeEvent;
import com.ultreon.hydro.event.bus.EventBus;
import com.ultreon.bubbles.render.gui.OptionsButton;
import com.ultreon.bubbles.render.gui.OptionsNumberInput;
import com.ultreon.bubbles.settings.GameSettings;
import com.ultreon.bubbles.util.Util;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;

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
        BubbleBlaster.getEventBus().register(this);

        maxBubblesOption.bindEvents();
        languageButton.bindEvents();
        cancelButton.bindEvents();
        saveButton.bindEvents();
    }

    @Override
    public boolean onClose(Screen to) {
        BubbleBlaster.getEventBus().unregister(this);

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
    public void render(Game game, Renderer gg) {
        this.render((BubbleBlaster) game, gg);
    }

    public void render(BubbleBlaster game, Renderer gg) {
        maxBubblesOption.setX((int) BubbleBlaster.getMiddleX() - 322);
        maxBubblesOption.setY((int) BubbleBlaster.getMiddleY() + 101);
        maxBubblesOption.setWidth(321);

        languageButton.setX((int) BubbleBlaster.getMiddleX() + 1);
        languageButton.setY((int) BubbleBlaster.getMiddleY() + 101);
        languageButton.setWidth(321);

        cancelButton.setX((int) BubbleBlaster.getMiddleX() - 322);
        cancelButton.setY((int) BubbleBlaster.getMiddleY() + 151);
        cancelButton.setWidth(321);

        saveButton.setX((int) BubbleBlaster.getMiddleX() + 1);
        saveButton.setY((int) BubbleBlaster.getMiddleY() + 151);
        saveButton.setWidth(321);

        renderBackground(game, gg);
    }

    @Override
    public void renderGUI(Game game, Renderer gg) {
        this.renderGUI((BubbleBlaster) game, gg);
    }

    public void renderGUI(BubbleBlaster game, Renderer gg) {
        cancelButton.setText(I18n.translateToLocal("other.cancel"));
        cancelButton.paint(gg);

        languageButton.setText(I18n.translateToLocal("scene.qbubbles.options.language"));
        languageButton.paint(gg);

        maxBubblesOption.paint(gg);

        saveButton.paint(gg);
        saveButton.setText(I18n.translateToLocal("other.save"));
    }

    public void renderBackground(BubbleBlaster game, Renderer gg) {
        gg.color(new Color(96, 96, 96));
        gg.rect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
    }

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
    }
}
