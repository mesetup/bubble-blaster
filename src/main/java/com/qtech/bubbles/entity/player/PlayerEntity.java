package com.qtech.bubbles.entity.player;

import com.google.common.annotations.Beta;
import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.LoadedGame;
import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.*;
import com.qtech.bubbles.entity.ammo.AmmoType;
import com.qtech.bubbles.entity.attribute.Attribute;
import com.qtech.bubbles.entity.damage.DamageSource;
import com.qtech.bubbles.entity.player.ability.AbilityContainer;
import com.qtech.bubbles.environment.Environment;
import com.qtech.bubbles.event.CollisionEvent;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.event.input.KeyboardEvent;
import com.qtech.bubbles.event.input.XInputEvent;
import com.qtech.bubbles.event.type.KeyEventType;
import com.qtech.bubbles.init.AmmoTypes;
import com.qtech.bubbles.init.Entities;
import com.qtech.bubbles.item.inventory.PlayerInventory;
import com.qtech.bubbles.screen.EnvLoadScreen;
import com.qtech.bubbles.screen.SaveLoadingScreen;
import com.qtech.bubbles.screen.Screen;
import com.qtech.bubbles.util.Util;
import com.qtech.bubbles.util.helpers.MathHelper;
import org.apache.batik.ext.awt.geom.Polygon2D;
import org.apache.commons.lang3.SystemUtils;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.util.Objects;

import static com.qtech.bubbles.BubbleBlaster.TPS;

@SuppressWarnings({"RedundantSuppression", "unused"})
public final class PlayerEntity extends DamageableEntity {

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

    private final Area shipShape;
    private final Area arrowShape;

    // Types
    private AmmoType currentAmmo = AmmoTypes.BASIC.get();

    // Motion (Arrow Keys).
    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;

    // Speed.
    private final double rotationSpeed = 16f;

    // Delta velocity.
    private double velDelta;

    // Motion (XInput).
    private float xInputX;
    private float xInputY;

    // Rotation
    private double rotation = 0;

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
     * @see DamageableEntity
     */
    public PlayerEntity(AbstractGameType gameType) {
        super(Entities.PLAYER.get(), gameType);

        this.addCollidable(Entities.BUBBLE.get());

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
        this.attributes.setBase(Attribute.DEFENSE, 1f);
        this.attributes.setBase(Attribute.ATTACK, 0.75f);
        this.attributes.setBase(Attribute.MAX_DAMAGE, 100f);
        this.attributes.setBase(Attribute.SPEED, 16f);

        // Health
        this.speed = 16f;
        this.damageValue = 100f;
    }

    /**
     * @param s the message.
     */
    public void sendMessage(String s) {
        @Nullable Screen currentScene = Util.getSceneManager().getCurrentScreen();
        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();
        if (loadedGame != null) {
            loadedGame.triggerMessage(s);
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
        @Nullable Screen currentScene = Objects.requireNonNull(Util.getSceneManager()).getCurrentScreen();
        if ((currentScene == null && BubbleBlaster.getInstance().isGameLoaded()) ||
                currentScene instanceof SaveLoadingScreen ||
                currentScene instanceof EnvLoadScreen) {
            Rectangle2D gameBounds = gameType.getGameBounds();
            this.x = (float) MathHelper.clamp(x, gameBounds.getMinX(), gameBounds.getMaxX());
            this.y = (float) MathHelper.clamp(y, gameBounds.getMinY(), gameBounds.getMaxY());
            bindEvents();
        }
    }

    @Override
    protected void bindEvents() {
        BubbleBlaster.getEventBus().register(this);
        this.areEventsBinded = true;
    }

    @Override
    protected void unbindEvents() {
        BubbleBlaster.getEventBus().unregister(this);
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
        return rotateToArea();
    }

    @NotNull
    private Area rotateToArea() {
        return rotateToArea0(shipShape);
    }

    @NotNull
    private Area rotateToArea0(Area shipShape) {
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
    public Area getShipArea() {
        return rotateToArea();
    }

    /**
     * @return the arrow shape of the ship.
     */
    public Area getArrowArea() {
        return rotateToArea0(arrowShape);
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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Spawn and load checks //
        ///////////////////////////

        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();

        if (loadedGame == null) {
            return;
        }

        if (!isSpawned()) return;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Player component ticking //
        //////////////////////////////

        this.abilityContainer.onEntityTick();
        this.inventory.tick();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Player motion. //
        ////////////////////

        this.accelerateX = accelerateX / ((double)TPS / 20d) / 1.1;
        this.accelerateY = accelerateY / ((double)TPS / 20d) / 1.1;

        double tempVelMotSpeed = 0.0f;
        double tempVelRotSpeed = 0.0f;

        // Check each direction, to create velocity
        if (this.forward) tempVelMotSpeed += this.speed;
        if (this.backward) tempVelMotSpeed -= this.speed;
        if (this.left) tempVelRotSpeed -= this.rotationSpeed;
        if (this.right) tempVelRotSpeed += this.rotationSpeed;
        if (this.xInputY != 0) tempVelMotSpeed = this.xInputY * this.speed;
        if (this.xInputX != 0) tempVelRotSpeed = this.xInputX * this.rotationSpeed;

        // Update rotation
        double delta = 1d / TPS;

        // Update X, and Y.
        if (isMotionEnabled()) {
            this.rotation += (tempVelRotSpeed) / TPS * 20;
        }

        // Calculate Velocity X and Y.
        double angelRadians = Math.toRadians(this.rotation);
        double tempVelX = Math.cos(angelRadians) * tempVelMotSpeed;
        double tempVelY = Math.sin(angelRadians) * tempVelMotSpeed;

//        System.out.println("Acc_X[0] = " + accelerateX);
//        System.out.println("Acc_Y[0] = " + accelerateY);

        if (isMotionEnabled()) {
            this.accelerateX += tempVelX / ((double) TPS);
            this.accelerateY += tempVelY / ((double) TPS);
        }

//        System.out.println("Acc_X[1] = " + accelerateX);
//        System.out.println("Acc_Y[1] = " + accelerateY);

        // Update X, and Y.
        this.x += (this.accelerateX) + this.velX / ((double) TPS * 1);
        this.y += (this.accelerateY) + this.velY / ((double) TPS * 1);

        // Velocity.
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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Collision detection. //
        //////////////////////////

        // Game border.
        Rectangle2D gameBounds = loadedGame.getGameType().getGameBounds();
        this.x = (float) MathHelper.clamp(this.x, gameBounds.getMinX() - this.getBounds().getWidth(), gameBounds.getMaxX() + this.getBounds().getWidth());
        this.y = (float) MathHelper.clamp(this.y, gameBounds.getMinY() - this.getBounds().getHeight(), gameBounds.getMaxY() + this.getBounds().getHeight());
    }

    @Override
    public void damage(double value, DamageSource source) {
        float defense = attributes.getBase(Attribute.DEFENSE);
        if (defense <= 0.0d) {
            this.destroy();
            return;
        }

        // Deal damage to the player.
        this.damageValue -= value / defense;

        // Check health.
        this.checkDamage();

        // Check if source has attack modifier.
        if (value > 0.0d) {

            // Check if window is not focused.
            if (!BubbleBlaster.getInstance().getFrame().isFocused()) {
                if (SystemUtils.IS_JAVA_9) {
                    // Let the taskbar icon flash. (Java 9+)
                    @SuppressWarnings("Since15") Taskbar taskbar = Taskbar.getTaskbar();
                    try {
                        //noinspection Since15
                        taskbar.requestWindowUserAttention(BubbleBlaster.getInstance().getFrame());
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

        // Entity bubble
        if (targetEntity instanceof GiantBubbleEntity bubble) {

            // Modifiers
            double scoreMod = bubble.getAttributeMap().getBase(Attribute.SCORE_MULTIPLIER);
            double attackMod = bubble.getAttributeMap().getBase(Attribute.ATTACK);
            double defenseMod = bubble.getAttributeMap().getBase(Attribute.DEFENSE);

            // Attributes
            double radius = bubble.getRadius();
            double speed = bubble.getSpeed();

            // Calculate score value.
            double visibleValue = radius * speed;
            double nonVisibleValue = attackMod * defenseMod;
            double scoreValue = ((visibleValue * (nonVisibleValue + 1)) * scoreMod * attributes.getBase(Attribute.SCORE_MULTIPLIER)) * evt.getDeltaTime() / 8;

            // Add score.
            addScore(scoreValue);
        } else
            // Entity bubble
            if (targetEntity instanceof BubbleEntity bubble) {
                // Modifiers
                AttributeMap attributeMap = bubble.getAttributeMap();
                double scoreMultiplier = attributeMap.getBase(Attribute.SCORE_MULTIPLIER);
                double attack = attributeMap.getBase(Attribute.ATTACK);  // Maybe used.
                double defense = attributeMap.getBase(Attribute.DEFENSE);  // Maybe used.

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
    public void renderEntity(Graphics2D gg) {
        if (!isSpawned()) return;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fill the ship with the correct color. //
        ///////////////////////////////////////////
        gg.setColor(Color.red);
        gg.fill(getShipArea());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fill the arrow with the correct color. //
        ////////////////////////////////////////////
        gg.setColor(Color.white);
        gg.fill(getArrowArea());
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
    public void checkDamage() {
        if (damageValue <= 0)
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
    public void setState(BsonDocument state) {
        super.getState();

        this.score = state.getDouble("score").getValue();
        this.rotation = state.getDouble("rotation").getValue();
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
