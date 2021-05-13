package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.bubble.NormalBubble;
import com.qsoftware.bubbles.common.gamestate.GameEvent;
import com.qsoftware.bubbles.common.init.ObjectInit;
import com.qsoftware.bubbles.state.BloodMoonEvent;
import com.qsoftware.bubbles.state.PauseState;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Bubble Initialization</h1>
 * Bubble init, used for initialize bubbles.
 * For example, the {@link NormalBubble} instance is assigned here.
 *
 * @see AbstractBubble
 */
@SuppressWarnings("unused")
//@ObjectHolder(addonId = "qbubbles")
public class StateInit implements ObjectInit<GameEvent> {
    public static final List<GameEvent> STATES = new ArrayList<>();

    // Bubbles
    public static final GameEvent BLOOD_MOON_STATE = new BloodMoonEvent();
    @Deprecated
    public static final GameEvent PAUSE_STATE = new PauseState();

//    public static void registerBubbles() {
//        for (BubbleType bubble : STATES) {
//            Registry.getRegistry(BubbleType.class).register(bubble.getKey(), bubble);
//        }
//    }
}
