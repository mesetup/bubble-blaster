package qtech.bubbles.common.ability

import qtech.bubbles.entity.Entity

class AbilityContainer(val entity: Entity) {
    private val abilities = HashMap<AbilityType<out Ability<*>>?, Ability<*>>()
    var current: Ability<*>? = null
        private set

    fun add(ability: Ability<*>) {
        abilities[ability.type] = ability
    }

    fun remove(abilityType: AbilityType<*>?) {
        abilities.remove(abilityType)
    }

    fun setCurrent(abilityType: AbilityType<*>?) {
        current = abilities[abilityType]
    }

    fun onEntityTick() {
        if (current != null) {
            current!!.onEntityTick()
        }
    }

    operator fun get(type: AbilityType<*>?): Ability<*> {
        return abilities[type]!!
    }
}