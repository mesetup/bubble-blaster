package com.qtech.bubbles.command

import com.qtech.bubbles.common.command.CommandExecutor
import com.qtech.bubbles.entity.player.PlayerEntity

open class BloodMoonCommand : CommandExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        player.gameMode.triggerBloodMoon()
        return true
    }
}