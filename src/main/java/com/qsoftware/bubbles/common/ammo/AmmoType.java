package com.qsoftware.bubbles.common.ammo;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.RegistryEntry;
import com.qsoftware.bubbles.entity.AmmoEntity;
import com.qsoftware.bubbles.event.CollisionEvent;

import java.awt.*;

public abstract class AmmoType extends RegistryEntry {
    public abstract Graphics render(Graphics g, AmmoEntity rotation);

    public void onCollision(CollisionEvent e) {
        // Do nothing
    }

    public abstract AttributeMap getDefaultAttributes();

    public abstract Shape getShape(AmmoEntity ammoEntity);
}
