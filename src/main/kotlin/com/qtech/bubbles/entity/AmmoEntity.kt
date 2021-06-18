package com.qtech.bubbles.entity

import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import com.qtech.bubbles.common.ammo.AmmoType
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.event.CollisionEvent
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.init.Entities
import java.awt.Rectangle
import java.awt.Shape
import javax.annotation.ParametersAreNonnullByDefault

@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
class AmmoEntity(ammoType: AmmoType?, gameMode: AbstractGameMode) : DamageableEntity(Entities.AMMO.get(), gameMode) {
    var ammoType = ammoType
        set(value) {
            field = value
            bases.setAll(ammoType!!.defaultAttributes)
            attributeMap.setAll(ammoType!!.defaultAttributes)
        }
    public override var rotation = 0.0

    constructor(gameMode: AbstractGameMode) : this(null, gameMode)

    override fun onCollision(event: CollisionEvent) {}
    override val shape: Shape
        get() = if (ammoType != null) ammoType!!.getShape(this) else Rectangle(0, 0, 0, 0)

    override fun renderEntity(gg: GraphicsProcessor?) {
        if (ammoType != null) {
            ammoType!!.render(gg!!, this)
        }
    }

    override fun delete() {}
    override fun bindEvents() {}
    override fun unbindEvents() {}
    override fun areEventsBound(): Boolean {
        return false
    }

    override fun tick(environment: Environment?) {}
    init {
        if (ammoType != null) {
            bases.setAll(ammoType.defaultAttributes)
            attributeMap.setAll(ammoType.defaultAttributes)
        }
    }
}