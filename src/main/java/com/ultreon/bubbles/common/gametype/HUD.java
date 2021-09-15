package com.ultreon.bubbles.common.gametype;

import com.ultreon.bubbles.gametype.hud.ClassicHUD;

import com.ultreon.hydro.render.Renderer;

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

    public void render(Renderer graphics2D) {

    }

    public AbstractGameType getGameType() {
        return gameType;
    }
}
