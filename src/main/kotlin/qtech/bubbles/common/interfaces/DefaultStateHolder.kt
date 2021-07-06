package qtech.bubbles.common.interfaces

import net.querz.nbt.tag.CompoundTag

interface DefaultStateHolder {
    fun getDefaultState(): CompoundTag
}