package com.qtech.bubbleblaster.ability;

import com.qtech.bubbleblaster.ability.triggers.AbilityKeyTrigger;
import com.qtech.bubbleblaster.ability.triggers.types.AbilityKeyTriggerType;
import com.qtech.bubbleblaster.common.ability.Ability;
import com.qtech.bubbleblaster.common.ability.AbilityTrigger;
import com.qtech.bubbleblaster.common.ability.AbilityTriggerType;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import com.qtech.bubbleblaster.init.Abilities;
import com.qtech.bubbleblaster.util.helpers.MathHelper;
import org.bson.BsonInt64;

import java.util.Objects;

public class TeleportAbility extends Ability<TeleportAbility> {
    public TeleportAbility() {
        super(Objects.requireNonNull(Abilities.TELEPORT_ABILITY).get());
    }

    @Override
    public int getTriggerKey() {
        return KeyEvent.VK_SHIFT;
    }

    @Override
    public AbilityTriggerType getTriggerType() {
        return AbilityTriggerType.KEY_TRIGGER;
    }

    @Override
    public AbilityKeyTriggerType getKeyTriggerType() {
        return AbilityKeyTriggerType.HOLD;
    }

    @Override
    public void trigger(AbilityTrigger trigger) {
        Entity entity = trigger.getEntity();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            long startTime = player.getTag().getNumber("TeleportAbilityStartTime").longValue();
            player.getTag().remove("TeleportAbilityStartTime");

            long deltaTime = System.currentTimeMillis() - startTime;
            deltaTime = MathHelper.clamp(deltaTime, 0, 2500);  // 0 to 2.5 seconds.

            double deltaMotion = Math.pow((double) deltaTime / 100, 2);

            // Calculate Velocity X and Y.
            double angelRadians = Math.toRadians(player.getRotation());
            double tempVelX = Math.cos(angelRadians) * deltaMotion;
            double tempVelY = Math.sin(angelRadians) * deltaMotion;

            Point2D pos = new Point2D.Double(player.getX() + tempVelX, player.getY() + tempVelY);

            player.teleport(pos);
            subtractValue((int) deltaTime);
            setCooldown((int) (deltaTime / 3));
        }
    }

    @Override
    public void onKeyTrigger(AbilityKeyTrigger trigger) {
        Entity entity = trigger.getEntity();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getTag().put("TeleportAbilityStartTime", new BsonInt64(System.currentTimeMillis()));
        }
    }

    @Override
    public void triggerEntity() {

    }

    @Override
    public boolean isTriggerable(Entity entity) {
        return entity instanceof PlayerEntity;
    }

    @Override
    public boolean isRegenerateable() {
        return true;
    }
}
