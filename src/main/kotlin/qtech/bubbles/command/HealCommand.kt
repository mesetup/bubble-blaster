package qtech.bubbles.command

import qtech.bubbles.common.command.CommandExecutor
import qtech.bubbles.entity.player.PlayerEntity

open class HealCommand : CommandExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        player.damageValue = player.maxDamageValue
        return true
    }
}