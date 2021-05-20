package com.qtech.bubbleblaster.effect;

import com.jhlabs.image.HSBAdjustFilter;
import com.qtech.bubbleblaster.common.effect.Effect;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.DamageSource;
import com.qtech.bubbleblaster.common.entity.DamageSourceType;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.event.FilterEvent;
import com.qtech.utilities.python.builtins.ValueError;
import org.bson.BsonDocument;
import org.bson.BsonInt64;

public class PoisonEffect extends Effect {
    public PoisonEffect() throws ValueError {
        super();
    }

    @Override
    public void onFilter(EffectInstance effectInstance, FilterEvent evt) {
        HSBAdjustFilter filter = new HSBAdjustFilter();
        filter.setHFactor((float) (System.currentTimeMillis() - effectInstance.getStartTime()) / 3000 % 1);
        evt.addFilter(filter);
    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return System.currentTimeMillis() >= effectInstance.getTag().getInt64("nextDamage").getValue();
    }

    @Override
    public void execute(Entity entity, EffectInstance effectInstance) {
        entity.getGameType().attack(entity, (double) effectInstance.getStrength() / 2, new DamageSource(null, DamageSourceType.POISON));
        BsonDocument tag = effectInstance.getTag();
        BsonInt64 nextDamage = tag.getInt64("nextDamage");
        tag.put("nextDamage", new BsonInt64(nextDamage.getValue() + 2000));
    }

    @Override
    public void onStart(EffectInstance effectInstance, Entity entity) {
        BsonDocument tag = effectInstance.getTag();
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
