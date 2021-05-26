package com.qtech.bubbles.common.ammo;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.RegistryEntry;
import com.qtech.bubbles.entity.AmmoEntity;

import java.awt.*;

public abstract class AmmoType extends RegistryEntry {
    public abstract void render(GraphicsProcessor g, AmmoEntity rotation);

    @SuppressWarnings("EmptyMethod")
    public void onCollision() {
        // Do nothing
    }

    public abstract AttributeMap getDefaultAttributes();

    public abstract Shape getShape(AmmoEntity ammoEntity);
}
