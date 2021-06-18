package com.qtech.bubbles.command

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.bubble.AbstractBubble
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.command.CommandExecutor
import com.qtech.bubbles.entity.player.PlayerEntity
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.registry.Registry
import com.qtech.utilities.python.builtins.ValueError
import net.querz.nbt.io.SNBTUtil
import net.querz.nbt.tag.CompoundTag
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang3.ArrayUtils
import java.util.*

open class SpawnCommand : CommandExecutor {
    val environment: Environment? = null
    override fun execute(player: PlayerEntity, args: Array<String>): Boolean {
        /*if (args.length == 1) {
            if (args[0].equals("clear")) {
                if (player.getActiveBubbles().isEmpty()) {
                    player.sendMessage("No bubble are active");
                    return true;
                }

                int amount = player.getActiveBubbles().size();

                //noinspection unchecked
                for (Bubble<?> bubble : (HashSet<Bubble<?>>) player.getActiveBubbles().clone()) {
                    player.removeBubble(bubble);
                }

                player.sendMessage("Removed " + amount + " bubbles.");

                return true;
            }
        } else */
        if (args.size >= 2) {
            if (args[0] == "spawn") {
                val bubble: AbstractBubble? = try {
                    Registry.getRegistry(AbstractBubble::class.java)[ResourceLocation.fromString(args[1])]
                } catch (exception: ValueError) {
                    player.sendMessage("Invalid key: " + args[1])
                    return false
                }
                if (bubble == null) {
                    player.sendMessage("Bubble with key ‘" + args[1] + "’ was not found.")
                    return true
                }
                val jsonParts = ArrayUtils.subarray(args, 1, args.size)
                val json = StringUtils.join(jsonParts, ' ')
                try {
                    Objects.requireNonNull(BubbleBlaster.instance.environment)!!.spawnEntityFromState(SNBTUtil.fromSNBT(json) as CompoundTag)
                } catch (e: Exception) {
                    e.printStackTrace()
                    player.sendMessage("Couldn't spawn entity.")
                }
                return true
            }
        } /* else if (args.length == 3) {
            if (args[0].equals("spawn")) {
                int radius;
                try {
                    radius = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    player.sendMessage("Invalid number for duration: ‘" + args[2] + "’");
                    return false;
                }

                AbstractBubble bubble;
                try {
                    bubble = Registry.getRegistry(AbstractBubble.class).get(ResourceLocation.fromString(args[1]));
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (bubble == null) {
                    player.sendMessage("Bubble with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                try {
                    player.getGameType().spawnBubble(radius, null, bubble);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    player.sendMessage("Illegal Access: " + e.getLocalizedMessage());
                }
                return true;
            }
        } else if (args.length == 4) {
            if (args[0].equals("spawn")) {
                int radius;
                try {
                    radius = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    player.sendMessage("Invalid number for radius: ‘" + args[2] + "’");
                    return false;
                }

                double speed;
                try {
                    speed = Double.parseDouble(args[3]);
                } catch (NumberFormatException exception) {
                    try {
                        speed = Integer.parseInt(args[3]);
                    } catch (NumberFormatException exception1) {
                        player.sendMessage("Invalid number for strength: ‘" + args[3] + "’");
                        return false;
                    }
                }

                AbstractBubble bubble;
                try {
                    bubble = Registry.getRegistry(AbstractBubble.class).get(ResourceLocation.fromString(args[1]));
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (bubble == null) {
                    player.sendMessage("Bubble with key ‘" + args[1] + "’ was not found.");
                    return true;
                }

                try {
                    player.getGameType().spawnBubble(radius, speed, bubble);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    player.sendMessage("Illegal Access: " + e.getLocalizedMessage());
                }
                return true;
            }
        }*/

//        player.sendMessage("Usage: /bubble clear");
        player.sendMessage("Usage: /bubble spawn <bubble:key> [radius:int] [speed:double]")
        return false
    }
}