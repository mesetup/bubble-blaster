package com.qtech.bubbles.command;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.registry.Registry;
import com.qtech.utilities.python.builtins.ValueError;

public class EffectCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        if (args.length == 1) {
            if (args[0].equals("clear")) {
                if (player.getActiveEffects().isEmpty()) {
                    player.sendMessage("No effect are active");
                    return true;
                }

                int amount = player.getActiveEffects().size();

                for (StatusEffectInstance statusEffectInstance : player.getActiveEffects()) {
                    player.removeEffect(statusEffectInstance);
                }

                player.sendMessage("Removed " + amount + " effects.");

                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equals("give")) {
                StatusEffectInstance statusEffectInstance;
                try {
                    StatusEffect statusEffect = Registry.getRegistry(StatusEffect.class).get(ResourceEntry.fromString(args[1]));
                    if (statusEffect == null) {
                        statusEffectInstance = null;
                    } else {
                        statusEffectInstance = new StatusEffectInstance(statusEffect, 15, (byte) 1);
                    }
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (statusEffectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                player.addEffect(statusEffectInstance);
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

                StatusEffectInstance statusEffectInstance;
                try {
                    StatusEffect statusEffect = Registry.getRegistry(StatusEffect.class).get(ResourceEntry.fromString(args[1]));
                    if (statusEffect == null) {
                        statusEffectInstance = null;
                    } else {
                        statusEffectInstance = new StatusEffectInstance(statusEffect, duration, 1);
                    }
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (statusEffectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                player.addEffect(statusEffectInstance);
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

                StatusEffectInstance statusEffectInstance;
                try {
                    StatusEffect statusEffect = Registry.getRegistry(StatusEffect.class).get(ResourceEntry.fromString(args[1]));
                    if (statusEffect == null) {
                        statusEffectInstance = null;
                    } else {
                        statusEffectInstance = new StatusEffectInstance(statusEffect, duration, strength);
                    }
                } catch (ValueError exception) {
                    player.sendMessage("Invalid key: " + args[1]);
                    return false;
                }

                if (statusEffectInstance == null) {
                    player.sendMessage("EffectInstance with key ‘" + args[1] + "’ was not found.");
                    return true;
                }
                player.addEffect(statusEffectInstance);
                return true;
            }
        }

        player.sendMessage("Usage: /effect clear");
        player.sendMessage("Usage: /effect give <effect:key> [duration:int] [strength:byte]");

        return false;
    }
}
