package com.qtech.bubbleblaster.screen;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.LoadedGame;
import com.qtech.bubbleblaster.bubble.AbstractBubble;
import com.qtech.bubbleblaster.common.GraphicsProcessor;
import com.qtech.bubbleblaster.common.bubble.BubbleSystem;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.common.text.translation.I18n;
import com.qtech.bubbleblaster.core.utils.categories.GraphicsUtils;
import com.qtech.bubbleblaster.environment.EnvironmentRenderer;
import com.qtech.bubbleblaster.event.KeyboardEvent;
import com.qtech.bubbleblaster.event.LanguageChangeEvent;
import com.qtech.bubbleblaster.event.SubscribeEvent;
import com.qtech.bubbleblaster.event.bus.EventBus;
import com.qtech.bubbleblaster.event.type.KeyEventType;
import com.qtech.bubbleblaster.gui.PauseButton;
import com.qtech.bubbleblaster.media.AudioSlot;
import com.qtech.bubbleblaster.registry.Registry;
import com.qtech.bubbleblaster.util.Util;
import com.qtech.bubbleblaster.util.helpers.MathHelper;

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

    //    private final Font bubbleTitleFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 32);
//    private final Font bubbleValueFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD + Font.ITALIC, 16);
//    private final Font bubbleInfoFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 16);
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
                AudioSlot focusChangeSFX = new AudioSlot(getClass().getResource("/assets/qbubbles/audio/sfx/ui/button/focus_change.wav"), "focusChange");
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
    public synchronized void onKeyboard(KeyboardEvent evt) {
        if (evt.getType() == KeyEventType.PRESS) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (!BubbleBlaster.getInstance().isGameLoaded()) {
                    return;
                }

                BubbleBlaster.getInstance().displayScene(null);
            }
        }
    }

    public void render(BubbleBlaster game, GraphicsProcessor gg) {
        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return;
        }

        GraphicsProcessor ngg = new GraphicsProcessor(gg);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Darkened background     //
        /////////////////////////////////
        ngg.setColor(new Color(0, 0, 0, 192));
        ngg.fillRect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
        Font oldFont = ngg.getFont();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Pause text     //
        ////////////////////////
        ngg.setColor(new Color(255, 255, 255, 128));
        ngg.setFont(pauseFont);
        GraphicsUtils.drawCenteredString(ngg, I18n.translateToLocal("screen.qbubbles.pause.text"), new Rectangle2D.Double(0, 90, BubbleBlaster.getInstance().getWidth(), ngg.getFontMetrics(pauseFont).getHeight()), pauseFont);

        ngg.setFont(oldFont);

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
        ngg.setColor(new Color(255, 255, 255, 128));
        ngg.drawRect((int) (BubbleBlaster.getMiddleX() - 480), 300, 960, 300);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Bubble     //
        ////////////////////

        // Bubble name.
        ngg.setColor(new Color(255, 255, 255, 192));
        ngg.setFont(bubbleTitleFont);
        ngg.setFallbackFont(fallbackTitleFont);
        ngg.drawString(I18n.translateToLocal("bubble." + bubble.getRegistryName().getNamespace() + "." + bubble.getRegistryName().getPath().replaceAll("/", ".") + ".name"), (int) BubbleBlaster.getMiddleX() - 470, 332);

        // Bubble icon.
        EnvironmentRenderer.drawBubble(ngg, (int) (BubbleBlaster.getMiddleX() - 470), 350, 122, bubble.colors);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Info Names     //
        ////////////////////////

        // Set color & font.
        ngg.setFont(bubbleInfoFont);
        ngg.setFallbackFont(fallbackInfoFont);
        ngg.setColor(new Color(255, 255, 255, 192));

        // Left data.
        ngg.drawString(minRadius, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 362);
        ngg.drawString(maxRadius, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 382);
        ngg.drawString(minSpeed, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 402);
        ngg.drawString(maxSpeed, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 422);
        ngg.drawString(defChance, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 442);
        ngg.drawString(curChance, (int) (BubbleBlaster.getMiddleX() - 326) + 10, 462);

        // Right data.
        ngg.drawString(defTotPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 322);
        ngg.drawString(curTotPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 342);
        ngg.drawString(defPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 362);
        ngg.drawString(curPriority, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 382);
        ngg.drawString(scoreMod, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 402);
        ngg.drawString(attackMod, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 422);
        ngg.drawString(defenseMod, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 442);
        ngg.drawString(canSpawn, (int) (BubbleBlaster.getMiddleX() + 72) + 10, 462);

        // Description
        ngg.drawString(description, (int) BubbleBlaster.getMiddleX() - 470, 502);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Values     //
        ////////////////////

        // Set color & font.
        ngg.setFont(bubbleValueFont);
        ngg.setFallbackFont(fallbackValueFont);
        ngg.setColor(new Color(255, 255, 255, 128));

        // Left data.
        ngg.drawString(Integer.toString(bubble.getMinRadius()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 362);
        ngg.drawString(Integer.toString(bubble.getMaxRadius()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 382);
        ngg.drawString(Double.toString(bubble.getMinSpeed()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 402);
        ngg.drawString(Double.toString(bubble.getMaxSpeed()), (int) (BubbleBlaster.getMiddleX() - 326) + 200, 422);
        ngg.drawString(MathHelper.round((double) 100 * BubbleSystem.getDefaultPercentageChance(bubble), 5) + "%", (int) (BubbleBlaster.getMiddleX() - 326) + 200, 442);
        ngg.drawString(MathHelper.round((double) 100 * BubbleSystem.getPercentageChance(bubble), 5) + "%", (int) (BubbleBlaster.getMiddleX() - 326) + 200, 462);
        ngg.drawString(BigDecimal.valueOf(BubbleSystem.getDefaultTotalPriority()).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 322);
        ngg.drawString(BigDecimal.valueOf(BubbleSystem.getTotalPriority()).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 342);
        ngg.drawString(BigDecimal.valueOf(BubbleSystem.getDefaultPriority(bubble)).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 362);
        ngg.drawString(BigDecimal.valueOf(BubbleSystem.getPriority(bubble)).toBigInteger().toString(), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 382);

        // Right data
        if (bubble.isScoreRandom()) {
            ngg.drawString(I18n.translateToLocal("other.random"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 402);
        } else {
            ngg.drawString(Double.toString(bubble.getScore()), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 402);
        }
        if (bubble.isAttackRandom()) {
            ngg.drawString(I18n.translateToLocal("other.random"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 422);
        } else {
            ngg.drawString(Double.toString(bubble.getAttack()), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 422);
        }
        if (bubble.isDefenseRandom()) {
            ngg.drawString(I18n.translateToLocal("other.random"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 442);
        } else {
            ngg.drawString(Double.toString(bubble.getDefense()), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 442);
        }
        ngg.drawString(bubble.canSpawn(loadedGame.getGameType()) ? I18n.translateToLocal("other.true") : I18n.translateToLocal("other.false"), (int) (BubbleBlaster.getMiddleX() + 72) + 200, 462);

        // Description
        ngg.drawWrappedString(I18n.translateToLocal("bubble." + bubble.getRegistryName().getNamespace() + "." + bubble.getRegistryName().getPath().replaceAll("/", ".") + ".description").replaceAll("\\\\n", "\n"), (int) BubbleBlaster.getMiddleX() - 470, 522, 940);

        ngg.setFont(oldFont);
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
