package qtech.bubbles.item

import qtech.bubbles.common.RegistryEntry

abstract class Item : RegistryEntry(), IItemProvider {
    override val item: Item?
        get() = this

    fun onEntityTick() {}
    fun tick() {}
}