package qtech.bubbles.common.ability

import qtech.bubbles.entity.Entity

abstract class AbilityTrigger protected constructor(val type: AbilityTriggerType, val entity: Entity)