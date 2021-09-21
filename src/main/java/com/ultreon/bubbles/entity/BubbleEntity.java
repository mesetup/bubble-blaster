package com.ultreon.bubbles.entity;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.bubbles.bubble.BubbleProperties;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.common.random.BubbleRandomizer;
import com.ultreon.bubbles.entity.attribute.Attribute;
import com.ultreon.bubbles.entity.damage.DamageSource;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.bubbles.entity.types.EntityType;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.event.EntityCollisionEvent;
import com.ultreon.bubbles.init.Bubbles;
import com.ultreon.bubbles.init.Entities;
import com.ultreon.bubbles.init.TextureCollections;
import com.ultreon.bubbles.util.Util;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.event.RenderEvent;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event.bus.AbstractEvents;
import com.ultreon.hydro.event.input.MouseMotionEvent;
import com.ultreon.hydro.input.KeyInput;
import com.ultreon.hydro.registry.Registry;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;
import org.bson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
@SuppressWarnings({"SameParameterValue", "unused"})
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
    private AbstractEvents.AbstractSubscription binding;
    private boolean markedForDeletion;

    public static EntityType<? extends BubbleEntity> getRandomType(Screen screen, AbstractGameType gameType) {
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

        if (KeyInput.isDown(KeyEvent.VK_F2)) {
            if (getShape().contains(evt.getParentEvent().getPoint())) {
                destroy();
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onCollision(EntityCollisionEvent evt) {
//        System.out.println("Collision 1a; " + evt.getSource().getEntityId() + "; " + evt.getTarget().getEntityId());
//        System.out.println("Collision 1b; " + getEntityId() + "; " + getEntityId());

        if (evt.getSource() == this) {

//            System.out.println("Collision 2");

            this.bubbleType.onCollision(this, evt.getTarget());

            if (evt.getTarget() instanceof PlayerEntity playerEntity) {

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
     *     <li>{@link com.ultreon.hydro.event.RenderEvent}</li>
     * </ul>
     *
     * @see TickEvent
     * @see com.ultreon.hydro.event.RenderEvent
     * @see Environment#spawn(EntityType)
     */
    @Override
    protected void bindEvents() {
        BubbleBlaster.getEventBus().subscribe(this);
        areEventsBinded = true;
    }

    /**
     * <h1>Unbind Events</h1>
     * <p><b>Warning: </b><i>Unsafe method! Use {@link #delete()}  removeBubble of GameType} instead.</i></p>
     * <p>Events:</p>
     * <ul>
     *     <li>{@link TickEvent}</li>
     *     <li>{@link RenderEvent}</li>
     * </ul>
     *
     * @throws NoSuchElementException If listener is already fully or partly removed.
     * @see TickEvent
     * @see RenderEvent
     * @see #delete()
     */
    @Override
    protected void unbindEvents() {
        try {
            BubbleBlaster.getEventBus().unsubscribe(this);
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
        if (player == null || !(BubbleBlaster.instance().isGameLoaded())) return;

        // Set velocity speed.
        if (BubbleBlaster.instance().isGameLoaded()) {
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
    public void render(Renderer gg) {
        if (!areEventsBinded) return;
        gg.image(TextureCollections.BUBBLE_TEXTURES.get().get(new ResourceEntry(getBubbleType().getRegistryName().namespace(), getBubbleType().getRegistryName().path() + "/" + radius)), (int) x - radius / 2, (int) y - radius / 2);
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

        ResourceEntry bubbleTypeKey = ResourceEntry.fromString(state.getString("bubbleType").getValue());
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
