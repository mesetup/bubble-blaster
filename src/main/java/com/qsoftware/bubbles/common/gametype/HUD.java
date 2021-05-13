package com.qsoftware.bubbles.common.gametype;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.Listener;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.gametype.hud.ClassicHUD;

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

    public void tick(AbstractGameType gameType) {

    }

    @Override
    public void bindEvents() {
//        this.tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.HIGHER);
//        this.renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.HIGHER);

        QBubbles.getEventBus().register(this);
        eventsActive = true;
    }

    @Override
    public void unbindEvents() {
//        QUpdateEvent.removeListener(this.tickEventCode);
//        QRenderEvent.removeListener(this.renderEventCode);

        QBubbles.getEventBus().unregister(this);
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
