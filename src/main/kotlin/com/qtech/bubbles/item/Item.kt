package com.qtech.bubbles.item

import com.qtech.bubbles.common.RegistryEntry

abstract class Item : RegistryEntry(), IItemProvider {
    override val item: Item?
        get() = this

    fun onEntityTick() {}
    fun tick() {}
}