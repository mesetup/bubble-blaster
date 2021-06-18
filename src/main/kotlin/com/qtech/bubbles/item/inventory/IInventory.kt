package com.qtech.bubbles.item.inventory

import com.qtech.bubbles.item.ItemInstance

interface IInventory {
    fun size(): Int
    val itemInstances: Array<ItemInstance?>
    fun set(index: Int, itemInstance: ItemInstance?)
    fun clear()
    fun get(index: Int): ItemInstance?
    fun removeItem(index: Int)
    fun tick()
}