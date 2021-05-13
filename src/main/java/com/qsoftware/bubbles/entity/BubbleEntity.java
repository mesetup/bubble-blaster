package com.qsoftware.bubbles.entity;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.common.BubbleProperties;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.entity.AbstractBubbleEntity;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.entity.DamageSource;
import com.qsoftware.bubbles.common.entity.EntitySpawnData;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.random.BubbleRandomizer;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.controllers.KeyboardController;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.event.CollisionEvent;
import com.qsoftware.bubbles.event.MouseMotionEvent;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.TickEvent;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.event.old.RenderEvent;
import com.qsoftware.bubbles.init.EntityInit;
import com.qsoftware.bubbles.registry.Registry;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;
import org.bson.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * <h1>Bubble Entity.</h1>
 * One of the most important parts of the game.
 *
 * @see AbstractBubbleEntity
 */
@SuppressWarnings({"SameParameterValue", "unused", "deprecation"})
public class BubbleEntity extends AbstractBubbleEntity {
    // Attributes
    protected int radius;
    protected int baseRadius;
    protected float bounceAmount;
    protected float baseBounceAmount;

    // Event codes
    /**
     * @deprecated not used anymore
     */
    @SuppressWarnings("unused")
    @Deprecated()
    private int collisionEventCode;

    /**
     * @deprecated not used anymore
     */
    @SuppressWarnings("unused")
    @Deprecated()
    private int renderEventCode;

    /**
     * @deprecated not used anymore
     */
    @SuppressWarnings("unused")
    @Deprecated()
    private int tickEventCode;

    // Bubble type.
    protected AbstractBubble bubbleType;

    // Entity type.
    private static final EntityType<BubbleEntity> entityType = EntityInit.BUBBLE.get();

    private static final Random random = new Random(Math.round((double) (System.currentTimeMillis() / 86400000))); // Random day. 86400000 milliseconds == 1 day.
    private boolean effectApplied = false;
    private EventBus.Handler binding;

    public static EntityType<? extends BubbleEntity> getRandomType(Scene scene, AbstractGameType gameType) {
        if (random.nextInt(3_000) == 0) {
            return EntityInit.GIANT_BUBBLE.get();
        }
        return EntityInit.BUBBLE.get();
    }

    public BubbleEntity(Scene scene, AbstractGameType gameType) {
        super(entityType, scene, gameType);

        // Add collisionables.
        this.addCollidable(EntityInit.PLAYER.get());

        // Get random properties
        BubbleRandomizer randomizer = this.gameType.getBubbleRandomizer();
        BubbleProperties properties = randomizer.getRandomProperties(gameType.getGameBounds(), gameType);

        // Bubble Type
        this.bubbleType = properties.getType();

        // Dynamic values
        this.radius = properties.getRadius();
        this.speed = properties.getSpeed();
        this.baseSpeed = properties.getSpeed();
        this.baseRadius = properties.getRadius();
        this.maxHardness = properties.getHardness();
        this.hardness = properties.getHardness();

        // Set attributes.
        this.attributes.set(Attribute.ATTACK, properties.getAttack());
        this.attributes.set(Attribute.DEFENSE, properties.getDefense());
        this.attributes.set(Attribute.SCORE_MULTIPLIER, properties.getScoreMultiplier());
        this.bounceAmount = bubbleType.getBounceAmount();

        // Set velocity
        this.velX = -baseSpeed;

        // Set attributes.
        this.attributes.set(Attribute.DEFENSE, 0.5f);
    }

    /**
     * <h1>Spawn Event Handler</h1>
     * On-spawn.
     *
     * @param spawnData the entity's spawn data.
     */
    @Override
    public void prepareSpawn(EntitySpawnData spawnData) {
        super.prepareSpawn(spawnData);

        bindEvents();
    }

    @SubscribeEvent
    public void onMouse(MouseMotionEvent evt) {
//        if (evt.getType() == MouseEventType.CLICK) {
//            if (evt.getParentEvent().getButton() == 1) {
//                if (KeyboardController.instance().isPressed(KeyEvent.VK_F2)) {
//                    if (getShape().contains(evt.getParentEvent().getPoint())) {
//                        instantDestroy();
//                    }
//                }
//            }
//        }

        if (KeyboardController.instance().isPressed(KeyEvent.VK_F2)) {
            if (getShape().contains(evt.getParentEvent().getPoint())) {
                instantDestroy();
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onCollision(CollisionEvent evt) {
//        System.out.println("Collision 1a; " + evt.getSource().getEntityId() + "; " + evt.getTarget().getEntityId());
//        System.out.println("Collision 1b; " + getEntityId() + "; " + getEntityId());

        if (evt.getSource() == this) {

//            System.out.println("Collision 2");

            this.bubbleType.onCollision(this, evt.getTarget());

            if (evt.getTarget() instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) evt.getTarget();

//                System.out.println(((BubbleEntity)(evt.getSource())).getHardness());

//                System.out.println("Collision 4");

                double relX = getBounds().getCenterX() - playerEntity.getBounds().getCenterX();
                double relY = getBounds().getCenterY() - playerEntity.getBounds().getCenterY();

//                double distance = getPos().distance(playerEntity.getPos());
//                double radius1 = getRadius();

                double radians = Math.atan2(relX, relY) * 180 / Math.PI;

                double tempVelX = Math.cos(radians) * bounceAmount;
                double tempVelY = Math.sin(radians) * bounceAmount;

                playerEntity.triggerReflect(new Point2D.Double(tempVelX, tempVelY), -0.01f);
            }
        }
    }

    /**
     * <h1>Bind Events</h1>
     * <p><b>Warning: </b><i>Unsafe method! Use {@link Environment#spawnEntity(EntityType)} instead.</i></p>
     * <p>Events:</p>
     * <ul>
     *     <li>{@link TickEvent}</li>
     *     <li>{@link RenderEvent}</li>
     * </ul>
     *
     * @see TickEvent
     * @see RenderEvent
     * @see AbstractGameType#spawnBubble(double, double, Integer, Double, AbstractBubble) GameType.spawnBubble(...)
     */
    @Override
    protected void bindEvents() {
//        tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.LOWER);
//        renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.LOWER);
//        collisionEventCode = QCollisionEvent.addListener(GameScene.getInstance(), this::onCollision, RenderEventPriority.NORMAL);

        QBubbles.getEventBus().register(this);
        areEventsBinded = true;
    }

    /**
     * <h1>Unbind Events</h1>
     * <p><b>Warning: </b><i>Unsafe method! Use {@link AbstractGameType#removeBubble(BubbleEntity) removeBubble of GameType} instead.</i></p>
     * <p>Events:</p>
     * <ul>
     *     <li>{@link TickEvent}</li>
     *     <li>{@link RenderEvent}</li>
     * </ul>
     *
     * @throws NoSuchElementException If listener is already fully or partly removed.
     * @see TickEvent
     * @see RenderEvent
     * @see AbstractGameType#removeBubble(BubbleEntity) GameType.removeBubble(...)
     */
    @Override
    protected void unbindEvents() {
//        QUpdateEvent.removeListener(tickEventCode);
//        QRenderEvent.removeListener(renderEventCode);
//        QCollisionEvent.removeListener(collisionEventCode);

//        System.out.println("unregister: " + this);
        try {
            QBubbles.getEventBus().unregister(this);
        } catch (IllegalArgumentException ignored) {

        }
        areEventsBinded = false;
    }

    @Override
    public Rectangle getBounds() {
        Rectangle rectangle = super.getBounds();
        rectangle.width += 4;
        rectangle.height += 4;
        return rectangle;
    }

    //    @Override
//    public boolean areEventsBinded() {
//        return eventsBinded;
//    }

    /**
     * Tick bubble entity.
     *
     * @param environment the environment where the entity is from.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    @SubscribeEvent
    public void tick(Environment environment) {
//        if (!areEventsBinded) return;

        // Check player and current scene.
        PlayerEntity player = this.gameType.getPlayer();
//        if (player == null) {
//            throw new IllegalStateException("Player of default game-type is null.");
//        }
//        if (!(Util.getSceneManager().getCurrentScene() instanceof GameScene) ){
//            throw new IllegalStateException("Invalid scene for entities: " + Util.getSceneManager().getCurrentScene().getClass().getSimpleName());
//        }

        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (player == null || !(currentScene instanceof GameScene)) return;

        // Set velocity speed.
        if (Util.getSceneManager().getCurrentScene() instanceof GameScene) {
            velX = -speed * (this.gameType.getPlayer().getLevel() / 10d + 1);
        } else {
            velX = -speed * (5.0);
        }

        velX *= getGameType().getGlobalBubbleSpeedModifier();

        super.tick(environment);

        if (this.x < -this.radius) {
            this.gameType.removeBubble(this);
        }
    }

    @Override
    public synchronized void renderEntity(Graphics2D gg) {
        if (!areEventsBinded) return;
        gameType.drawBubble(gg, x, y, (int) ((double) radius + ((double) bubbleType.colors.length * 3.0d)), bubbleType.colors);
    }

    /**
     * <h1>Delete Bubble</h1>
     * <b>WARNING: </b><i>This method is unsafe! Use {@link AbstractGameType#removeBubble(BubbleEntity)} instead.</i>
     */
    @Override
    public void delete() {
        if (areEventsBound()) {
            unbindEvents();
        }
    }

    @Override
    public Ellipse2D getShape() {
        int rad = radius + (bubbleType.colors.length * 2);
        return new Ellipse2D.Double(this.x - (float) rad / 2, this.y - (float) rad / 2, rad, rad);
    }

    ///////////////////////////////////////////////////////////////////////////
    //     Attributes     //
    ////////////////////////

    // Radius.
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public int getBaseRadius() {
        return baseRadius;
    }

    // Bounce amount.
    public void setBounceAmount(float bounceAmount) {
        this.bounceAmount = bounceAmount;
    }

    public float getBounceAmount() {
        return bounceAmount;
    }

    public float getBaseBounceAmount() {
        return baseBounceAmount;
    }

    ///////////////////////////////////////////////////////////////////////////
    //     Bubble Type     //
    /////////////////////////
    public AbstractBubble getBubbleType() {
        return bubbleType;
    }

    public void setBubbleType(AbstractBubble type) {
        this.bubbleType = type;
    }

    @Override
    public void checkHardness() {
        if (this.hardness <= 0d) {
            gameType.removeBubble(this);
        }
    }

    @Override
    public void setHardness(double hardness) {
        super.setHardness(hardness);
        radius = (int) hardness + 4;
        checkHardness();
    }

    @Override
    public void damage(double value, DamageSource source) {
//        System.out.println(value);
//        System.out.println(defenseModifier);
//        System.out.println(value / defenseModifier);

        super.damage(value / attributes.get(Attribute.DEFENSE), source);
        radius = (int) hardness + 4;
    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument document = super.getState();
        document.put("Radius", new BsonInt32(radius));
        document.put("BaseRadius", new BsonInt32(baseRadius));

        document.put("BounceAmount", new BsonDouble(bounceAmount));
        document.put("BaseBounceAmount", new BsonDouble(baseBounceAmount));

        document.put("IsEffectApplied", new BsonBoolean(effectApplied));
        document.put("BubbleName", new BsonString(bubbleType.getRegistryName().toString()));

        return document;
    }

    @Override
    public void setState(BsonDocument document) {
        super.setState(document);

        this.radius = document.getInt32("Radius").getValue();
        this.baseRadius = document.getInt32("BaseRadius").getValue();

        this.bounceAmount = document.getInt32("BounceAmount").getValue();
        this.baseBounceAmount = document.getInt32("BaseBounceAmount").getValue();

        ResourceLocation bubbleTypeKey = ResourceLocation.fromString(document.getString("bubbleType").getValue());
        this.effectApplied = document.getBoolean("IsEffectApplied").getValue();
        this.bubbleType = Registry.getRegistry(AbstractBubble.class).get(bubbleTypeKey);
    }

    public void setEffectApplied(boolean effectApplied) {
        this.effectApplied = effectApplied;
    }

    public boolean isEffectApplied() {
        return effectApplied;
    }

}
