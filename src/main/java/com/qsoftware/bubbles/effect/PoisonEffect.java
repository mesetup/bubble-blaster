package com.qsoftware.bubbles.effect;

import com.jhlabs.image.HSBAdjustFilter;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.DamageSource;
import com.qsoftware.bubbles.common.entity.DamageSourceType;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.event.FilterEvent;
import com.qsoftware.utilities.python.builtins.ValueError;
import org.bson.BsonDocument;
import org.bson.BsonInt64;

public class PoisonEffect extends Effect<PoisonEffect> {
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
    public void tick(Entity entity, EffectInstance effectInstance) {
        if (System.currentTimeMillis() >= effectInstance.getTag().getInt64("nextDamage").getValue()) {
            entity.getGameType().attack(entity, (double) effectInstance.getStrength() / 2, new DamageSource(null, DamageSourceType.POISON));
            BsonDocument tag = effectInstance.getTag();
            BsonInt64 nextDamage = tag.getInt64("nextDamage");
            tag.put("nextDamage", new BsonInt64(nextDamage.getValue() + 2000));
        }
    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return true;
    }

    @Override
    public void onStart(EffectInstance effectInstance, Entity entity) {
        BsonDocument tag = effectInstance.getTag();
        tag.put("nextDamage", new BsonInt64(System.currentTimeMillis() + 2000));
        tag.put("startTime", new BsonInt64(System.currentTimeMillis()));
    }

    @Override
    public void onStop(EffectInstance effect, Entity entity) {
        // Do nothing
    }

    @Override
    protected void updateStrength(EffectInstance effectInstance, int old, int _new) {
        // Do nothing
    }
}
