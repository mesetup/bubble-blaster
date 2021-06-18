package com.qtech.bubbles.common.holders

import net.querz.nbt.tag.ListTag
import net.querz.nbt.tag.Tag

interface IArrayDataHolder<T : Tag<*>?, R> {
    fun write(listTag: ListTag<T>): ListTag<T>
    fun read(listTag: ListTag<T>): R
}