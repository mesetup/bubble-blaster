package com.ultreon.bubbles.entity;

import com.ultreon.commons.annotation.FieldsAreNonnullByDefault;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.entity.ammo.AmmoType;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.event.EntityCollisionEvent;
import com.ultreon.bubbles.init.Entities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import com.ultreon.hydro.render.Renderer;

@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class AmmoEntity extends Entity {
    @Nullable
    private AmmoType ammoType;
    private double rotation;

    public AmmoEntity(AbstractGameType gameType) {
        this(null, gameType);
    }

    public AmmoEntity(@Nullable AmmoType type, AbstractGameType gameType) {
        super(Entities.AMMO.get(), gameType);

        this.ammoType = type;
        if (type != null) {
            this.bases.setAll(type.getDefaultAttributes());
            this.attributes.setAll(type.getDefaultAttributes());
        }
    }

    @Override
    public void onCollision(EntityCollisionEvent evtObj) {

    }

    @Override
    public Shape getShape() {
        return ammoType != null ? ammoType.getShape(this) : new Rectangle(0, 0, 0, 0);
    }

    @Override
    public void renderEntity(Renderer gg) {
        if (this.ammoType != null) {
            this.ammoType.render(gg, this);
        }
    }

    @Override
    public void delete() {

    }

    @Override
    protected void bindEvents() {

    }

    @Override
    protected void unbindEvents() {

    }

    @Override
    protected boolean areEventsBound() {
        return false;
    }

    @Override
    public void tick(Environment environment) {

    }

    @SuppressWarnings("unused")
    @Nullable
    public AmmoType getAmmoType() {
        return ammoType;
    }

    @SuppressWarnings("unused")
    public void setAmmoType(@NotNull AmmoType ammoType) {
        this.ammoType = ammoType;
        this.bases.setAll(ammoType.getDefaultAttributes());
        this.attributes.setAll(ammoType.getDefaultAttributes());
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
