package com.qsoftware.bubbles.state;

import com.jhlabs.image.NoiseFilter;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.gamestate.GameEvent;
import com.qsoftware.bubbles.core.utils.Utils;
import com.qsoftware.bubbles.core.utils.categories.ColorUtils;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.event.FilterEvent;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.TickEvent;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.utilities.datetime.Date;
import com.qsoftware.utilities.datetime.DateTime;
import com.qsoftware.utilities.datetime.Time;
import org.jetbrains.annotations.Nullable;

import java.time.DayOfWeek;
import java.util.HashMap;

@SuppressWarnings("unused")
public class BloodMoonEvent extends GameEvent {
    private final Date date = new Date(31, 10, 0);
    private final Time timeLo = new Time(3, 0, 0);
    private final Time timeHi = new Time(3, 59, 59);

    //    private final Date date = new Date(13, 8, 0);
//    private final Time timeLo = new Time(9, 0, 0);
//    private final Time timeHi = new Time(13, 10, 59);
    private boolean wasActive = false;
    private boolean wasPlayerActive = false;

    private final HashMap<PlayerEntity, Double> playerDefenses = new HashMap<>();
    private boolean activating;
    private boolean deactivating;
    private long stopTime;

    public BloodMoonEvent() {
        super();

        setBackgroundColor(ColorUtils.hex2Rgb("#af0000"));
    }

    @SubscribeEvent
    public synchronized void onUpdate(TickEvent evt) {
        @Nullable GameScene gameScene = Utils.getGameScene();
        if (gameScene == null) return;

//        if (wouldActive(DateTime.current()) && !activating) {
//            GameScene.getGameType().triggerBloodMoon();
//        }

        if (stopTime < System.currentTimeMillis()) {
            gameScene.getGameType().stopBloodMoon();
        }

        if (activating) {
            activating = false;
            deactivating = false;

            stopTime = System.currentTimeMillis() + 60000;

            // Game effects.
            if (!wasActive) QBubbles.getLogger().info("Blood Moon activated!");

            gameScene.getGameType().triggerBloodMoon();
            gameScene.getGameType().setStateDifficultyModifier(this, 16f);
            wasActive = true;

            // Player effects.
            if (!wasPlayerActive && gameScene.getGameType().getPlayer() != null) {
                QBubbles.getLogger().info("Blood Moon for player activated!");
                // Todo: implement this.
//                playerDefenses.put(GameScene.getGameType().getPlayer(), GameScene.getGameType().getPlayer().getDefenseModifier());
                wasPlayerActive = true;
            }
        } else if (deactivating) {
            deactivating = false;
            // Game effects.
            if (wasActive) {
                QBubbles.getLogger().info("Blood Moon deactivated!");
                gameScene.getGameType().removeStateDifficultyModifier(this);
                wasActive = false;
            }
        }
    }

    @SubscribeEvent
    public synchronized void onFilter(FilterEvent evt) {
        if (!isActive(DateTime.current())) return;

        NoiseFilter filter = new NoiseFilter();

        filter.setMonochrome(true);
        filter.setDensity(0.25f);
        filter.setAmount(60);
        filter.setDistribution(1);

        evt.addFilter(filter);
    }

    @Override
    public final synchronized boolean isActive(DateTime dateTime) {
        super.isActive(dateTime);

        @Nullable GameScene gameScene = Utils.getGameScene();
        if (gameScene == null) return false;

        return gameScene.getGameType().isBloodMoonActive();
    }

    public final synchronized boolean wouldActive(DateTime dateTime) {
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

    public HashMap<PlayerEntity, Double> getPlayerDefenses() {
        return playerDefenses;
    }
}
