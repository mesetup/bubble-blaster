package com.qsoftware.bubbles.common.entity;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.ability.AbilityType;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.meta.MetaData;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.interfaces.StateHolder;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.event.CollisionEvent;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.registry.Registers;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <h1>Entity base class.</h1>
 * The base for all entities, such as the player or a bubble.
 *
 * @author Quinten Jungblut
 * @see LivingEntity
 * @see AbstractBubbleEntity
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class Entity implements StateHolder {
    // ID's
    protected final long entityId;
    private final UUID uniqueId;

    // Types
    protected EntityType<?> type;

    // Misc
    protected Shape shape;
    protected HashSet<EntityType<?>> collisionWith = new HashSet<>();
    protected CopyOnWriteArraySet<EffectInstance> activeEffects = new CopyOnWriteArraySet<>();

    // Attributes
    protected double scale = 1;
    protected AttributeMap attributes = new AttributeMap();
    protected AttributeMap bases = new AttributeMap();

    // Flags
    private boolean motionEnabled = true;

    private final Scene scene;
    protected float x;
    protected float y;
    protected double velX, velY;
    protected boolean areEventsBinded;
    private boolean spawned;
    protected final AbstractGameType gameType;

    // Custom attributes
//    private BsonDocument numberAttributes = new BsonDocument();
//    private BsonDocument stringAttributes = new BsonDocument();
//    private BsonDocument booleanAttributes = new BsonDocument();

    // Metadata
//    private MetaData metaData = new MetaData(this);

    // Temporary data.
    @Deprecated
    private MetaData temporaryData;

    // Tag
    private BsonDocument tag = new BsonDocument();

    // Fields.
    protected int rotation = 0;

    // Abilities.
    private final HashMap<AbilityType<?>, BsonDocument> abilities = new HashMap<>();

    // Constructor
    public Entity(EntityType<?> type, Scene scene, AbstractGameType gameType) {
        this.scene = scene;
        this.gameType = gameType;
        this.entityId = gameType.getEntityId(this);
        this.type = type;
        this.uniqueId = UUID.randomUUID();
    }

    /**
     * Get entity uuid.
     *
     * @return the entity's uuid.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Get entity id.
     *
     * @return the entity's id.
     */
    public long getEntityId() {
        return entityId;
    }

    public void prepareSpawn(EntitySpawnData spawnData) {
        Point pos = spawnData.getPos();
        if (pos != null) {
            this.x = pos.x;
            this.y = pos.y;
        }
    }

    /**
     * On spawn.
     *
     * @param pos the position to spawn at.
     */
    public void onSpawn(Point pos) {
        spawned = true;
    }

    /**
     * On collision for entity.
     *
     * @param target collision target.
     */
    @Deprecated
    protected void onCollision(Entity target) {

    }

    /**
     * On collision using event.
     *
     * @param evtObj a {@link CollisionEvent collision event}.
     */
    @SubscribeEvent
    public abstract void onCollision(CollisionEvent evtObj);

    /**
     * @return True if the events are binded, false otherwise.
     */
    protected boolean areEventsBound() {
        return areEventsBinded;
    }

    /**
     * Tick event.
     *
     * @param environment the environment where the entity is from.
     */
    public void tick(Environment environment) {
//        if (!isSpawned()) return;

        for (EffectInstance effectInstance : this.activeEffects) {
            effectInstance.tick(this);
        }

        this.activeEffects.removeIf((effectInstance -> effectInstance.getRemainingTime() < 0d));

        this.x += this.motionEnabled ? this.velX * 0.2 : 0;
        this.y += this.motionEnabled ? this.velY * 0.2 : 0;
    }

    public abstract void renderEntity(Graphics2D gg);

    public abstract void delete();

    /**
     * Get the shape of the entity.
     *
     * @return the requested shape.
     */
    public abstract Shape getShape();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Equals & HashCode     //
    ///////////////////////////////
    @Override
    @SubscribeEvent
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return entityId == entity.entityId;
    }

    protected abstract void bindEvents();

    protected abstract void unbindEvents();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Get Bounds     //
    ////////////////////////
    public Rectangle getBounds() {
        Shape shapeObj = getShape();
        return shapeObj.getBounds();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Getters     //
    /////////////////////
    public final ResourceLocation getRegistryName() {
        return this.type.getRegistryName();
    }

    public EntityType<?> getType() {
        return type;
    }

//    public MetaData getMetaData() {
//        return metaData;
//    }
//
//    public void setMetaData(MetaData metaData) {
//        this.metaData = metaData;
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Teleport and Position     //
    ///////////////////////////////////
    public final void teleport(double x, double y) {
        this.teleport(new Point2D.Double(x, y));
    }

    public final void teleport(Point2D pos) {
        onTeleporting(new Point2D.Double(x, y), new Point2D.Double(pos.getX(), pos.getY()));
        this.x = (float) pos.getX();
        this.y = (float) pos.getY();
        onTeleported(new Point2D.Double(x, y), new Point2D.Double(pos.getX(), pos.getY()));
    }

    public Point2D getPos() {
        return new Point2D.Double(getX(), getY());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Teleport events     //
    /////////////////////////////
    public void onTeleported(Point2D from, Point2D to) {

    }

    public void onTeleporting(Point2D from, Point2D to) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Motion     //
    ////////////////////
    public void move(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public Point2D getVelocity() {
        return new Point2D.Double(velX, velY);
    }

    public void setVelocity(double velX, double velY) {
        this.velX = velX;
        this.velY = velY;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Collidable     //
    ////////////////////////
    public void addCollidable(Entity entity) {
        collisionWith.add(entity.getType());
    }

    public void addCollidable(EntityType<?> entityType) {
        collisionWith.add(entityType);
    }

    public boolean isCollidingWith(Entity entity) {
        return collisionWith.contains(entity.getType());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Effects     //
    /////////////////////
    @SuppressWarnings("UnusedReturnValue")
    public EffectInstance addEffect(EffectInstance effectInstance) {
        for (EffectInstance effectInstance1 : activeEffects) {
            if (effectInstance1.getType() == effectInstance.getType()) {
                if (effectInstance1.getRemainingTime() < effectInstance.getRemainingTime()) {
                    effectInstance1.setRemainingTime(effectInstance.getRemainingTime());
                }
                return effectInstance1;
            }
        }
        activeEffects.add(effectInstance);
        effectInstance.start(this);
        return effectInstance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     To-String     //
    ///////////////////////
    public String toSimpleString() {
        return getRegistryName() + "@(" + Math.round(getX()) + "," + Math.round(getY()) + ")";
    }

    public String toAdvancedString() {
        BsonDocument nbt = getState();
        String data = nbt.toString();

        return getRegistryName() + "@" + data;
    }

    public CopyOnWriteArraySet<EffectInstance> getActiveEffects() {
        return activeEffects;
    }

    public void removeEffect(EffectInstance effectInstance) {
        activeEffects.remove(effectInstance);
        if (effectInstance.isActive()) {
            effectInstance.stop(this);
        }
    }

    @Override
    public void setState(BsonDocument document) {
        this.tag = document.getDocument("Tag");
    }

    @Override
    @NotNull
    public BsonDocument getState() {
        BsonDocument document = new BsonDocument();
        document.put("Tag", this.tag);
        document.put("Type", new BsonString(this.type.getRegistryName().toString()));
        return document;
    }

    public void setMotionEnabled(boolean motionEnabled) {
        this.motionEnabled = motionEnabled;
    }

    public boolean isMotionEnabled() {
        return motionEnabled;
    }

    public BsonDocument getAbilityCompound(AbilityType<?> abilityType) {
        return this.abilities.get(abilityType);
    }

    public <E> BsonDocument getTag() {
        return tag;
    }

    public AttributeMap getAttributeMap() {
        return attributes;
    }

    @Deprecated
    public static Entity getEntity(Scene scene, AbstractGameType gameType, BsonDocument document) {
        String name = document.getString("Name").getValue();
        ResourceLocation rl = ResourceLocation.fromString(name);
        EntityType<?> entityType = Registers.ENTITIES.get(rl);
        return entityType.create(scene, gameType, document);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public Scene getScene() {
        return scene;
    }

    public final boolean isSpawned() {
        return spawned;
    }

    public AbstractGameType getGameType() {
        return gameType;
    }
}
