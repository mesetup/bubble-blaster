package com.qtech.bubbles.bubble

import com.qtech.bubbles.bubble.AbstractBubble.BubbleEffectCallback
import com.qtech.bubbles.common.RegistryEntry
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.random.Rng
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.entity.player.PlayerEntity
import com.qtech.utilities.python.builtins.ValueError
import lombok.NoArgsConstructor
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color
import java.awt.Composite
import java.awt.geom.Point2D
import java.io.Serializable
import java.util.*

/**1
 * @see BubbleEntity
 */
//@Builder
@Suppress("UNUSED_PARAMETER")
@NoArgsConstructor
abstract class AbstractBubble : RegistryEntry(), Serializable {
    var colors: Array<out Color>? = null
    var priority = 0.0
        protected set
    var radius: IntRange? = null
        protected set
    var speed: DoubleRange? = null
        protected set
    var bounceAmount = 0f
        protected set
    var effect = BubbleEffectCallback { _: BubbleEntity?, _: Entity? -> null }
        protected set
    var score = 0f
        protected set
    var defense = 0f
        protected set
    var attack = 0f
        protected set
    var hardness = 0.0
        protected set
    var rarity = 0
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AbstractBubble
        return that.registryName == registryName
    }

    override fun hashCode(): Int {
        return Objects.hash(registryName)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Effect     //
    ////////////////////
    open fun getEffect(source: BubbleEntity, target: Entity): EffectInstance? {
        return effect[source, target]
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Attributes     //
    ////////////////////////
    var minRadius: Int
        get() = radius!!.minimumInteger
        protected set(radius) {
            this.radius = IntRange(radius, this.radius!!.maximumInteger)
        }
    var maxRadius: Int
        get() = radius!!.maximumInteger
        protected set(radius) {
            this.radius = IntRange(this.radius!!.minimumInteger, radius)
        }
    var minSpeed: Double
        get() = speed!!.minimumDouble
        protected set(speed) {
            this.speed = DoubleRange(speed, radius!!.maximumDouble)
        }
    var maxSpeed: Double
        get() = speed!!.maximumDouble
        protected set(speed) {
            this.speed = DoubleRange(this.speed!!.minimumInteger, speed)
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Modifiers     //
    ///////////////////////
    open val isDefenseRandom: Boolean
        get() = false
    val isAttackRandom: Boolean
        get() = false
    val isScoreRandom: Boolean
        get() = false

    open fun getDefense(gameMode: AbstractGameMode, rng: Rng): Float {
        return defense
    }

    fun getAttack(gameMode: AbstractGameMode?, rng: Rng?): Float {
        return attack
    }

    fun getScore(gameMode: AbstractGameMode?, rng: Rng?): Float {
        return score
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Other     //
    ///////////////////
    open fun getModifiedPriority(localDifficulty: Double): Double {
        return priority
    }

    open fun canSpawn(gameMode: AbstractGameMode): Boolean {
        return true
    }

    open fun getFilters(bubbleEntity: BubbleEntity?): ArrayList<Any> {
        return ArrayList()
    }

    fun interface BubbleEffectCallback {
        operator fun get(source: BubbleEntity, target: Entity): EffectInstance?
    }

    @Suppress("unused")
    class Builder {
        private var _name: String? = null
        private var _priority: Long? = null
        private var score = 1f
        private var defense = 0.1f
        private var attack = 0f
        private var radius = IntRange(21, 80)
        private var speed = DoubleRange(1.0, 2.5)
        private var _rarity = 0
        private var bounceAmount = 0f
        private var hardness = 1.0
        private var colors: Array<out Color>? = null
        private var _bubbleEffect = BubbleEffectCallback { _: BubbleEntity?, _: Entity? -> null }
        fun build(): AbstractBubble {
            val bubbleType: AbstractBubble = object : AbstractBubble() {
                override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance? {
                    return _bubbleEffect[source, target]
                }
            }
            //            if (_name == null) {
//                throw new IllegalArgumentException("Name must be specified");
//            }
            requireNotNull(_priority) { "Priority must be specified" }
            requireNotNull(colors) { "Colors must be specified" }
            if (_name != null) bubbleType.setRegistryName(_name!!)
            bubbleType.priority = _priority!!.toDouble()
            bubbleType.rarity = _rarity
            bubbleType.score = score
            bubbleType.attack = attack
            bubbleType.defense = defense
            bubbleType.radius = radius
            bubbleType.speed = speed
            bubbleType.hardness = hardness
            bubbleType.bounceAmount = bounceAmount
            bubbleType.colors = colors
            return bubbleType
        }

        fun name(name: String?): Builder {
            _name = name
            return this
        }

        // Longs
        fun priority(priority: Long): Builder {
            _priority = priority
            return this
        }

        // Ints
        fun score(score: Float): Builder {
            this.score = score
            return this
        }

        fun rarity(rarity: Int): Builder {
            _rarity = rarity
            return this
        }

        // Doubles
        fun attack(attack: Float): Builder {
            this.attack = attack
            return this
        }

        fun defense(defense: Float): Builder {
            this.defense = defense
            return this
        }

        fun hardness(hardness: Double): Builder {
            this.hardness = hardness
            return this
        }

        // Floats
        fun bounceAmount(bounceAmount: Float): Builder {
            this.bounceAmount = bounceAmount
            return this
        }

        // Ranges
        fun radius(_min: Int, _max: Int): Builder {
            radius = IntRange(_min, _max)
            return this
        }

        fun radius(range: IntRange): Builder {
            radius = range
            return this
        }

        fun speed(_min: Double, _max: Double): Builder {
            speed = DoubleRange(_min, _max)
            return this
        }

        fun speed(range: DoubleRange): Builder {
            speed = range
            return this
        }

        // Arrays (Dynamic)
        fun colors(vararg _colors: Color): Builder {
            colors = _colors
            return this
        }

        // Callbacks
        fun effect(_bubbleEffect: BubbleEffectCallback): Builder {
            this._bubbleEffect = _bubbleEffect
            return this
        }
    }

    open fun getComposite(ticksLived: Int): Composite? {
        return null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Collision     //
    ///////////////////////
    open fun onCollision(source: BubbleEntity, target: Entity) {
        val effectInstance = getEffect(source, target) ?: return
        if (source.isEffectApplied) return
        if (target is PlayerEntity) {
            try {
                source.isEffectApplied = true
                target.addEffect(effectInstance)
            } catch (valueError: ValueError) {
                valueError.printStackTrace()
            }
        }
    }

    fun getRandomLocation(entity: BubbleEntity, seed: Long, x: Rng, y: Rng): Point2D.Float {
        val gameBounds = entity.environment!!.gameMode.gameBounds
        val width = entity.bounds!!.width / 2 - 2
        val height = entity.bounds!!.height / 2 - 2
        val xCoord = x.getNumber(gameBounds!!.minX - width, gameBounds.maxX + width, seed).toFloat()
        val yCoord = y.getNumber(gameBounds.minY - height, gameBounds.maxY + height, seed).toFloat()
        return Point2D.Float(xCoord, yCoord)
    }

    companion object {
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }
}