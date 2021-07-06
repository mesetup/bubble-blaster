package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.common.effect.Effect
import qtech.bubbles.effect.*
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

/**
 * <h1>Effect Initialization</h1>
 * Effect init, used for initialize effects (temporary effects for players / bubbles that do something overtime).
 * For example, the [DefenseBoostEffect] instance is assigned here.
 *
 * @see Effect
 *
 * @see DeferredRegister<Effect>
</Effect> */
object Effects {
    val EFFECTS = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.EFFECTS)
    val DEFENSE_BOOST: RegistryObject<DefenseBoostEffect> = register("defense") { DefenseBoostEffect() }
    val BUBBLE_FREEZE: RegistryObject<BubbleFreezeEffect> = register("bubble_freeze") { BubbleFreezeEffect() }
    val ATTACK_BOOST: RegistryObject<AttackBoostEffect> = register("attack") { AttackBoostEffect() }
    val MULTI_SCORE: RegistryObject<MultiScoreEffect> = register("multi_score") { MultiScoreEffect() }
    val SPEED_BOOST: RegistryObject<SpeedBoostEffect> = register("speed_boost") { SpeedBoostEffect() }
    val BLINDNESS: RegistryObject<BlindnessEffect> = register("blindness") { BlindnessEffect() }
    val PARALYZE: RegistryObject<ParalyzeEffect> = register("paralyze") { ParalyzeEffect() }
    val POISON: RegistryObject<PoisonEffect> = register("poison") { PoisonEffect() }
    val LUCK: RegistryObject<LuckEffect> = register("luck") { LuckEffect() }
    private fun <T : Effect> register(name: String, effectSupplier: Supplier<T>): RegistryObject<T> {
        return EFFECTS.register(name, effectSupplier)
    }
}