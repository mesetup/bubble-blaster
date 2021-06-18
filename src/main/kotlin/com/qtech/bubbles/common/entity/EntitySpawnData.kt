package com.qtech.bubbles.common.entity

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.command.Command
import com.qtech.bubbles.environment.Environment
import net.querz.nbt.tag.CompoundTag
import java.awt.Point

open class EntitySpawnData private constructor(
    var reason: SpawnReason,
    state: CompoundTag?,
    pos: Point?,
    command: Command?,
    var environment: Environment?
) {
    var command = command
        set(value) {
            if (field != null) {
                field = value
            } else {
                throw NullPointerException("Command property was initialized with null.")
            }
        }
    var pos = pos
        set(value) {
            if (field != null) {
                field = value
            } else {
                throw NullPointerException("Command property was initialized with null.")
            }
        }
    var state = state
        set(value) {
            if (field != null) {
                field = value
            } else {
                throw NullPointerException("Command property was initialized with null.")
            }
        }

    enum class SpawnReason {
        LOAD, NATURAL, COMMAND
    }

    companion object {
        @JvmStatic
        fun fromLoadSpawn(state: CompoundTag?): EntitySpawnData {
            return EntitySpawnData(SpawnReason.LOAD, state, null, null, BubbleBlaster.instance.environment)
        }

        fun fromNaturalSpawn(pos: Point?): EntitySpawnData {
            return EntitySpawnData(SpawnReason.NATURAL, null, pos, null, BubbleBlaster.instance.environment)
        }

        fun fromNaturalSpawn(pos: Point?, environment: Environment?): EntitySpawnData {
            return EntitySpawnData(SpawnReason.NATURAL, null, pos, null, environment)
        }

        fun fromCommand(command: Command?): EntitySpawnData {
            return EntitySpawnData(SpawnReason.COMMAND, null, null, command, BubbleBlaster.instance.environment)
        }

        fun fromCommand(command: Command?, environment: Environment?): EntitySpawnData {
            return EntitySpawnData(SpawnReason.COMMAND, null, null, command, environment)
        }
    }
}