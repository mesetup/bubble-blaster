package com.qtech.bubbles.media

import java.util.function.Predicate

class AudioPlayer(var maximal: Int) {
    private val slots: ArrayList<AudioSlot> = ArrayList()
    fun add(slot: AudioSlot) {
        if (slots.size >= maximal) return
        slots.add(slot)
        if (!slot.isPlaying) slot.play()
    }

    fun addIf(slot: AudioSlot, predicate: Predicate<AudioSlot?>) {
        if (slots.size >= maximal) return
        var flag = false
        for (slot1 in slots) {
            val flag1 = predicate.test(slot1)
            if (flag1) {
                flag = true
            }
        }
        if (!flag) return
        slots.add(slot)
        if (!slot.isPlaying) slot.play()
    }

    fun removeIf(filter: Predicate<AudioSlot>?): Boolean {
        return slots.removeIf(filter!!)
    }

    fun removeStopped(): Boolean {
        return removeIf { obj: AudioSlot -> obj.isStopped }
    }

    fun removePlaying(): Boolean {
        return removeIf { obj: AudioSlot -> obj.isPlaying }
    }

    fun remove(slot: AudioSlot): Boolean {
        return slots.remove(slot)
    }

    fun remove(index: Int): AudioSlot {
        return slots.removeAt(index)
    }

    operator fun get(index: Int): AudioSlot {
        return slots[index]
    }

    fun stopAll() {
        for (slot in slots) {
            slot.stop()
        }
    }

    fun playAll() {
        for (slot in slots) {
            slot.play()
        }
    }

    val isSilent: Boolean
        get() {
            for (slot in slots) {
                if (slot.isPlaying) return false
            }
            return true
        }

    fun clear() {
        for (slot in slots) {
            slot.stop()
        }
        slots.clear()
    }

    fun getSlots(): List<AudioSlot> {
        return slots
    }

}