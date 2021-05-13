package com.qsoftware.bubbles.common;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.entity.types.EntityType;

import java.awt.*;

/**
 * <h1>Game Object base class</h1>
 * Base class for all types of game objects.
 *
 * @author Quinten Jungblut
 * @see Entity
 */
@Deprecated
public abstract class GameObject extends Listener {
    private final Scene scene;
    protected double x;
    protected double y;
    protected double velocityX, velocityY;
    protected boolean areEventsBinded;
    private boolean spawned;
    protected final AbstractGameType gameType;

    /**
     * Game Object constructor.
     * Base class for the entity.
     *
     * @param gameType the game type.
     * @author Quinten Jungblut
     * @see EntityType
     */
    public GameObject(Scene scene, AbstractGameType gameType) {
        this.gameType = gameType;
        this.spawned = false;
        this.scene = scene;
    }

    public final void spawn(Point pos) {
        // Game object subclass specific spawn handling.
        this.onSpawn(pos);

        // Add game-object to game-type.
        this.gameType.getGameObjects().add(this);

        // Set spawned.
        this.spawned = true;
    }

    public abstract void onSpawn(Point pos);

    public final boolean isSpawned() {
        return this.spawned;
    }

    public abstract void renderEntity(QBubbles game, Graphics2D gg);

    public abstract Rectangle getBounds();

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVelocityX(double velX) {
        this.velocityX = velX;
    }

    public void setVelocityY(double velY) {
        this.velocityY = velY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return this.velocityX;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    public abstract void delete();

    public Scene getScene() {
        return this.scene;
    }

    public AbstractGameType getGameType() {
        return this.gameType;
    }

    public void prepareSpawn(double x, double y) {
        // Assign x and y coordinates from position.
        this.x = x;
        this.y = y;
    }
}
