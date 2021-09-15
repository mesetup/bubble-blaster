package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.bubbles.common.text.translation.I18n;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.util.GraphicsUtils;
import com.ultreon.bubbles.entity.bubble.BubbleSystem;
import com.ultreon.bubbles.environment.EnvironmentRenderer;
import com.ultreon.bubbles.event.LanguageChangeEvent;
import com.ultreon.hydro.event._common.SubscribeEvent;
import com.ultreon.hydro.event.bus.EventBus;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.bubbles.render.gui.PauseButton;
import com.ultreon.bubbles.media.AudioSlot;
import com.ultreon.hydro.registry.Registry;
import com.ultreon.bubbles.util.Util;
import com.ultreon.bubbles.util.helpers.MathHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class PauseScreen extends Screen {
    private final PauseButton exitButton;
    private final PauseButton prevButton;
    private final PauseButton nextButton;

    private String minRadius = I18n.translateToLocal("screen.qbubbles.pause.min_radius");
    private String maxRadius = I18n.translateToLocal("screen.qbubbles.pause.max_radius");
    private String minSpeed = I18n.translateToLocal("screen.qbubbles.pause.min_speed");
    private String maxSpeed = I18n.translateToLocal("screen.qbubbles.pause.max_speed");
    private String defChance = I18n.translateToLocal("screen.qbubbles.pause.default_chance");
    private String curChance = I18n.translateToLocal("screen.qbubbles.pause.current_chance");
    private String defPriority = I18n.translateToLocal("screen.qbubbles.pause.default_priority");
    private String curPriority = I18n.translateToLocal("screen.qbubbles.pause.current_priority");
    private String defTotPriority = I18n.translateToLocal("screen.qbubbles.pause.default_total_priority");
    private String curTotPriority = I18n.translateToLocal("screen.qbubbles.pause.current_total_priority");
    private String scoreMod = I18n.translateToLocal("screen.qbubbles.pause.score_modifier");
    private String attackMod = I18n.translateToLocal("screen.qbubbles.pause.attack_modifier");
    private String defenseMod = I18n.translateToLocal("screen.qbubbles.pause.defense_modifier");
    private String canSpawn = I18n.translateToLocal("screen.qbubbles.pause.can_spawn");
    private String description = I18n.translateToLocal("screen.qbubbles.pause.description");
    private String random = I18n.translateToLocal("other.random");

    private final Font bubbleTitleFont = new Font(BubbleBlaster.getInstance().getMonospaceFontName(), Font.BOLD, 32);
    private final Font bubbleValueFont = new Font(BubbleBlaster.getInstance().getMonospaceFontName(), Font.BOLD + Font.ITALIC, 16);
    private final Font bubbleInfoFont = new Font(BubbleBlaster.getInstance().getMonospaceFontName(), Font.BOLD, 16);
    private final Font fallbackTitleFont = new Font(BubbleBlaster.getInstance().getFont().getFontName(), Font.BOLD, 32);
    private final Font fallbackValueFont = new Font(BubbleBlaster.getInstance().getFont().getFontName(), Font.BOLD + Font.ITALIC, 16);
    private final Font fallbackInfoFont = new Font(BubbleBlaster.getInstance().getFont().getFontName(), Font.BOLD, 16);

    private final Font pauseFont = new Font(BubbleBlaster.getInstance().getGameFontName(), Font.PLAIN, 75);
    private final int differentBubbles;
    private static int helpIndex = 0;
    private AbstractBubble bubble;
    private EventBus.Handler binding;

    public PauseScreen() {
        super();
        exitButton = new PauseButton.Builder().bounds((int) (BubbleBlaster.getMiddleX() - 128), 200, 256, 48).text("Exit and Quit Game").command(BubbleBlaster.getInstance()::shutdown).build();
        prevButton = new PauseButton.Builder().bounds((int) (BubbleBlaster.getMiddleX() - 480), 250, 96, 48).text("Prev").command(this::previousPage).build();
        nextButton = new PauseButton.Builder().bounds((int) (BubbleBlaster.getMiddleX() + 480 - 95), 250, 96, 48).text("Next").command(this::nextPage).build();

        differentBubbles = Registry.getRegistry(AbstractBubble.class).values().size();
        tickPage();
    }

    private void previousPage() {
        if (helpIndex > 0) {
            try {
                AudioSlot focusChangeSFX = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/sfx/ui/button/focus_change.wav")), "focusChange");
                focusChangeSFX.setVolume(0.1d);
                focusChangeSFX.play();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        helpIndex = MathHelper.clamp(helpIndex - 1, 0, differentBubbles - 1);
        tickPage();
    }

    private void nextPage() {
        if (helpIndex < differentBubbles - 1) {
            try {
                AudioSlot focusChangeSFX = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/sfx/ui/button/focus_change.wav")), "focusChange");
                focusChangeSFX.setVolume(0.1d);
                focusChangeSFX.play();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        helpIndex = MathHelper.clamp(helpIndex + 1, 0, differentBubbles - 1);
        tickPage();
    }

    private void tickPage() {
        bubble = new ArrayList<>(Registry.getRegistry(AbstractBubble.class).values()).get(helpIndex);

        if (helpIndex >= differentBubbles - 1 && nextButton.eventsAreActive()) {
            nextButton.unbindEvents();
        } else if (!nextButton.eventsAreActive()) {
            nextButton.bindEvents();
        }

        if (helpIndex <= 0 && prevButton.eventsAreActive()) {
            prevButton.unbindEvents();
        } else if (!prevButton.eventsAreActive()) {
            prevButton.bindEvents();
        }
    }

    @SubscribeEvent
    private void onLanguageChange(LanguageChangeEvent evt) {
        minRadius = I18n.translateToLocal("screen.qbubbles.pause.min_radius");
        maxRadius = I18n.translateToLocal("screen.qbubbles.pause.max_radius");
        minSpeed = I18n.translateToLocal("screen.qbubbles.pause.min_speed");
        maxSpeed = I18n.translateToLocal("screen.qbubbles.pause.max_speed");
        defChance = I18n.translateToLocal("screen.qbubbles.pause.default_chance");
        curChance = I18n.translateToLocal("screen.qbubbles.pause.current_chance");
        defPriority = I18n.translateToLocal("screen.qbubbles.pause.default_priority");
        curPriority = I18n.translateToLocal("screen.qbubbles.pause.current_priority");
        defTotPriority = I18n.translateToLocal("screen.qbubbles.pause.default_total_priority");
        curTotPriority = I18n.translateToLocal("screen.qbubbles.pause.current_total_priority");
        scoreMod = I18n.translateToLocal("screen.qbubbles.pause.score_modifier");
        attackMod = I18n.translateToLocal("screen.qbubbles.pause.attack_modifier");
        defenseMod = I18n.translateToLocal("screen.qbubbles.pause.defense_modifier");
        canSpawn = I18n.translateToLocal("screen.qbubbles.pause.can_spawn");
        description = I18n.translateToLocal("screen.qbubbles.pause.description");
        random = I18n.translateToLocal("other.random");
    }

    @Override
    public void init() {
        exitButton.bindEvents();
        prevButton.bindEvents();
        nextButton.bindEvents();
        BubbleBlaster.getEventBus().register(this);

        if (!BubbleBlaster.getInstance().isGameLoaded()) {
            return;
        }

        Util.setCursor(BubbleBlaster.getInstance().getDefaultCursor());
    }

    @Override
    public boolean onClose(Screen to) {
        exitButton.unbindEvents();
        prevButton.unbindEvents();
        nextButton.unbindEvents();
        BubbleBlaster.getEventBus().unregister(this);

        Util.setCursor(BubbleBlaster.getInstance().getBlankCursor());
        return super.onClose(to);
    }

    @SubscribeEvent
    public void onKeyboard(KeyboardEvent evt) {
        if (evt.getType() == KeyEventType.PRESS) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (!BubbleBlaster.getInstance().isGameLoaded()) {
                    return;
                }

                BubbleBlaster.getInstance().showScreen(null);
            }
        }
    }

    @Override
    public void render(Game game, Renderer gg) {
        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return;
        }

        Renderer ngg = new Renderer(gg);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Darkened background     //
        /////////////////////////////////
        ngg.color(new Color(0, 0, 0, 192));
        ngg.rect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
        Font oldFont = ngg.getFont();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Pause text     //
        ////////////////////////
        ngg.color(new Color(255, 255, 255, 128));
        ngg.font(pauseFont);
        GraphicsUtils.drawCenteredString(ngg, I18n.translateToLocal("screen.qbubbles.pause.text"), new Rectangle2D.Double(0, 90, BubbleBlaster.getInstance().getWidth(), ngg.getFontMetrics(pauseFont).getHeight()), pauseFont);

        ngg.font(oldFont);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Exit button     //
        /////////////////////////
        exitButton.setText(I18n.translateToLocal("screen.qbubbles.pause.exit"));
        exitButton.paint(ngg);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Navigation Buttons & border     //
        /////////////////////////////////////////

        // Navigation buttons.
        nextButton.setText(I18n.translateToLocal("other.next"));
        prevButton.setText(I18n.translateToLocal("other.prev"));

        if (helpIndex > 0) prevButton.paint(ngg);
        if (helpIndex < differentBubbles - 1) nextButton.paint(ngg);

        // Border
        ngg.color(new Color(255, 255, 255, 128));
        ngg.rectLine((int) (BubbleBlaster.getMiddleX() - 480), 300, 960, 300);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Bubble     //
        ////////////////////

        // Bubble name.
        ngg.color(new Color(255, 255, 255, 192));
        ngg.font(bubbleTitleFont);
        ngg.fallbackFont(fallbackTitleFont);
        ngg.text(I18n.translateToLocal("bubble." + bubble.getRegistryName().namespace() + "." + bubble.getRegistryName().path().replaceAll("/", ".") + ".name"), (int) BubbleBlaster.getMiddleX() - 470, 332);

        // Bubble icon.
        EnvironmentRenderer.drawBubble(ngg, (int) (BubbleBlaster.getMiddleX() - 470), 350, 122, bubble.colors);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Info Names     //
        ////////////////////////

        // Set color & font.
        ngg.font(bubbleInfoFont);
        ngg.fallbackFont(fallbackInfoFont);
        ngg.color(new Color(255, 255, 255, 192));

        // Left data.
        ngg.text(minRadius, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 362);
        ngg.text(maxRadius, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 382);
        ngg.text(minSpeed, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 402);
        ngg.text(maxSpeed, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 422);
        ngg.text(defChance, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 442);
        ngg.text(curChance, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 462);

        // Right data.
        ngg.text(defTotPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 322);
        ngg.text(curTotPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 342);
        ngg.text(defPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 362);
        ngg.text(curPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 382);
        ngg.text(scoreMod, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 402);
        ngg.text(attackMod, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 422);
        ngg.text(defenseMod, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 442);
        ngg.text(canSpawn, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 462);

        // Description
        ngg.text(description, (int) BubbleBlaster.getMiddleX() - 470, 502);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Values     //
        ////////////////////

        // Set color & font.
        ngg.font(bubbleValueFont);
        ngg.fallbackFont(fallbackValueFont);
        ngg.color(new Color(255, 255, 255, 128));

        // Left data.
        ngg.text(Integer.toString(bubble.getMinRadius()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 362);
        ngg.text(Integer.toString(bubble.getMaxRadius()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 382);
        ngg.text(Double.toString(bubble.getMinSpeed()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 402);
        ngg.text(Double.toString(bubble.getMaxSpeed()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 422);
        ngg.text(MathHelper.round((double) 100 * BubbleSystem.getDefaultPercentageChance(bubble), 5) + "%", (int) (BubbleBlaster.getMiddleX() - 326) + 200, 442);
        ngg.text(MathHelper.round((double) 100 * BubbleSystem.getPercentageChance(bubble), 5) + "%", (int) (BubbleBlaster.getMiddleX() - 326) + 200, 462);
        ngg.text(BigDecimal.valueOf(BubbleSystem.getDefaultTotalPriority()).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 322);
        ngg.text(BigDecimal.valueOf(BubbleSystem.getTotalPriority()).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 342);
        ngg.text(BigDecimal.valueOf(BubbleSystem.getDefaultPriority(bubble)).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 362);
        ngg.text(BigDecimal.valueOf(BubbleSystem.getPriority(bubble)).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 382);

        // Right data
        if (bubble.isScoreRandom()) {
            ngg.text(I18n.translateToLocal("other.random"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 402);
        } else {
            ngg.text(Double.toString(bubble.getScore()), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 402);
        }
        if (bubble.isAttackRandom()) {
            ngg.text(I18n.translateToLocal("other.random"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 422);
        } else {
            ngg.text(Double.toString(bubble.getAttack()), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 422);
        }
        if (bubble.isDefenseRandom()) {
            ngg.text(I18n.translateToLocal("other.random"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 442);
        } else {
            ngg.text(Double.toString(bubble.getDefense()), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 442);
        }
        ngg.text(bubble.canSpawn(loadedGame.getGameType()) ? I18n.translateToLocal("other.true") : I18n.translateToLocal("other.false"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 462);

        // Description
        ngg.wrappedText(I18n.translateToLocal("bubble." + bubble.getRegistryName().namespace() + "." + bubble.getRegistryName().path().replaceAll("/", ".") + ".description").replaceAll("\\\\n", "\n"), (int) BubbleBlaster.getMiddleX() - 470, 522, 940);

        ngg.font(oldFont);
    }

    private String compress(double totalPriority) {
        if (totalPriority >= 0d && totalPriority < 1_000d) {
            return Double.toString(totalPriority);
        }
        if (totalPriority >= 1_000d && totalPriority < 1_000_000d) {
            return MathHelper.round(totalPriority / 1_000d, 1) + "K";
        }
        if (totalPriority >= 1_000_000d && totalPriority < 1_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000d, 1) + "M";
        }
        if (totalPriority >= 1_000_000_000d && totalPriority < 1_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000d, 1) + "B";
        }
        if (totalPriority >= 1_000_000_000_000d && totalPriority < 1_000_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000_000d, 1) + "T";
        }
        if (totalPriority >= 1_000_000_000_000_000d && totalPriority < 1_000_000_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000_000_000d, 1) + "QD";
        }
        if (totalPriority >= 1_000_000_000_000_000_000d && totalPriority < 1_000_000_000_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000_000_000_000d, 1) + "QT";
        }
        if (totalPriority >= 1_000_000_000_000_000_000_000d && totalPriority < 1_000_000_000_000_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000_000_000_000_000d, 1) + "S";
        }
        if (totalPriority >= 1_000_000_000_000_000_000_000_000d && totalPriority < 1_000_000_000_000_000_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000_000_000_000_000_000d, 1) + "SX";
        }
        if (totalPriority >= 1_000_000_000_000_000_000_000_000_000d && totalPriority < 1_000_000_000_000_000_000_000_000_000_000d) {
            return MathHelper.round(totalPriority / 1_000_000_000_000_000_000_000_000_000d, 1) + "C";
        }
        return Double.toString(totalPriority);
    }

    @Override
    public boolean doesPauseGame() {
        return true;
    }
}
