package qtech.bubbles.common.entity.meta

import qtech.bubbles.common.entity.Attribute
import qtech.bubbles.entity.Entity
import qtech.bubbles.common.entity.Modifier

@Deprecated("")
class MetaData(val entity: Entity) {
    var isInvulnerable = false
    private val modifiers = HashMap<Modifier, Double>()
    private val attributes = HashMap<Attribute, Any>()
    fun setModifier(modifier: Modifier, value: Double) {
        modifiers[modifier] = value
    }

    fun getModifier(modifier: Modifier): Double {
        var out = 0.0
        val `val` = modifiers[modifier]
        if (`val` != null) {
            out = `val`
        }
        return out
    }

    fun <T> setAttribute(attribute: Attribute, value: T) {
        attributes[attribute] = value!!
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getAttribute(attribute: Attribute): T? {
        return attributes[attribute] as T?
    }
}