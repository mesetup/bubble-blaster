package com.qtech.bubbles.common.entity.meta;

import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.common.entity.Modifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Deprecated
@SuppressWarnings("unchecked")
public class MetaData {
    private final Entity entity;
    private boolean invulnerable;

    private final HashMap<Modifier, Double> modifiers = new HashMap<>();
    private final HashMap<Attribute, Object> attributes = new HashMap<>();

    public MetaData(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public void setModifier(Modifier modifier, double value) {
        modifiers.put(modifier, value);
    }

    public double getModifier(Modifier modifier) {
        double out = 0.0d;
        Double val = modifiers.get(modifier);
        if (val != null) {
            out = val;
        }
        return out;
    }

    public <T> void setAttribute(Attribute attribute, T value) {
        attributes.put(attribute, value);
    }

    @Nullable
    public <T> T getAttribute(Attribute attribute) {
        return (T) attributes.get(attribute);
    }
}
