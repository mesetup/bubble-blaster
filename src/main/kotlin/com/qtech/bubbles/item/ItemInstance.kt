package com.qtech.bubbles.item

import com.qtech.bubbles.common.ResourceLocation.Companion.fromString
import com.qtech.bubbles.common.TagHolder
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.interfaces.StateHolder
import com.qtech.bubbles.registry.Registers
import net.querz.nbt.tag.CompoundTag

class ItemInstance : IItemProvider, StateHolder, TagHolder {
    override var item: Item? = null
    override var tag: CompoundTag? = null
        private set

    override fun getState(): CompoundTag {
        val document = CompoundTag()
        document.putString("type", item!!.registryName.toString())
        document.put("Tag", tag)
        return document
    }

    override fun setState(state: CompoundTag) {
        item = Registers.ITEMS[fromString(state.getString("Type"))]
        tag = state.getCompoundTag("Tag")
    }

    @Suppress("UNUSED_PARAMETER")
    fun onEntityTick(entity: Entity?) {
        item!!.onEntityTick()
    }

    fun tick() {
        item!!.tick()
    }
}