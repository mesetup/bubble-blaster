package qtech.bubbles.state

import com.jhlabs.image.NoiseFilter
import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.gamestate.GameEvent
import qtech.bubbles.core.utils.categories.ColorUtils.hex2Rgb
import qtech.bubbles.event.FilterEvent
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.event.TickEvent
import qtech.utilities.datetime.Date
import qtech.utilities.datetime.DateTime
import qtech.utilities.datetime.Time
import java.time.DayOfWeek

class BloodMoonEvent : GameEvent() {
    private val date = Date(31, 10, 0)
    private val timeLo = Time(3, 0, 0)
    private val timeHi = Time(3, 59, 59)
    private var wasActive = false
    private var wasPlayerActive = false
    private var activating = false
    private var deactivating = false
    private var stopTime: Long = 0
    @SubscribeEvent
    @Synchronized
    fun onUpdate(evt: TickEvent?) {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
        if (stopTime < System.currentTimeMillis()) {
            loadedGame.gameMode.stopBloodMoon()
        }
        if (activating) {
            activating = false
            deactivating = false
            stopTime = System.currentTimeMillis() + 60000

            // Game effects.
            if (!wasActive) BubbleBlaster.logger.info("Blood Moon activated!")
            loadedGame.gameMode.triggerBloodMoon()
            loadedGame.gameMode.setStateDifficultyModifier(this, 16f)
            wasActive = true

            // Player effects.
            if (!wasPlayerActive && loadedGame.gameMode.player != null) {
                BubbleBlaster.logger.info("Blood Moon for player activated!")
                // Todo: implement this.
//                playerDefenses.put(GameScene.getGameType().getPlayer(), GameScene.getGameType().getPlayer().getDefenseModifier());
                wasPlayerActive = true
            }
        } else if (deactivating) {
            deactivating = false
            // Game effects.
            if (wasActive) {
                BubbleBlaster.logger.info("Blood Moon deactivated!")
                loadedGame.gameMode.removeStateDifficultyModifier(this)
                wasActive = false
            }
        }
    }

    @SubscribeEvent
    @Synchronized
    fun onFilter(evt: FilterEvent) {
        if (!isActive(DateTime.current())) return
        val filter = NoiseFilter()
        filter.monochrome = true
        filter.density = 0.25f
        filter.amount = 60
        filter.distribution = 1
        evt.addFilter(filter)
    }

    @Synchronized
    override fun isActive(dateTime: DateTime?): Boolean {
        super.isActive(dateTime)
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return false
        return loadedGame.gameMode.isBloodMoonActive
    }

    @Synchronized
    fun wouldActive(dateTime: DateTime): Boolean {
        val flag1 = dateTime.time.isBetween(timeLo, timeHi) // Devil's hour.
        val flag2 = dateTime.date.equalsIgnoreYear(date) // Halloween.
        val flag3 = dateTime.date.dayOfWeek == DayOfWeek.FRIDAY // Friday
        val flag4 = dateTime.date.day == 13 // 13th
        return flag1 && flag2 || flag3 && flag4 // Every October 31st in devil's hour. Or Friday 13th.
    }

    fun deactivate() {
        deactivating = true
    }

    fun activate() {
        activating = true
    }

    init {
        backgroundColor = hex2Rgb("#af0000")
    }
}