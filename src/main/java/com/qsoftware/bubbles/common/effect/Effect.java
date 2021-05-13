package com.qsoftware.bubbles.common.effect;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.RegistryEntry;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.event.FilterEvent;
import com.qsoftware.bubbles.util.helpers.SvgHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;


public abstract class Effect<T extends Effect<T>> extends RegistryEntry {
    // Empty Image.
    protected static Image image;

    static {
        image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = (Graphics2D) image.getGraphics();
        gg.setBackground(new Color(0, 0, 0, 0));
        gg.clearRect(0, 0, 32, 32);
    }

    private final HashMap<String, Object> cache = new HashMap<>();

    public Effect() {

    }

    public URL getIconResource() {
        return getClass().getResource("assets/" + getRegistryName().getNamespace() + "/vectors/effects/" + getRegistryName().getPath() + ".svg");
    }

    public synchronized InputStream getIconResourceAsStream() {
        if (cache.containsKey("icon-stream")) {
            return (InputStream) cache.get("icon-stream");
        }
        InputStream inputStream = Effect.class.getClassLoader().getResourceAsStream("assets/" + getRegistryName().getNamespace() + "/vectors/effects/" + getRegistryName().getPath() + ".svg");
        return (InputStream) cache.put("icon-stream", inputStream);
    }

    public Image getIcon(int w, int h, Color color) throws IOException {
//        if (cache.containsKey("icon-img")) {
//            return (Image) cache.get("icon-img");
//        }
//
//        Image img;
//        InputStream inputStream = getIconResourceAsStream();
//        if (inputStream != null) {
//            SvgHelper svgHelper = new SvgHelper(getIconResource());
//            img = svgHelper.getColoredImage(w, h, color);
//        } else {
//            Game.getLogger().warn("Cannot find effect-icon: " + getIconResource().toString());
//            img = image;
//        }
//
//        cache.put("icon-img", img);
//
//        return img;

        InputStream inputStream = getIconResourceAsStream();
        if (inputStream != null && getIconResource() != null) {
            SvgHelper svgHelper = new SvgHelper(getIconResource());
            return svgHelper.getColoredImage(w, h, color);
        } else {
            QBubbles.getLogger().warn("Cannot find effect-icon: " + "/assets/" + getRegistryName().getNamespace() + "/vectors/effects/" + getRegistryName().getPath() + ".svg");
            return image;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effect<?> that = (Effect<?>) o;
        return Objects.equals(getRegistryName(), that.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public void tick(Entity evt, EffectInstance effectInstance) {
        if (canExecute(effectInstance)) {
            execute(evt, effectInstance);
        }
    }

    protected abstract boolean canExecute(EffectInstance effectInstance);

    public void execute(Entity evt, EffectInstance effectInstance) {

    }

    public void onFilter(EffectInstance effectInstance, FilterEvent evt) {

    }

    public void onStart(EffectInstance effectInstance, Entity entity) {

    }

    public void onStop(EffectInstance effectInstance, Entity entity) {

    }

    public AttributeMap getAttributeModifiers() {
        return new AttributeMap();
    }

    protected void updateStrength(EffectInstance effectInstance, int old, int _new) {

    }
}
