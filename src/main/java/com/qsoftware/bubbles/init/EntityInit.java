package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.QInternalAddon;
import com.qsoftware.bubbles.common.init.ObjectInit;
import com.qsoftware.bubbles.entity.AmmoEntity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.entity.GiantBubbleEntity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.registry.DeferredRegister;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("ALL")
//@ObjectHolder(addonId = "qbubbles")
public class EntityInit implements ObjectInit<EntityType> {
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
