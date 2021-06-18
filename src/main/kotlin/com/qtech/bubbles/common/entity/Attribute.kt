package com.qtech.bubbles.common.entity

class Attribute(name: String) {
    val name: String

    companion object {
        private val attributeMap = HashMap<String, Attribute>()
        @JvmField
        val SCORE_MULTIPLIER = Attribute("bubbleblaster.score_multiplier")
        @JvmField
        val MAX_DAMAGE = Attribute("bubbleblaster.max_health")
        @JvmField
        val DEFENSE = Attribute("bubbleblaster.defense")
        @JvmField
        val ATTACK = Attribute("bubbleblaster.attack")
        @JvmField
        val SPEED = Attribute("bubbleblaster.speed")
        @JvmField
        val LUCK = Attribute("bubbleblaster.luck")
        @JvmStatic
        fun fromName(name: String): Attribute? {
            return attributeMap[name]
        }
    }

    init {
        require(!attributeMap.containsKey(name)) { "Duplicate attribute detected!" }
        attributeMap[name] = this
        this.name = name
    }
}