package qtech.bubbles.common.ammo

import qtech.bubbles.common.AttributeMap
import qtech.bubbles.common.RegistryEntry
import qtech.bubbles.entity.AmmoEntity
import qtech.hydro.GraphicsProcessor
import java.awt.Shape

abstract class AmmoType : RegistryEntry() {
    abstract fun render(g: GraphicsProcessor, ammoEntity: AmmoEntity)
    fun onCollision() {
        // Do nothing
    }

    abstract val defaultAttributes: AttributeMap
    abstract fun getShape(ammoEntity: AmmoEntity): Shape
}