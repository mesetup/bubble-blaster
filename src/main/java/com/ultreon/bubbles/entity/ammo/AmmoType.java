package com.ultreon.bubbles.entity.ammo;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.AmmoEntity;
import com.ultreon.hydro.common.RegistryEntry;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;

public abstract class AmmoType extends RegistryEntry {
    public abstract void render(Renderer g, AmmoEntity rotation);

    @SuppressWarnings("EmptyMethod")
    public void onCollision() {
        // Do nothing
    }

    public abstract AttributeMap getDefaultAttributes();

    public abstract Shape getShape(AmmoEntity ammoEntity);

    @Override
    public String toString() {
        return "AmmoType[" + getRegistryName() + "]";
    }
}
