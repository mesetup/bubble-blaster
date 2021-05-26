package com.qtech.bubbles.common.gametype;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.Listener;
import com.qtech.bubbles.event.bus.EventBus;
import com.qtech.bubbles.gametype.hud.ClassicHUD;

/**
 * <h1>HUD Baseclass</h1>
 * The baseclass for all HUD's.
 *
 * @see ClassicHUD
 * @see AbstractGameType
 */
public abstract class HUD extends Listener {
    private final AbstractGameType gameType;
    private EventBus.Handler binding;
//    private int tickEventCode;
//    private int renderEventCode;

    public HUD(AbstractGameType gameType) {
        this.gameType = gameType;
        this.bindEvents();
    }

    @SuppressWarnings("EmptyMethod")
    public void tick() {

    }

    @Override
    public void bindEvents() {
//        this.tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.HIGHER);
//        this.renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.HIGHER);

        BubbleBlaster.getEventBus().register(this);
        eventsActive = true;
    }

    @Override
    public void unbindEvents() {
//        QUpdateEvent.removeListener(this.tickEventCode);
//        QRenderEvent.removeListener(this.renderEventCode);

        BubbleBlaster.getEventBus().unregister(this);
        eventsActive = false;
    }

    @Override
    public boolean areEventsBound() {
        return eventsActive;
    }

    public AbstractGameType getGameType() {
        return gameType;
    }
}
