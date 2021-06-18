package com.qtech.bubbles.common.ability

import com.qtech.bubbles.entity.Entity

abstract class AbilityTrigger protected constructor(val type: AbilityTriggerType, val entity: Entity)