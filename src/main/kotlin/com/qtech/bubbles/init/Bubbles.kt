@file:Suppress("unused")

package com.qtech.bubbles.init

import com.qtech.bubbles.BBInternalAddon
import com.qtech.bubbles.bubble.*
import com.qtech.bubbles.bubble.AbstractBubble.Companion.builder
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.init.ObjectInit
import com.qtech.bubbles.core.utils.categories.ColorUtils.parseColorString
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.registry.DeferredRegister
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.registry.`object`.RegistryObject
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color
import java.util.function.Supplier

/**
 * <h1>Bubble Initialization</h1>
 * Bubble init, used for initialize bubbles.
 * For example, the [DefenseBoostBubble] instance is assigned here.
 *
 * @see AbstractBubble
 */
//@ObjectHolder(addonId = "qbubbles")
object Bubbles : ObjectInit<AbstractBubble?> {
    val BUBBLES: DeferredRegister<AbstractBubble> = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.BUBBLES)

    // Bubbles
    val NORMAL_BUBBLE: RegistryObject<AbstractBubble> = register("normal") {
        builder()
            .priority(150000000L)
            .radius(IntRange(12, 105))
            .speed(DoubleRange(4, 8.7))
            .colors(Color.white)
            .score(1f)
            .build()
    }
    val DOUBLE_BUBBLE: RegistryObject<AbstractBubble> = register("double") {
        builder()
            .priority(4600000L)
            .radius(IntRange(24, 75))
            .speed(DoubleRange(8, 17.4))
            .colors(Color.orange, Color.orange)
            .score(2f)
            .build()
    }
    val TRIPLE_BUBBLE: RegistryObject<AbstractBubble> = register("triple") {
        builder()
            .priority(1150000L)
            .radius(IntRange(48, 60))
            .speed(DoubleRange(12, 38.8))
            .colors(Color.cyan, Color.cyan, Color.cyan)
            .score(3f)
            .build()
    }
    val BOUNCE_BUBBLE: RegistryObject<AbstractBubble> = register("bounce") {
        builder()
            .priority(715000L)
            .radius(IntRange(15, 85))
            .speed(DoubleRange(3.215, 4.845))
            .score(0.625f)
            .colors(*parseColorString("#ff0000,#ff3f00,#ff7f00,#ffbf00")) // Color.decode("#ff0000"), Color.decode("#ff3f00"), Color.decode("#ff7f00"), Color.decode("#ffaf00"))
            .bounceAmount(5f)
            .build()
    }
    val BUBBLE_FREEZE_BUBBLE: RegistryObject<AbstractBubble> = register("bubble_freeze") {
        builder()
            .priority(52750L)
            .radius(IntRange(17, 58))
            .speed(DoubleRange(4.115, 6.845))
            .score(1.3125f)
            .effect { source: BubbleEntity, _: Entity -> EffectInstance(Effects.BUBBLE_FREEZE.get(), (source.radius / 8).toLong(), ((source.speed * 4).toInt())) }
            .colors(*parseColorString("#ff0000,#ff7f00,#ffff00,#ffff7f,#ffffff", false))
            .build()
    }
    val PARALYZE_BUBBLE: RegistryObject<AbstractBubble> = register("paralyze") {
        builder()
            .priority(1825000L)
            .radius(IntRange(28, 87))
            .speed(DoubleRange(1.215, 2.845))
            .score(0.325f)
            .effect { source: BubbleEntity, _: Entity -> EffectInstance(Effects.PARALYZE.get(), (source.radius / 16).toLong(), 1) }
            .colors(*parseColorString("#ffff00,#ffff5f,#ffffdf,#ffffff"))
            .build()
    }

    //    public static final BubbleType DAMAGE_BUBBLE = new BubbleType.Builder().priority(8850000L).radius(new IntRange(15, 85)).speed(new DoubleRange(3.215d, 4.845d)).colors(Color.red, new Color(255, 63, 0), Color.red).attackMod(1d).build();
    val DAMAGE_BUBBLE: RegistryObject<DamageBubble> = register("damage") { DamageBubble() }
    val POISON_BUBBLE: RegistryObject<AbstractBubble> = register("poison") {
        builder()
            .priority(1313131L)
            .radius(IntRange(34, 83))
            .speed(DoubleRange(1.0, 2.7))
            .defense(0.225f)
            .attack(0.0f)
            .score(0.375f)
            .hardness(1.0)
            .colors(*parseColorString("#7fff00,#9faf1f,#bf7f3f,#df3f5f,#ff007f")) // new Color[]{new Color(128, 255, 0), new Color(160, 192, 32), new Color(192, 128, 64), new Color(224, 64, 96), new Color(255, 0, 128)})
            .effect { source: BubbleEntity, _: Entity -> EffectInstance(Effects.POISON.get(), (source.radius / 8).toLong(), 4) }
            .build()
    }
    val HEAL_BUBBLE: RegistryObject<HealBubble> = register("heal") { HealBubble() }
    val HYPER_BUBBLE: RegistryObject<UltraBubble> = register("ultra") { UltraBubble() }
    val LEVEL_UP_BUBBLE: RegistryObject<LevelUpBubble> = register("level_up") { LevelUpBubble() }
    val HARDENED_BUBBLE: RegistryObject<HardenedBubble> = register("hardened") { HardenedBubble() }
    val BLINDNESS_BUBBLE: RegistryObject<BlindnessBubble> = register("blindness") { BlindnessBubble() }
    val ACCELERATE_BUBBLE: RegistryObject<AccelerateBubble> = register("accelerate") { AccelerateBubble() }
    val SPEED_BOOST_BUBBLE: RegistryObject<SpeedBoostBubble> = register("speed_boost") { SpeedBoostBubble() }
    val DOUBLE_STATE_BUBBLE: RegistryObject<DoubleStateBubble> = register("double") { DoubleStateBubble() }
    val TRIPLE_STATE_BUBBLE: RegistryObject<TripleStateBubble> = register("triple") { TripleStateBubble() }
    val ATTACK_BOOST_BUBBLE: RegistryObject<AttackBoostBubble> = register("attack") { AttackBoostBubble() }
    val DEFENSE_BOOST_BUBBLE: RegistryObject<DefenseBoostBubble> = register("defense") { DefenseBoostBubble() }

    //    public static void registerBubbles() {
    //        for (Bubble bubble : BUBBLES) {
    //            Registry.getRegistry(Bubble.class).register(bubble.getRegistryName(), bubble);
    //        }
    //    }
    fun <T : AbstractBubble> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return BUBBLES.register(name, supplier)
    }
}