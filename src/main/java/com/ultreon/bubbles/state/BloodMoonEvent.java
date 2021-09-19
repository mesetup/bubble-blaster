package com.ultreon.bubbles.state;

import com.jhlabs.image.NoiseFilter;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.bubbles.common.gamestate.GameEvent;
import com.ultreon.commons.util.ColorUtils;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.hydro.event.FilterEvent;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.commons.time.Date;
import com.ultreon.commons.time.DateTime;
import com.ultreon.commons.time.Time;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class BloodMoonEvent extends GameEvent {
    private final Date date = new Date(31, 10, 0);
    private final Time timeLo = new Time(3, 0, 0);
    private final Time timeHi = new Time(3, 59, 59);

    private boolean wasActive = false;
    private boolean wasPlayerActive = false;

    private final Map<PlayerEntity, Double> playerDefenses = new ConcurrentHashMap<>();
    private boolean activating;
    private boolean deactivating;
    private long stopTime;

    public BloodMoonEvent() {
        super();

        setBackgroundColor(ColorUtils.hex2Rgb("#af0000"));
    }

    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();

        if (loadedGame == null) {
            return;
        }

        if (stopTime < System.currentTimeMillis()) {
            loadedGame.getGameType().stopBloodMoon();
        }

        if (activating) {
            activating = false;
            deactivating = false;

            stopTime = System.currentTimeMillis() + 60000;

            // Game effects.
            if (!wasActive) BubbleBlaster.getLogger().info("Blood Moon activated!");

            loadedGame.getGameType().triggerBloodMoon();
            loadedGame.getGameType().setStateDifficultyModifier(this, 16f);
            wasActive = true;

            // Player effects.
            if (!wasPlayerActive && loadedGame.getGameType().getPlayer() != null) {
                BubbleBlaster.getLogger().info("Blood Moon for player activated!");
                // Todo: implement this.
//                playerDefenses.put(GameScene.getGameType().getPlayer(), GameScene.getGameType().getPlayer().getDefenseModifier());
                wasPlayerActive = true;
            }
        } else if (deactivating) {
            deactivating = false;
            // Game effects.
            if (wasActive) {
                BubbleBlaster.getLogger().info("Blood Moon deactivated!");
                loadedGame.getGameType().removeStateDifficultyModifier(this);
                wasActive = false;
            }
        }
    }

    @SubscribeEvent
    public void onFilter(FilterEvent evt) {
        if (!isActive(DateTime.current())) return;

        NoiseFilter filter = new NoiseFilter();

        filter.setMonochrome(true);
        filter.setDensity(0.25f);
        filter.setAmount(60);
        filter.setDistribution(1);

        evt.addFilter(filter);
    }

    @Override
    public final boolean isActive(DateTime dateTime) {
        super.isActive(dateTime);

        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return false;
        }

        return loadedGame.getGameType().isBloodMoonActive();
    }

    public final boolean wouldActive(DateTime dateTime) {
        boolean flag1 = dateTime.getTime().isBetween(timeLo, timeHi);  // Devil's hour.
        boolean flag2 = dateTime.getDate().equalsIgnoreYear(date);  // Halloween.

        boolean flag3 = dateTime.getDate().getDayOfWeek() == DayOfWeek.FRIDAY;  // Friday
        boolean flag4 = dateTime.getDate().getDay() == 13;  // 13th

        return (flag1 && flag2) || (flag3 && flag4);  // Every October 31st in devil's hour. Or Friday 13th.
    }

    public void deactivate() {
        this.deactivating = true;
    }

    public void activate() {
        this.activating = true;
    }

    public Map<PlayerEntity, Double> getPlayerDefenses() {
        return playerDefenses;
    }
}
