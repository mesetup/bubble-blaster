package com.qtech.bubbles.init;

import com.qtech.bubbles.QInternalAddon;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.init.ObjectInit;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.entity.AmmoEntity;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.GiantBubbleEntity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;

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
