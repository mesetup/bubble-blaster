@file:Suppress("unused")

package com.qtech.bubbles.ability.triggers

import com.qtech.bubbles.common.ability.AbilityTrigger
import com.qtech.bubbles.common.ability.AbilityTriggerType
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.core.controllers.KeyboardController
import com.qtech.bubbles.event.KeyboardEvent
import com.qtech.bubbles.event.type.KeyEventType

class AbilityKeyTrigger(evt: KeyboardEvent, entity: Entity?) : AbilityTrigger(AbilityTriggerType.KEY_TRIGGER, entity!!) {
    val controller: KeyboardController = evt.controller
    val keyCode: Int = evt.keyCode
    val currentlyPressed: HashMap<Int, Boolean> = evt.pressed
    val keyEventType: KeyEventType = evt.type

}