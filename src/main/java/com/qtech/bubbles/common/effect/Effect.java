package com.qtech.bubbles.common.effect;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.RegistryEntry;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.event.FilterEvent;
import com.qtech.bubbles.util.helpers.SvgHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;


public abstract class Effect extends RegistryEntry {
    // Empty Image.
    protected static final Image image;

    static {
        image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        GraphicsProcessor gg = new GraphicsProcessor(image.getGraphics());
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
        if (inputStream == null && getIconResource() == null) {
            BubbleBlaster.getLogger().warn("Cannot find effect-icon: " + "/assets/" + getRegistryName().getNamespace() + "/vectors/effects/" + getRegistryName().getPath() + ".svg");
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
        Effect that = (Effect) o;
        return Objects.equals(getRegistryName(), that.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public final void tick(Entity entity, EffectInstance effectInstance) {
        if (canExecute(entity, effectInstance)) {
            execute(entity, effectInstance);
        }
    }

    protected abstract boolean canExecute(Entity entity, EffectInstance effectInstance);

    @SuppressWarnings("EmptyMethod")
    public void execute(Entity entity, EffectInstance effectInstance) {

    }

    public void onFilter(EffectInstance effectInstance, FilterEvent evt) {

    }

    public void onStart(EffectInstance effectInstance, Entity entity) {

    }

    public void onStop(Entity entity) {

    }

    public AttributeMap getAttributeModifiers() {
        return new AttributeMap();
    }

    @SuppressWarnings("EmptyMethod")
    protected void updateStrength() {

    }
}
