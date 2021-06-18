package com.qtech.bubbles.common.ammo

import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.RegistryEntry
import com.qtech.bubbles.entity.AmmoEntity
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Shape

abstract class AmmoType : RegistryEntry() {
    abstract fun render(g: GraphicsProcessor, ammoEntity: AmmoEntity)
    fun onCollision() {
        // Do nothing
    }

    abstract val defaultAttributes: AttributeMap
    abstract fun getShape(ammoEntity: AmmoEntity): Shape
}