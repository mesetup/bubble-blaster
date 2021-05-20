package com.qtech.bubbleblaster.core.utils.categories;


public class ShapeUtils {
    public static boolean checkIntersection(Shape shapeA, Shape shapeB) {
        Area areaA = new Area(shapeA);
        areaA.intersect(new Area(shapeB));
        return !areaA.isEmpty();
    }
}
