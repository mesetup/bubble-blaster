package com.qtech.bubbles.effect;

import com.jhlabs.image.HSBAdjustFilter;
import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.damage.DamageSource;
import com.qtech.bubbles.entity.damage.DamageSourceType;
import com.qtech.bubbles.event.FilterEvent;
import com.qtech.utilities.python.builtins.ValueError;
import org.bson.BsonDocument;
import org.bson.BsonInt64;

public class PoisonEffect extends StatusEffect {
    public PoisonEffect() throws ValueError {
        super();
    }

    @Override
    public void onFilter(StatusEffectInstance statusEffectInstance, FilterEvent evt) {
        HSBAdjustFilter filter = new HSBAdjustFilter();
        filter.setHFactor((float) (System.currentTimeMillis() - statusEffectInstance.getStartTime()) / 3000 % 1);
        evt.addFilter(filter);
    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return System.currentTimeMillis() >= statusEffectInstance.getTag().getInt64("nextDamage").getValue();
    }

    @Override
    public void execute(Entity entity, StatusEffectInstance statusEffectInstance) {
        entity.getGameType().attack(entity, (double) statusEffectInstance.getStrength() / 2, new DamageSource(null, DamageSourceType.POISON));
        BsonDocument tag = statusEffectInstance.getTag();
        BsonInt64 nextDamage = tag.getInt64("nextDamage");
        tag.put("nextDamage", new BsonInt64(nextDamage.getValue() + 2000));
    }

    @Override
    public void onStart(StatusEffectInstance statusEffectInstance, Entity entity) {
        BsonDocument tag = statusEffectInstance.getTag();
        tag.put("nextDamage", new BsonInt64(System.currentTimeMillis() + 2000));
        tag.put("startTime", new BsonInt64(System.currentTimeMillis()));
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onStop(Entity entity) {
        // Do nothing
    }

    @Override
    protected void updateStrength() {
        // Do nothing
    }
}
