package com.qsoftware.bubbles.command;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.command.Command;
import com.qsoftware.bubbles.common.command.CommandExecutor;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.registry.Registry;
import com.qsoftware.utilities.python.builtins.ValueError;

public class EffectCommand implements CommandExecutor {
    @Override
    public boolean execute(Command command, PlayerEntity player, String[] args) {
        if (args.length == 1) {
            if (args[0].equals("clear")) {
                if (player.getActiveEffects().isEmpty()) {
                    player.sendMessage("No effect are active");
                    return true;
                }

                int amount = player.getActiveEffects().size();

                for (EffectInstance effectInstance : player.getActiveEffects()) {
                    player.removeEffect(effectInstance);
                }

                player.sendMessage("Removed " + amount + " effects.");

                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equals("give")) {
                EffectInstance effectInstance;
                try {
                    Effect<?> effect = Registry.getRegistry(Effect.class).get(ResourceLocation.fromString(args[1]));
                    if (effect == null) {
                        effectInstance = null;
                    } else {
                        effectInstance = new EffectInstance(effect, 15, (byte) 1);
                    }
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (effectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                player.addEffect(effectInstance);
                return true;
            }
        } else if (args.length == 3) {
            if (args[0].equals("give")) {
                int duration;
                try {
                    duration = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    player.sendMessage("Invalid number for duration: ‘" + args[2] + "’");
                    return false;
                }

                EffectInstance effectInstance;
                try {
                    Effect<?> effect = Registry.getRegistry(Effect.class).get(ResourceLocation.fromString(args[1]));
                    if (effect == null) {
                        effectInstance = null;
                    } else {
                        effectInstance = new EffectInstance(effect, duration, 1);
                    }
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (effectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                player.addEffect(effectInstance);
                return true;
            }
        } else if (args.length == 4) {
            if (args[0].equals("give")) {
                int duration;
                try {
                    duration = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    player.sendMessage("Invalid number for duration: ‘" + args[2] + "’");
                    return false;
                }

                int strength;
                try {
                    strength = Integer.parseInt(args[3]);
                } catch (NumberFormatException exception) {
                    player.sendMessage("Invalid number for strength: ‘" + args[3] + "’");
                    return false;
                }

                if (strength < 1) {
                    player.sendMessage("Strength is less than 1; range is 1 to 255!");
                    return false;
                }

                if (strength > 255) {
                    player.sendMessage("Strength is more than 255; range is 1 to 255!");
                    return false;
                }

                EffectInstance effectInstance;
                try {
                    Effect<?> effect = Registry.getRegistry(Effect.class).get(ResourceLocation.fromString(args[1]));
                    if (effect == null) {
                        effectInstance = null;
                    } else {
                        effectInstance = new EffectInstance(effect, duration, strength);
                    }
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (effectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                player.addEffect(effectInstance);
                return true;
            }
        }

        player.sendMessage("Usage: /effect clear");
        player.sendMessage("Usage: /effect give <effect:key> [duration:int] [strength:byte]");

        return false;
    }
}
