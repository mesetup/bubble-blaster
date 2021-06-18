package com.qtech.bubbles.common.bubble

import com.qtech.bubbles.bubble.AbstractBubble
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.random.Rng
import com.qtech.bubbles.core.exceptions.ValueExistsException
import com.qtech.bubbles.registry.Registers

object BubbleSystem {
    private var bubblePriorities = HashMap<AbstractBubble, List<Long>>()
    private var maxPriority = 0L
    private val defaultsPriorities = DynamicPartitions<AbstractBubble>()
    private val priorities = DynamicPartitions<AbstractBubble>()
    @JvmStatic
    fun getDefaultPriority(bubble: AbstractBubble?): Double {
        val index = defaultsPriorities.indexOf(bubble!!)
        return if (index == -1) {
            0.0
        } else defaultsPriorities.getSize(index)
    }

    @JvmStatic
    val defaultTotalPriority: Double
        get() = defaultsPriorities.totalSize

    @JvmStatic
    fun getDefaultPercentageChance(bubble: AbstractBubble?): Double {
        return getDefaultPriority(bubble) / defaultTotalPriority
    }

    @JvmStatic
    fun getPriority(bubble: AbstractBubble?): Double {
        val index = priorities.indexOf(bubble!!)
        return if (index == -1) {
            0.0
        } else priorities.getSize(index)
    }

    @JvmStatic
    val totalPriority: Double
        get() = priorities.totalSize

    @JvmStatic
    fun getPercentageChance(bubble: AbstractBubble?): Double {
        return getPriority(bubble) / totalPriority
    }

    /**
     * Bubble initialization for random spawning.
     *
     * @see AbstractBubble
     *
     * @see Registry<Bubble> */
    fun init() {
//        if (bubbleTypes == null)
        val bubbleTypes = Registers.BUBBLES.values()
        bubblePriorities = HashMap()
        maxPriority = 0
        for (bubbleType in bubbleTypes) {
            try {
                priorities.add(bubbleType.priority, bubbleType)
                defaultsPriorities.add(bubbleType.priority, bubbleType)
            } catch (valueExistsException: ValueExistsException) {
                valueExistsException.printStackTrace()
            }
        }
    }

    /**
     * Returns a random bubble from the bubbles initialized in [.init].
     *
     * @param rand The random instance used for the bubble system e.g. `qbubbles:bubble_system` from the initDefaults in [AbstractGameMode].
     * @return A random bubble.
     */
    @JvmStatic
    fun random(rand: Rng, gameMode: AbstractGameMode): AbstractBubble? {
        val localDifficulty = gameMode.localDifficulty.toDouble()
        priorities.editLengths { bubbleType2: AbstractBubble? -> bubbleType2!!.getModifiedPriority(localDifficulty) }
        val randValue = rand.getNumber(0.0, priorities.totalSize, gameMode.ticks)
        return priorities.getValue(randValue)
    }
}