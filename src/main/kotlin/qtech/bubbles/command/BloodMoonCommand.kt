package qtech.bubbles.command

import qtech.bubbles.common.command.CommandExecutor
import qtech.bubbles.entity.player.PlayerEntity

open class BloodMoonCommand : CommandExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        player.gameMode.triggerBloodMoon()
        return true
    }
}