package com.qtech.bubbles.common.ammo;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.entity.AmmoEntity;

import java.awt.*;
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
    public void render(GraphicsProcessor g, AmmoEntity entity) {
        GraphicsProcessor g2 = (GraphicsProcessor) g;
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
