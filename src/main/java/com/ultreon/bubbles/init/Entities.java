package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalMod;
import com.ultreon.bubbles.entity.AmmoEntity;
import com.ultreon.bubbles.entity.BubbleEntity;
import com.ultreon.bubbles.entity.GiantBubbleEntity;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.bubbles.entity.types.EntityType;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.hydro.registry.ObjectInit;
import com.ultreon.hydro.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("ALL")
//@ObjectHolder(modId = "bubbleblaster")
public class Entities implements ObjectInit<EntityType> {
    public static final DeferredRegister<EntityType> ENTITIES = DeferredRegister.create(InternalMod.MOD_ID, Registers.ENTITIES);

    public static final RegistryObject<EntityType<AmmoEntity>> AMMO = register("bubble", () -> new EntityType<>(AmmoEntity::new));
    public static final RegistryObject<EntityType<BubbleEntity>> BUBBLE = register("bubble", () -> new EntityType<>(BubbleEntity::new));
    public static final RegistryObject<EntityType<GiantBubbleEntity>> GIANT_BUBBLE = register("giant_bubble", () -> new EntityType<>(GiantBubbleEntity::new));
    public static final RegistryObject<EntityType<PlayerEntity>> PLAYER = register("entity", () -> new EntityType<>(PlayerEntity::new));

    @SuppressWarnings("SameParameterValue")
    private static <T extends EntityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ENTITIES.register(name, supplier);
    }
}
