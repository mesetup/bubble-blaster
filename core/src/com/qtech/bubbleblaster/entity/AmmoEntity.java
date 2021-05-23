package com.qtech.bubbleblaster.entity;

import com.qtech.bubbleblaster.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbleblaster.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbleblaster.common.ammo.AmmoType;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.environment.Environment;
import com.qtech.bubbleblaster.event.CollisionEvent;
import com.qtech.bubbleblaster.init.Entities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

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
    public void onCollision(CollisionEvent evtObj) {

    }

    @Override
    public Shape getShape() {
        return ammoType != null ? ammoType.getShape(this) : new Rectangle(0, 0, 0, 0);
    }

    @Override
    public void renderEntity(GraphicsProcessor gg) {
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

    @Nullable
    public AmmoType getAmmoType() {
        return ammoType;
    }

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
