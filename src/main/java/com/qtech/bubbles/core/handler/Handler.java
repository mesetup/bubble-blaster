package com.qtech.bubbles.core.handler;

import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.player.PlayerEntity;

import java.util.LinkedList;

/**
 * @author Quinten Jungblut
 */
public class Handler {
    final LinkedList<Entity> object = new LinkedList<>();

    /**
     * @author Quinten Jungblut
     */
    @SuppressWarnings("EmptyMethod")
    public void tick() {
//        for (int i = 0; i < object.size(); i++) {
//            GameObject tempObject = object.get(i);
//            tempObject.tick();
//        }
    }

    /**
     * @author Quinten Jungblut
     */
    @SuppressWarnings("EmptyMethod")
    public void render() {
//        for (int i = 0; i  < object.size(); i++) {
//            GameObject tempObject = object.get(i);
//
//            tempObject.render(g);
//        }
    }

    /**
     * @author Quinten Jungblut
     */
    public void addObject(Entity object) {
        this.object.add(object);
    }

    public void removeObject(Entity object) {
        this.object.remove(object);
    }

    public void clearEnemies() {
        for (int i = 0; i < object.size(); i++) {
            Entity tempObject = object.get(i);

            if (tempObject instanceof PlayerEntity) {
                object.clear();
                addObject(tempObject);
            }
        }
    }
}
