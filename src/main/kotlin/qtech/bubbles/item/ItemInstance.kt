package qtech.bubbles.item

import qtech.bubbles.common.ResourceLocation.Companion.fromString
import qtech.bubbles.common.TagHolder
import qtech.bubbles.entity.Entity
import qtech.bubbles.common.interfaces.StateHolder
import qtech.bubbles.registry.Registers
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