package com.qtech.bubbles.command

import com.qtech.bubbles.common.command.CommandExecutor
import com.qtech.bubbles.entity.player.PlayerEntity

open class LevelCommand : CommandExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        if (args.size == 2) {
            val value: Int
            when (args[0]) {
                "add" -> {
                    value = try {
                        args[1].toInt()
                    } catch (exception: NumberFormatException) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’")
                        return false
                    }
                    player.level = player.level + value
                    return true
                }
                "set" -> {
                    value = try {
                        args[1].toInt()
                    } catch (exception: NumberFormatException) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’")
                        return false
                    }
                    player.level = value
                    return true
                }
                "subtract" -> {
                    value = try {
                        args[1].toInt()
                    } catch (exception: NumberFormatException) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’")
                        return false
                    }
                    player.level = player.level - value
                    return true
                }
            }
        } else if (args.size == 1) {
            when (args[0]) {
                "up" -> {
                    player.levelUp()
                    return true
                }
                "down" -> {
                    player.levelDown()
                    return true
                }
            }
        }
        player.sendMessage("Usage: /level <up|down>")
        player.sendMessage("Usage: /level <add|set|subtract> <value>")
        return false
    }
}