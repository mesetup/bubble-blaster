package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.save.SaveLoader;
import com.ultreon.bubbles.save.SavedGame;
import com.ultreon.bubbles.util.Util;
import com.ultreon.commons.annotation.FieldsAreNonnullByDefault;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;

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
@SuppressWarnings({"FieldCanBeLocal", "unused", "CommentedOutCode"})
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

        this.saves = SaveLoader.instance().getSaves();
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
            BubbleBlaster.instance().loadSave(selectedSave);
        }
    }

    @Nullable
    public static SavesScreen instance() {
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
        BubbleBlaster.getEventBus().subscribe(this);

//        panel.setVisible(true);

//        languageButton.bindEvents();
//        cancelButton.bindEvents();
//        scrollPane.validate();
    }

    @Override
    public boolean onClose(Screen to) {
        BubbleBlaster.getEventBus().unsubscribe(this);

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
    public void render(Game game, Renderer gg) {
//        languageButton.setX((int) Game.getMiddleX() - 322);
//        languageButton.setY((int) Game.getMiddleY() + 101);

//        cancelButton.setX((int) Game.getMiddleX() - 322);
//        cancelButton.setY((int) Game.getMiddleY() + 151);

//        if (evt.getPriority() == RenderEventPriority.BACKGROUND) {
//        }

//        if (evt.getPriority() == RenderEventPriority.FOREGROUND) {
//        }

        renderBackground((BubbleBlaster) game, gg);
    }

    @Override
    public void renderGUI(Game game, Renderer gg) {
//        cancelButton.setText(I18n.translateToLocal("other.cancel"));
//        cancelButton.render(game, gg);

//        languageButton.setText(I18n.translateToLocal("scene.BubbleBlaster.options.language"));
//        languageButton.render(game, gg);

//        scrollPane.setPreferredSize(new Dimension(800, BubbleBlaster.getInstance().getScaledHeight()));
//        scrollPane.setSize(new Dimension(800, BubbleBlaster.getInstance().getScaledHeight()));
//        panel.setPreferredSize(new Dimension(800, BubbleBlaster.getInstance().getScaledHeight()));
//        panel.setSize(new Dimension(800, BubbleBlaster.getInstance().getScaledHeight()));
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

    public void renderBackground(BubbleBlaster game, Renderer gg) {
        gg.color(new Color(96, 96, 96));
        gg.rect(0, 0, game.getWidth(), game.getHeight());
    }

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
    }
}
