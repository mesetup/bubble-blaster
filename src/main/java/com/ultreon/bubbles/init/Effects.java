package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalAddon;
import com.ultreon.bubbles.effect.StatusEffect;
import com.ultreon.bubbles.effect.*;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * <h1>Effect Initialization</h1>
 * Effect init, used for initialize effects (temporary effects for players / bubbles that do something overtime).
 * For example, the {@link DefenseBoostEffect} instance is assigned here.
 *
 * @see StatusEffect
 * @see DeferredRegister< StatusEffect >
 */
@SuppressWarnings("unused")
public class Effects {
    public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(InternalAddon.ADDON_ID, Registers.EFFECTS);

    public static final RegistryObject<DefenseBoostEffect> DEFENSE_BOOST = register("defense", DefenseBoostEffect::new);
    public static final RegistryObject<BubbleFreezeEffect> BUBBLE_FREEZE = register("bubble_freeze", BubbleFreezeEffect::new);
    public static final RegistryObject<AttackBoostEffect> ATTACK_BOOST = register("attack", AttackBoostEffect::new);
    public static final RegistryObject<MultiScoreEffect> MULTI_SCORE = register("multi_score", MultiScoreEffect::new);
    public static final RegistryObject<SpeedBoostEffect> SPEED_BOOST = register("speed_boost", SpeedBoostEffect::new);
    public static final RegistryObject<BlindnessEffect> BLINDNESS = register("blindness", BlindnessEffect::new);
    public static final RegistryObject<ParalyzeEffect> PARALYZE = register("paralyze", ParalyzeEffect::new);
    public static final RegistryObject<PoisonEffect> POISON = register("poison", PoisonEffect::new);
    public static final RegistryObject<LuckEffect> LUCK = register("luck", LuckEffect::new);

    private static <T extends StatusEffect> RegistryObject<T> register(String name, Supplier<T> effectSupplier) {
        return EFFECTS.register(name, effectSupplier);
    }

}
