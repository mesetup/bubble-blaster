package qtech.bubbles.common.command

import qtech.bubbles.entity.player.PlayerEntity

object CommandConstructor {
    private val commands = HashMap<String, CommandExecutor>()
    @JvmStatic
    fun add(name: String, handler: CommandExecutor) {
        commands[name] = handler
    }

    operator fun get(name: String): CommandExecutor? {
        return if (commands.containsKey(name)) {
            commands[name]
        } else null
    }

    @JvmStatic
    fun execute(name: String, player: PlayerEntity, args: Array<String>): Boolean {
        val handler = CommandConstructor[name] ?: return false
        try {
            handler.execute(player, args)
        } catch (throwable: Throwable) {
            player.sendMessage("Error occurred when executing the command.")
            player.sendMessage("  See the log for more information.")
            throwable.printStackTrace()
            return false
        }
        return true
    }
}