package com.ultreon.bubbles.state;

import com.ultreon.bubbles.common.gamestate.GameEvent;
import com.ultreon.commons.utilities.datetime.DateTime;

@Deprecated
public class PauseState extends GameEvent {
    @Override
    public boolean isActive(DateTime dateTime) {
        return false;
    }
//    private final Date date = new Date(31, 10, 0);
//    private final Time timeLo = new Time(3, 0, 0);
//    private final Time timeHi = new Time(3, 59, 59);
}
