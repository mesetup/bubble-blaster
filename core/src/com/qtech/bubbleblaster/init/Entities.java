package com.qtech.bubbleblaster.init;

import com.qtech.bubbleblaster.QInternalAddon;
import com.qtech.bubbleblaster.common.init.ObjectInit;
import com.qtech.bubbleblaster.entity.AmmoEntity;
import com.qtech.bubbleblaster.entity.BubbleEntity;
import com.qtech.bubbleblaster.entity.GiantBubbleEntity;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import com.qtech.bubbleblaster.entity.types.EntityType;
import com.qtech.bubbleblaster.registry.DeferredRegister;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("ALL")
//@ObjectHolder(addonId = "qbubbles")
public class Entities implements ObjectInit<EntityType> {
    public static final DeferredRegister<EntityType> ENTITIES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.ENTITIES);

    public static final RegistryObject<EntityType<AmmoEntity>> AMMO = register("bubble", () -> new EntityType<>(AmmoEntity::new));
    public static final RegistryObject<EntityType<BubbleEntity>> BUBBLE = register("bubble", () -> new EntityType<>(BubbleEntity::new));
    public static final RegistryObject<EntityType<GiantBubbleEntity>> GIANT_BUBBLE = register("giant_bubble", () -> new EntityType<>(GiantBubbleEntity::new));
    public static final RegistryObject<EntityType<PlayerEntity>> PLAYER = register("entity", () -> new EntityType<>(PlayerEntity::new));

    @SuppressWarnings("SameParameterValue")
    private static <T extends EntityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ENTITIES.register(name, supplier);
    }
}
