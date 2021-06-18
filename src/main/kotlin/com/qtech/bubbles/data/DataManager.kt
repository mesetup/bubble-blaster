package com.qtech.bubbles.data

import com.qtech.bubbles.entity.Entity
import net.querz.nbt.tag.CompoundTag
import java.awt.geom.Point2D

class DataManager {
    fun storeEntity(entity: Entity): CompoundTag {
        val nbt = CompoundTag()
        nbt.put("position", storePosition(entity.pos))
        nbt.put("data", entity.getState())
        return nbt
    }

    private fun storePosition(pos: Point2D): CompoundTag {
        val nbt = CompoundTag()
        nbt.putDouble("x", pos.x)
        nbt.putDouble("y", pos.y)
        return nbt
    }
}