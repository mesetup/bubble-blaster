package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.util.helpers.SvgHelper;
import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.event.FilterEvent;
import com.ultreon.hydro.render.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public abstract class StatusEffect extends RegistryEntry {
    // Empty Image.
    protected static final Image image;

    static {
        image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Renderer gg = new Renderer(image.getGraphics());
        gg.clearColor(new Color(0, 0, 0, 0));
        gg.clearRect(0, 0, 32, 32);
    }

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public StatusEffect() {

    }

    public URL getIconResource() {
        return getClass().getResource("assets/" + getRegistryName().namespace() + "/vectors/effects/" + getRegistryName().path() + ".svg");
    }

    public InputStream getIconResourceAsStream() {
        if (cache.containsKey("icon-stream")) {
            return (InputStream) cache.get("icon-stream");
        }
        InputStream inputStream = StatusEffect.class.getClassLoader().getResourceAsStream("assets/" + getRegistryName().namespace() + "/vectors/effects/" + getRegistryName().path() + ".svg");
        if (inputStream == null && getIconResource() == null) {
            BubbleBlaster.getLogger().warn("Cannot find effect-icon: " + "/assets/" + getRegistryName().namespace() + "/vectors/effects/" + getRegistryName().path() + ".svg");
        }
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
        }
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEffect that = (StatusEffect) o;
        return Objects.equals(getRegistryName(), that.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public final void tick(Entity entity, StatusEffectInstance statusEffectInstance) {
        if (canExecute(entity, statusEffectInstance)) {
            execute(entity, statusEffectInstance);
        }
    }

    protected abstract boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance);

    @SuppressWarnings("EmptyMethod")
    public void execute(Entity entity, StatusEffectInstance statusEffectInstance) {

    }

    public void onFilter(StatusEffectInstance statusEffectInstance, FilterEvent evt) {

    }

    public void onStart(StatusEffectInstance statusEffectInstance, Entity entity) {

    }

    public void onStop(Entity entity) {

    }

    public AttributeMap getAttributeModifiers() {
        return new AttributeMap();
    }

    @SuppressWarnings("EmptyMethod")
    protected void updateStrength() {

    }

    @Override
    public String toString() {
        return "StatusEffect[" + getRegistryName() + "]";
    }
}
