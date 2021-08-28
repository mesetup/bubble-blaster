package com.qtech.bubbles.screen;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.text.translation.I18n;
import com.qtech.bubbles.event._common.RenderEventPriority;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.graphics.ValueAnimator;
import com.qtech.bubbles.gui.OptionsButton;
import com.qtech.bubbles.registry.LocaleManager;
import com.qtech.bubbles.settings.GameSettings;
import com.qtech.bubbles.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

@SuppressWarnings("FieldCanBeLocal")
public final class LanguageScreen extends Screen {
    private static LanguageScreen INSTANCE;
    private final LanguageLoader loader;
    private int languageIndex = 1;
    private final OptionsButton button1 = new OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build();
    private final OptionsButton button2 = new OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build();
    private final OptionsButton button3 = new OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build();
    private final OptionsButton button4 = new OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build();
    private final OptionsButton button5 = new OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build();
    private final OptionsButton button6 = new OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build();
    private final OptionsButton prev = new OptionsButton.Builder().bounds(0, 0, 64, 298).renderPriority(RenderEventPriority.GUI_SCREEN).text("Prev").build();
    private final OptionsButton next = new OptionsButton.Builder().bounds(0, 0, 64, 298).renderPriority(RenderEventPriority.GUI_SCREEN).text("Next").build();

    private final OptionsButton cancelButton = new OptionsButton.Builder().bounds(0, 0, 644, 48).renderPriority(RenderEventPriority.GUI_SCREEN).text("Cancel").build();
    private final TreeMap<String, Locale> nameLocaleMap = new TreeMap<>();
    private Screen backScene;
    private int deltaIndex;
    private ValueAnimator valueAnimator1;
    private ValueAnimator valueAnimator2;
    private int deltaPage;

    private class LanguageLoader {
        @SubscribeEvent
        public void onLoadComplete() {
            LanguageScreen.this.onPostInitialize();
        }
    }

    public LanguageScreen(Screen backScene) {
        LanguageScreen.INSTANCE = this;

        this.backScene = backScene;

        this.button1.setCommand(this::cmdButton1);
        this.button2.setCommand(this::cmdButton2);
        this.button3.setCommand(this::cmdButton3);
        this.button4.setCommand(this::cmdButton4);
        this.button5.setCommand(this::cmdButton5);
        this.button6.setCommand(this::cmdButton6);
        this.prev.setCommand(this::prevPage);
        this.next.setCommand(this::nextPage);
        this.cancelButton.setCommand(this::back);

        this.loader = new LanguageLoader();
        BubbleBlaster.getEventBus().register(loader);
    }

    private void back() {
        Objects.requireNonNull(Util.getSceneManager()).displayScreen(backScene);
    }

    private void onPostInitialize() {
        for (Locale locale : LocaleManager.getManager().keys()) {
            String name;
            if (!Objects.equals(locale.getDisplayCountry(), ""))
                name = locale.getDisplayLanguage() + " (" + locale.getDisplayCountry() + ")";
            else {
                name = locale.getDisplayLanguage();
            }

            nameLocaleMap.put(name, locale);
        }

        BubbleBlaster.getEventBus().unregister(loader);
    }

    private void cmdButton6() {
        setLanguage(languageIndex + 5);
    }

    private void cmdButton5() {
        setLanguage(languageIndex + 4);
    }

    private void cmdButton4() {
        setLanguage(languageIndex + 3);
    }

    private void cmdButton3() {
        setLanguage(languageIndex + 2);
    }

    private void cmdButton2() {
        setLanguage(languageIndex + 1);
    }

    private void cmdButton1() {
        setLanguage(languageIndex);
    }

    private void setLanguage(int languageIndex) {
        Locale locale = new ArrayList<>(nameLocaleMap.values()).get(languageIndex);
        GameSettings settings = GameSettings.instance();

        settings.setLanguage(locale);

        settings.save();

        back();
    }

    private void nextPage() {
        if (valueAnimator1 == null && valueAnimator2 == null) {
//            languageIndex += 6;

            deltaIndex = 6;
            deltaPage = 1;

            valueAnimator1 = new ValueAnimator(0, 512 * -deltaPage, 0.05);
            valueAnimator1.start();

            System.out.println("next");
        }
    }

    private void prevPage() {
        if (valueAnimator1 == null && valueAnimator2 == null) {
//            languageIndex -= 6;

            deltaIndex = -6;
            deltaPage = -1;

            valueAnimator1 = new ValueAnimator(0, 512 * -deltaPage, 0.05);
            valueAnimator1.start();

            System.out.println("prev");
        }
    }

    public static LanguageScreen instance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        BubbleBlaster.getEventBus().register(this);

        button1.bindEvents();
        button2.bindEvents();
        button3.bindEvents();
        button4.bindEvents();
        button5.bindEvents();
        button6.bindEvents();
        next.bindEvents();
        prev.bindEvents();

        cancelButton.bindEvents();
    }

    @Override
    public boolean onClose(Screen to) {
        BubbleBlaster.getEventBus().unregister(this);

        button1.unbindEvents();
        button2.unbindEvents();
        button3.unbindEvents();
        button4.unbindEvents();
        button5.unbindEvents();
        button6.unbindEvents();
        next.unbindEvents();
        prev.unbindEvents();

        cancelButton.unbindEvents();

        if (to == backScene) {
            backScene = null;
        }
        return super.onClose(to);
    }

    @Override
    public void render(BubbleBlaster game, Graphics2D gg) {
        prev.setX((int) BubbleBlaster.getMiddleX() - 322);
        prev.setY((int) BubbleBlaster.getMiddleY() - 149);
        button1.setVisualX(0/*(int) Game.getMiddleX() - 256*/);
        button2.setVisualX(0/*(int) Game.getMiddleX() - 256*/);
        button3.setVisualX(0/*(int) Game.getMiddleX() - 256*/);
        button4.setVisualX(0/*(int) Game.getMiddleX() - 256*/);
        button5.setVisualX(0/*(int) Game.getMiddleX() - 256*/);
        button6.setVisualX(0/*(int) Game.getMiddleX() - 256*/);
        button1.setVisualY(/*(int) Game.getMiddleY() + */0/* - 149*/);
        button2.setVisualY(/*(int) Game.getMiddleY() + */50/* - 99*/);
        button3.setVisualY(/*(int) Game.getMiddleY() + */100/* - 49*/);
        button4.setVisualY(/*(int) Game.getMiddleY() + */150/* + 1*/);
        button5.setVisualY(/*(int) Game.getMiddleY() + */200/* + 51*/);
        button6.setVisualY(/*(int) Game.getMiddleY() + */250/* + 101*/);
        button1.setX((int) BubbleBlaster.getMiddleX() - 256);
        button2.setX((int) BubbleBlaster.getMiddleX() - 256);
        button3.setX((int) BubbleBlaster.getMiddleX() - 256);
        button4.setX((int) BubbleBlaster.getMiddleX() - 256);
        button5.setX((int) BubbleBlaster.getMiddleX() - 256);
        button6.setX((int) BubbleBlaster.getMiddleX() - 256);
        button1.setY((int) BubbleBlaster.getMiddleY() - 149);
        button2.setY((int) BubbleBlaster.getMiddleY() - 99);
        button3.setY((int) BubbleBlaster.getMiddleY() - 49);
        button4.setY((int) BubbleBlaster.getMiddleY() + 1);
        button5.setY((int) BubbleBlaster.getMiddleY() + 51);
        button6.setY((int) BubbleBlaster.getMiddleY() + 101);
        next.setX((int) BubbleBlaster.getMiddleX() + 258);
        next.setY((int) BubbleBlaster.getMiddleY() - 149);

        cancelButton.setX((int) BubbleBlaster.getMiddleX() - 322);
        cancelButton.setY((int) BubbleBlaster.getMiddleY() + 151);

//        if (evt.getPriority() == RenderEventPriority.BACKGROUND) {
//        }

//        if (evt.getPriority() == RenderEventPriority.FOREGROUND) {
//        }

        renderBackground(gg);
    }

    @Override
    public void renderGUI(BubbleBlaster game, Graphics2D gg) {
        Locale loc1 = null;
        Locale loc2 = null;
        Locale loc3 = null;
        Locale loc4 = null;
        Locale loc5 = null;
        Locale loc6 = null;
        try {
            loc1 = new ArrayList<>(nameLocaleMap.values()).get(languageIndex);
            button1.setText(loc1.getDisplayLanguage(loc1) + " (" + loc1.getDisplayLanguage() + ")");
        } catch (IndexOutOfBoundsException ignored) {

        }
        try {
            loc2 = new ArrayList<>(nameLocaleMap.values()).get(languageIndex + 1);
            button2.setText(loc2.getDisplayLanguage(loc2) + " (" + loc2.getDisplayLanguage() + ")");
        } catch (IndexOutOfBoundsException ignored) {

        }
        try {
            loc3 = new ArrayList<>(nameLocaleMap.values()).get(languageIndex + 2);
            button3.setText(loc3.getDisplayLanguage(loc3) + " (" + loc3.getDisplayLanguage() + ")");
        } catch (IndexOutOfBoundsException ignored) {

        }

        try {
            loc4 = new ArrayList<>(nameLocaleMap.values()).get(languageIndex + 3);
            button4.setText(loc4.getDisplayLanguage(loc4) + " (" + loc4.getDisplayLanguage() + ")");
        } catch (IndexOutOfBoundsException ignored) {

        }
        try {
            loc5 = new ArrayList<>(nameLocaleMap.values()).get(languageIndex + 4);
            button5.setText(loc5.getDisplayLanguage(loc5) + " (" + loc5.getDisplayLanguage() + ")");
        } catch (IndexOutOfBoundsException ignored) {

        }
        try {
            loc6 = new ArrayList<>(nameLocaleMap.values()).get(languageIndex + 5);
            button6.setText(loc6.getDisplayLanguage(loc6) + " (" + loc6.getDisplayLanguage() + ")");
        } catch (IndexOutOfBoundsException ignored) {

        }

        next.setText(I18n.translateToLocal("other.next"));
        prev.setText(I18n.translateToLocal("other.prev"));

        cancelButton.setText(I18n.translateToLocal("other.cancel"));

        Graphics2D gg1 = (Graphics2D) gg.create((int) BubbleBlaster.getMiddleX() - 256, (int) BubbleBlaster.getMiddleY() - 149, 512, 300);
        Graphics2D gg2;
        if (valueAnimator1 != null) {
            if (valueAnimator1.isEnded()) {
                languageIndex += deltaIndex;
                valueAnimator1 = null;

                valueAnimator2 = new ValueAnimator(512 * deltaPage, 0, 0.05);
                valueAnimator2.start();

                int x = (int) valueAnimator2.animate();
                gg2 = (Graphics2D) gg1.create(x, 0, 512, 300);
            } else {
                int x = (int) valueAnimator1.animate();
                gg2 = (Graphics2D) gg1.create(x, 0, 512, 300);
            }
        } else {
            if (valueAnimator2 != null) {
                if (valueAnimator2.isEnded()) {
                    gg2 = (Graphics2D) gg1.create(0, 0, 512, 300);
                    valueAnimator2 = null;
                } else {
                    int x = (int) valueAnimator2.animate();
                    gg2 = (Graphics2D) gg1.create(x, 0, 512, 300);
                }
            } else {
                gg2 = (Graphics2D) gg1.create(0, 0, 512, 300);
            }
        }


        if (loc1 != null) button1.paint(gg2);
        if (loc2 != null) button2.paint(gg2);
        if (loc3 != null) button3.paint(gg2);
        if (loc4 != null) button4.paint(gg2);
        if (loc5 != null) button5.paint(gg2);
        if (loc6 != null) button6.paint(gg2);

        gg2.dispose();
        gg1.dispose();

        next.paint(gg);
        prev.paint(gg);

        cancelButton.paint(gg);
    }

    public void renderBackground(Graphics2D gg) {
        gg.setColor(new Color(96, 96, 96));
        gg.fillRect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());
    }
}
