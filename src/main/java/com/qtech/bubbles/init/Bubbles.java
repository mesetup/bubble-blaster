package com.qtech.bubbles.init;

import com.qtech.bubbles.QInternalAddon;
import com.qtech.bubbles.bubble.*;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.init.ObjectInit;
import com.qtech.bubbles.core.utils.categories.ColorUtils;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;
import java.util.function.Supplier;

/**
 * <h1>Bubble Initialization</h1>
 * Bubble init, used for initialize bubbles.
 * For example, the {@link DefenseBoostBubble} instance is assigned here.
 *
 * @see AbstractBubble
 */
@SuppressWarnings("unused")
//@ObjectHolder(addonId = "qbubbles")
public class Bubbles implements ObjectInit<AbstractBubble> {
    public static final DeferredRegister<AbstractBubble> BUBBLES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.BUBBLES);

    // Bubbles
    public static final RegistryObject<AbstractBubble> NORMAL_BUBBLE = register("normal", () -> AbstractBubble.builder()
            .priority(150_000_000L)
            .radius(new IntRange(12, 105))
            .speed(new DoubleRange(0.875d, 2.200d))
            .colors(Color.white)
            .score(1f)
            .build());
    public static final RegistryObject<AbstractBubble> DOUBLE_BUBBLE = register("double", () -> AbstractBubble.builder()
            .priority(4_600_000L)
            .radius(new IntRange(24, 75))
            .speed(new DoubleRange(1.000d, 2.700d))
            .colors(Color.orange, Color.orange)
            .score(2f)
            .build());
    public static final RegistryObject<AbstractBubble> TRIPLE_BUBBLE = register("triple", () -> AbstractBubble.builder()
            .priority(1_150_000L)
            .radius(new IntRange(48, 60))
            .speed(new DoubleRange(2.000d, 3.825d))
            .colors(Color.cyan, Color.cyan, Color.cyan)
            .score(3f)
            .build());
    public static final RegistryObject<AbstractBubble> BOUNCE_BUBBLE = register("bounce", () -> AbstractBubble.builder()
            .priority(715_000L)
            .radius(new IntRange(15, 85))
            .speed(new DoubleRange(3.215d, 4.845d))
            .score(0.625f)
            .colors(ColorUtils.parseColorString("#ff0000,#ff3f00,#ff7f00,#ffbf00")) // Color.decode("#ff0000"), Color.decode("#ff3f00"), Color.decode("#ff7f00"), Color.decode("#ffaf00"))
            .bounceAmount(5f)
            .build());
    public static final RegistryObject<AbstractBubble> BUBBLE_FREEZE_BUBBLE = register("bubble_freeze", () -> AbstractBubble.builder()
            .priority(52_750L)
            .radius(new IntRange(17, 58))
            .speed(new DoubleRange(4.115d, 6.845d))
            .score(1.3125f)
            .effect((source, target) -> (new EffectInstance(Effects.BUBBLE_FREEZE.get(), source.getRadius() / 8, (byte) ((byte) source.getSpeed() * 4))))
            .colors(ColorUtils.parseColorString("#ff0000,#ff7f00,#ffff00,#ffffff")) // Color.decode("#ff0000"), Color.decode("#ff3f00"), Color.decode("#ff7f00"), Color.decode("#ffaf00"))
            .build());
    public static final RegistryObject<AbstractBubble> PARALYZE_BUBBLE = register("paralyze", () -> AbstractBubble.builder()
            .priority(3_325_000L)
            .radius(new IntRange(28, 87))
            .speed(new DoubleRange(1.215d, 2.845d))
            .score(0.325f)
            .effect((source, target) -> (new EffectInstance(Effects.PARALYZE.get(), source.getRadius() / 16, (byte) 1)))
            .colors(ColorUtils.parseColorString("#ffff00,#ffff00,#ffff7f,#ffffff")) // Color.decode("#ff0000"), Color.decode("#ff3f00"), Color.decode("#ff7f00"), Color.decode("#ffaf00"))
            .build());
    //    public static final BubbleType DAMAGE_BUBBLE = new BubbleType.Builder().priority(8850000L).radius(new IntRange(15, 85)).speed(new DoubleRange(3.215d, 4.845d)).colors(Color.red, new Color(255, 63, 0), Color.red).attackMod(1d).build();
    public static final RegistryObject<DamageBubble> DAMAGE_BUBBLE = register("damage", DamageBubble::new);

    public static final RegistryObject<AbstractBubble> POISON_BUBBLE = register("poison", () -> AbstractBubble.builder()
            .priority(1_313_131L)
            .radius(new IntRange(34, 83))
            .speed(new DoubleRange(1.0d, 2.7d))
            .defense(0.225f)
            .attack(0.0f)
            .score(0.375f)
            .hardness(1.0d)
            .colors(ColorUtils.parseColorString("#7fff00,#9faf1f,#bf7f3f,#df3f5f,#ff007f")) // new Color[]{new Color(128, 255, 0), new Color(160, 192, 32), new Color(192, 128, 64), new Color(224, 64, 96), new Color(255, 0, 128)})
            .effect((source, target) -> (new EffectInstance(Effects.POISON.get(), source.getRadius() / 8, (byte) ((byte) source.getSpeed() * 4))))
            .build());

    public static final RegistryObject<HealBubble> HEAL_BUBBLE = register("heal", HealBubble::new);
    public static final RegistryObject<UltraBubble> HYPER_BUBBLE = register("ultra", UltraBubble::new);
    public static final RegistryObject<LevelUpBubble> LEVEL_UP_BUBBLE = register("level_up", LevelUpBubble::new);
    public static final RegistryObject<HardenedBubble> HARDENED_BUBBLE = register("hardened", HardenedBubble::new);
    public static final RegistryObject<BlindnessBubble> BLINDNESS_BUBBLE = register("blindness", BlindnessBubble::new);
    public static final RegistryObject<AccelerateBubble> ACCELERATE_BUBBLE = register("accelerate", AccelerateBubble::new);
    public static final RegistryObject<SpeedBoostBubble> SPEED_BOOST_BUBBLE = register("speed_boost", SpeedBoostBubble::new);
    public static final RegistryObject<DoubleStateBubble> DOUBLE_STATE_BUBBLE = register("double", DoubleStateBubble::new);
    public static final RegistryObject<TripleStateBubble> TRIPLE_STATE_BUBBLE = register("triple", TripleStateBubble::new);
    public static final RegistryObject<AttackBoostBubble> ATTACK_BOOST_BUBBLE = register("attack", AttackBoostBubble::new);
    public static final RegistryObject<DefenseBoostBubble> DEFENSE_BOOST_BUBBLE = register("defense", DefenseBoostBubble::new);

//    public static void registerBubbles() {
//        for (Bubble bubble : BUBBLES) {
//            Registry.getRegistry(Bubble.class).register(bubble.getRegistryName(), bubble);
//        }
//    }

    public static <T extends AbstractBubble> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return BUBBLES.register(name, supplier);
    }

}
