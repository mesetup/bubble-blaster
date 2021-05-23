package com.qtech.bubbleblaster.common.ammo;

import com.qtech.bubbleblaster.common.AttributeMap;
import com.qtech.bubbleblaster.common.entity.Attribute;
import com.qtech.bubbleblaster.entity.AmmoEntity;


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
    public void render(Graphics g, AmmoEntity entity) {
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
