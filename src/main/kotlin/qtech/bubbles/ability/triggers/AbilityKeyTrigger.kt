@file:Suppress("unused")

package qtech.bubbles.ability.triggers

import qtech.bubbles.common.ability.AbilityTrigger
import qtech.bubbles.common.ability.AbilityTriggerType
import qtech.bubbles.entity.Entity
import qtech.bubbles.core.controllers.KeyboardController
import qtech.bubbles.event.KeyboardEvent
import qtech.bubbles.event.type.KeyEventType

class AbilityKeyTrigger(evt: KeyboardEvent, entity: Entity?) : AbilityTrigger(AbilityTriggerType.KEY_TRIGGER, entity!!) {
    val controller: KeyboardController = evt.controller
    val keyCode: Int = evt.keyCode
    val currentlyPressed: HashMap<Int, Boolean> = evt.pressed
    val keyEventType: KeyEventType = evt.type

}