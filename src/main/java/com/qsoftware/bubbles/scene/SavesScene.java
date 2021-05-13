package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.TickEvent;
import com.qsoftware.bubbles.save.SaveLoader;
import com.qsoftware.bubbles.util.Util;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class SavesScene extends Scene {
    @Nullable
    private static SavesScene INSTANCE;
    private final Collection<Supplier<SavedGame>> saves;
    @Nullable
    private SavedGame selectedSave;
    private Scene backScene;

    public SavesScene(Scene backScene) {
        super();

        SavesScene.INSTANCE = this;

        // Configure back scene.
        this.backScene = backScene;

        // Logging.
        System.out.println("Initializing SavesScene");

        this.saves = SaveLoader.getInstance().getSaves();
    }

    // Mouse listener.
    private static class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent me) {
            System.out.println(me);
        }
    }

    private void newSave() {
//        Objects.requireNonNull(Util.getSceneManager()).displayScene(new CreateSaveScene(this));
    }

    private void openSave() {
        Objects.requireNonNull(Util.getSceneManager()).displayScene(new SaveLoadingScene(selectedSave));
    }

    public static SavesScene instance() {
        return INSTANCE;
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

//        scrollPane.validate();
    }

    @Override
    public boolean hideScene(Scene to) {
        unbindEvents();

        if (to == backScene) {
            backScene = null;
        }

//        scrollPane.invalidate();
        return super.hideScene(to);
    }

    @Override
    public void bindEvents() {
        super.bindEvents();
        QBubbles.getEventBus().register(this);

//        panel.setVisible(true);

//        languageButton.bindEvents();
//        cancelButton.bindEvents();
    }

    @Override
    public void unbindEvents() {
        super.unbindEvents();
        QBubbles.getEventBus().unregister(this);

//        panel.setVisible(false);

//        languageButton.unbindEvents();
//        cancelButton.unbindEvents();
    }

    @Override
    public void render(QBubbles game, Graphics2D gg) {
//        languageButton.setX((int) Game.getMiddleX() - 322);
//        languageButton.setY((int) Game.getMiddleY() + 101);

//        cancelButton.setX((int) Game.getMiddleX() - 322);
//        cancelButton.setY((int) Game.getMiddleY() + 151);

//        if (evt.getPriority() == RenderEventPriority.BACKGROUND) {
//        }

//        if (evt.getPriority() == RenderEventPriority.FOREGROUND) {
//        }

        renderBackground(game, gg);
    }

    @Override
    public synchronized void renderGUI(QBubbles game, Graphics2D gg) {
//        cancelButton.setText(I18n.translateToLocal("other.cancel"));
//        cancelButton.render(game, gg);

//        languageButton.setText(I18n.translateToLocal("scene.qbubbles.options.language"));
//        languageButton.render(game, gg);

//        scrollPane.setPreferredSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        scrollPane.setSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        panel.setPreferredSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        panel.setSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        scrollPane.setLocation(Game.instance().getWidth() / 2 - 300, 0);

//        scrollPane.paintAll(gg.create(Game.instance().getWidth() / 2 - 400, 0, 800, Game.instance().getHeight()));
//        scrollPane.repaint(0);
//        scrollPane.addNotify();
//        scrollPane.invalidate();

//        Game.instance().getWindow().repaint();
//        Game.instance().getWindow().revalidate();

//        scrollPane.setVisible(true);

//        panel.revalidate();
//        scrollPane.repaint();
//        panel.repaint(0);
//        scrollPane.repaint(0);
//        panel.revalidate();
//        scrollPane.revalidate();

////        savesDisplay.paint(gg);
////        savesDisplay.paintComponents(gg);
////        savesDisplay.paintAll(gg);
//        savesDisplay.paint(gg.create(Game.instance().getWidth() / 2 - 400, 0, 800, Game.instance().getHeight()));
//        savesDisplay.repaint(0);
    }

    public synchronized void renderBackground(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(96, 96, 96));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
    }

    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
    }
}
