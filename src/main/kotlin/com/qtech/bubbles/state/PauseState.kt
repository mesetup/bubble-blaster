package com.qtech.bubbles.state

import com.qtech.bubbles.common.gamestate.GameEvent
import com.qtech.utilities.datetime.DateTime

@Deprecated("")
class PauseState : GameEvent() {
    override fun isActive(dateTime: DateTime?): Boolean {
        return false
    } //    private final Date date = new Date(31, 10, 0);
    //    private final Time timeLo = new Time(3, 0, 0);
    //    private final Time timeHi = new Time(3, 59, 59);
}