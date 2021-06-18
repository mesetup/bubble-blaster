package com.qtech.bubbles.common.ammo

import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.entity.Attribute
import com.qtech.bubbles.entity.AmmoEntity
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Line2D

class BasicAmmoType : AmmoType() {
    private val line: Line2D = Line2D.Double(0.0, 0.0, 5.0, 0.0)
    override fun getShape(ammoEntity: AmmoEntity): Shape {
        val area = Area(line)
        val transform = AffineTransform()
        transform.rotate(Math.toRadians(ammoEntity.rotation), area.bounds.centerX, area.bounds.centerY)
        area.transform(transform)
        return line
    }

    override fun render(g: GraphicsProcessor, ammoEntity: AmmoEntity) {
        g.shapeF(getShape(ammoEntity))
    }

    override val defaultAttributes: AttributeMap
        get() {
            val map = AttributeMap()
            map.set(Attribute.ATTACK, 1f)
            map.set(Attribute.DEFENSE, 4f)
            return map
        }
}