package com.qtech.bubbles.util

import net.querz.nbt.tag.CompoundTag

class NbtUtils private constructor() {
    companion object {
        fun generateTagCompound(compound: CompoundTag, tag: CompoundTag?): CompoundTag {
            compound.put("Tag", tag)
            return compound
        }

        fun getTagCompound(compound: CompoundTag): CompoundTag {
            return compound.getCompoundTag("Tag")
        }
    }

    init {
        throw ExceptionUtils.utilityClass()
    }
}