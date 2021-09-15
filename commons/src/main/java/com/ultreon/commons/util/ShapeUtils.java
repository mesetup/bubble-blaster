package com.ultreon.commons.util;

import java.awt.*;
import java.awt.geom.Area;

public class ShapeUtils {
    public static boolean checkIntersection(Shape shapeA, Shape shapeB) {
        Area areaA = new Area(shapeA);
        areaA.intersect(new Area(shapeB));
        return !areaA.isEmpty();
    }
}
