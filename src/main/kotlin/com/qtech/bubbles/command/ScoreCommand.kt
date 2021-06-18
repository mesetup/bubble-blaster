package com.qtech.bubbles.command

import com.qtech.bubbles.common.command.TabExecutor
import com.qtech.bubbles.common.command.tabcomplete.TabCompleter
import com.qtech.bubbles.entity.player.PlayerEntity

open class ScoreCommand : TabExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        if (args.size == 2) {
            val value: Long
            when (args[0]) {
                "add" -> {
                    value = try {
                        args[1].toLong()
                    } catch (exception: NumberFormatException) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’")
                        return false
                    }
                    player.addScore(value.toDouble())
                    return true
                }
                "set" -> {
                    value = try {
                        args[1].toLong()
                    } catch (exception: NumberFormatException) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’")
                        return false
                    }
                    player.score = value.toDouble()
                    return true
                }
                "subtract" -> {
                    value = try {
                        args[1].toLong()
                    } catch (exception: NumberFormatException) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’")
                        return false
                    }
                    player.subtractScore(value)
                    return true
                }
            }
        }
        player.sendMessage("Usage: /score <add|set|subtract> <value>")
        return false
    }

    override fun tabComplete(args: Array<String>): List<String>? {
        if (args.size == 1) {
            return TabCompleter.getStrings(args[0], "add", "set", "subtract")
        } else if (args.size == 2) {
            return TabCompleter.getInts(args[1])
        }
        return ArrayList()
    }
}