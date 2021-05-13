package com.qsoftware.bubbles.entity.player;

import com.google.common.annotations.Beta;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.ability.Ability;
import com.qsoftware.bubbles.common.ability.AbilityContainer;
import com.qsoftware.bubbles.common.ability.AbilityType;
import com.qsoftware.bubbles.common.ammo.AmmoType;
import com.qsoftware.bubbles.common.entity.*;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.entity.GiantBubbleEntity;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.event.CollisionEvent;
import com.qsoftware.bubbles.event.KeyboardEvent;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.XInputEvent;
import com.qsoftware.bubbles.event.type.KeyEventType;
import com.qsoftware.bubbles.init.AmmoInit;
import com.qsoftware.bubbles.init.EntityInit;
import com.qsoftware.bubbles.item.inventory.PlayerInventory;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.scene.SaveLoadingScene;
import com.qsoftware.bubbles.util.Util;
import com.qsoftware.bubbles.util.helpers.MathHelper;
import org.apache.batik.ext.awt.geom.Polygon2D;
import org.apache.commons.lang3.SystemUtils;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.util.Objects;

@SuppressWarnings({"RedundantSuppression", "unused"})
public final class PlayerEntity extends LivingEntity {

    // These are the vertex coordinates:
    //
    //          |- Middle point
    //          |
    // 9876543210123456789
    // --------------------,
    // ....#*********#.....| -8
    // ....*.........*.....| -7
    // ....#***#.#***#.....| -6
    // ........*.*.........| -5
    // ..#*****#.#******#..| -4
    // .*..................| -3
    // #..................#| -2
    // *..................*| -1
    // *..................*| 0  // Middle point.
    // *..................*| 1
    // #..................#| 2
    // .*................*.| 3
    // ..#..............#..| 4

    protected Area shipShape;
    protected Area arrowShape;

    // Types
    private AmmoType currentAmmo = AmmoInit.BASIC.get();

    // Motion (Arrow Keys).
    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;

    // Speed.
    private final double rotationSpeed = 128f;

    // Delta velocity.
    private double velDelta;

    // Motion (XInput).
    private float xInputX;
    private float xInputY;

    // Rotation
    protected double rotation = 0;

    // Normal values/
    private double score = 0d;
    private int level = 1;

    // Modifiers.
    private long abilityEnergy;

    // Ability
    private final AbilityContainer abilityContainer = new AbilityContainer(this);

    private double accelerateX = 0.0d;
    private double accelerateY = 0.0d;
    private final PlayerInventory inventory = new PlayerInventory(this);

    /**
     * <h1>Player entity.</h1>
     * The player is controlled be the keyboard and is one of the important features of the game. (Almost any game).
     *
     * @see LivingEntity
     */
    public PlayerEntity(Scene scene, AbstractGameType gameType) {
        super(EntityInit.PLAYER.get(), scene, gameType);

        this.addCollidable(EntityInit.BUBBLE.get());

        // Ship shape.
        Ellipse2D shipShape1 = new Ellipse2D.Double(-20, -20, 40, 40);
        Ellipse2D shipShape2 = new Ellipse2D.Double(-18, -18, 36, 36);
        Ellipse2D shipShape3 = new Ellipse2D.Double(-16, -16, 32, 32);
        Ellipse2D shipShape4 = new Ellipse2D.Double(-14, -14, 28, 28);
        this.shipShape = new Area(shipShape1);

        // Arrow shape.
        Polygon2D arrowShape1 = new Polygon2D(new int[]{-5, -10, 15, -10}, new int[]{0, -10, 0, 10}, 4);
        this.arrowShape = new Area(arrowShape1);

        // Velocity.
        this.velX = 0;
        this.velY = 0;

        // Set attributes.
        this.attributes.set(Attribute.DEFENSE, 0.1f);
        this.attributes.set(Attribute.ATTACK, 0.75f);
        this.attributes.set(Attribute.MAX_HEALTH, 100f);
        this.attributes.set(Attribute.SPEED, 0.325f);

        // Health
        this.speed = 128.325f;
        this.health = 100f;
    }

    /**
     * @param s the message.
     */
    public void sendMessage(String s) {
        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (currentScene instanceof GameScene) {
            GameScene gameScene = (GameScene) currentScene;
            gameScene.triggerMessage(s);
        }
    }

    /**
     * <h1>Prepare spawn</h1>
     * Called when the entity was spawned.
     *
     * @param spawnData the data to spawn with.
     */
    @Override
    public void prepareSpawn(EntitySpawnData spawnData) {
        super.prepareSpawn(spawnData);
        Scene currentScene = Objects.requireNonNull(Util.getSceneManager()).getCurrentScene();
        if (currentScene instanceof GameScene || currentScene instanceof SaveLoadingScene) {
            Rectangle2D gameBounds = gameType.getGameBounds();
            this.x = (float) MathHelper.clamp(x, gameBounds.getMinX(), gameBounds.getMaxX());
            this.y = (float) MathHelper.clamp(y, gameBounds.getMinY(), gameBounds.getMaxY());
            bindEvents();
        }
    }

    @Override
    protected void bindEvents() {
        QBubbles.getEventBus().register(this);
        this.areEventsBinded = true;
    }

    @Override
    protected void unbindEvents() {
        QBubbles.getEventBus().unregister(this);
        this.areEventsBinded = false;
    }

    @Override
    protected boolean areEventsBound() {
        return this.areEventsBinded;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Shape     //
    ///////////////////
    @Override
    public Area getShape() {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation), shipShape.getBounds().getCenterX(), shipShape.getBounds().getCenterY());

        Area area = new Area(shipShape);
        area.transform(transform);

        transform = new AffineTransform();
        transform.scale(scale, scale);
        transform.translate(x, y);

        area.transform(transform);

        return area;
    }

    /**
     * @return the x acceleration.
     */
    public double getAccelerateX() {
        return accelerateX;
    }

    /**
     * @return the y acceleration.
     */
    public double getAccelerateY() {
        return accelerateY;
    }

    /**
     * @param accelerateX the x acceleration to set.
     */
    public void setAccelerateX(double accelerateX) {
        this.accelerateX = accelerateX;
    }

    /**
     * @param accelerateY the y acceleration to set.
     */
    public void setAccelerateY(double accelerateY) {
        this.accelerateY = accelerateY;
    }

    /**
     * @return the shape of the ship.
     */
    public Area getShipShape() {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation), shipShape.getBounds().getCenterX(), shipShape.getBounds().getCenterY());

        Area area = new Area(shipShape);
        area.transform(transform);

        transform = new AffineTransform();
        transform.scale(scale, scale);
        transform.translate(x, y);

        area.transform(transform);

        return area;
    }

    /**
     * @return the arrow shape of the ship.
     */
    public Area getArrowShape() {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation), shipShape.getBounds().getCenterX(), shipShape.getBounds().getCenterY());

        Area area = new Area(arrowShape);
        area.transform(transform);

        transform = new AffineTransform();
        transform.scale(scale, scale);
        transform.translate(x, y);

        area.transform(transform);

        return area;
    }

    /**
     * @return the center position.
     */
    @SuppressWarnings("unused")
    public Point getCenter() {
        return new Point((int) getBounds().getCenterX(), (int) getBounds().getCenterY());
    }

    /**
     * Tick the player.
     *
     * @param environment the game-type where the entity is from.
     */
    @Override
    @SubscribeEvent
    public void tick(Environment environment) {
        super.tick(environment);

        if (!isSpawned()) return;

        this.abilityContainer.onEntityTick();
        this.inventory.tick();

        this.accelerateX = this.accelerateX / 1.05;
        this.accelerateY = this.accelerateY / 1.05;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Player motion. //
        ////////////////////

        double tempVelMotion = 0.0f;
        double tempVelRotation = 0.0f;

        // Check each direction, to create velocity
        if (this.forward) tempVelMotion += this.speed;
        if (this.backward) tempVelMotion -= this.speed;
        if (this.left) tempVelRotation -= this.rotationSpeed;
        if (this.right) tempVelRotation += this.rotationSpeed;
        if (this.xInputY != 0) tempVelMotion = this.xInputY * this.speed;
        if (this.xInputX != 0) tempVelRotation = this.xInputX * this.rotationSpeed;

        // Update rotation
        double delta = 0.05;
        // Update X, and Y.
        if (isMotionEnabled()) {
            this.rotation += tempVelRotation * delta;
//                rotation = MathHelper.clamp(rotation, -80f, 80f);
        }

        // Calculate Velocity X and Y.
        double angelRadians = Math.toRadians(this.rotation);
        double tempVelX = Math.cos(angelRadians) * tempVelMotion;
        double tempVelY = Math.sin(angelRadians) * tempVelMotion;

        if (isMotionEnabled()) {
            this.accelerateX += Math.cos(Math.toRadians(this.rotation)) * delta * tempVelMotion;
            this.accelerateY += Math.sin(Math.toRadians(this.rotation)) * delta * tempVelMotion;
        }

//            // Set Velocity.
//            velX = tempVelX;
//            velY = tempVelY;

        // Update X, and Y.
        this.x += ((this.accelerateX + this.velX) * 0.05);
        this.y += ((this.accelerateY + this.velY) * 0.05);

        if (this.velX > 0) {
            if (this.velX + this.velDelta < 0) {
                this.velX = 0;
            } else {
                this.velX += this.velDelta;
            }
        } else if (this.velX < 0) {
            if (this.velX + this.velDelta > 0) {
                this.velX = 0;
            } else {
                this.velX -= this.velDelta;
            }
        }

        if (this.velY > 0) {
            if (this.velY + velDelta < 0) {
                this.velY = 0;
            } else {
                this.velY += this.velDelta;
            }
        } else if (this.velX < 0) {
            if (this.velY + this.velDelta > 0) {
                this.velY = 0;
            } else {
                this.velY -= this.velDelta;
            }
        }

        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (!(currentScene instanceof GameScene)) return;
        GameScene gameScene = (GameScene) currentScene;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Collision detection. //
        //////////////////////////

        Rectangle2D gameBounds = gameScene.getGameType().getGameBounds();

        // Game border.
        this.x = (float) MathHelper.clamp(this.x, gameBounds.getX() + this.getBounds().getWidth(), gameBounds.getMaxX() - this.getBounds().getWidth());
        this.y = (float) MathHelper.clamp(this.y, gameBounds.getY() + this.getBounds().getHeight(), gameBounds.getY() + gameBounds.getHeight());
    }

    @Override
    public void attack(double value, DamageSource source) {
        float defense = attributes.get(Attribute.DEFENSE);
        if (defense <= 0.0d) {
            this.instantDeath();
            return;
        }

        // Deal damage to the player.
        this.health -= value / defense;

        // Check health.
        this.checkHealth();

        // Check if source has attack modifier.
        if (value > 0.0d) {

            // Check if window is not focused.
            if (!QBubbles.getInstance().getFrame().isFocused()) {
                if (SystemUtils.IS_JAVA_9) {
                    // Let the taskbar icon flash. (Java 9+)
                    @SuppressWarnings("Since15") Taskbar taskbar = Taskbar.getTaskbar();
                    try {
                        //noinspection Since15
                        taskbar.requestWindowUserAttention(QBubbles.getInstance().getFrame());
                    } catch (UnsupportedOperationException ignored) {

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onCollision(CollisionEvent evt) {
        if (evt.getSource() != this) return;

        Entity targetEntity = evt.getTarget();
        if (targetEntity instanceof GiantBubbleEntity) {

            // Entity bubble
            GiantBubbleEntity bubble = (GiantBubbleEntity) targetEntity;

            // Modifiers
            double scoreMod = bubble.getAttributeMap().get(Attribute.SCORE_MULTIPLIER);
            double attackMod = bubble.getAttributeMap().get(Attribute.ATTACK);
            double defenseMod = bubble.getAttributeMap().get(Attribute.DEFENSE);

            // Attributes
            double radius = bubble.getRadius();
            double speed = bubble.getSpeed();

            // Calculate score value.
            double visibleValue = radius * speed;
            double nonVisibleValue = attackMod * defenseMod;
            double scoreValue = ((visibleValue * (nonVisibleValue + 1)) * scoreMod * attributes.get(Attribute.SCORE_MULTIPLIER)) * evt.getDeltaTime() / 8;

            // Add score.
            addScore(scoreValue);
        } else if (targetEntity instanceof BubbleEntity) {
            // Entity bubble
            BubbleEntity bubble = (BubbleEntity) targetEntity;

            // Modifiers
            AttributeMap attributeMap = bubble.getAttributeMap();
            double scoreMultiplier = attributeMap.get(Attribute.SCORE_MULTIPLIER);
            double attack = attributeMap.get(Attribute.ATTACK);  // Maybe used.
            double defense = attributeMap.get(Attribute.DEFENSE);  // Maybe used.

            // Attributes
            double radius = bubble.getRadius();
            double speed = bubble.getSpeed();

            // Calculate score value.
            double visibleValue = radius * speed;
            double nonVisibleValue = attack * defense;
            double scoreValue = ((visibleValue * (nonVisibleValue + 1)) * scoreMultiplier * scoreMultiplier) * evt.getDeltaTime() / 8;

            // Add score.
            addScore(scoreValue);
        }
    }

    @SubscribeEvent
    public void onKeyboardEvent(KeyboardEvent evt) {
        if (evt != null) {
            KeyEvent e = evt.getParentEvent();

            if (evt.getType() == KeyEventType.PRESS) {
                if (e.getKeyCode() == KeyEvent.VK_UP) forward = true;
                if (e.getKeyCode() == KeyEvent.VK_DOWN) backward = true;
                if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
            }

            if (evt.getType() == KeyEventType.RELEASE) {
                if (e.getKeyCode() == KeyEvent.VK_UP) forward = false;
                if (e.getKeyCode() == KeyEvent.VK_DOWN) backward = false;
                if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
            }
        }
    }

    @SubscribeEvent
    public void onXInputEvent(XInputEvent evt) {
        System.out.println(evt);

        if (evt != null) {
            if (forward || backward || left || right) return;

            this.xInputX = evt.getRightStickX();
            this.xInputY = evt.getLeftStickY();
        }
    }

    @Override
    public synchronized void renderEntity(Graphics2D gg) {
        if (!isSpawned()) return;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fill the ship with the correct color. //
        ///////////////////////////////////////////
        gg.setColor(Color.red);
        gg.fill(getShipShape());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fill the arrow with the correct color. //
        ////////////////////////////////////////////
        gg.setColor(Color.white);
        gg.fill(getArrowShape());
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //     Rotate Delta     //
    //////////////////////////
    @SuppressWarnings("unused")
    public void rotateDelta(int deltaRotation) {
        this.rotation += deltaRotation;
    }

    /**
     * <h1>Trigger a Reflection</h1>
     * Triggers a reflection, there are some problems with the velocity.
     * That's why it's currently in beta.
     *
     * @param velocity the amount velocity for bounce.
     * @param delta    the delta change.
     */
    @Beta
    public void triggerReflect(Point2D velocity, double delta) {
        this.velDelta = delta;
        this.velX = velocity.getX();
        this.velY = velocity.getY();
    }

    /**
     * <h1>Delete method</h1>
     * <i><b>WARNING: </b><u>This is an unsafe method, use {@link AbstractGameType#triggerGameOver()} instead.</u></i>
     *
     * @see AbstractGameType#triggerGameOver()
     */
    @Override
    public void delete() {
        this.unbindEvents();
    }

    /**
     * <h1>Checks health</h1>
     * Checks health, if health is zero or less, it will trigger game-over.
     */
    @Override
    public void checkHealth() {
        if (health <= 0)
            if (!getGameType().isGameOver())
                getGameType().triggerGameOver();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Entity Data     //
    /////////////////////////
    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument document = super.getState();

        document.put("score", new BsonDouble(score));
        document.put("rotation", new BsonDouble(rotation));
        return document;
    }

    @Override
    public void setState(BsonDocument document) {
        super.getState();

        this.score = document.getDouble("score").getValue();
        this.rotation = document.getDouble("rotation").getValue();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Speed     //
    ///////////////////
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Rotation     //
    //////////////////////
    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Values     //
    ////////////////////

    // Score
    public double getScore() {
        return score;
    }

    public void addScore(double value) {
        score += value;
    }

    public void subtractScore(long value) {
        score -= value;
    }

    public void setScore(double value) {
        score = value;
    }

    // Level
    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
    }

    public void levelDown() {
        setLevel(getLevel() - 1);
    }

    public void setLevel(int level) {
        this.level = Math.max(level, 1);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Modifiers     //
    ///////////////////////

    /**
     * Subtract ability value.
     *
     * @param amount amount to subtract.
     * @deprecated Use {@link Ability#subtractValue(int)} instead. To get the ability use {@link AbilityContainer#get(AbilityType)} with {@link #getAbilityContainer()}
     */
    @Deprecated
    public void subtractAbilityEnergy(int amount) {
        setAbilityEnergy(getAbilityEnergy() - amount);
    }

    /**
     * Add ability value.
     *
     * @param amount amount to add.
     * @deprecated Use {@link Ability#addValue(int)} instead. To get the ability use {@link AbilityContainer#get(AbilityType)} with {@link #getAbilityContainer()}
     */
    @Deprecated
    public void addAbilityEnergy(int amount) {
        setAbilityEnergy(getAbilityEnergy() + amount);
    }


    /**
     * Get the ability value (most likely energy).
     *
     * @return the ability value.
     * @deprecated Use {@link Ability#getValue()} instead. To get the ability use {@link AbilityContainer#get(AbilityType)} with {@link #getAbilityContainer()}
     */
    @Deprecated
    public int getAbilityEnergy() {
        return this.abilityContainer.getCurrent().getValue();
    }

    /**
     * Set the ability value (most likely energy).
     *
     * @param amount the ability value.
     * @deprecated Use {@link Ability#setValue(int)} instead. To get the ability use {@link AbilityContainer#get(AbilityType)} with {@link #getAbilityContainer()}
     */
    @Deprecated
    public void setAbilityEnergy(int amount) {
        this.abilityContainer.getCurrent().setValue(amount);
    }

    /**
     * Set the ability use cooldown.
     * Cooldown is in ticks.
     *
     * @param type  the ability type.
     * @param ticks the ticks of cooldown.
     * @see AbilityType
     * @deprecated Use {@link Ability#setCooldown(int)} instead. To get the ability use {@link AbilityContainer#get(AbilityType)} with {@link #getAbilityContainer()}
     */
    @Deprecated
    public void setAbilityCooldown(AbilityType<Ability<?>> type, int ticks) {
        this.abilityContainer.get(type).setCooldown(ticks);
    }

    /**
     * Get the ability container of the entity.
     *
     * @return the requested {@link AbilityContainer}.
     * @see AbilityContainer
     */
    public AbilityContainer getAbilityContainer() {
        return abilityContainer;
    }

    /**
     * Get current ammo type.
     *
     * @return the current {@link AmmoType ammo type}.
     * @see AmmoType
     * @see #setCurrentAmmo(AmmoType)
     */
    public AmmoType getCurrentAmmo() {
        return currentAmmo;
    }

    /**
     * Set current ammo type.
     *
     * @param currentAmmo the ammo to set.
     * @see AmmoType
     * @see #getCurrentAmmo()
     */
    public void setCurrentAmmo(AmmoType currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    /**
     * Set current ship scale.
     *
     * @param scale the scale to set.
     * @see #getScale()
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    void forward(boolean bool) {
        this.forward = bool;
    }

    void backward(boolean bool) {
        this.backward = bool;
    }

    void left(boolean bool) {
        this.left = bool;
    }

    void right(boolean bool) {
        this.right = bool;
    }
}
