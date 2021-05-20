package com.qtech.bubbleblaster.entity;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.bubble.AbstractBubble;
import com.qtech.bubbleblaster.common.BubbleProperties;
import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.common.entity.*;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.common.random.BubbleRandomizer;
import com.qtech.bubbleblaster.common.scene.Scene;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.core.controllers.KeyboardController;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import com.qtech.bubbleblaster.entity.types.EntityType;
import com.qtech.bubbleblaster.environment.Environment;
import com.qtech.bubbleblaster.event.*;
import com.qtech.bubbleblaster.event.bus.EventBus;
import com.qtech.bubbleblaster.init.Bubbles;
import com.qtech.bubbleblaster.init.Entities;
import com.qtech.bubbleblaster.init.TextureCollections;
import com.qtech.bubbleblaster.registry.Registry;
import com.qtech.bubbleblaster.util.Util;
import org.bson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private static final EntityType<BubbleEntity> entityType = Entities.BUBBLE.get();

    private static final Random random = new Random(Math.round((double) (System.currentTimeMillis() / 86400000))); // Random day. 86400000 milliseconds == 1 day.
    private boolean effectApplied = false;
    private EventBus.Handler binding;
    private boolean markedForDeletion;

    public static EntityType<? extends BubbleEntity> getRandomType(Scene scene, AbstractGameType gameType) {
        if (random.nextInt(3_000) == 0) {
            return Entities.GIANT_BUBBLE.get();
        }
        return Entities.BUBBLE.get();
    }

    public BubbleEntity(AbstractGameType gameType) {
        super(entityType, gameType);

        // Add collisionables.
        this.addCollidable(Entities.PLAYER.get());

        // Get random properties
        BubbleRandomizer randomizer = this.gameType.getBubbleRandomizer();
        BubbleProperties properties = randomizer.getRandomProperties(gameType.getGameBounds(), gameType);

        // Bubble Type
        this.bubbleType = properties.getType().canSpawn(gameType) ? properties.getType() : Bubbles.NORMAL_BUBBLE.get();

        // Dynamic values
        this.radius = properties.getRadius();
        this.speed = properties.getSpeed();
        this.baseSpeed = properties.getSpeed();
        this.baseRadius = properties.getRadius();
        attributes.setBase(Attribute.MAX_DAMAGE, properties.getDamageValue());
        this.damageValue = properties.getDamageValue();

        // Set attributes.
        this.attributes.setBase(Attribute.ATTACK, properties.getAttack());
        this.attributes.setBase(Attribute.DEFENSE, properties.getDefense());
        this.attributes.setBase(Attribute.SCORE_MULTIPLIER, properties.getScoreMultiplier());
        this.bounceAmount = bubbleType.getBounceAmount();

        // Set velocity
        this.velX = -baseSpeed;

        // Set attributes.
        this.attributes.setBase(Attribute.DEFENSE, 0.5f);
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
                destroy();
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
     * <p><b>Warning: </b><i>Unsafe method! Use {@link Environment#spawn(EntityType)} instead.</i></p>
     * <p>Events:</p>
     * <ul>
     *     <li>{@link TickEvent}</li>
     *     <li>{@link com.qtech.bubbleblaster.event.RenderEvent}</li>
     * </ul>
     *
     * @see TickEvent
     * @see com.qtech.bubbleblaster.event.RenderEvent
     * @see Environment#spawn(EntityType)
     */
    @Override
    protected void bindEvents() {
//        tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.LOWER);
//        renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.LOWER);
//        collisionEventCode = QCollisionEvent.addListener(GameScene.getInstance(), this::onCollision, RenderEventPriority.NORMAL);

        BubbleBlaster.getEventBus().register(this);
        areEventsBinded = true;
    }

    /**
     * <h1>Unbind Events</h1>
     * <p><b>Warning: </b><i>Unsafe method! Use {@link Environment#removeEntity(Entity)}  removeBubble of GameType} instead.</i></p>
     * <p>Events:</p>
     * <ul>
     *     <li>{@link TickEvent}</li>
     *     <li>{@link RenderEvent}</li>
     * </ul>
     *
     * @throws NoSuchElementException If listener is already fully or partly removed.
     * @see TickEvent
     * @see RenderEvent
     * @see Environment#removeEntity(Entity)
     */
    @Override
    protected void unbindEvents() {
        try {
            BubbleBlaster.getEventBus().unregister(this);
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

    /**
     * Tick bubble entity.
     *
     * @param environment the environment where the entity is from.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    @SubscribeEvent
    public void tick(Environment environment) {
        // Check player and current scene.
        PlayerEntity player = this.gameType.getPlayer();
        @Nullable Screen currentScreen = Util.getSceneManager().getCurrentScreen();
        if (player == null || !(BubbleBlaster.getInstance().isGameLoaded())) return;

        // Set velocity speed.
        if (BubbleBlaster.getInstance().isGameLoaded()) {
            velX = -speed * (this.gameType.getPlayer().getLevel() / 10d + 1);
        } else {
            velX = -speed * (5.0);
        }

        velX *= getGameType().getGlobalBubbleSpeedModifier();

        super.tick(environment);

        if (this.x < -this.radius) {
            delete();
        }
    }

    @Override
    public synchronized void renderEntity(GraphicsProcessor gg) {
        if (!areEventsBinded) return;
        gg.drawImage(TextureCollections.BUBBLE_TEXTURES.get().get(new ResourceLocation(getBubbleType().getRegistryName().getNamespace(), getBubbleType().getRegistryName().getPath() + "/" + radius)), (int) x - radius / 2, (int) y - radius / 2, BubbleBlaster.getInstance());
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
    public void checkDamage() {
        if (this.damageValue <= 0d) {
            delete();
        }
    }

    @Override
    public void setDamageValue(float hardness) {
        super.setDamageValue(hardness);
        radius = (int) hardness + 4;
        checkDamage();
    }

    @Override
    public void damage(double value, DamageSource source) {
//        System.out.println(value);
//        System.out.println(defenseModifier);
//        System.out.println(value / defenseModifier);

        super.damage(value / attributes.getBase(Attribute.DEFENSE), source);
        radius = (int) damageValue + 4;
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
    public void setState(BsonDocument state) {
        super.setState(state);

        this.radius = state.getInt32("Radius").getValue();
        this.baseRadius = state.getInt32("BaseRadius").getValue();

        this.bounceAmount = state.getInt32("BounceAmount").getValue();
        this.baseBounceAmount = state.getInt32("BaseBounceAmount").getValue();

        ResourceLocation bubbleTypeKey = ResourceLocation.fromString(state.getString("bubbleType").getValue());
        this.effectApplied = state.getBoolean("IsEffectApplied").getValue();
        this.bubbleType = Registry.getRegistry(AbstractBubble.class).get(bubbleTypeKey);
    }

    public void setEffectApplied(boolean effectApplied) {
        this.effectApplied = effectApplied;
    }

    public boolean isEffectApplied() {
        return effectApplied;
    }

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }
}