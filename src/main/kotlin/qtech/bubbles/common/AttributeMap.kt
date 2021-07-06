package qtech.bubbles.common

import qtech.bubbles.common.entity.Attribute
import qtech.bubbles.common.holders.IArrayDataHolder
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.ListTag

class AttributeMap : IArrayDataHolder<CompoundTag?, AttributeMap?> {
    private val map = HashMap<Attribute?, Float>()
    operator fun set(attribute: Attribute?, value: Float) {
        map[attribute] = value
    }

    operator fun get(attribute: Attribute): Float {
        return map[attribute] ?: throw NoSuchElementException("Attribute \"" + attribute.name + "\" has no set value.")
    }

    fun getModified(attribute: Attribute, function: (Float, Float) -> Float, attributeMaps: List<AttributeMap>) {
        if (!map.containsKey(attribute)) {
            throw NoSuchElementException("Attribute \"" + attribute.name + "\" has no set base value.")
        }
        var f = get(attribute)
        for (map in attributeMaps) {
            f *= function.invoke(f, map.get(attribute))
        }
    }

    fun getModified(attribute: Attribute, function: (Float, Float) -> Float, vararg attributeMaps: AttributeMap) {
        if (!map.containsKey(attribute)) {
            throw NoSuchElementException("Attribute \"" + attribute.name + "\" has no set base value.")
        }
        var f = get(attribute)
        for (map in attributeMaps) {
            f *= function.invoke(f, map.get(attribute))
        }
    }

    fun getModified(attribute: Attribute, functions: List<(Float) -> Float>) {
        if (!map.containsKey(attribute)) {
            throw NoSuchElementException("Attribute \"" + attribute.name + "\" has no set base value.")
        }
        var f = get(attribute)
        for (function in functions) {
            f *= function.invoke(f)
        }
    }

    fun getModified(attribute: Attribute, vararg functions: (Float) -> Float) {
        if (!map.containsKey(attribute)) {
            throw NoSuchElementException("Attribute \"" + attribute.name + "\" has no set base value.")
        }
        var f = get(attribute)
        for (function in functions) {
            f *= function.invoke(f)
        }
    }

    override fun write(listTag: ListTag<CompoundTag?>): ListTag<CompoundTag?> {
        for ((key, value) in map) {
            val document = CompoundTag()
            document.putString("name", key!!.name)
            document.putFloat("value", value)
            listTag.add(document)
        }
        return listTag
    }

    override fun read(listTag: ListTag<CompoundTag?>): AttributeMap {
        for (item in listTag) {
            val key = Attribute.fromName(item!!.getString("name"))
            val value = item.getDouble("value").toFloat()
            map[key] = value
        }
        return this
    }

    fun setAll(defaultAttributes: AttributeMap) {
        map.putAll(defaultAttributes.map)
    }
}