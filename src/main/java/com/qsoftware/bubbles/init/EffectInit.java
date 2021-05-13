package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.QInternalAddon;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.effect.*;
import com.qsoftware.bubbles.registry.DeferredRegister;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * <h1>Effect Initialization</h1>
 * Effect init, used for initialize effects (temporary effects for players / bubbles that do something overtime).
 * For example, the {@link DefenseBoostEffect} instance is assigned here.
 *
 * @see Effect
 * @see DeferredRegister<Effect>
 */
@SuppressWarnings("unused")
//@ObjectHolder(addonId = QInternalAddon.ADDON_ID)
public class EffectInit {
    @SuppressWarnings("rawtypes")
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.EFFECTS);

    public static final RegistryObject<DefenseBoostEffect> DEFENSE_BOOST = register("defense", DefenseBoostEffect::new);
    public static final RegistryObject<BubbleFreezeEffect> BUBBLE_FREEZE = register("bubble_freeze", BubbleFreezeEffect::new);
    public static final RegistryObject<AttackBoostEffect> ATTACK_BOOST = register("attack", AttackBoostEffect::new);
    public static final RegistryObject<MultiScoreEffect> MULTI_SCORE = register("multi_score", MultiScoreEffect::new);
    public static final RegistryObject<SpeedBoostEffect> SPEED_BOOST = register("speed_boost", SpeedBoostEffect::new);
    public static final RegistryObject<BlindnessEffect> BLINDNESS = register("blindness", BlindnessEffect::new);
    public static final RegistryObject<ParalyzeEffect> PARALYZE = register("paralyze", ParalyzeEffect::new);
    public static final RegistryObject<PoisonEffect> POISON = register("poison", PoisonEffect::new);
    public static final RegistryObject<LuckEffect> LUCK = register("luck", LuckEffect::new);

    private static <T extends Effect<T>> RegistryObject<T> register(String name, Supplier<T> effectSupplier) {
        return EFFECTS.register(name, effectSupplier);
    }

}
