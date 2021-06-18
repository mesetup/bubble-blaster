package com.qtech.bubbles.common.gametype

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.Listener
import com.qtech.bubbles.event.bus.EventBus

/**
 * <h1>HUD Baseclass</h1>
 * The baseclass for all HUD's.
 *
 * @see ClassicHUD
 *
 * @see AbstractGameMode
 */
abstract class HUD(val gameMode: AbstractGameMode) : Listener() {
    private val binding: EventBus.Handler? = null
    open fun tick() {}
    public override fun bindEvents() {
//        this.tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.HIGHER);
//        this.renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.HIGHER);
        BubbleBlaster.eventBus.register(this)
        eventsActive = true
    }

    public override fun unbindEvents() {
//        QUpdateEvent.removeListener(this.tickEventCode);
//        QRenderEvent.removeListener(this.renderEventCode);
        BubbleBlaster.eventBus.unregister(this)
        eventsActive = false
    }

    public override fun areEventsBound(): Boolean {
        return eventsActive
    }

    //    private int tickEventCode;
    //    private int renderEventCode;
    init {
        bindEvents()
    }
}