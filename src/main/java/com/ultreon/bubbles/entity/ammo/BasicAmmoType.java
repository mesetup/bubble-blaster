package com.ultreon.bubbles.entity.ammo;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.AmmoEntity;
import com.ultreon.bubbles.entity.attribute.Attribute;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

public class BasicAmmoType extends AmmoType {
    private final Line2D line = new Line2D.Double(0, 0, 5, 0);

    @Override
    public Shape getShape(AmmoEntity entity) {
        Area area = new Area(line);

        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(entity.getRotation()), area.getBounds().getCenterX(), area.getBounds().getCenterY());

        area.transform(transform);

        return line;
    }

    @Override
    public void render(Renderer g, AmmoEntity entity) {
        Renderer g2 = (Renderer) g;
        g2.fill(getShape(entity));

    }

    @Override
    public AttributeMap getDefaultAttributes() {
        AttributeMap map = new AttributeMap();
        map.setBase(Attribute.ATTACK, 1f);
        map.setBase(Attribute.DEFENSE, 4f);
        return map;
    }
}
