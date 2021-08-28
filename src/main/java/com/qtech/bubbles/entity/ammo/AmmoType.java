package com.qtech.bubbles.entity.ammo;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.entity.AmmoEntity;
import com.qtech.bubbles.registry.RegistryEntry;

import java.awt.*;

public abstract class AmmoType extends RegistryEntry {
    public abstract void render(Graphics g, AmmoEntity rotation);

    @SuppressWarnings("EmptyMethod")
    public void onCollision() {
        // Do nothing
    }

    public abstract AttributeMap getDefaultAttributes();

    public abstract Shape getShape(AmmoEntity ammoEntity);
}
