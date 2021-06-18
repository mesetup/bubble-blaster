package com.qtech.bubbles.common.interfaces

import net.querz.nbt.tag.CompoundTag

interface StateHolder {
    fun getState(): CompoundTag
    fun setState(state: CompoundTag)
}