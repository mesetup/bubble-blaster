package com.qtech.bubbles.common.gamestate

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.RegistryEntry
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.util.Util
import com.qtech.utilities.datetime.DateTime
import java.awt.Color

abstract class GameEvent : RegistryEntry() {
    var backgroundColor: Color? = null
    open fun isActive(dateTime: DateTime?): Boolean {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return false
        return loadedGame.gameMode.isGameStateActive(this)
    }

    @Synchronized
    fun renderBackground(game: BubbleBlaster?, gg: GraphicsProcessor) {
        if (backgroundColor == null) return
        if (!isActive(DateTime.current())) return
        gg.color(backgroundColor)
        gg.shapeF(BubbleBlaster.instance.bounds)
    }

    init {
        BubbleBlaster.eventBus.register(this)
    }
}