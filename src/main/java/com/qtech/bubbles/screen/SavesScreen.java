package com.qtech.bubbles.screen;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.core.common.SavedGame;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.TickEvent;
import com.qtech.bubbles.save.SaveLoader;
import com.qtech.bubbles.util.Util;

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
public class SavesScreen extends Screen {
    @Nullable
    private static SavesScreen instance;
    private final Collection<Supplier<SavedGame>> saves;
    @Nullable
    private SavedGame selectedSave;
    @Nullable
    private Screen backScreen;

    public SavesScreen(Screen backScreen) {
        super();

        SavesScreen.instance = this;

        // Configure back scene.
        this.backScreen = backScreen;

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

    @SuppressWarnings("EmptyMethod")
    private void newSave() {
//        Objects.requireNonNull(Util.getSceneManager()).displayScene(new CreateSaveScene(this));
    }

    private void openSave() {
        if (selectedSave != null) {
            Objects.requireNonNull(Util.getSceneManager()).displayScreen(new SaveLoadingScreen(selectedSave));
        }
    }

    @Nullable
    public static SavesScreen getInstance() {
        return instance;
    }

    private void showLanguages() {
        Objects.requireNonNull(Util.getSceneManager()).displayScreen(new LanguageScreen(this));
    }

    private void back() {
        Objects.requireNonNull(Util.getSceneManager()).displayScreen(backScreen);
    }

    @Override
    public void init() {
        QBubbles.getEventBus().register(this);

//        panel.setVisible(true);

//        languageButton.bindEvents();
//        cancelButton.bindEvents();
//        scrollPane.validate();
    }

    @Override
    public boolean onClose(Screen to) {
        QBubbles.getEventBus().unregister(this);

//        panel.setVisible(false);

//        languageButton.unbindEvents();
//        cancelButton.unbindEvents();

        if (to == backScreen) {
            backScreen = null;
        }

//        scrollPane.invalidate();
        return super.onClose(to);
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

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
    }
}
