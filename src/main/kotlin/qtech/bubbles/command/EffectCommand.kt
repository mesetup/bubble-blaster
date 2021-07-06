package qtech.bubbles.command

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.command.CommandExecutor
import qtech.bubbles.common.effect.Effect
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.registry.Registry
import qtech.utilities.python.builtins.ValueError

open class EffectCommand : CommandExecutor {
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        if (args.size == 1) {
            if (args[0] == "clear") {
                if (player.activeEffects.isEmpty()) {
                    player.sendMessage("No effect are active")
                    return true
                }
                val amount = player.activeEffects.size
                for (effectInstance in player.activeEffects) {
                    player.removeEffect(effectInstance)
                }
                player.sendMessage("Removed $amount effects.")
                return true
            }
        } else if (args.size == 2) {
            if (args[0] == "give") {
                val effectInstance: EffectInstance? = try {
                    val effect = Registry.getRegistry(Effect::class.java)[ResourceLocation.fromString(args[1])]
                    if (effect == null) {
                        null
                    } else {
                        EffectInstance(effect, 15, 1)
                    }
                } catch (exception: ValueError) {
                    player.sendMessage("Invalid key: " + args[1])
                    return false
                }
                if (effectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.")
                    return true
                }
                player.addEffect(effectInstance)
                return true
            }
        } else if (args.size == 3) {
            if (args[0] == "give") {
                val duration: Int = try {
                    args[2].toInt()
                } catch (exception: NumberFormatException) {
                    player.sendMessage("Invalid number for duration: ‘" + args[2] + "’")
                    return false
                }
                val effectInstance: EffectInstance? = try {
                    val effect = Registry.getRegistry(Effect::class.java)[ResourceLocation.fromString(args[1])]
                    if (effect == null) {
                        null
                    } else {
                        EffectInstance(effect, duration.toLong(), 1)
                    }
                } catch (exception: ValueError) {
                    player.sendMessage("Invalid key: " + args[1])
                    return false
                }
                if (effectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.")
                    return true
                }
                player.addEffect(effectInstance)
                return true
            }
        } else if (args.size == 4) {
            if (args[0] == "give") {
                val duration: Int = try {
                    args[2].toInt()
                } catch (exception: NumberFormatException) {
                    player.sendMessage("Invalid number for duration: ‘" + args[2] + "’")
                    return false
                }
                val strength: Int = try {
                    args[3].toInt()
                } catch (exception: NumberFormatException) {
                    player.sendMessage("Invalid number for strength: ‘" + args[3] + "’")
                    return false
                }
                if (strength < 1) {
                    player.sendMessage("Strength is less than 1; range is 1 to 255!")
                    return false
                }
                if (strength > 255) {
                    player.sendMessage("Strength is more than 255; range is 1 to 255!")
                    return false
                }
                val effectInstance: EffectInstance? = try {
                    val effect = Registry.getRegistry(Effect::class.java)[ResourceLocation.fromString(args[1])]
                    if (effect == null) {
                        null
                    } else {
                        EffectInstance(effect, duration.toLong(), strength)
                    }
                } catch (exception: ValueError) {
                    player.sendMessage("Invalid key: " + args[1])
                    return false
                }
                if (effectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.")
                    return true
                }
                player.addEffect(effectInstance)
                return true
            }
        }
        player.sendMessage("Usage: /effect clear")
        player.sendMessage("Usage: /effect give <effect:key> [duration:int] [strength:byte]")
        return false
    }
}