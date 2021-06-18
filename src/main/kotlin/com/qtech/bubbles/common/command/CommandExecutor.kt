package com.qtech.bubbles.common.command

import com.qtech.bubbles.entity.player.PlayerEntity

interface CommandExecutor {
    fun execute(player: PlayerEntity, args: Array<String>): Boolean
}