package com.qtech.bubbles.common.gametype;

import com.qtech.bubbles.gametype.hud.ClassicHUD;

import java.awt.*;

/**
 * <h1>HUD Baseclass</h1>
 * The baseclass for all HUD's.
 *
 * @see ClassicHUD
 * @see AbstractGameType
 */
public abstract class HUD {
    private final AbstractGameType gameType;

    public HUD(AbstractGameType gameType) {
        this.gameType = gameType;
    }

    @SuppressWarnings("EmptyMethod")
    public void tick() {

    }

    public void render(Graphics2D graphics2D) {

    }

    public AbstractGameType getGameType() {
        return gameType;
    }
}
