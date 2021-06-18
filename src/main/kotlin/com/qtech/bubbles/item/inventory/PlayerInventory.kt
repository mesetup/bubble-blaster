package com.qtech.bubbles.item.inventory

import com.qtech.bubbles.entity.player.PlayerEntity
import com.qtech.bubbles.item.ItemInstance
import java.util.*

class PlayerInventory(val player: PlayerEntity) : AbstractInventory() {
    val watchers: List<PlayerEntity> = ArrayList()
        get() = Collections.unmodifiableList(field)
    override var itemInstances = arrayOfNulls<ItemInstance>(size())
        private set

    fun openInventoryTo() {}
    override fun size(): Int {
        return 18
    }

    override fun set(index: Int, itemInstance: ItemInstance?) {
        itemInstances[index] = itemInstance
    }

    override fun clear() {
        itemInstances = arrayOfNulls(size())
    }

    override fun get(index: Int): ItemInstance? {
        return itemInstances[index]
    }

    override fun removeItem(index: Int) {
        itemInstances[index] = null
    }

    override fun tick() {
        for (item in itemInstances) {
            if (item != null) {
                item.onEntityTick(player)
                item.tick()
            }
        }
    }
}