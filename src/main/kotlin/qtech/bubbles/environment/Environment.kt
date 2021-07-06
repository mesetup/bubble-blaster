package qtech.bubbles.environment

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.ResourceLocation.Companion.fromString
import qtech.bubbles.entity.Entity
import qtech.bubbles.common.entity.EntitySpawnData
import qtech.bubbles.common.entity.EntitySpawnData.Companion.fromLoadSpawn
import qtech.bubbles.common.gamestate.GameEvent
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.entity.types.EntityType
import qtech.bubbles.registry.Registers
import qtech.utilities.datetime.DateTime
import net.querz.nbt.tag.CompoundTag
import java.awt.Point
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.stream.Collectors
import kotlin.reflect.KClass

class Environment(val gameMode: AbstractGameMode) {
    private var entities0: MutableList<Entity> = CopyOnWriteArrayList()
    val entities: MutableList<Entity>
        get() {
            return entities0
        }

    val game: BubbleBlaster = BubbleBlaster.instance

    //        this.currentGameEvent.activate();
    var currentGameEvent: GameEvent? = null
    private var gameEventHandlerThread: Thread? = null
    /**
     * Spawn entity from loading.
     *
     * @param entityData the data of the entity to spawn.
     */
    fun spawnEntityFromState(entityData: CompoundTag) {
        if (!BubbleBlaster.instance.isOnMainThread) {
            BubbleBlaster.instance.runLater {
                val type = entityData.getString("Type")
                val entityType = Registers.ENTITIES[fromString(type)]!!
                val entity = entityType.create(gameMode, entityData)
                entity.prepareSpawn(fromLoadSpawn(entityData))
                entity.setState(entityData)
                entities0.add(entity)
            }
            return
        }
        val type = entityData.getString("Type")
        val entityType = Registers.ENTITIES[fromString(type)]!!
        val entity = entityType.create(gameMode, entityData)
        entity.prepareSpawn(fromLoadSpawn(entityData))
        entity.setState(entityData)
        entities0.add(entity)
    }

    private fun gameEventHandlerThread() {
        while (BubbleBlaster.instance.environment === this) {
            if (currentGameEvent != null) {
                if (!currentGameEvent!!.isActive(DateTime.current())) {
                    currentGameEvent = null
                }
                continue
            }
            for (gameEvent in Registers.GAME_EVENTS.values()) {
                if (gameEvent.isActive(DateTime.current())) {
                    currentGameEvent = gameEvent
                    break
                }
            }
        }
    }

    /**
     * Naturally spawn an entity.
     *
     * @param entityType type of entity to spawn.
     */
    fun spawn(entityType: EntityType<*>) {
        if (!BubbleBlaster.instance.isOnMainThread) {
            BubbleBlaster.instance.runLater {
                val entity = entityType.create(gameMode)
                val pos = gameMode.getSpawnLocation(entity)
                spawn(entity, pos)
            }
            return
        }
        val entity = entityType.create(gameMode)
        val pos = gameMode.getSpawnLocation(entity)
        spawn(entity, pos)
    }

    /**
     * Naturally spawn an entity using a specific position.
     *
     * @param entity entity to spawn.
     * @param pos    spawn location.
     */
    fun spawn(entity: Entity?, pos: Point?) {
        if (!BubbleBlaster.instance.isOnMainThread) {
            BubbleBlaster.instance.runLater {
                entity!!.prepareSpawn(EntitySpawnData.fromNaturalSpawn(pos))
                entity.onSpawn(pos, this)
                entities0.add(entity)
            }
            return
        }
        entity!!.prepareSpawn(EntitySpawnData.fromNaturalSpawn(pos))
        entity.onSpawn(pos, this)
        entities0.add(entity)
    }

    fun tick() {
        if (gameMode.isInitialized) {
            if (!BubbleBlaster.isPaused) {
                entities0.removeIf { obj: Any? -> obj is Entity && obj.isMarkedForDeletion }
                for (entity in entities0) {
                    entity.tick(this)
                }
                gameMode.tick()
            }
        }
    }

    fun gameOver(player: PlayerEntity) {
        entities0.remove(player)
    }

    fun joinPlayer(player: PlayerEntity) {
        entities0.add(player)
    }

    /**
     * <h1>Safe Remove Entity</h1>
     * Safely removes a entity. Removes the entity from the game-objects set, then removes it from
     * the gameObjects in [Environment.getEntities]  GameScene}
     *
     * @param entity The entity to remove.
     */
    @Deprecated("Use {@link Entity#delete()} instead.")
    fun removeEntity(entity: Entity) {
        if (!BubbleBlaster.instance.isOnMainThread) {
            BubbleBlaster.instance.runLater { entity.delete() }
        }
        entity.delete()
    }

    fun <T : Entity?> getEntitiesByClass(entityClass: Class<T>): MutableList<T> {
        return entities0.stream()
            .filter { entity: Any? -> entityClass.isAssignableFrom(entity!!.javaClass) }
            .map { obj: Any? -> entityClass.cast(obj) }
            .collect(Collectors.toUnmodifiableList())
    }

    fun <T : Entity> getEntitiesByClass(entityClass: KClass<T>): MutableList<T>? {
        return entities0.stream()
            .filter { entity: Any? -> entityClass.java.isAssignableFrom(entity!!.javaClass) }
            .map { obj: Any? -> entityClass.java.cast(obj) }
            .collect(Collectors.toUnmodifiableList())
    }

    fun start() {
        gameEventHandlerThread = Thread({ gameEventHandlerThread() }, "GameEventHandler")
        gameEventHandlerThread!!.start()
    }

    @Deprecated("")
    fun quit() {
        gameEventHandlerThread?.interrupt()
        gameMode.quit()
    }

}