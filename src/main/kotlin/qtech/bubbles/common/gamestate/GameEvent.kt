package qtech.bubbles.common.gamestate

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.RegistryEntry
import qtech.hydro.GraphicsProcessor
import qtech.utilities.datetime.DateTime
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