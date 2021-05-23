package com.qtech.bubbleblaster.common.ammo;

import com.qtech.bubbleblaster.common.AttributeMap;
import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.entity.AmmoEntity;


public abstract class AmmoType extends RegistryEntry {
    public abstract void render(Graphics g, AmmoEntity rotation);

    @SuppressWarnings("EmptyMethod")
    public void onCollision() {
        // Do nothing
    }

    public abstract AttributeMap getDefaultAttributes();

    public abstract Shape getShape(AmmoEntity ammoEntity);
}
