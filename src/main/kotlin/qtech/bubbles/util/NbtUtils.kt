package qtech.bubbles.util

import net.querz.nbt.tag.CompoundTag

object NbtUtils {
    fun generateTagCompound(compound: CompoundTag, tag: CompoundTag?): CompoundTag {
        compound.put("Tag", tag)
        return compound
    }

    fun getTagCompound(compound: CompoundTag): CompoundTag {
        return compound.getCompoundTag("Tag")
    }
}