package com.qtech.bubbles.command

import com.qtech.bubbles.common.command.CommandExecutor
import com.qtech.bubbles.entity.player.PlayerEntity

open class TeleportCommand : CommandExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        if (args.size == 2) {
            var x: Double
            var y: Double
            try {
                x = args[0].toDouble()
                y = args[1].toDouble()
            } catch (exception: NumberFormatException) {
                x = try {
                    args[0].toLong().toDouble()
                } catch (exception1: NumberFormatException) {
                    player.sendMessage("Invalid number for x!")
                    return false
                }
                y = try {
                    args[1].toLong().toDouble()
                } catch (exception1: NumberFormatException) {
                    player.sendMessage("Invalid number for y!")
                    return false
                }
            }
            player.teleport(x, y)
            return true
        }
        return false
    }
}