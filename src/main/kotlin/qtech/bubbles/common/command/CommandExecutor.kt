package qtech.bubbles.common.command

import qtech.bubbles.entity.player.PlayerEntity

interface CommandExecutor {
    fun execute(player: PlayerEntity, args: Array<String>): Boolean
}