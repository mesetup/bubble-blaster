package qtech.bubbles.command

import qtech.bubbles.common.command.CommandExecutor
import qtech.bubbles.entity.player.PlayerEntity
import qtech.utilities.python.OS
import qtech.utilities.python.Platform
import qtech.utilities.system.SystemEnum

open class ShutdownCommand : CommandExecutor {
    protected enum class Types {
        SHUTDOWN, HIBERNATE, SLEEP, REBOOT
    }

    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        var type: Types? = null
        var time: Int? = null
        for (arg in args) {
            if (arg == "-sh" || arg == "--shutdown") {
                if (type != null) {
                    player.sendMessage("Only one type is allowed")
                    return true
                }
                type = Types.SHUTDOWN
            } else if (arg == "-h" || arg == "--hibernate") {
                if (type != null) {
                    player.sendMessage("Only one type is allowed")
                    return true
                }
                type = Types.HIBERNATE
            } else if (arg == "-sl" || arg == "--sleep") {
                if (type != null) {
                    player.sendMessage("Only one type is allowed")
                    return true
                }
                type = Types.SLEEP
            } else if (arg == "-r" || arg == "--reboot" || arg == "--restart") {
                if (type != null) {
                    player.sendMessage("Only one type is allowed")
                    return true
                }
                type = Types.REBOOT
            } else if (arg.startsWith("-t=")) {
                if (time != null) {
                    player.sendMessage("Only one time is allowed.")
                    return true
                }
                val timeStr = arg.substring("-t=".length)
                time = try {
                    timeStr.toInt()
                } catch (exception: NumberFormatException) {
                    player.sendMessage("Invalid number for time: $timeStr")
                    return true
                }
            } else if (arg.startsWith("--time=")) {
                if (time != null) {
                    player.sendMessage("Only one time is allowed.")
                    return true
                }
                val timeStr = arg.substring("--time=".length)
                time = try {
                    timeStr.toInt()
                } catch (exception: NumberFormatException) {
                    player.sendMessage("Invalid number for time: $timeStr")
                    return true
                }
            }
        }
        if (type == null) {
            player.sendMessage("Type is not specified.")
            return false
        }
        if (time == null) {
            time = 0
        }
        val cmd: String
        if (Platform.system == SystemEnum.WINDOWS) {
            val typeStr: String
            typeStr = if (type == Types.SHUTDOWN) "/s" else if (type == Types.HIBERNATE) "/h" else if (type == Types.REBOOT) "/r" else {
                player.sendMessage("That type is not supported on your operating system.")
                return true
            }
            cmd = "shutdown $typeStr /t $time"
        } else if (Platform.system == SystemEnum.LINUX) {
            cmd = if (type == Types.SHUTDOWN) "shutdown $time" else if (type == Types.REBOOT) "reboot $time" else {
                player.sendMessage("That type is not supported on your operating system.")
                return true
            }
        } else {
            player.sendMessage("Your system is not supported.")
            return true
        }
        player.sendMessage(cmd)
        if (Platform.system == SystemEnum.WINDOWS) {
            OS.system(cmd)
        }
        return true
    }
}