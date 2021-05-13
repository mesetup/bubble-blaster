package com.qsoftware.bubbles.entity;

import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.common.ammo.AmmoType;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.event.CollisionEvent;
import com.qsoftware.bubbles.init.EntityInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class AmmoEntity extends Entity {
    @Nullable
    private AmmoType ammoType;
    private double rotation;

    public AmmoEntity(Scene scene, AbstractGameType gameType) {
        this(null, scene, gameType);
    }

    public AmmoEntity(@Nullable AmmoType type, Scene scene, AbstractGameType gameType) {
        super(EntityInit.AMMO.get(), scene, gameType);

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
        return ammoType.getShape(this);
    }

    @Override
    public void renderEntity(Graphics2D gg) {
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
