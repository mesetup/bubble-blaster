package com.ultreon.bubbles.entity.bubble;

import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.common.random.Rng;
import com.ultreon.bubbles.common.exceptions.ValueExists;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.registry.Registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class BubbleSystem {
    protected static HashMap<AbstractBubble, List<Long>> bubblePriorities = new HashMap<>();
    protected static long maxPriority = 0L;
    private static final DynamicPartitions<AbstractBubble> defaults = new DynamicPartitions<>();
    private static final DynamicPartitions<AbstractBubble> priorities = new DynamicPartitions<>();

    public static DynamicPartitions<AbstractBubble> getPriorities() {
        return priorities;
    }

    public static DynamicPartitions<AbstractBubble> getDefaultsPriorities() {
        return defaults;
    }

    public static Double getDefaultPriority(AbstractBubble bubble) {
        int index = defaults.indexOf(bubble);
        if (index == -1) {
            return 0d;
        }

        return defaults.getSize(index);
    }

    public static Double getDefaultTotalPriority() {
        return defaults.getTotalSize();
    }

    public static Double getDefaultPercentageChance(AbstractBubble bubble) {
        return getDefaultPriority(bubble) / (double) getDefaultTotalPriority();
    }

    public static Double getPriority(AbstractBubble bubble) {
        int index = priorities.indexOf(bubble);
        if (index == -1) {
            return 0d;
        }

        return priorities.getSize(index);
    }

    public static Double getTotalPriority() {
        return priorities.getTotalSize();
    }

    public static Double getPercentageChance(AbstractBubble bubble) {
        return (double) getPriority(bubble) / getTotalPriority();
    }

    /**
     * Bubble initialization for random spawning.
     *
     * @see AbstractBubble
     * @see Registry <Bubble>
     */
    public static void init() {
//        if (bubbleTypes == null)
        Collection<AbstractBubble> bubbleTypes = Registers.BUBBLES.values();
        BubbleSystem.bubblePriorities = new HashMap<>();
        BubbleSystem.maxPriority = 0;

        if (bubbleTypes == null) {
            throw new NullPointerException();
        }

        for (AbstractBubble bubbleType : bubbleTypes) {
            try {
                priorities.add(bubbleType.getPriority(), bubbleType);
                defaults.add(bubbleType.getPriority(), bubbleType);
            } catch (ValueExists valueExists) {
                valueExists.printStackTrace();
            }
        }
    }

    /**
     * Returns a random bubble from the bubbles initialized in {@link #init()}.
     *
     * @param rand The random instance used for the bubble system e.g. {@code qbubbles:bubble_system} from the initDefaults in {@link AbstractGameType}.
     * @return A random bubble.
     */
    public static AbstractBubble random(Rng rand, AbstractGameType gameType) {
        double localDifficulty = gameType.getLocalDifficulty();
        priorities.editLengths((bubbleType2) -> bubbleType2.getModifiedPriority(localDifficulty));

        double randValue = rand.getNumber(0, priorities.getTotalSize(), gameType.getTicks());

        return priorities.getValue(randValue);
    }
}
