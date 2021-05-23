package com.qtech.bubbleblaster.bubble;

import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.common.random.Rng;
import com.qtech.bubbleblaster.entity.BubbleEntity;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import com.qtech.bubbleblaster.entity.types.EntityType;
import com.qtech.utilities.python.builtins.ValueError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @see EntityType
 */
@SuppressWarnings({"unused", "SameParameterValue", "SameReturnValue"})
//@Builder
@NoArgsConstructor
public abstract class AbstractBubble extends RegistryEntry implements Serializable {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    public Color[] colors;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private double priority;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private IntRange radius;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private DoubleRange speed;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private float bounceAmount;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BubbleEffectCallback effect = (source, target) -> null;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private float score;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private float defense;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private float attack;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private double hardness;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int rarity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBubble that = (AbstractBubble) o;
        return that.getRegistryName().equals(getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Effect     //
    ////////////////////
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return effect.get(source, target);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Attributes     //
    ////////////////////////
    public int getMinRadius() {
        return radius.getMinimumInteger();
    }

    public int getMaxRadius() {
        return radius.getMaximumInteger();
    }

    public double getMinSpeed() {
        return speed.getMinimumDouble();
    }

    public double getMaxSpeed() {
        return speed.getMaximumDouble();
    }

    protected final void setMinRadius(int radius) {
        this.radius = new IntRange(radius, this.radius.getMaximumInteger());
    }

    protected final void setMaxRadius(int radius) {
        this.radius = new IntRange(this.radius.getMinimumInteger(), radius);
    }

    protected final void setMinSpeed(double speed) {
        this.speed = new DoubleRange(speed, this.radius.getMaximumDouble());
    }

    protected final void setMaxSpeed(double speed) {
        this.speed = new DoubleRange(this.speed.getMinimumInteger(), speed);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Modifiers     //
    ///////////////////////
    public boolean isDefenseRandom() {
        return false;
    }

    public boolean isAttackRandom() {
        return false;
    }

    public boolean isScoreRandom() {
        return false;
    }

    public float getDefense(AbstractGameType gameType, Rng rng) {
        return getDefense();
    }

    public float getAttack(AbstractGameType gameType, Rng rng) {
        return getAttack();
    }

    public float getScore(AbstractGameType gameType, Rng rng) {
        return getScore();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Other     //
    ///////////////////
    public double getModifiedPriority(double localDifficulty) {
        return getPriority();
    }

    public boolean canSpawn(AbstractGameType gameType) {
        return true;
    }

    public ArrayList<Object> getFilters(BubbleEntity bubbleEntity) {
        return new ArrayList<>();
    }

    @FunctionalInterface
    public interface BubbleEffectCallback {
        EffectInstance get(BubbleEntity source, Entity target);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String _name;
        private Long _priority = null;
        private float score = 1f;
        private float defense = 0.1f;
        private float attack = 0f;
        private IntRange radius = new IntRange(21, 80);
        private DoubleRange speed = new DoubleRange(1d, 2.5d);
        private int _rarity;
        private float bounceAmount = 0f;
        private double hardness = 1d;
        private Color[] colors;
        private BubbleEffectCallback _bubbleEffect = (source, target) -> null;
        private DoubleRange _speed;

        private Builder() {
        }

        public AbstractBubble build() {
            AbstractBubble bubbleType = new AbstractBubble() {
                @Override
                public EffectInstance getEffect(BubbleEntity source, Entity target) {
                    return _bubbleEffect.get(source, target);
                }
            };
//            if (_name == null) {
//                throw new IllegalArgumentException("Name must be specified");
//            }
            if (_priority == null) {
                throw new IllegalArgumentException("Priority must be specified");
            }
            if (colors == null) {
                throw new IllegalArgumentException("Colors must be specified");
            }

            if (_name != null) bubbleType.setRegistryName(_name);
            bubbleType.setPriority(_priority);
            bubbleType.setRarity(_rarity);
            bubbleType.setScore(score);
            bubbleType.setAttack(attack);
            bubbleType.setDefense(defense);
            bubbleType.setRadius(radius);
            bubbleType.setSpeed(speed);
            bubbleType.setHardness(hardness);
            bubbleType.setBounceAmount(bounceAmount);
            bubbleType.colors = colors;
            return bubbleType;
        }

        public Builder name(String name) {
            this._name = name;
            return this;
        }

        // Longs
        public Builder priority(long priority) {
            this._priority = priority;
            return this;
        }

        // Ints
        public Builder score(float score) {
            this.score = score;
            return this;
        }

        public Builder rarity(int rarity) {
            this._rarity = rarity;
            return this;
        }

        // Doubles
        public Builder attack(float attack) {
            this.attack = attack;
            return this;
        }

        public Builder defense(float defense) {
            this.defense = defense;
            return this;
        }

        public Builder hardness(double hardness) {
            this.hardness = hardness;
            return this;
        }

        // Floats
        public Builder bounceAmount(float bounceAmount) {
            this.bounceAmount = bounceAmount;
            return this;
        }

        // Ranges
        public Builder radius(int _min, int _max) {
            this.radius = new IntRange(_min, _max);
            return this;
        }

        public Builder radius(IntRange range) {
            this.radius = range;
            return this;
        }

        public Builder speed(double _min, double _max) {
            this.speed = new DoubleRange(_min, _max);
            return this;
        }

        public Builder speed(DoubleRange range) {
            this.speed = range;
            return this;
        }

        // Arrays (Dynamic)
        public Builder colors(Color... _colors) {
            this.colors = _colors;
            return this;
        }

        // Callbacks
        public Builder effect(BubbleEffectCallback _bubbleEffect) {
            this._bubbleEffect = _bubbleEffect;
            return this;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Collision     //
    ///////////////////////
    public void onCollision(BubbleEntity source, Entity target) {
        EffectInstance effectInstance = getEffect(source, target);
        if (effectInstance == null) {
            return;
        }

        if (source.isEffectApplied()) return;

        if (target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;
            try {
                source.setEffectApplied(true);
                player.addEffect(effectInstance);
            } catch (ValueError valueError) {
                valueError.printStackTrace();
            }
        }
    }
}
